package com.web.tempalte.board.application;

import com.web.tempalte.board.application.data.BoardAddCommand;
import com.web.tempalte.board.application.data.BoardPresentation;
import com.web.tempalte.common.application.data.PageListCommand;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardService {
    BoardPresentation create(BoardAddCommand boardAddCommand, Long creatorId);

    @Transactional
    BoardPresentation update(Long boardId, BoardAddCommand boardAddCommand, Long requestUserId);

    void delete(Long id, Long requestUserId);

    @Transactional
    BoardPresentation get(Long boardId);

    Page<BoardPresentation> getList(PageListCommand pageListCommand);
}
