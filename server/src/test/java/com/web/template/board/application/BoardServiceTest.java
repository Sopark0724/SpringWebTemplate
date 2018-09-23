package com.web.template.board.application;

import com.web.template.TemplateApplication;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.common.AbstractServiceHelper;
import com.web.template.common.AccountTestService;
import com.web.template.common.MockMvcHelper;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
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

        Map account = accountService.getTestAccount();
        String title = "title test";
        String content = "cotent test";
        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title(title).contents(content).build();

        // When
        Map board = boardServiceJPAImpl.create(boardAddCommand, (Long) account.get("id"));

        // Then
        assertNotNull("생성된 객체는 null 이 아니다.", board);
        assertEquals(title, board.get("title"));
        assertEquals(content, board.get("contents"));
    }


    @Test
    public void update() {
        // Given
        Map account = accountService.getTestAccount();
        String originTitle = "board test";
        String originContent = "content test";
        Map board = boardServiceJPAImpl.create(BoardAddCommand.builder().title(originTitle).contents(originContent).build(), (Long) account.get("id"));

        // When
        String updateContentText = "update content test";
        BoardAddCommand updateCommand = BoardAddCommand.builder().contents(updateContentText).title(originTitle).build();

        Map result = boardServiceJPAImpl.update((Long)board.get("id"), updateCommand, (Long)account.get("id"));


        // Then
        assertEquals("변경된 컨텐츠 내용 출력", updateContentText, result.get("contents"));
    }

    @Test(expected = NoSuchElementException.class)
    public void delete() {
        // Given
        Map account = accountService.getTestAccount();

        Map board = boardServiceJPAImpl.create(BoardAddCommand.builder().title("test title").contents("test content").build(), (Long) account.get("id"));

        // When
        boardServiceJPAImpl.delete((Long)board.get("id"), (Long)account.get("id"));
        Map boardPresentation = boardServiceJPAImpl.get((Long)board.get("id"));

        // Then

    }

    @Test
    public void getList() {

        // Given
        Map account = accountService.getTestAccount();
        boardServiceJPAImpl.create(BoardAddCommand.builder().title("test title").contents("test content").build(), (Long)account.get("id"));
        boardServiceJPAImpl.create(BoardAddCommand.builder().title("test title").contents("test content").build(), (Long)account.get("id"));
        boardServiceJPAImpl.create(BoardAddCommand.builder().title("test title").contents("test content").build(), (Long)account.get("id"));

        PageCommand pageListCommand = new PageCommand(0, 10, "", "");

        // When
        PagePresentation<Map> pageList = boardServiceJPAImpl.getList(pageListCommand);

        // Then
        assert pageList.getResults() >= 3;

    }

}