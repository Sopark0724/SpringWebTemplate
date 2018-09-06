package com.web.template.board.application;

import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import org.springframework.transaction.annotation.Transactional;

public interface BoardService {
    BoardPresentation create(BoardAddCommand boardAddCommand, Long creatorId);

    @Transactional
    BoardPresentation update(Long boardId, BoardAddCommand boardAddCommand, Long requestUserId);

    void delete(Long id, Long requestUserId);

    @Transactional
    BoardPresentation get(Long boardId);

    PagePresentation<BoardPresentation> getList(PageCommand pageListCommand);
}
