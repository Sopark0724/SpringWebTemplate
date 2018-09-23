package com.web.template.user.domain.dao;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
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

    public List<LinkedHashMap> findAll() {
        return this.sqlSession.selectList("AccountDAO.findAll");
    }

    public LinkedHashMap findById(Object id) {
        return this.sqlSession.selectOne("AccountDAO.findById", id);
    }

    public LinkedHashMap findFirsByUsername(String userName) {
        return this.sqlSession.selectOne("AccountDAO.findFirsByUsername", userName);
    }

    public List<LinkedHashMap> saveAll(List<LinkedHashMap> accountList) {
        return accountList.stream().map(this::save).collect(Collectors.toList());
    }

    public LinkedHashMap save(LinkedHashMap account) {

        if (account.get("id") != null) {
            this.sqlSession.update("AccountDAO.update", account);
        } else {
            account.put("id", this.getNextId());
            this.sqlSession.insert("AccountDAO.insert", account);
        }
        return this.sqlSession.selectOne("AccountDAO.findById", account.get("id"));
    }

    public void deleteAll(List<LinkedHashMap> accountList) {
        accountList.forEach(this::delete);
    }

    public void delete(LinkedHashMap account) {
        this.sqlSession.delete("AccountDAO.delete", account);
    }

}
