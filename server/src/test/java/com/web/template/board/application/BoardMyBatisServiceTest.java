package com.web.template.board.application;

import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.common.AbstractServiceHelper;
import com.web.template.common.application.data.PageListCommand;
import com.web.template.user.domain.dao.AccountDao;
import com.web.template.user.domain.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@Transactional
public class BoardMyBatisServiceTest extends AbstractServiceHelper {

    @Autowired
    @Qualifier(value = "boardServiceMyBatisImpl")
    private BoardService boardService;

    @Autowired
    private AccountDao accountDao;

    @Test
    public void createTest() {
        // Given
        AccountDto account = accountDao.save(new AccountDto("test", "test", "test", "USER"));
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
        AccountDto account = accountDao.save(new AccountDto("test", "test", "test", "USER"));
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
        AccountDto account = accountDao.save(new AccountDto("test", "test", "test", "USER"));
        BoardPresentation board = boardService.create(new BoardAddCommand("test title", "test content"), account.getId());

        // When
        boardService.delete(board.getId(), account.getId());
        BoardPresentation boardPresentation = boardService.get(board.getId());

        // Then

    }


    @Test
    public void getList() {
        // Given
        AccountDto account = accountDao.save(new AccountDto("test", "test", "test", "USER"));
        boardService.create(new BoardAddCommand("test title", "test content"), account.getId());
        boardService.create(new BoardAddCommand("test title", "test content"), account.getId());
        boardService.create(new BoardAddCommand("test title", "test content"), account.getId());

        PageListCommand pageListCommand = new PageListCommand(0, 2, "", "");

        // When
        Page<BoardPresentation> pageList = boardService.getList(pageListCommand);

        // Then
        System.out.println(((PageImpl)pageList).toString());
        Assert.assertEquals("리스트의 총개수는 3개", 3, pageList.getTotalElements());
        Assert.assertEquals("리스트의 페이지는 1번", 1, pageList.getTotalPages());
    }
}