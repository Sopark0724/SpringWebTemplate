package com.web.template.board.application;

import com.web.template.attchments.domain.Attachments;
import com.web.template.attchments.mapper.AttachmentsBoardMap;
import com.web.template.attchments.repository.AttachmentsBoardMapRepository;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.board.domain.Board;
import com.web.template.board.domain.BoardRepository;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import com.web.template.common.util.PageRequestUtil;
import com.web.template.user.domain.Account;
import com.web.template.user.domain.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service(value = "boardServiceJPAImpl")
public class BoardServiceJPAImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AttachmentsBoardMapRepository attachmentsBoardMapRepository;

    @Override
    public BoardPresentation create(BoardAddCommand boardAddCommand, Long creatorId) {
        Account account = accountRepository.findById(creatorId).orElseThrow(() -> new NoSuchElementException());
        Board board = boardRepository.save(new Board(account, boardAddCommand.getTitle(), boardAddCommand.getContents()));

        return BoardPresentation.convertFromEntity(board);
    }

    @Override
    @Transactional
    public BoardPresentation update(Long boardId, BoardAddCommand boardAddCommand, Long requestUserId) {
        Account account = accountRepository.findById(requestUserId).orElseThrow(() -> new NoSuchElementException());
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException());

        if (board.canNotUpdate(account)) {
            throw new InvalidParameterException();
        }

        board.update(boardAddCommand.getTitle(), boardAddCommand.getContents());

        boardRepository.save(board);

        return BoardPresentation.convertFromEntity(board);
    }

    @Override
    public void delete(Long id, Long requestUserId) {
        Account account = accountRepository.findById(requestUserId).orElseThrow(() -> new NoSuchElementException());
        Board board = boardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());

        if (board.canNotDelete(account)) {
            throw new InvalidParameterException();
        }

        boardRepository.delete(board);
    }

    @Override
    @Transactional
    public BoardPresentation get(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException());
        List<AttachmentsBoardMap> attachmentsBoardMaps = this.attachmentsBoardMapRepository.findByBoard(board);
        List<Attachments> attachments = attachmentsBoardMaps.stream().map(AttachmentsBoardMap::getAttachments).collect(Collectors.toList());

        return BoardPresentation.convertFromEntity(board, attachments);
    }

    @Override
    public PagePresentation<BoardPresentation> getList(PageCommand command) {
        Page<Board> list = boardRepository.findAll(PageRequestUtil.create(command));
        List<BoardPresentation> boardPresentations;

        boardPresentations = list.getContent().stream()
                .map(BoardPresentation::convertFromEntity)
                .collect(Collectors.toList());

        return new PagePresentation<>(true, list.getTotalElements(), boardPresentations);
    }
}
