package com.web.template.board.domain.dao;

import com.web.template.board.domain.dto.BoardDto;
import com.web.template.common.application.data.PageListCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    public Page<BoardDto> findAll(PageListCommand pageListCommand) {
        List<BoardDto> list = this.sqlSession.selectList("findBoardAll", null, pageListCommand.rowBounds());
        int totalCount = this.sqlSession.selectOne("findCountAll", null);

        return new PageImpl<>(list, PageRequest.of(pageListCommand.getPage(), pageListCommand.getOffset()), totalCount);
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
        boardList.forEach(this::delete);
    }

    public void delete(BoardDto board) {
        this.sqlSession.delete("BoardDAO.delete", board);
    }

}
