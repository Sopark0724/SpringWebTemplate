package com.web.template.board.domain;

import com.web.template.board.domain.dto.BoardDto;
import com.web.template.user.domain.dto.AccountDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BoardTest {

    @Test
    public void canUpdate_자신이쓴글() {
        // Given

        AccountDto account = new AccountDto("test1", "login1", "123123", "USER");
        account.setId(1L);
        BoardDto board = new BoardDto(account, "title test", "content test");

        // When
        boolean canUpdate = board.canUpdate(account);

        // Then
        Assert.assertTrue("자기 자신이 쓴글은 수정이 가능", canUpdate);
    }


    @Test
    public void canUpdate_다른사람이쓴글() {
        // Given

        AccountDto account = new AccountDto("test1", "login1", "123123", "USER");
        account.setId(1L);
        AccountDto account2 = new AccountDto("test1", "login1", "123123", "USER");

        account.setId(2L);
        BoardDto board = new BoardDto(account, "title test", "content test");

        // When
        boolean canUpdate = board.canUpdate(account2);

        // Then
        Assert.assertFalse("다른사람이 쓴글은 수정이 불가", canUpdate);
    }
}