package com.web.template.board.application;

import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.common.AbstractServiceHelper;
import com.web.template.common.application.data.PageListCommand;
import com.web.template.user.application.AccountService;
import com.web.template.user.application.data.AccountAddCommand;
import com.web.template.user.application.data.AccountPresentation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class BoardMyBatisServiceTest extends AbstractServiceHelper {

    @Autowired
    @Qualifier(value = "boardServiceMyBatisImpl")
    private BoardService boardService;

    @Autowired
    private AccountService accountService;

    @Test
    public void createTest() {
        // Given
        AccountPresentation account = accountService.create(new AccountAddCommand("test", "test", "test", "USER"));
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


    @Test
    public void update() {
        // Given
        AccountPresentation account = accountService.create(new AccountAddCommand("test", "test", "test", "USER"));
        String originTitle = "board test";
        String originContent = "content test";
        BoardPresentation board = boardService.create(new BoardAddCommand(originTitle, originContent), account.getId());

        // When
        String updateContentText = "update content test";
        BoardAddCommand boardAddCommand = new BoardAddCommand(originTitle, updateContentText);
        BoardPresentation result = boardService.update(board.getId(), boardAddCommand, account.getId());

        // Then
        assertEquals("변경된 컨텐츠 내용 출력", updateContentText, result.getContent());
    }
}