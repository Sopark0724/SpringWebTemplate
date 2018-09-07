package com.web.template.attchments.mapper.component;

import com.web.template.attchments.domain.Attachments;
import com.web.template.attchments.mapper.AttachmentsBoardMap;
import com.web.template.attchments.repository.AttachmentsBoardMapRepository;
import com.web.template.attchments.type.AttachmentsType;
import com.web.template.board.domain.Board;
import com.web.template.board.domain.BoardRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttachBoardMap implements AttachMap {

    private final @NonNull
    AttachmentsBoardMapRepository attachmentsBoardMapRepository;
    private final @NonNull
    BoardRepository boardRepository;

    @Override
    public void map(Attachments attachments, Long id) {
        Board board = this.boardRepository.findById(id).get();
        this.attachmentsBoardMapRepository.save(AttachmentsBoardMap.mapping(board, attachments));
    }

    @Override
    public AttachmentsType attachmentType() {
        return AttachmentsType.BOARD;
    }
}
