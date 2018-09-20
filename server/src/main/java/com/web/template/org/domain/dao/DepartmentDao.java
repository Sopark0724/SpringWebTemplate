package com.web.template.org.domain.dao;

import com.web.template.org.domain.dto.DepartmentDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DepartmentDao {
    private final SqlSession sqlSession;

    private Long getNextId() {
        Long nextId = this.sqlSession.selectOne("DepartmentDao.findNextId");
        return nextId != null ? nextId : 1L;
    }

    public DepartmentDto findByParentIsNull() {
        return sqlSession.selectOne("DepartmentDao.findByParentIsNull");
    }

    public List<DepartmentDto> findByParent(Long parentId) {
        return sqlSession.selectList("DepartmentDao.findByParent", parentId);
    }

    public DepartmentDto findById(Long departmentId) {
        return sqlSession.selectOne("DepartmentDao.findById", departmentId);

    }

    public DepartmentDto save(DepartmentDto department) {

        if (department.getId() == null) {
            department.setId(this.getNextId());
            sqlSession.insert("DepartmentDao.insert", department);
        } else {
            sqlSession.update("DepartmentDao.update", department);
        }
        return department;

    }
}
