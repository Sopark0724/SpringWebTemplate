package com.web.tempalte.board.application;

import com.web.tempalte.board.application.data.BoardAddCommand;
import com.web.tempalte.board.application.data.BoardPresentation;
import com.web.tempalte.common.AbstractServiceHelper;
import com.web.tempalte.common.application.data.PageListCommand;
import com.web.tempalte.user.application.AccountService;
import com.web.tempalte.user.application.data.AccountAddCommand;
import com.web.tempalte.user.application.data.AccountPresentation;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
public class BoardServiceTest extends AbstractServiceHelper {

    @Autowired
    @Qualifier(value = "boardServiceJPAImpl")
    private BoardService boardService;

    @Autowired
    private AccountService accountService;

    @Test
    public void createTest(){
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

    @Test(expected = NoSuchElementException.class)
    public void delete() {
        // Given
        AccountPresentation account = accountService.create(new AccountAddCommand("test", "test", "test", "USER"));
        BoardPresentation board = boardService.create(new BoardAddCommand("test title" , "test content"), account.getId());

        // When
        boardService.delete(board.getId(), account.getId());
        BoardPresentation boardPresentation = boardService.get(board.getId());

        // Then

    }

    @Test
    public void getList() {
        // Given
        AccountPresentation account = accountService.create(new AccountAddCommand("test", "test", "test", "USER"));
        boardService.create(new BoardAddCommand("test title" , "test content"), account.getId());
        boardService.create(new BoardAddCommand("test title" , "test content"), account.getId());
        boardService.create(new BoardAddCommand("test title" , "test content"), account.getId());

        PageListCommand pageListCommand = new PageListCommand(0, 10, "","");

        // When
        Page<BoardPresentation> pageList = boardService.getList(pageListCommand);

        // Then
        Assert.assertEquals("리스트의 총개수는 3개", 3, pageList.getTotalElements());
        Assert.assertEquals("리스트의 페이지는 1번", 1, pageList.getTotalPages());

    }
}