package com.web.template.user.domain.dao;

import com.web.template.user.domain.Account;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountDao {

    private final @NonNull
    SqlSession sqlSession;

    private Long getNextId() {
        Long nextId = this.sqlSession.selectOne("findAccountNextId");
        return nextId != null ? nextId : 1L;
    }

    public List<Account> findAll() {
        return this.sqlSession.selectList("findAccountAll");
    }

    public Account findById(Long id) {
        return this.sqlSession.selectOne("findAccountById", id);
    }

    public List<Account> saveAll(List<Account> accountList) {
        return accountList.stream().map(this::save).collect(Collectors.toList());
    }

    public Account save(Account account) {
        if (account.getId() != null) {
            this.sqlSession.update("updateAccount", account);
        } else {
            account.setId(this.getNextId());
            this.sqlSession.insert("insertAccount", account);
        }
        return this.sqlSession.selectOne("findAccountById", account.getId());
    }

    public void deleteAll(List<Account> accountList) {
        accountList.forEach(this::delete);
    }

    public void delete(Account account) {
        this.sqlSession.delete("deleteAccount", account);
    }

}
