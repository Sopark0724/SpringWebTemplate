package com.web.template.attachments.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.template.TemplateApplication;
import com.web.template.attchments.data.AttachmentsPresentation;
import com.web.template.board.application.BoardService;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.board.domain.Board;
import com.web.template.common.AbstractServiceHelper;
import com.web.template.common.AccountTestService;
import com.web.template.common.MockMvcHelper;
import com.web.template.user.application.AccountService;
import com.web.template.user.application.data.AccountAddCommand;
import com.web.template.user.application.data.AccountPresentation;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AttachmentsControllerTest extends AbstractServiceHelper {

    @Autowired
    private MockMvcHelper mockMvcHelper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("boardServiceJPAImpl")
    private BoardService boardJpaService;

    @Autowired
    private AccountTestService accountService;


    @Test
    public void test_01_uploadfile_Jpa() throws Exception {
        // Given

        AccountPresentation account = accountService.getTestAccount();
        BoardPresentation board = this.boardJpaService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "testfile.txt", MediaType.TEXT_PLAIN_VALUE, "testFileContent".getBytes());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v1/attachments/upload/BOARD/" + board.getId()).file(mockMultipartFile);


        // When
        ResultActions resultAction =
                mockMvcHelper.perform(builder);

        // Then
        MvcResult mvcResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        List<AttachmentsPresentation> attachmentsPresentations = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), this.objectMapper.getTypeFactory().constructCollectionType(List.class, AttachmentsPresentation.class));

        Assert.assertEquals(attachmentsPresentations.size(), 1);

        this.test_02_downloadfile(attachmentsPresentations.get(0));
    }


    public void test_02_downloadfile(AttachmentsPresentation attachmentsPresentation) throws Exception {
        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        get("/v1/attachments/download/" + attachmentsPresentation.getFakename())
                );

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void test_03_uploadfile_Mybatis() throws Exception {
        AccountPresentation account = accountService.getTestAccount();
        BoardPresentation board = this.boardJpaService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), account.getId());

        // Given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "testfile.txt", MediaType.TEXT_PLAIN_VALUE, "testFileContent".getBytes());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v2/attachments/upload/BOARD/" + board.getId()).file(mockMultipartFile);


        // When
        ResultActions resultAction =
                mockMvcHelper.perform(builder);

        // Then
        MvcResult mvcResult = resultAction
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        List<AttachmentsPresentation> attachmentsPresentations = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), this.objectMapper.getTypeFactory().constructCollectionType(List.class, AttachmentsPresentation.class));

        Assert.assertEquals(attachmentsPresentations.size(), 1);

        this.test_02_downloadfile(attachmentsPresentations.get(0));
    }

    public void test_04_downloadfile(AttachmentsPresentation attachmentsPresentation) throws Exception {
        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        get("/v2/attachments/download/" + attachmentsPresentation.getFakename())
                );

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }

}