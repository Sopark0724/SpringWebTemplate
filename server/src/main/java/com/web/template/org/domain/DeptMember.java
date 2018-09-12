package com.web.template.org.domain;

import com.web.template.user.domain.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeptMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Account account;

    public DeptMember(Account account) {
        this.account = account;
    }

    public String getName() {
        return this.account.getName();
    }
}
