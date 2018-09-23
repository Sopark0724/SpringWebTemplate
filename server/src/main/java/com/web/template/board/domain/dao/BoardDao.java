package com.web.template.board.domain.dao;

import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    public int getTotalCount(){
        return  this.sqlSession.selectOne("findCountAll", null);
    }

    public List<LinkedHashMap> findAll(PageCommand pageCommand) {

        return this.sqlSession.selectList("findBoardAll", null, pageCommand.rowBounds());
    }

    public LinkedHashMap findById(Long id) {
        return this.sqlSession.selectOne("BoardDAO.findById", id);
    }

    public List<LinkedHashMap> saveAll(List<LinkedHashMap> boardList) {
        List<LinkedHashMap> saveResult = new ArrayList<>();

        for (LinkedHashMap boardDto : boardList) {
            saveResult.add(this.save(boardDto));
        }
        return saveResult;
    }

    public LinkedHashMap save(LinkedHashMap board) {

        if (board.get("id") != null) {
            this.sqlSession.update("BoardDAO.update", board);
        } else {
            board.put("id", this.getNextId());
            this.sqlSession.insert("BoardDAO.insert", board);
        }
        return this.sqlSession.selectOne("BoardDAO.findById", board.get("id"));
    }

    public void deleteAll(List<LinkedHashMap> boardList) {
        for (LinkedHashMap hashMap : boardList) {
            this.delete(hashMap);
        }
    }

    public void delete(LinkedHashMap board) {
        this.sqlSession.delete("BoardDAO.delete", board);
    }

}
