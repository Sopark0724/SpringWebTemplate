package com.web.template.org.domain.dao;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DepartmentDao {
    private final SqlSession sqlSession;

    private Long getNextId() {
        Long nextId = this.sqlSession.selectOne("DepartmentDao.findNextId");
        return nextId != null ? nextId : 1L;
    }

    public LinkedHashMap findByParentIsNull() {
        return sqlSession.selectOne("DepartmentDao.findByParentIsNull");
    }

    public List<LinkedHashMap> findByParent(Long parentId) {
        return sqlSession.selectList("DepartmentDao.findByParent", parentId);
    }

    public LinkedHashMap findById(Long departmentId) {
        return sqlSession.selectOne("DepartmentDao.findById", departmentId);

    }

    public LinkedHashMap save(LinkedHashMap department) {

        if (department.get("id") == null) {
            department.put("id", this.getNextId());
            sqlSession.insert("DepartmentDao.insert", department);
        } else {
            sqlSession.update("DepartmentDao.update", department);
        }
        return department;

    }
}
