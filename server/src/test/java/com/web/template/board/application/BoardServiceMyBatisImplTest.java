package com.web.template.board.application;

import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.common.AbstractServiceHelper;
import com.web.template.user.application.AccountService;
import com.web.template.user.application.data.AccountAddCommand;
import com.web.template.user.application.data.AccountPresentation;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BoardServiceMyBatisImplTest extends AbstractServiceHelper {

    @Autowired
    private BoardServiceMyBatisImpl boardServiceMyBatis;

    @Autowired
    private AccountService accountService;

    @Test
    public void create() {
        // Given
        AccountPresentation accountPresentation = accountService.create(new AccountAddCommand("홍길동", "test1", "1234", "USER"));
        String title = "test title";
        String contents = "test contents";
        BoardAddCommand boardAddCommand = new BoardAddCommand(title, contents);

        // When
        BoardPresentation boardPresentation = boardServiceMyBatis.create(boardAddCommand, accountPresentation.getId());

        // Then
        Assert.assertEquals("생성된 보드의 타이틀 확인", title, boardPresentation.getTitle());
        Assert.assertEquals("생성된 보드의 내용 확인", contents, boardPresentation.getContent());
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void get() {
    }

    @Test
    public void getList() {
    }
}