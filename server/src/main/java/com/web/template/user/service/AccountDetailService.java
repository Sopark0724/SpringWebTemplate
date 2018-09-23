package com.web.template.user.service;

import com.web.template.user.domain.dao.AccountDao;
import com.web.template.user.model.AccountDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AccountDetailService implements UserDetailsService {

    private final @NonNull
    AccountDao accountDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HashMap account = this.accountDao.findFirsByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }

        return AccountDetails.converterFrom(account);
    }
}
