package com.web.template.user.application;

import com.web.template.common.AbstractServiceHelper;
import com.web.template.user.application.data.AccountAddCommand;
import com.web.template.user.application.data.AccountPresentation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@Transactional
public class UserServiceTest extends AbstractServiceHelper {

    @Autowired
    private AccountService accountService;

    @Test
    public void create() {
        // Given

        AccountAddCommand accountAddCommand = new AccountAddCommand("홍길동", "test1", "1234", "USER");

        // When
        AccountPresentation accountPresentation = accountService.create(accountAddCommand);
        AccountPresentation result =accountService.findById(accountPresentation.getId());

        // Then
        System.out.println(result.toString());
        assertThat(result, is(notNullValue()));
    }
}