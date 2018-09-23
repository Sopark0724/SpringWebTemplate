package com.web.template.user.application;

import com.web.template.common.AbstractServiceHelper;
import com.web.template.user.application.data.AccountAddCommand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@Transactional
public class UserServiceTest extends AbstractServiceHelper {

    @Autowired
    private AccountService accountService;

    @Test
    public void create() {
        // Given
        AccountAddCommand accountAddCommand = AccountAddCommand.builder().name("홍길동").username("hong").password("1234").role("USER").build();

        // When
        Map accountPresentation = accountService.create(accountAddCommand);
        Map result = accountService.findById((Long) accountPresentation.get("id"));

        // Then
        System.out.println(result.toString());
        assertThat(result, is(notNullValue()));
    }
}