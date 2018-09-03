package com.web.tempalte.board.application.data;

import com.web.tempalte.board.domain.Board;
import lombok.Value;

import java.util.Date;

@Value
public class BoardPresentation {

    private Long id;

    private String writerName;

    private String title;

    private String content;

    private Date createdAt;

    private Date updatedAt;

    public static BoardPresentation convertFromEntity(Board board){
        return new BoardPresentation(
                board.getId(), board.getWriterName(), board.getTitle(),
                board.getContents(), board.getCreatedAt(), board.getUpdatedAt());
    }
}
