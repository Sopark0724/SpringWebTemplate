package com.web.template.board.domain.dao;

import com.web.template.board.domain.dto.BoardDto;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BoardDao {

    private final @NonNull
    SqlSession sqlSession;

    public BoardDto save(Map<String, Object> params) {
        if (params.get("id") != null) {
            this.sqlSession.update("BoardDAO.update", params);
        } else {
            params.put("id", this.getNextId());
            Map sqlParam = new HashMap<>();
            sqlParam.put("params", params);
            this.sqlSession.insert("BoardDAO.insert", sqlParam);
        }
        return this.sqlSession.selectOne("BoardDAO.findById", params.get("id"));
    }

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

    public void deleteAll(List<BoardDto> boardList) {
        for (BoardDto boardDto : boardList) {
            this.delete(boardDto);
        }
    }

    public void delete(BoardDto board) {
        this.sqlSession.delete("BoardDAO.delete", board);
    }

}
