package com.web.template.attchments.domain.dao;

import com.web.template.attchments.domain.dto.AttachmentsBoardMapDto;
import com.web.template.board.domain.dto.BoardDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttachmentsBoardMapDao {

    private final @NonNull
    SqlSession sqlSession;

    public List<AttachmentsBoardMapDto> findAll() {
        return this.sqlSession.selectList("AttachmentsBoardMapDAO.findAll");
    }

    public AttachmentsBoardMapDto findById(Long id) {
        return this.sqlSession.selectOne("AttachmentsBoardMapDAO.findById", id);
    }

    public List<AttachmentsBoardMapDto> findByBoard(BoardDto boardDto) {
        return this.sqlSession.selectList("AttachmentsBoardMapDAO.findByBoard", boardDto);
    }

    private Long getNextId() {
        return this.sqlSession.selectOne("AttachmentsBoardMapDAO.findNextId");
    }

    public List<AttachmentsBoardMapDto> saveAll(List<AttachmentsBoardMapDto> attachmentsDtos) {
        List<AttachmentsBoardMapDto> savedList = new ArrayList<>();
        for (AttachmentsBoardMapDto attachmentsDto : attachmentsDtos) {
            savedList.add(this.save(attachmentsDto));
        }
        return savedList;
    }

    public AttachmentsBoardMapDto save(AttachmentsBoardMapDto attachmentsDto) {
        if (attachmentsDto.getId() == null) {
            attachmentsDto.setId(this.getNextId());
            sqlSession.insert("AttachmentsBoardMapDAO.insert", attachmentsDto);
        } else {
            sqlSession.update("AttachmentsBoardMapDAO.update", attachmentsDto);
        }
        return attachmentsDto;
    }

    public void deleteAll(List<AttachmentsBoardMapDto> attachmentsDtos) {
        for (AttachmentsBoardMapDto attachmentsDto : attachmentsDtos) {
            this.delete(attachmentsDto);
        }
    }

    public void delete(AttachmentsBoardMapDto attachmentsDto) {
        sqlSession.delete("AttachmentsBoardMapDAO.delete", attachmentsDto);
    }


}
