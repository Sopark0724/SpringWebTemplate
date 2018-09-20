package com.web.template.user.application;

import com.web.template.user.application.data.AccountAddCommand;
import com.web.template.user.application.data.AccountPresentation;
import com.web.template.user.domain.dao.AccountDao;
import com.web.template.user.domain.dto.AccountDto;
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

    public AccountPresentation create(AccountAddCommand userAddCommand) {
        AccountDto existAccount = this.accountDao.findFirsByUsername(userAddCommand.getUsername());
        assert existAccount == null;

        AccountDto account = new AccountDto(userAddCommand.getName(), userAddCommand.getUsername(), userAddCommand.getPassword(), userAddCommand.getRole());
        accountDao.save(account.encryptPassword(this.passwordEncoder));
        return AccountPresentation.convertFrom(account);
    }

    public AccountPresentation findById(Long id) {
        AccountDto account = accountDao.findById(id);
        if (account == null) {
            throw new NoSuchElementException();
        }
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
