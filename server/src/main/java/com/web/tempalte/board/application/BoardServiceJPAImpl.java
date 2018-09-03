package com.web.tempalte.board.application;

import com.web.tempalte.board.application.data.BoardAddCommand;
import com.web.tempalte.board.application.data.BoardPresentation;
import com.web.tempalte.board.domain.Board;
import com.web.tempalte.board.domain.BoardRepository;
import com.web.tempalte.common.application.data.PageListCommand;
import com.web.tempalte.common.util.PageRequestUtil;
import com.web.tempalte.user.domain.Account;
import com.web.tempalte.user.domain.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service(value = "boardServiceJPAImpl")
public class BoardServiceJPAImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public BoardPresentation create(BoardAddCommand boardAddCommand, Long creatorId){
        Account account = accountRepository.findById(creatorId).orElseThrow(() -> new NoSuchElementException());
        Board board = boardRepository.save(new Board(account, boardAddCommand.getTitle(), boardAddCommand.getContents()));

        return new BoardPresentation(board.getId(), account.getName(), board.getTitle(), board.getContents(), board.getCreatedAt(), board.getUpdatedAt());
    }

    @Override
    @Transactional
    public BoardPresentation update(Long boardId, BoardAddCommand boardAddCommand, Long requestUserId) {
        Account account = accountRepository.findById(requestUserId).orElseThrow(() -> new NoSuchElementException());
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException());

        if(board.canNotUpdate(account)){
            throw new InvalidParameterException();
        }

        board.update(boardAddCommand.getTitle(), boardAddCommand.getContents());

        boardRepository.save(board);

        return new BoardPresentation(board.getId(), account.getName(), board.getTitle(), board.getContents(), board.getCreatedAt(), board.getUpdatedAt());

    }

    @Override
    public void delete(Long id, Long requestUserId) {
        Account account = accountRepository.findById(requestUserId).orElseThrow(() -> new NoSuchElementException());
        Board board = boardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());

        if(board.canNotDelete(account)){
            throw new InvalidParameterException();
        }

        boardRepository.delete(board);
    }

    @Override
    @Transactional
    public BoardPresentation get(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException());
        return new BoardPresentation(board.getId(), board.getWriterName(), board.getTitle(), board.getContents(), board.getCreatedAt(), board.getUpdatedAt());

    }

    @Override
    public Page<BoardPresentation> getList(PageListCommand command) {
        Page<Board> list = boardRepository.findAll(PageRequestUtil.create(command));
        List<BoardPresentation> boardPresentations;

        boardPresentations = list.getContent().stream()
                .map(BoardPresentation::convertFromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(boardPresentations, list.getPageable(), list.getTotalElements());
    }
}
