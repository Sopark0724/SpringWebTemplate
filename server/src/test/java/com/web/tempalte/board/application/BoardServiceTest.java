package com.web.tempalte.board.application;

import com.web.tempalte.board.application.data.BoardAddCommand;
import com.web.tempalte.board.application.data.BoardPresentation;
import com.web.tempalte.common.AbstractServiceHelper;
import com.web.tempalte.user.application.AccountService;
import com.web.tempalte.user.application.data.AccountAddCommand;
import com.web.tempalte.user.application.data.AccountPresentation;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@Transactional
public class BoardServiceTest extends AbstractServiceHelper {

    @Autowired
    private BoardService boardService;

    @Autowired
    private AccountService accountService;

    @Test
    public void createTest(){
        // Given
        AccountPresentation account = accountService.create(new AccountAddCommand("test", "test", "test"));
        String title = "title test";
        String content = "cotent test";
        BoardAddCommand boardAddCommand = new BoardAddCommand(title, content);

        // When
        BoardPresentation board = boardService.create(boardAddCommand, account.getId());

        // Then
        assertNotNull("생성된 객체는 null 이 아니다.", board);
        assertEquals(title, board.getTitle());
        assertEquals(content, board.getContent());
    }

}