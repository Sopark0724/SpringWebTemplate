package com.web.template.board.application;

import com.web.template.attchments.domain.dao.AttachmentsBoardMapDao;
import com.web.template.attchments.domain.dao.AttachmentsDao;
import com.web.template.attchments.domain.dto.AttachmentsBoardMapDto;
import com.web.template.attchments.domain.dto.AttachmentsDto;
import com.web.template.attchments.mapper.AttachmentsBoardMap;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.application.data.BoardPresentation;
import com.web.template.board.domain.dao.BoardDao;
import com.web.template.board.domain.dto.BoardDto;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import com.web.template.user.domain.dao.AccountDao;
import com.web.template.user.domain.dto.AccountDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service("boardServiceMyBatisImpl")
@RequiredArgsConstructor
public class BoardServiceMyBatisImpl implements BoardService {

    private final @NonNull
    BoardDao boardDao;

    private final @NonNull
    AccountDao accountDao;

    private final @NonNull
    AttachmentsBoardMapDao attachmentsBoardMapDao;

    private final @NonNull
    AttachmentsDao attachmentsDao;

    private AccountDto getAccount(Long id) {
        AccountDto account = this.accountDao.findById(id);
        if (account == null) {
            throw new NoSuchElementException();
        }
        return account;
    }

    private BoardDto getBoard(Long id) {

        BoardDto board = this.boardDao.findById(id);

        if (board == null) {
            throw new NoSuchElementException();
        }
        return board;
    }

    @Override
    public BoardPresentation create(BoardAddCommand boardAddCommand, Long creatorId) {

        AccountDto account = this.getAccount(creatorId);

        BoardDto board = this.boardDao.save(new BoardDto(account, boardAddCommand.getTitle(), boardAddCommand.getContents()));
        return BoardPresentation.convertFromDto(board, account);
    }

    @Override
    public BoardPresentation update(Long boardId, BoardAddCommand boardAddCommand, Long requestUserId) {
        AccountDto account = this.getAccount(requestUserId);
        BoardDto board = this.getBoard(boardId);

        if (board.canNotUpdate(account)) {
            throw new InvalidParameterException();
        }

        board.update(boardAddCommand.getTitle(), boardAddCommand.getContents());

        board = this.boardDao.save(board);

        return BoardPresentation.convertFromDto(board, account);
    }

    @Override
    public void delete(Long id, Long requestUserId) {
        AccountDto account = this.getAccount(requestUserId);
        BoardDto board = this.getBoard(id);
        if (board.canNotDelete(account)) {
            throw new InvalidParameterException();
        }

        this.boardDao.delete(board);
    }

    @Override
    public BoardPresentation get(Long boardId) {
        BoardDto board = this.getBoard(boardId);
        AccountDto account = this.getAccount(board.getUser_id());

        List<AttachmentsBoardMapDto> attachmentsBoardMaps = this.attachmentsBoardMapDao.findByBoard(board);
        List<AttachmentsDto> attachmentsDtos = new ArrayList<>();
        for (AttachmentsBoardMapDto attachmentsBoardMap : attachmentsBoardMaps) {
            attachmentsDtos.add(this.attachmentsDao.findById(attachmentsBoardMap.getAttachments_id())) ;
        }

        return BoardPresentation.convertFromDto(board, account, attachmentsDtos);
    }

    @Override
    public PagePresentation<BoardPresentation> getList(PageCommand pageListCommand) {
        Page<BoardDto> list = this.boardDao.findAll(pageListCommand);

        List<BoardPresentation> result = new ArrayList<>();

        for (BoardDto boardDto : list.getContent()) {
            result.add(BoardPresentation.convertFromDto(boardDto, this.getAccount(boardDto.getUser_id())));
        }

        return new PagePresentation<>(true, list.getTotalElements(), result);
    }
}
