package com.web.template.attchments.map.component;

import com.web.template.attchments.domain.dao.AttachmentsBoardMapDao;
import com.web.template.attchments.util.AttachmentsUtil;
import com.web.template.attchments.type.AttachmentsType;
import com.web.template.board.domain.dao.BoardDao;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
@RequiredArgsConstructor
public class AttachBoardMap implements AttachMap {

    private final @NonNull
    AttachmentsBoardMapDao attachmentsBoardMapDao;

    private final @NonNull
    BoardDao boardDao;

    @Override
    public void map(LinkedHashMap attachments, Long id) {
        LinkedHashMap board = this.boardDao.findById(id);
        if (board == null) {
            throw new NullPointerException();
        }
        LinkedHashMap map = new LinkedHashMap();
        map.put("board_id", (Long)board.get("id"));
        map.put("attachments_id", (Long)attachments.get("id"));


        this.attachmentsBoardMapDao.save(map);
    }

    @Override
    public AttachmentsType attachmentType() {
        return AttachmentsType.BOARD;
    }
}
