package com.web.tempalte.user.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.tempalte.TemplateApplication;
import com.web.tempalte.common.MockMvcHelper;
import com.web.tempalte.user.model.AccountDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class})
public class AccountControllerTest {

    @Autowired
    private MockMvcHelper mockMvcHelper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void create() throws Exception {
        // Given
        String requestPayload = "{\"name\" : \"길동이\", \"username\" : \"test2\", \"password\" : \"1234\"}";

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        post("/account/create")
                                .content(requestPayload)
                );

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void login() throws Exception{
        // Given
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setUsername("test2");
        accountDetails.setPassword("1234");

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        post("/account/login")
                                .content(objectMapper.writeValueAsString(accountDetails))

                );

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }
}