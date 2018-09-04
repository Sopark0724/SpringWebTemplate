package com.web.template.board.application;

import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.board.domain.Board;
import com.web.template.board.domain.dao.BoardDao;
import com.web.template.common.application.data.PageListCommand;
import com.web.template.user.domain.Account;
import com.web.template.user.domain.dao.AccountDao;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service("boardServiceMyBatisImpl")
@RequiredArgsConstructor
public class BoardServiceMyBatisImpl implements BoardService {

    private final @NonNull
    BoardDao boardDao;

    private final @NonNull
    AccountDao accountDao;

    @Override
    public BoardPresentation create(BoardAddCommand boardAddCommand, Long creatorId) {
        Account account = this.accountDao.findById(creatorId);
        if (account == null) {
            throw new NoSuchElementException();
        }

        Board board = this.boardDao.save(new Board(account, boardAddCommand.getTitle(), boardAddCommand.getContents()));
        return BoardPresentation.convertFromEntity(board);
    }

    @Override
    public BoardPresentation update(Long boardId, BoardAddCommand boardAddCommand, Long requestUserId) {
        return null;
    }

    @Override
    public void delete(Long id, Long requestUserId) {

    }

    @Override
    public BoardPresentation get(Long boardId) {
        return null;
    }

    @Override
    public Page<BoardPresentation> getList(PageListCommand pageListCommand) {
        return null;
    }
}
