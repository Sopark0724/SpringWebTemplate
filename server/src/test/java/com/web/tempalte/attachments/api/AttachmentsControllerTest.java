package com.web.tempalte.attachments.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.tempalte.TemplateApplication;
import com.web.tempalte.attchments.data.AttachmentsPresentation;
import com.web.tempalte.common.AbstractServiceHelper;
import com.web.tempalte.common.MockMvcHelper;
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

    @Test
    public void test_01_uploadfile() throws Exception {
        // Given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "testfile.txt", MediaType.TEXT_PLAIN_VALUE, "testFileContent".getBytes());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/attachments/upload").file(mockMultipartFile);


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