package com.web.template.org.domain.dao;

import com.web.template.org.domain.dto.DeptMemberDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeptMemberDao {
    private final SqlSession sqlSession;

    private Long getNextId() {
        Long nextId = this.sqlSession.selectOne("DeptMemberDao.findNextId");
        return nextId != null ? nextId : 1L;
    }

    public List<DeptMemberDto> findByParent(Long departmentId){
        return sqlSession.selectList("DeptMemberDao.findByDepartment", departmentId);
    }

    public DeptMemberDto save(DeptMemberDto deptMemberDto) {
        if(deptMemberDto.getId() == null) {
            deptMemberDto.setId(this.getNextId());
            sqlSession.insert("DeptMemberDao.insert", deptMemberDto);
        }else{
            sqlSession.update("DeptMemberDao.update", deptMemberDto);
        }

        return deptMemberDto;
    }
}
