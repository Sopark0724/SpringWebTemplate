package com.web.tempalte.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findFirstByUsername(String userName);
}
