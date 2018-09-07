package com.web.template.attchments.repository;

import com.web.template.attchments.mapper.AttachmentsBoardMap;
import com.web.template.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentsBoardMapRepository extends JpaRepository<AttachmentsBoardMap, Long> {
    List<AttachmentsBoardMap> findByBoard(Board board);
}
