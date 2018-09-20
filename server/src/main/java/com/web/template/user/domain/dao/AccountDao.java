package com.web.template.user.domain.dao;

import com.web.template.user.domain.dto.AccountDto;
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
        Long nextId = this.sqlSession.selectOne("AccountDAO.findNextId");
        return nextId != null ? nextId : 1L;
    }

    public List<AccountDto> findAll() {
        return this.sqlSession.selectList("AccountDAO.findAll");
    }

    public AccountDto findById(Long id) {
        return this.sqlSession.selectOne("AccountDAO.findById", id);
    }

    public AccountDto findFirsByUsername(String userName) {
        return this.sqlSession.selectOne("AccountDAO.findFirsByUsername", userName);
    }

    public List<AccountDto> saveAll(List<AccountDto> accountList) {
        return accountList.stream().map(this::save).collect(Collectors.toList());
    }

    public AccountDto save(AccountDto account) {
        if (account.getId() != null) {
            this.sqlSession.update("AccountDAO.update", account);
        } else {
            account.setId(this.getNextId());
            this.sqlSession.insert("AccountDAO.insert", account);
        }
        return this.sqlSession.selectOne("AccountDAO.findById", account.getId());
    }

    public void deleteAll(List<AccountDto> accountList) {
        accountList.forEach(this::delete);
    }

    public void delete(AccountDto account) {
        this.sqlSession.delete("AccountDAO.delete", account);
    }

}
