package com.web.template.board.application.data;

import com.web.template.attchments.data.AttachmentsPresentation;
import com.web.template.attchments.domain.Attachments;
import com.web.template.attchments.domain.dto.AttachmentsDto;
import com.web.template.board.domain.Board;
import com.web.template.board.domain.dto.BoardDto;
import com.web.template.user.domain.dto.AccountDto;
import lombok.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class BoardPresentation {

    private Long id;

    private String writerName;

    private String title;

    private String content;

    private Date createdAt;

    private Date updatedAt;

    private List<AttachmentsPresentation> attachments;

    private static List<AttachmentsPresentation> mapAttachments(List<Attachments> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return new ArrayList<>();
        }
        return attachments.stream().map(AttachmentsPresentation::convertFrom).collect(Collectors.toList());
    }

    private static List<AttachmentsPresentation> mapAttachmentsDto(List<AttachmentsDto> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return new ArrayList<>();
        }
        List<AttachmentsPresentation> list = new ArrayList<>();
        for (AttachmentsDto attachment : attachments) {
            list.add(AttachmentsPresentation.convertFrom(attachment));
        }

        return list;
    }

    public static BoardPresentation convertFromDto(BoardDto board, AccountDto accountDto) {
        return convertFromDto(board, accountDto, null);
    }

    public static BoardPresentation convertFromDto(BoardDto board, AccountDto accountDto, List<AttachmentsDto> attachments) {
        return new BoardPresentation(
                board.getId(), accountDto.getName(), board.getTitle(),
                board.getContents(), board.getCreated_at(), board.getUpdated_at(), mapAttachmentsDto(attachments));
    }

    public static BoardPresentation convertFromEntity(Board board) {
        return convertFromEntity(board, null);
    }

    public static BoardPresentation convertFromEntity(Board board, List<Attachments> attachments) {
        return new BoardPresentation(
                board.getId(), board.getWriterName(), board.getTitle(),
                board.getContents(), board.getCreatedAt(), board.getUpdatedAt(), mapAttachments(attachments));
    }
}
