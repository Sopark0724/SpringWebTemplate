package com.web.template.user.service;

import com.web.template.user.domain.Account;
import com.web.template.user.domain.AccountRepository;
import com.web.template.user.model.AccountDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailService implements UserDetailsService {

    private final @NonNull
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.accountRepository.findFirstByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }

        return AccountDetails.converterFrom(account);
    }
}
