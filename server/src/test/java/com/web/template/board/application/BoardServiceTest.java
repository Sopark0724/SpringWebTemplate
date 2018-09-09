package com.web.template.board.application;

import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.board.domain.BoardRepository;
import com.web.template.common.AbstractServiceHelper;
import com.web.template.common.AccountTestService;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
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

@Transactional
@Slf4j
public class BoardServiceTest extends AbstractServiceHelper {

    @Autowired
    @Qualifier(value = "boardServiceJPAImpl")
    private BoardService boardService;

    @Autowired
    private AccountTestService accountService;

    @Autowired
    private BoardRepository boardRepository;


    @Test
    public void createTest() {
        // Given

        AccountPresentation account = accountService.getTestAccount();
        String title = "title test";
        String content = "cotent test";
        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title(title).contents(content).build();

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
        AccountPresentation account = accountService.getTestAccount();
        String originTitle = "board test";
        String originContent = "content test";
        BoardPresentation board = boardService.create(BoardAddCommand.builder().title (originTitle).contents(originContent).build(), account.getId());

        // When
        String updateContentText = "update content test";
        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title(originTitle).contents(updateContentText).build();
        BoardPresentation result = boardService.update(board.getId(), boardAddCommand, account.getId());

        // Then
        assertEquals("변경된 컨텐츠 내용 출력", updateContentText, result.getContent());
    }

    @Test(expected = NoSuchElementException.class)
    public void delete() {
        // Given
        AccountPresentation account = accountService.getTestAccount();

        BoardPresentation board = boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());

        // When
        boardService.delete(board.getId(), account.getId());
        BoardPresentation boardPresentation = boardService.get(board.getId());

        // Then

    }

    @Test
    public void getList() {

        // Given
        AccountPresentation account = accountService.getTestAccount();
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());

        PageCommand pageListCommand = new PageCommand(0, 10, "", "");

        // When
        PagePresentation<BoardPresentation> pageList = boardService.getList(pageListCommand);

        // Then
        assert pageList.getResults() >= 3;

    }

}