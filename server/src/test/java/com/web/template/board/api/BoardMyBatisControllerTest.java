package com.web.template.board.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.template.TemplateApplication;
import com.web.template.board.application.BoardService;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.common.AccountTestService;
import com.web.template.common.MockMvcHelper;
import com.web.template.user.application.data.AccountPresentation;
import com.web.template.user.domain.dao.AccountDao;
import com.web.template.user.domain.dto.AccountDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class})
public class BoardMyBatisControllerTest {

    @Autowired
    private MockMvcHelper mockMvcHelper;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountTestService accountTestService;

    @Autowired
    @Qualifier("boardServiceMyBatisImpl")
    private BoardService boardService;

    @Test
    public void test_01_create_text_only() throws Exception {

        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title("test board").contents("test content").build();
        String body = this.objectMapper.writeValueAsString(boardAddCommand);


        ResultActions resultAction =
                mockMvcHelper.perform(
                        post("/v2/board").session(this.accountTestService.getTestSession())
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
                        post("/v2/board").session(this.accountTestService.getTestSession())
                                .content(body));

        // Then
        MvcResult mvcResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        BoardPresentation boardPresentation = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BoardPresentation.class);

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

        this.test_03_board_upload_attachments(boardPresentation);
    }

    private void test_03_board_upload_attachments(BoardPresentation board) throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "testfile.txt", MediaType.TEXT_PLAIN_VALUE, "testFileContent".getBytes());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v2/attachments/upload/BOARD/" + board.getId()).file(mockMultipartFile);

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(builder.session(this.accountTestService.getTestSession()));

        // Then
        MvcResult mvcResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        this.board_attachments_file_check(board);
    }

    private void board_attachments_file_check(BoardPresentation board) throws Exception {
        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        get("/v2/board/" + board.getId()).session(this.accountTestService.getTestSession()));

        // Then
        MvcResult mvcResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        BoardPresentation boardPresentation = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BoardPresentation.class);

        Assert.notEmpty(boardPresentation.getAttachments(), "Attahcments file is empty");

    }

    @Test
    public void test_04_update() throws Exception {

        // create
        AccountPresentation accountPresentation = this.accountTestService.getTestAccount();

        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title("test board").contents("test content").build();
        BoardPresentation boardPresentation = boardService.create(boardAddCommand, accountPresentation.getId());
        //update
        boardAddCommand = BoardAddCommand.builder().title("updated").contents("updated").build();
        String body = this.objectMapper.writeValueAsString(boardAddCommand);

        ResultActions resultAction =
                mockMvcHelper.perform(
                        put("/v2/board/" + boardPresentation.getId()).session(this.accountTestService.getTestSession())
                                .content(body));

        MvcResult updateResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        BoardPresentation updated = this.objectMapper.readValue(updateResult.getResponse().getContentAsString(), BoardPresentation.class);

        org.junit.Assert.assertEquals(updated.getTitle(), "updated");
        org.junit.Assert.assertEquals(updated.getContent(), "updated");
    }

    @Test
    public void test_05_delete_test() throws Exception {

        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title("test board").contents("test content").build();
        BoardPresentation boardPresentation = boardService.create(boardAddCommand, this.accountTestService.getTestAccount().getId());

        ResultActions resultAction =
                mockMvcHelper.perform(
                        delete("/v2/board/" + boardPresentation.getId()).session(this.accountTestService.getTestSession()));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void getList() throws Exception {
        // Given
        AccountPresentation account = this.accountTestService.getTestAccount();
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        get("/v2/boards").session(this.accountTestService.getTestSession())
                                .param("page", "0")
                                .param("offset", "10"));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());
    }
}