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
        Long nextId = this.sqlSession.selectOne("BoardDAO.findNextId");
        return nextId != null ? nextId : 1L;
    }

    public int findBoardCount() {
        return this.sqlSession.selectOne("BoardDAO.findCountAll");
    }

    public List<BoardDto> findBoardPage(PageListCommand pageListCommand) {
        return this.sqlSession.selectList("BoardDAO.findPage", pageListCommand);
    }

    public List<BoardDto> findAll() {
        return this.sqlSession.selectList("BoardDAO.findAll");
    }

    public BoardDto findById(Long id) {
        return this.sqlSession.selectOne("BoardDAO.findById", id);
    }

    public List<BoardDto> saveAll(List<BoardDto> boardList) {
        return boardList.stream().map(this::save).collect(Collectors.toList());
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
