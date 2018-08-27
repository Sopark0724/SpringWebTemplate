package com.web.tempalte.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MockMvcHelper {

    private MockMvc mockMvc;

    @Autowired
    public MockMvcHelper(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(wac)
            .build();
    }

    public ResultActions perform(MockHttpServletRequestBuilder request) throws Exception {

        return this.mockMvc
                .perform(request
                        .contentType(APPLICATION_JSON_UTF8));
    }
}
