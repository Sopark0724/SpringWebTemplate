package com.web.tempalte.board.application.data;

import lombok.Value;

import java.util.Date;

@Value
public class BoardPresentation {

    private Long id;

    private String userName;

    private String title;

    private String content;

    private Date createdAt;

    private Date updatedAt;
}
