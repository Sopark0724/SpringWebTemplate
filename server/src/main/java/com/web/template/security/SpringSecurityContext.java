package com.web.template.security;

import com.web.template.user.model.AccountDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringSecurityContext extends SecurityContextHolder {

    private boolean isHavingContext() {
        if (getContext() == null || getContext().getAuthentication() == null || getContext().getAuthentication().getPrincipal() == null) {
            return false;
        }
        return getContext().getAuthentication().getPrincipal() instanceof AccountDetails;
    }

    public AccountDetails getAccount() {
        if (this.isHavingContext()) {
            return AccountDetails.class.cast(getContext().getAuthentication().getPrincipal());
        }

        return null;
    }

}
