package com.web.template.board.domain.dao;

import com.web.template.board.domain.dto.BoardDto;
import com.web.template.common.application.data.PageListCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public int findBoardCount() {
        return this.sqlSession.selectOne("findBoardCountAll");
    }

    public List<BoardDto> findBoardPage(PageListCommand pageListCommand) {
        return this.sqlSession.selectList("findBoardPage", pageListCommand);
    }

    public List<BoardDto> findAll() {
        return this.sqlSession.selectList("findBoardAll");
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
