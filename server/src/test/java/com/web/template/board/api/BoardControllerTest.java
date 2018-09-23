package com.web.template.board.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.template.TemplateApplication;
import com.web.template.board.application.BoardService;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.common.AccountTestService;
import com.web.template.common.MockMvcHelper;
import com.web.template.user.domain.dao.AccountDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class, AccountTestService.class})
@Transactional
public class BoardControllerTest {

    @Autowired
    private MockMvcHelper mockMvcHelper;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountTestService accountTestService;

    @Autowired
    private BoardService boardService;

    @Test
    public void test_01_create_text_only() throws Exception {

        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title("test board").contents("test content").build();
        String body = this.objectMapper.writeValueAsString(boardAddCommand);


        ResultActions resultAction =
                mockMvcHelper.perform(
                        post("/board").session(this.accountTestService.getTestSession())
                                .content(body));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void test_02_create_attachments() throws Exception {
        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title("test board").contents("test content").build();
        String body = this.objectMapper.writeValueAsString(boardAddCommand);

        ResultActions resultAction =
                mockMvcHelper.perform(
                        post("/board").session(this.accountTestService.getTestSession())
                                .content(body));

        // Then
        MvcResult mvcResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        HashMap boardPresentation = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), HashMap.class);

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

        this.test_03_board_upload_attachments(boardPresentation);
    }

    private void test_03_board_upload_attachments(HashMap board) throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "testfile.txt", MediaType.TEXT_PLAIN_VALUE, "testFileContent".getBytes());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/attachments/upload/BOARD/" + board.get("id")).file(mockMultipartFile);

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(builder.session(this.accountTestService.getTestSession()));

        // Then
        MvcResult mvcResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        this.board_attachments_file_check(board);
    }

    private void board_attachments_file_check(HashMap board) throws Exception {
        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        get("/board/" + board.get("id")).session(this.accountTestService.getTestSession()));

        // Then
        MvcResult mvcResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        HashMap boardPresentation = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), HashMap.class);

        Assert.notEmpty((ArrayList) boardPresentation.get("attachments"), "Attahcments file is empty");

    }

    @Test
    public void test_04_update() throws Exception {

        // create
        Map accountPresentation = this.accountTestService.getTestAccount();

        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title("test board").contents("test content").build();
        Map boardPresentation = boardService.create(boardAddCommand, (Long) accountPresentation.get("id"));
        //update
        boardAddCommand = BoardAddCommand.builder().title("updated").contents("updated").build();
        String body = this.objectMapper.writeValueAsString(boardAddCommand);

        ResultActions resultAction =
                mockMvcHelper.perform(
                        put("/board/" + boardPresentation.get("id")).session(this.accountTestService.getTestSession())
                                .content(body));

        MvcResult updateResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        HashMap updated = this.objectMapper.readValue(updateResult.getResponse().getContentAsString(), HashMap.class);

        org.junit.Assert.assertEquals(updated.get("title"), "updated");
        org.junit.Assert.assertEquals(updated.get("contents"), "updated");
    }

    @Test
    public void test_05_delete_test() throws Exception {

        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title("test board").contents("test content").build();
        Map boardPresentation = boardService.create(boardAddCommand, (Long) this.accountTestService.getTestAccount().get("id"));

        ResultActions resultAction =
                mockMvcHelper.perform(
                        delete("/board/" + boardPresentation.get("id")).session(this.accountTestService.getTestSession()));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void getList() throws Exception {
        // Given
        Map account = this.accountTestService.getTestAccount();
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(),(Long)account.get("id"));
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(),(Long)account.get("id"));
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(),(Long)account.get("id"));

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        get("/boards").session(this.accountTestService.getTestSession())
                                .param("page", "0")
                                .param("offset", "10"));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());
    }
}