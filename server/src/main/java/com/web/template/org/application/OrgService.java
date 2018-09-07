package com.web.template.org.application;

import com.web.template.org.application.data.DepartmentAddCommand;
import com.web.template.org.application.data.DepartmentPresentation;
import com.web.template.org.application.data.DeptMemberAddCommand;
import com.web.template.org.application.data.OrgPresentation;
import org.springframework.transaction.annotation.Transactional;

public interface OrgService {
    DepartmentPresentation create(DepartmentAddCommand addCommand);

    @Transactional
    OrgPresentation getTree();

    void addMember(DeptMemberAddCommand addCommand);
}
