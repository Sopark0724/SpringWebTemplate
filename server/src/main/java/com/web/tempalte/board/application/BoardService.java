package com.web.tempalte.board.application;

import com.web.tempalte.board.application.data.BoardAddCommand;
import com.web.tempalte.board.application.data.BoardPresentation;
import com.web.tempalte.board.domain.Board;
import com.web.tempalte.board.domain.BoardRepository;
import com.web.tempalte.user.domain.Account;
import com.web.tempalte.user.domain.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AccountRepository accountRepository;

    public BoardPresentation create(BoardAddCommand boardAddCommand, Long userId){
        Account account = accountRepository.findById(userId).orElseThrow(() -> new NoSuchElementException());
        Board board = boardRepository.save(new Board(account, boardAddCommand.getTitle(), boardAddCommand.getContents()));

        return new BoardPresentation(board.getId(), account.getName(), board.getTitle(), board.getContents(), board.getCreatedAt(), board.getUpdatedAt());
    }

    public BoardPresentation update(Long boardId, BoardAddCommand boardAddCommand, Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new NoSuchElementException());
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException());

        if(board.canNotUpdate(account)){
            throw new InvalidParameterException();
        }

        board.update(boardAddCommand.getTitle(), boardAddCommand.getContents());

        boardRepository.save(board);

        return new BoardPresentation(board.getId(), account.getName(), board.getTitle(), board.getContents(), board.getCreatedAt(), board.getUpdatedAt());

    }
}
