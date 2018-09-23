package com.web.template.board.domain;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Transactional
public class BoardTest {

    private HashMap createAccount(String name, String username, String password, String role, Long id) {

        HashMap<String, Object> account = new HashMap();
        account.put("name", name);
        account.put("username", username);
        account.put("password", password);
        account.put("role", role);
        account.put("id", id);
        return account;
    }


    @Test
    public void canUpdate_자신이쓴글() {
        // Given

        HashMap account = this.createAccount("test1", "login1", "123123", "USER", 1L);

        LinkedHashMap<String, Object> board = new LinkedHashMap();
        board.put("account_id", account.get("id"));
        board.put("title", "title test");
        board.put("contents", "contents test");


        // When
        boolean canUpdate = board.get("account_id").equals(account.get("id"));

        // Then
        Assert.assertTrue("자기 자신이 쓴글은 수정이 가능", canUpdate);
    }


    @Test
    public void canUpdate_다른사람이쓴글() {
        // Given

        HashMap<String, Object> account = this.createAccount("test1", "login1", "123123", "USER", 1L);
        HashMap<String, Object> account2 = this.createAccount("test2", "login2", "123123", "USER", 2L);

        LinkedHashMap<String, Object> board = new LinkedHashMap();
        board.put("account_id", account2.get("id"));
        board.put("title", "title test");
        board.put("contents", "contents test");


        // When
        boolean canUpdate = board.get("account_id").equals(account.get("id"));

        // Then
        Assert.assertFalse("다른사람이 쓴글은 수정이 불가", canUpdate);
    }
}