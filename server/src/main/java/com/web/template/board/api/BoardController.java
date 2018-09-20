package com.web.template.board.api;

import com.web.template.board.application.BoardService;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import com.web.template.security.SpringSecurityContext;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private SpringSecurityContext context;

    @PostMapping("/public/board")
    public BoardPresentation publicCreate(@RequestBody BoardAddCommand boardAddCommand) {
        return boardService.create(boardAddCommand, 1L);
    }

    @ApiOperation(value = "게시판 생성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boardAddCommand", value = "게시판 정보", required = true, dataType = "BoardAddCommand", defaultValue = "")
    })
    @PostMapping("/board")
    public BoardPresentation create(@RequestBody BoardAddCommand boardAddCommand) {
        return boardService.create(boardAddCommand, context.getAccount().getId());
    }

    @ApiOperation(value = "게시판 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boardId", value = "게시판 고유키", dataType = "Long", paramType = "path", defaultValue = ""),
            @ApiImplicitParam(name = "boardAddCommand", value = "게시판 정보", required = true, dataType = "BoardAddCommand", defaultValue = "")
    })
    @PutMapping("/board/{boardId}")
    public BoardPresentation update(@PathVariable Long boardId, @RequestBody BoardAddCommand boardAddCommand) {
        return boardService.update(boardId, boardAddCommand, context.getAccount().getId());
    }

    @ApiOperation(value = "게시판 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "boardId", value = "게시판 고유키", required = true, dataType = "string", paramType = "path", defaultValue = "")
    })
    @DeleteMapping("/board/{boardId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable Long boardId) {
        boardService.delete(boardId, context.getAccount().getId());
    }

    @ApiOperation(value = "게시판 목록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "시작 목록갯수", required = true, dataType = "string", paramType = "queryString", defaultValue = "0"),
            @ApiImplicitParam(name = "limit", value = "리스트 갯수", required = true, dataType = "string", paramType = "queryString", defaultValue = "10")
    })
    @GetMapping("/boards")
    public PagePresentation<BoardPresentation> getList(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String properties,
            @RequestParam(required = false) String direction) {
        return boardService.getList(new PageCommand(start, limit, properties, direction));
    }

    @ApiOperation(value = "게시판 상세정보")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "게시판 ID", required = true, dataType = "string", paramType = "path", defaultValue = "0")
    })
    @GetMapping("/board/{id}")
    public BoardPresentation getBoard(@PathVariable Long id) {
        return boardService.get(id);
    }
}
