package com.web.template.board.api;

import com.web.template.board.application.BoardService;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import com.web.template.security.SpringSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2")
public class BoardMyBatisController {

    @Autowired
    @Qualifier(value = "boardServiceMyBatisImpl")
    private BoardService boardService;

    @Autowired
    private SpringSecurityContext context;

    @PostMapping("/board")
    public BoardPresentation create(@RequestBody BoardAddCommand boardAddCommand) {
        return boardService.create(boardAddCommand, context.getAccount().getId());
    }

    @PutMapping("/board/{boardId}")
    public BoardPresentation update(@PathVariable Long boardId, @RequestBody BoardAddCommand boardAddCommand) {
        return boardService.update(boardId, boardAddCommand, context.getAccount().getId());
    }

    @DeleteMapping("/board/{boardId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long boardId) {
        boardService.delete(boardId, context.getAccount().getId());
    }

    @GetMapping("/boards")
    public PagePresentation<BoardPresentation> getList(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String properties,
            @RequestParam(required = false) String direction) {
        return boardService.getList(new PageCommand(start, limit, properties, direction));
    }

    @GetMapping("/board/{id}")
    public BoardPresentation getBoard(@PathVariable Long id) {
        return boardService.get(id);
    }
}
