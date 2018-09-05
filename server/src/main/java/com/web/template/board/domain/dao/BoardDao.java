package com.web.template.board.domain.dao;

import com.web.template.board.domain.dto.BoardDto;
import com.web.template.common.application.data.PageListCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BoardDao {

    private final @NonNull
    SqlSession sqlSession;

    private Long getNextId() {
        Long nextId = this.sqlSession.selectOne("findBoardNextId");
        return nextId != null ? nextId : 1L;
    }

    public Page<BoardDto> findAll(PageListCommand pageListCommand) {
        Map<String, String> params = new HashMap<>();
        List<BoardDto> list = this.sqlSession.selectList("findBoardAll", null, new RowBounds(pageListCommand.getOffset(), pageListCommand.getPage()));
        int totalCount = this.sqlSession.selectOne("countBoardAll", null);

        return new PageImpl<>(list, PageRequest.of(pageListCommand.getPage(), totalCount), totalCount);
    }

    public BoardDto findById(Long id) {
        return this.sqlSession.selectOne("findBoardById", id);
    }

    public List<BoardDto> saveAll(List<BoardDto> boardList) {
        return boardList.stream().map(this::save).collect(Collectors.toList());
    }

    public BoardDto save(BoardDto board) {
        if (board.getId() != null) {
            this.sqlSession.update("updateBoard", board);
        } else {
            board.setId(this.getNextId());
            this.sqlSession.insert("insertBoard", board);
        }
        return this.sqlSession.selectOne("findBoardById", board.getId());
    }

    public void deleteAll(List<BoardDto> boardList) {
        boardList.forEach(this::delete);
    }

    public void delete(BoardDto board) {
        this.sqlSession.delete("deleteBoard", board);
    }

}
