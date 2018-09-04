package com.web.template.board.application;

import com.web.template.board.domain.dao.BoardDao;
import com.web.template.board.domain.dto.BoardDto;
import com.web.template.common.AbstractServiceHelper;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BoardDaoTest extends AbstractServiceHelper {

    @Autowired
    private BoardDao boardDao;

    @Test
    public void mybatis_01_InsertTest() {
        BoardDto board = new BoardDto(null, "mybatis title", "test mybatis content");
        board = this.boardDao.save(board);
        Assert.assertNotNull(board.getId());
    }

    @Test
    public void mybatis_02_FindAllTest() {
        List<BoardDto> boardList = this.boardDao.findAll();
        Assert.assertNotEquals(boardList.size(), 0);
    }

}
