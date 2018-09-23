package com.web.template.board.application;

import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface BoardService {
    Map create(BoardAddCommand boardAddCommand, Long creatorId);

    @Transactional
    Map update(Long boardId, BoardAddCommand boardAddCommand, Long requestUserId);

    void delete(Long id, Long requestUserId);

    @Transactional
    Map get(Long boardId);

    PagePresentation<Map> getList(PageCommand pageListCommand);
}
