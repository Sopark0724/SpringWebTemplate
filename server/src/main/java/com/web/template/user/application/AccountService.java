package com.web.template.user.application;

import com.web.template.user.application.data.AccountAddCommand;
import com.web.template.user.domain.dao.AccountDao;
import com.web.template.user.model.AccountDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final @NonNull
    AccountDao accountDao;

    private final @NonNull
    AuthenticationManager authenticationManager;

    private final @NonNull
    PasswordEncoder passwordEncoder;

    public Map create(AccountAddCommand userAddCommand) {
        HashMap existAccount = this.accountDao.findFirsByUsername(userAddCommand.getUsername());
        assert existAccount == null;

        LinkedHashMap<String, Object> accountMap = new LinkedHashMap<>();
        accountMap.put("name", userAddCommand.getName());
        accountMap.put("username", userAddCommand.getUsername());
        accountMap.put("role", userAddCommand.getRole());
        accountMap.put("password", passwordEncoder.encode(userAddCommand.getPassword()));
        return accountDao.save(accountMap);

    }

    public Map findById(Long id) {
        HashMap account = accountDao.findById(id);
        if (account == null) {
            throw new NoSuchElementException();
        }
        return account;
    }

    public Object login(AccountDetails accountDetails, HttpSession httpSession) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(accountDetails.getUsername(), accountDetails.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return authentication.getPrincipal();
    }

}
