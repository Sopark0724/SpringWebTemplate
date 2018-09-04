package com.web.template.board.domain;

import com.web.template.user.domain.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Account user;

    private String title;

    private Date createdAt;

    private Date updatedAt;


    @Type(type = "org.hibernate.type.TextType")
    private String contents;


    public void initId(Long id) {
        if (this.id != null) {
            return;
        }
        this.id = id;
    }

    public void initUser(Account user) {
        if(this.user != null){
            return;
        }
        this.user = user;
    }

    public Board(Account user, String title, String contents) {
        this.user = user;
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
        if (this.user == null) {
            return false;
        }
        return this.user.getId().equals(requestUser.getId());
    }

    public boolean canNotUpdate(Account requestUser) {
        return !this.canUpdate(requestUser);
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public String getWriterName() {
        if (this.user == null) {
            return "";
        }
        return this.user.getName();
    }
}
