package com.web.template.board.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.template.TemplateApplication;
import com.web.template.board.application.BoardService;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.common.MockMvcHelper;
import com.web.template.user.application.AccountService;
import com.web.template.user.application.data.AccountAddCommand;
import com.web.template.user.application.data.AccountPresentation;
import com.web.template.user.domain.Account;
import com.web.template.user.model.AccountDetails;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BoardJPAControllerTest {

    @Autowired
    private MockMvcHelper mockMvcHelper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("boardServiceJPAImpl")
    private BoardService boardService;

    @Before
    public void auth() {
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setUsername("test");
        accountDetails.setPassword("test");
        accountDetails.setName("test");
        accountDetails.setRole("USER");

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(accountDetails ,null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }

    @Test
    public void test_01_create_text_only() throws Exception {
        AccountPresentation account = accountService.create(new AccountAddCommand("test", "test", "test", "USER"));

        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title("test board").contents("test content").build();
        String body = this.objectMapper.writeValueAsString(boardAddCommand);


        ResultActions resultAction =
                mockMvcHelper.perform(
                        post("/v1/public/board")
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
                        post("/v1/public/board")
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
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v1/attachments/upload/BOARD/" + board.getId()).file(mockMultipartFile);

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(builder);

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
                        get("/v1/board/" + board.getId()));

        // Then
        MvcResult mvcResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        BoardPresentation boardPresentation = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BoardPresentation.class);

        Assert.notEmpty(boardPresentation.getAttachments(), "Attahcments file is empty");

    }

    @Test
    @WithMockUser(username = "test", authorities = {"USER"})
    public void test_04_update() throws Exception {

        // create
        AccountPresentation account = accountService.create(new AccountAddCommand("test", "test", "test", "USER"));

        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title("test board").contents("test content").build();
        BoardPresentation boardPresentation = boardService.create(boardAddCommand, account.getId());
        //update
        boardAddCommand = BoardAddCommand.builder().title("updated").contents("updated").build();
        String body = this.objectMapper.writeValueAsString(boardAddCommand);

        ResultActions resultAction =
                mockMvcHelper.perform(
                        put("/v1/board/" + boardPresentation.getId())
                                .content(body));

        MvcResult updateResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        BoardPresentation updated = this.objectMapper.readValue(updateResult.getResponse().getContentAsString(), BoardPresentation.class);

        org.junit.Assert.assertEquals(updated.getTitle(), "updated");
        org.junit.Assert.assertEquals(updated.getContent(), "updated");
    }

    @Test
    @WithMockUser(username = "test", authorities = {"USER"})
    public void test_05_delete_test() throws Exception {

        // create
        AccountPresentation account = accountService.create(new AccountAddCommand("test", "test", "test", "USER"));

        BoardAddCommand boardAddCommand = BoardAddCommand.builder().title("test board").contents("test content").build();
        BoardPresentation boardPresentation = boardService.create(boardAddCommand, account.getId());

        ResultActions resultAction =
                mockMvcHelper.perform(
                        delete("/v1/board/" + boardPresentation.getId()));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void test_07_getList() throws Exception {
        // Given
        AccountPresentation account = accountService.create(new AccountAddCommand("test", "test", "test", "USER"));
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());
        boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        get("/v1/boards")
                                .param("page", "0")
                                .param("offset", "10"));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());
    }
}