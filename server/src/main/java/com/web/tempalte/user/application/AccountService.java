package com.web.tempalte.user.application;

import com.web.tempalte.user.application.data.AccountAddCommand;
import com.web.tempalte.user.application.data.AccountPresentation;
import com.web.tempalte.user.domain.Account;
import com.web.tempalte.user.domain.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AccountService {

    @Autowired
    private AccountRepository userRepository;

    public AccountPresentation create(AccountAddCommand userAddCommand){
        Account account = new Account(userAddCommand.getName(), userAddCommand.getUsername(), userAddCommand.getPassword());
        userRepository.save(account);
        return new AccountPresentation(account.getId(), account.getName(), account.getUsername());
    }

    public AccountPresentation findById(Long id){
        Account account = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        return new AccountPresentation(account.getId(), account.getName(), account.getUsername());
    }
}
