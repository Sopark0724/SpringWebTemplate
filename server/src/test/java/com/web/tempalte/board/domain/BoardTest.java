package com.web.tempalte.board.domain;

import com.web.tempalte.user.domain.Account;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void canUpdate_자신이쓴글() {
        // Given
        Account account = new Account("test1", "login1", "123123");
        account.setId(1L);
        Board board = new Board(account, "title test", "content test");

        // When
        boolean canUpdate = board.canUpdate(account);

        // Then
        Assert.assertTrue("자기 자신이 쓴글은 수정이 가능", canUpdate);
    }


    @Test
    public void canUpdate_다른사람이쓴글() {
        // Given
        Account account = new Account("test1", "login1", "123123");
        account.setId(1L);
        Account account2 = new Account("test1", "login1", "123123");
        account.setId(2L);
        Board board = new Board(account, "title test", "content test");

        // When
        boolean canUpdate = board.canUpdate(account2);

        // Then
        Assert.assertFalse("다른사람이 쓴글은 수정이 불가", canUpdate);
    }
}