package com.web.template.attchments.domain.dao;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttachmentsBoardMapDao {

    private final @NonNull
    SqlSession sqlSession;

    public List<LinkedHashMap> findAll() {
        return this.sqlSession.selectList("AttachmentsBoardMapDAO.findAll");
    }

    public LinkedHashMap findById(Long id) {
        return this.sqlSession.selectOne("AttachmentsBoardMapDAO.findById", id);
    }

    public List<LinkedHashMap> findByBoard(LinkedHashMap board) {
        return this.sqlSession.selectList("AttachmentsBoardMapDAO.findByBoard", board);
    }

    private Long getNextId() {
        Long nextId = this.sqlSession.selectOne("AttachmentsBoardMapDAO.findNextId");
        return nextId == null ? 1L : nextId;
    }

    public List<LinkedHashMap> saveAll(List<LinkedHashMap> attachmentsDtos) {
        List<LinkedHashMap> savedList = new ArrayList<>();
        for (LinkedHashMap attachmentsDto : attachmentsDtos) {
            savedList.add(this.save(attachmentsDto));
        }
        return savedList;
    }

    public LinkedHashMap save(LinkedHashMap attachmentsDto) {
        if (attachmentsDto.get("id") == null) {
            attachmentsDto.put("id",this.getNextId());
            sqlSession.insert("AttachmentsBoardMapDAO.insert", attachmentsDto);
        } else {
            sqlSession.update("AttachmentsBoardMapDAO.update", attachmentsDto);
        }
        return attachmentsDto;
    }

    public void deleteAll(List<LinkedHashMap> attachmentsDtos) {
        for (LinkedHashMap attachmentsDto : attachmentsDtos) {
            this.delete(attachmentsDto);
        }
    }

    public void delete(LinkedHashMap attachmentsDto) {
        sqlSession.delete("AttachmentsBoardMapDAO.delete", attachmentsDto);
    }


}
