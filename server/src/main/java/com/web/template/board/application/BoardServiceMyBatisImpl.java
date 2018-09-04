package com.web.template.board.application;

import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.board.domain.dao.BoardDao;
import com.web.template.board.domain.dto.BoardDto;
import com.web.template.common.application.data.PageListCommand;
import com.web.template.user.domain.dao.AccountDao;
import com.web.template.user.domain.dto.AccountDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
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
        AccountDto account = this.accountDao.findById(creatorId);
        if (account == null) {
            throw new NoSuchElementException();
        }

        BoardDto board = this.boardDao.save(new BoardDto(account, boardAddCommand.getTitle(), boardAddCommand.getContents()));
        return BoardPresentation.convertFromDto(board, account);
    }

    @Override
    public BoardPresentation update(Long boardId, BoardAddCommand boardAddCommand, Long requestUserId) {
        AccountDto account = this.accountDao.findById(requestUserId);
        BoardDto board = this.boardDao.findById(boardId);
        if (account == null || board == null) {
            throw new NoSuchElementException();
        }

        if (board.canNotUpdate(account)) {
            throw new InvalidParameterException();
        }

        board.update(boardAddCommand.getTitle(), boardAddCommand.getContents());

        board = this.boardDao.save(board);

        return new BoardPresentation(board.getId(), account.getName(), board.getTitle(), board.getContents(), board.getCreated_at(), board.getUpdated_at());
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
