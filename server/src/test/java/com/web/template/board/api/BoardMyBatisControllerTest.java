package com.web.template.board.api;

import com.web.template.TemplateApplication;
import com.web.template.board.application.BoardService;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.common.MockMvcHelper;
import com.web.template.user.domain.dao.AccountDao;
import com.web.template.user.domain.dto.AccountDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class})
public class BoardMyBatisControllerTest {

    @Autowired
    private MockMvcHelper mockMvcHelper;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    @Qualifier("boardServiceMyBatisImpl")
    private BoardService boardService;

    @Test
    public void create() {

    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void getList() throws Exception {
        // Given
        AccountDto account = accountDao.save(new AccountDto("test", "test", "test", "USER"));
        boardService.create(new BoardAddCommand("test title", "test content"), account.getId());
        boardService.create(new BoardAddCommand("test title", "test content"), account.getId());
        boardService.create(new BoardAddCommand("test title", "test content"), account.getId());

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        get("/v2/boards")
                .param("page", "0")
                .param("offset", "10"));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());
    }
}