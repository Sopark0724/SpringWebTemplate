package com.web.template.attchments.domain.dto;

import com.web.template.board.domain.dto.BoardDto;
import lombok.Data;

@Data
public class AttachmentsBoardMapDto {

    private Long id;
    private Long board_id;
    private Long attachments_id;


    public static AttachmentsBoardMapDto mapping(BoardDto board, AttachmentsDto attachments) {
        AttachmentsBoardMapDto boardMap = new AttachmentsBoardMapDto();
        boardMap.board_id = board.getId();
        boardMap.attachments_id = attachments.getId();
        return boardMap;
    }
}
