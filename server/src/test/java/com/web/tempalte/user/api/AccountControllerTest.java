package com.web.tempalte.user.api;

import com.web.tempalte.TemplateApplication;
import com.web.tempalte.common.MockMvcHelper;
import com.web.tempalte.user.application.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class})
public class AccountControllerTest {

    @Autowired
    private MockMvcHelper mockMvcHelper;

    @Test
    public void create() throws Exception {
        // Given
        String requestPayload = "{\"name\" : \"길동이\", \"username\" : \"test2\", \"password\" : \"1234\"}";

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                    post("/account")
                        .content(requestPayload)
                );

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }
}