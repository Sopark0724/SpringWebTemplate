package com.web.tempalte.board.application;

import com.web.tempalte.board.application.data.BoardAddCommand;
import com.web.tempalte.board.application.data.BoardPresentation;
import org.springframework.transaction.annotation.Transactional;

public interface BoardService {
    BoardPresentation create(BoardAddCommand boardAddCommand, Long creatorId);

    @Transactional
    BoardPresentation update(Long boardId, BoardAddCommand boardAddCommand, Long requestUserId);

    void delete(Long id, Long requestUserId);

    @Transactional
    BoardPresentation get(Long boardId);
}
