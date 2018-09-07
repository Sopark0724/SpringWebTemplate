package com.web.template.org.domain;

import com.web.template.user.domain.Account;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class DeptMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Account account;

    private int sortOrder;

    public DeptMember(Account account) {
        this.account = account;
    }

    public String getName() {
        return this.account.getName();
    }
}
