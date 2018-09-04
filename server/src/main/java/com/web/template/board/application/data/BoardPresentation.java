package com.web.template.board.application.data;

import com.web.template.board.domain.Board;
import com.web.template.board.domain.dto.BoardDto;
import com.web.template.user.domain.dto.AccountDto;
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

    public static BoardPresentation convertFromDto(BoardDto board, AccountDto accountDto) {
        return new BoardPresentation(
                board.getId(), accountDto.getName(), board.getTitle(),
                board.getContents(), board.getCreated_at(), board.getUpdated_at());
    }

    public static BoardPresentation convertFromEntity(Board board) {
        return new BoardPresentation(
                board.getId(), board.getWriterName(), board.getTitle(),
                board.getContents(), board.getCreatedAt(), board.getUpdatedAt());
    }
}
