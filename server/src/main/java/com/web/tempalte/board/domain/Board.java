package com.web.tempalte.board.domain;

import com.web.tempalte.user.domain.Account;
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

    public Board(Account user, String title, String contents) {
        this.user = user;
        this.title = title;
        this.contents = contents;
    }

    public boolean canUpdate(Account user){
        return this.user.equals(user);
    }

    @PrePersist
    public void prePersist(){
        this.createdAt = new Date();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = new Date();
    }
}
