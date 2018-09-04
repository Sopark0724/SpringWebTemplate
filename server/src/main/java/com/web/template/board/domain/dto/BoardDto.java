package com.web.template.board.domain.dto;

import com.web.template.user.domain.Account;
import com.web.template.user.domain.dto.AccountDto;
import lombok.Data;

import java.util.Date;

@Data
public class BoardDto {

    private Long id;
    private Long user_id;
    private String title;
    private Date created_at;
    private Date updated_at;
    private String contents;


    public BoardDto(AccountDto user, String title, String contents) {
        if(user != null) {
            this.user_id = user.getId();
        }
        this.title = title;
        this.contents = contents;
    }

    public boolean canUpdate(Account requestUser) {
        return this.isOwner(requestUser);
    }

    public boolean canDelete(Account requestUser) {
        return this.isOwner(requestUser);
    }

    public boolean canNotDelete(Account requestUser) {
        return !this.canDelete(requestUser);
    }

    private boolean isOwner(Account requestUser) {
        return this.user_id.equals(requestUser.getId());
    }

    public boolean canNotUpdate(Account requestUser) {
        return !this.canUpdate(requestUser);
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

}
