package com.web.template.attachments.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.template.TemplateApplication;
import com.web.template.attchments.data.AttachmentsPresentation;
import com.web.template.board.application.BoardService;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.common.AbstractServiceHelper;
import com.web.template.common.AccountTestService;
import com.web.template.common.MockMvcHelper;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class, AccountTestService.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class AttachmentsControllerTest extends AbstractServiceHelper {

    @Autowired
    private MockMvcHelper mockMvcHelper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardService boardService;

    @Autowired
    private AccountTestService accountService;


    @Test
    public void test_01_uploadfile() throws Exception {
        Map account = accountService.getTestAccount();
        Map board = this.boardService.create(BoardAddCommand.builder().title("test title").contents("test content").build(), (Long) account.get("id"));

        // Given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "testfile.txt", MediaType.TEXT_PLAIN_VALUE, "testFileContent".getBytes());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/attachments/upload/BOARD/" + board.get("id")).file(mockMultipartFile);


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
                        get("/attachments/download/" + attachmentsPresentation.getFakename())
                );

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }

}