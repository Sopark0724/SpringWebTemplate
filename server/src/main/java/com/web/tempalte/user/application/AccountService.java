package com.web.tempalte.user.application;

import com.web.tempalte.user.application.data.AccountAddCommand;
import com.web.tempalte.user.application.data.AccountPresentation;
import com.web.tempalte.user.domain.Account;
import com.web.tempalte.user.domain.AccountRepository;
import com.web.tempalte.user.model.AccountDetails;
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
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final @NonNull
    AccountRepository userRepository;
    
    private final @NonNull
    AuthenticationManager authenticationManager;

    private final @NonNull
    PasswordEncoder passwordEncoder;

    public AccountPresentation create(AccountAddCommand userAddCommand) {
        Account account = new Account(userAddCommand.getName(), userAddCommand.getUsername(), userAddCommand.getPassword(), userAddCommand.getRole());
        userRepository.save(account.encryptPassword(this.passwordEncoder));
        return AccountPresentation.convertFrom(account);
    }

    public AccountPresentation findById(Long id) {
        Account account = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return AccountPresentation.convertFrom(account);
    }

    public AccountPresentation login(AccountDetails accountDetails, HttpSession httpSession) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(accountDetails.getUsername(), accountDetails.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        return AccountPresentation.convertFrom(authentication.getPrincipal());
    }

}
