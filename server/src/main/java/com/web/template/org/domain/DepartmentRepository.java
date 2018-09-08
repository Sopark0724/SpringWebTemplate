package com.web.template.org.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByParentIsNull();

    List<Department> findByParent(Department parent);
}
