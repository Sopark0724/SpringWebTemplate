package com.web.template.board.application;

import com.web.template.attchments.data.AttachmentsPresentation;
import com.web.template.attchments.domain.dao.AttachmentsBoardMapDao;
import com.web.template.attchments.domain.dao.AttachmentsDao;
import com.web.template.attchments.util.AttachmentsUtil;
import com.web.template.board.application.data.BoardAddCommand;
import com.web.template.board.domain.dao.BoardDao;
import com.web.template.common.application.data.PageCommand;
import com.web.template.common.application.data.PagePresentation;
import com.web.template.user.domain.dao.AccountDao;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final @NonNull
    BoardDao boardDao;

    private final @NonNull
    AccountDao accountDao;

    private final @NonNull
    AttachmentsBoardMapDao attachmentsBoardMapDao;

    private final @NonNull
    AttachmentsDao attachmentsDao;


    private void checkOwner(LinkedHashMap account, LinkedHashMap board){
        assert account != null && account.get("id") != null;
        assert board != null && board.get("id") != null;

        if (!board.get("user_id").equals(account.get("id"))) {
            throw new InvalidParameterException();
        }
    }


    private LinkedHashMap getAccount(Long id) {
        LinkedHashMap account = this.accountDao.findById(id);
        if (account == null) {
            throw new NoSuchElementException();
        }
        return account;
    }

    private LinkedHashMap getBoard(Long id) {

        LinkedHashMap board = this.boardDao.findById(id);

        if (board == null) {
            throw new NoSuchElementException();
        }
        return board;
    }

    @Override
    public Map create(BoardAddCommand boardAddCommand, Long creatorId) {

        HashMap account = this.getAccount(creatorId);
        LinkedHashMap<String, Object> board = new LinkedHashMap<>();
        board.put("user_id", account.get("id"));
        board.put("title", boardAddCommand.getTitle());
        board.put("contents", boardAddCommand.getContents());

        return this.boardDao.save(board);
    }

    @Override
    public Map update(Long boardId, BoardAddCommand boardAddCommand, Long requestUserId) {
        LinkedHashMap account = this.getAccount(requestUserId);
        LinkedHashMap board = this.getBoard(boardId);

        this.checkOwner(account, board);


        board.put("title", boardAddCommand.getTitle());
        board.put("contents", boardAddCommand.getContents());

        board = this.boardDao.save(board);

        board.put("writer_name", account.get("name"));

        return board;

    }

    @Override
    public void delete(Long id, Long requestUserId) {
        LinkedHashMap account = this.getAccount(requestUserId);
        LinkedHashMap board = this.getBoard(id);
        this.checkOwner(account, board);

        this.boardDao.delete(board);
    }

    @Override
    public Map get(Long boardId) {

        LinkedHashMap board = this.getBoard(boardId);
        HashMap account = this.getAccount((Long) board.get("user_id"));

        List<LinkedHashMap> attachmentsBoardMaps = this.attachmentsBoardMapDao.findByBoard(board);
        List<LinkedHashMap> attachmentsDtos = new ArrayList<>();
        for (LinkedHashMap attachmentsBoardMap : attachmentsBoardMaps) {
            attachmentsDtos.add(this.attachmentsDao.findById((Long) attachmentsBoardMap.get("attachments_id")));
        }

        board.put("writer_name", account.get("name"));

        List<AttachmentsPresentation> attachmentsPresentations =
                attachmentsDtos.stream().map(att -> AttachmentsPresentation.convertFrom(att)).collect(Collectors.toList());

        board.put("attachments", attachmentsPresentations);

        return board;
    }

    @Override
    public PagePresentation<Map> getList(PageCommand pageListCommand) {

        int totalCount = this.boardDao.getTotalCount();
        List<LinkedHashMap> boardList = this.boardDao.findAll(pageListCommand);

        List<Map> result = new ArrayList<>();

        for (LinkedHashMap board : boardList) {
            LinkedHashMap account = this.getAccount((Long) board.get("user_id"));
            board.put("writerName", account.get("name"));
            result.add(board);
        }

        return new PagePresentation<Map>(true, totalCount, result);

    }
}
