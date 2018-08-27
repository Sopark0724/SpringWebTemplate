package com.web.tempalte.user.api;

import com.web.tempalte.user.application.AccountService;
import com.web.tempalte.user.application.data.AccountAddCommand;
import com.web.tempalte.user.application.data.AccountPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/account")
    public AccountPresentation create(AccountAddCommand addCommand){
        return accountService.create(addCommand);
    }
}
