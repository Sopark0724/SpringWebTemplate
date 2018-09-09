package com.web.template.attchments.domain.dao;

import com.web.template.attchments.domain.dto.AttachmentsDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttachmentsDao {

    private final @NonNull
    SqlSession sqlSession;

    public List<AttachmentsDto> findAll() {
        return this.sqlSession.selectList("AttachmentsDAO.findAll");
    }

    public AttachmentsDto findById(Long id) {
        return this.sqlSession.selectOne("AttachmentsDAO.findById", id);
    }
    public AttachmentsDto findByFakename(String fakename) {
        return this.sqlSession.selectOne("AttachmentsDAO.findByFakename", fakename);
    }

    private Long getNextId() {
        Long nextId = this.sqlSession.selectOne("AttachmentsDAO.findNextId");
        return nextId == null ? 1L : nextId;
    }

    public List<AttachmentsDto> saveAll(List<AttachmentsDto> attachmentsDtos) {
        List<AttachmentsDto> savedList = new ArrayList<>();
        for (AttachmentsDto attachmentsDto : attachmentsDtos) {
            savedList.add(this.save(attachmentsDto));
        }
        return savedList;
    }

    public AttachmentsDto save(AttachmentsDto attachmentsDto) {
        if (attachmentsDto.getId() == null) {
            attachmentsDto.setId(this.getNextId());
            sqlSession.insert("AttachmentsDAO.insert", attachmentsDto);
        } else {
            sqlSession.update("AttachmentsDAO.update", attachmentsDto);
        }
        return attachmentsDto;
    }

    public void deleteAll(List<AttachmentsDto> attachmentsDtos) {
        for (AttachmentsDto attachmentsDto : attachmentsDtos) {
            this.delete(attachmentsDto);
        }
    }

    public void delete(AttachmentsDto attachmentsDto) {
        sqlSession.delete("AttachmentsDAO.delete", attachmentsDto);
    }


}
