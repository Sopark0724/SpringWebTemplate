package com.web.template.attchments.map.component;

import com.web.template.attchments.domain.dao.AttachmentsBoardMapDao;
import com.web.template.attchments.domain.dto.AttachmentsBoardMapDto;
import com.web.template.attchments.domain.dto.AttachmentsDto;
import com.web.template.attchments.type.AttachmentsType;
import com.web.template.board.domain.dao.BoardDao;
import com.web.template.board.domain.dto.BoardDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttachBoardMap implements AttachMap {

    private final @NonNull
    AttachmentsBoardMapDao attachmentsBoardMapDao;

    private final @NonNull
    BoardDao boardDao;

    @Override
    public void map(AttachmentsDto attachments, Long id) {
        BoardDto board = this.boardDao.findById(id);
        if (board == null) {
            throw new NullPointerException();
        }

        this.attachmentsBoardMapDao.save(AttachmentsBoardMapDto.mapping(board, attachments));
    }

    @Override
    public AttachmentsType attachmentType() {
        return AttachmentsType.BOARD;
    }
}
