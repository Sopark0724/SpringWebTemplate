package com.web.template.board.application;

import com.web.template.TemplateApplication;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.common.AbstractServiceHelper;
import com.web.template.common.AccountTestService;
import com.web.template.common.MockMvcHelper;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import com.web.template.user.application.data.AccountPresentation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class, AccountTestService.class})
@Slf4j
public class BoardServiceTest extends AbstractServiceHelper {

    @Autowired
    private BoardService boardServiceJPAImpl;

    @Autowired
    private AccountTestService accountService;


    @Test
    public void createTest() {
        // Given

        AccountPresentation account = accountService.getTestAccount();
        String title = "title test";
        String content = "cotent test";
        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title(title).contents(content).build();

        // When
        BoardPresentation board = boardServiceJPAImpl.create(boardAddCommand, account.getId());

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
        BoardPresentation board = boardServiceJPAImpl.create(BoardAddCommand.builder().title(originTitle).contents(originContent).build(), account.getId());

        // When
        String updateContentText = "update content test";
        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title(originTitle).contents(updateContentText).build();
        BoardPresentation result = boardServiceJPAImpl.update(board.getId(), boardAddCommand, account.getId());

        // Then
        assertEquals("변경된 컨텐츠 내용 출력", updateContentText, result.getContent());
    }

    @Test(expected = NoSuchElementException.class)
    public void delete() {
        // Given
        AccountPresentation account = accountService.getTestAccount();

        BoardPresentation board = boardServiceJPAImpl.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());

        // When
        boardServiceJPAImpl.delete(board.getId(), account.getId());
        BoardPresentation boardPresentation = boardServiceJPAImpl.get(board.getId());

        // Then

    }

    @Test
    public void getList() {

        // Given
        AccountPresentation account = accountService.getTestAccount();
        boardServiceJPAImpl.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());
        boardServiceJPAImpl.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());
        boardServiceJPAImpl.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());

        PageCommand pageListCommand = new PageCommand(0, 10, "", "");

        // When
        PagePresentation<BoardPresentation> pageList = boardServiceJPAImpl.getList(pageListCommand);

        // Then
        assert pageList.getResults() >= 3;

    }

}