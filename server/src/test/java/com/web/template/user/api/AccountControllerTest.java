package com.web.template.user.api;

import com.web.template.TemplateApplication;
import com.web.template.common.AccountTestService;
import com.web.template.common.MockMvcHelper;
import com.web.template.user.application.AccountService;
import com.web.template.user.application.data.AccountAddCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class, AccountTestService.class})
@Transactional
public class AccountControllerTest {

    @Autowired
    private MockMvcHelper mockMvcHelper;

    @Autowired
    private AccountService accountService;

    @Test
    public void create() throws Exception {
        // Given
        String requestPayload = "{\"name\" : \"tester\", \"role\" : \"USER\",\"username\" : \"test_2\", \"password\" : \"1234\"}";

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        post("/account/create")
                                .content(requestPayload));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void login() throws Exception {

        this.accountService.create(AccountAddCommand.builder().name("tester").role("USER").username("test_2").password("1234").build());
        // Given
        String requestPayload = "{\"name\" : \"tester\", \"role\" : \"USER\",\"username\" : \"test_2\", \"password\" : \"1234\"}";

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        post("/account/login")
                                .content(requestPayload));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }
}