package com.web.template.common;

import com.web.template.user.application.AccountService;
import com.web.template.user.application.data.AccountAddCommand;
import com.web.template.user.domain.dao.AccountDao;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import sun.applet.resources.MsgAppletViewer;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountTestService {

    private final @NonNull
    AccountDao accountDao;
    private final @NonNull
    AccountService accountService;

    private final @NonNull
    AuthenticationManager authenticationManager;


    public Map getTestAccount() {
        AccountAddCommand accountAddCommand = AccountAddCommand.builder().name("hsim").username("hsim").password("1234").role("USER").build();
        HashMap account = this.accountDao.findFirsByUsername(accountAddCommand.getUsername());
        if (account == null) {
            return this.accountService.create(accountAddCommand);
        }
        return account;

    }

    public MockHttpSession getTestSession() {
        Map accountPresentation = this.getTestAccount();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(accountPresentation.get("username"), "1234");
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return session;
    }

}
