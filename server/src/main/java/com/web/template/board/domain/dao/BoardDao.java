package com.web.template.board.domain.dao;

import com.web.template.board.domain.dto.BoardDto;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardDao {

    private final @NonNull
    SqlSession sqlSession;

    private Long getNextId() {
        Long nextId = this.sqlSession.selectOne("BoardDAO.findNextId");
        return nextId != null ? nextId : 1L;
    }

    public PagePresentation<BoardDto> findAll(PageCommand pageListCommand) {
        List<BoardDto> list = this.sqlSession.selectList("findBoardAll", null, pageListCommand.rowBounds());
        int totalCount = this.sqlSession.selectOne("findCountAll", null);

        return new PagePresentation<>(true, totalCount, list);
    }

    public BoardDto findById(Long id) {
        return this.sqlSession.selectOne("BoardDAO.findById", id);
    }

    public List<BoardDto> saveAll(List<BoardDto> boardList) {
        List<BoardDto> saveResult = new ArrayList<>();

        for (BoardDto boardDto : boardList) {
            saveResult.add(this.save(boardDto));
        }
        return saveResult;
        //return boardList.stream().map(this::save).collect(Collectors.toList());
    }

    public BoardDto save(BoardDto board) {
        if (board.getId() != null) {
            this.sqlSession.update("BoardDAO.update", board);
        } else {
            board.setId(this.getNextId());
            this.sqlSession.insert("BoardDAO.insert", board);
        }
        return this.sqlSession.selectOne("BoardDAO.findById", board.getId());
    }

    public void deleteAll(List<BoardDto> boardList) {
        for (BoardDto boardDto : boardList) {
            this.delete(boardDto);
        }
    }

    public void delete(BoardDto board) {
        this.sqlSession.delete("BoardDAO.delete", board);
    }

}
