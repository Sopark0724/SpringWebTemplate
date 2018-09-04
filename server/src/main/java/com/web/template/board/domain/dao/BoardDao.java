package com.web.template.board.domain.dao;

import com.web.template.board.domain.Board;
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

    public List<Board> findAll() {
        return this.sqlSession.selectList("findBoardAll");
    }

    public Board findById(Long id){
        return this.sqlSession.selectOne("findBoardById", id);
    }

    public List<Board> saveAll(List<Board> boardList) {
        return boardList.stream().map(this::save).collect(Collectors.toList());
    }

    public Board save(Board board) {
        if (board.getId() != null) {
            this.sqlSession.update("updateBoard", board);
        } else {
            board.setId(this.getNextId());
            this.sqlSession.insert("insertBoard", board);
        }
        return this.sqlSession.selectOne("findBoardById", board.getId());
    }

    public void deleteAll(List<Board> boardList){
        boardList.forEach(this::delete);
    }

    public void delete(Board board){
        this.sqlSession.delete("deleteBoard", board);
    }

}
