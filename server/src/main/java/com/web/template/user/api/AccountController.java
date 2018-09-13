package com.web.template.user.api;

import com.web.template.user.application.AccountService;
import com.web.template.user.application.data.AccountAddCommand;
import com.web.template.user.application.data.AccountPresentation;
import com.web.template.user.model.AccountDetails;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "사용자 생성")
    @PostMapping("/create")
    public AccountPresentation create(@RequestBody  AccountAddCommand addCommand) {
        return accountService.create(addCommand);
    }

    @ApiOperation(value = "사용자 로그인")
    @PostMapping("/login")
    public AccountPresentation login(@RequestBody AccountDetails accountDetails, HttpSession httpSession) {
        return accountService.login(accountDetails, httpSession);
    }

}
