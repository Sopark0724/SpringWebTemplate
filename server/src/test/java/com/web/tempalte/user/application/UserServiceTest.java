package com.web.tempalte.user.application;

import com.web.tempalte.common.AbstractServiceHelper;
import com.web.tempalte.user.application.data.AccountAddCommand;
import com.web.tempalte.user.application.data.AccountPresentation;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
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