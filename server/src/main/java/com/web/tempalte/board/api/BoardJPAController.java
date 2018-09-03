package com.web.tempalte.board.api;

import com.web.tempalte.board.application.BoardService;
import com.web.tempalte.board.application.data.BoardAddCommand;
import com.web.tempalte.board.application.data.BoardPresentation;
import com.web.tempalte.security.SpringSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class BoardJPAController {

    @Autowired
    @Qualifier(value = "boardServiceJPAImpl")
    private BoardService boardService;

    @Autowired
    private SpringSecurityContext context;

    @PostMapping("/board")
    public BoardPresentation create(BoardAddCommand boardAddCommand) {
        return boardService.create(boardAddCommand, context.getAccount().getId());
    }

    @PutMapping("/board/{boardId}")
    public BoardPresentation update(@PathVariable Long boardId, BoardAddCommand boardAddCommand) {
        return boardService.update(boardId, boardAddCommand, context.getAccount().getId());
    }

    @DeleteMapping("/board/{boardId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long boardId) {
        boardService.delete(boardId, context.getAccount().getId());
    }
}
