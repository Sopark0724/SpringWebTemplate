package com.web.template.org.domain.dao;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeptMemberDao {
    private final SqlSession sqlSession;

    private Long getNextId() {
        Long nextId = this.sqlSession.selectOne("DeptMemberDao.findNextId");
        return nextId != null ? nextId : 1L;
    }

    public List<LinkedHashMap> findByParent(Long departmentId) {
        return sqlSession.selectList("DeptMemberDao.findByDepartment", departmentId);
    }

    public LinkedHashMap save(LinkedHashMap deptMemberDto) {
        if (deptMemberDto.get("id") == null) {
            deptMemberDto.put("id", this.getNextId());
            sqlSession.insert("DeptMemberDao.insert", deptMemberDto);
        } else {
            sqlSession.update("DeptMemberDao.update", deptMemberDto);
        }

        return deptMemberDto;
    }
}
