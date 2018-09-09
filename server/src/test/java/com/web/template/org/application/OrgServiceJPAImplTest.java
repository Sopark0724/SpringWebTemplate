package com.web.template.org.application;

import com.web.template.common.AbstractServiceHelper;
import com.web.template.common.AccountTestService;
import com.web.template.org.application.data.DepartmentAddCommand;
import com.web.template.org.application.data.DepartmentPresentation;
import com.web.template.org.application.data.DeptMemberAddCommand;
import com.web.template.org.application.data.OrgPresentation;
import com.web.template.user.application.AccountService;
import com.web.template.user.application.data.AccountAddCommand;
import com.web.template.user.application.data.AccountPresentation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@Transactional
@Slf4j
public class OrgServiceJPAImplTest extends AbstractServiceHelper {

    @Autowired
    private OrgServiceJPAImpl service;

    @Autowired
    private AccountTestService accountService;

    @Test
    public void create() {
        // Given
        DepartmentAddCommand command = new DepartmentAddCommand(null, "test");

        // When
        DepartmentPresentation dept = service.create(command);

        // Then
        Assert.assertNotNull(dept);
    }

    @Test
    public void getTree() {
        // Given
        AccountPresentation account = accountService.getTestAccount();

        DepartmentPresentation dept = service.create(new DepartmentAddCommand(null, "test"));
        service.addMember(new DeptMemberAddCommand(dept.getId(), account.getId()));

        DepartmentPresentation dept1 = service.create(new DepartmentAddCommand(dept.getId(), "test1"));

        DepartmentPresentation dept2 = service.create(new DepartmentAddCommand(dept.getId(), "test2"));
        service.addMember(new DeptMemberAddCommand(dept2.getId(), account.getId()));

        DepartmentPresentation dept1_1 = service.create(new DepartmentAddCommand(dept1.getId(), "test1_1"));
        service.addMember(new DeptMemberAddCommand(dept1_1.getId(), account.getId()));
        service.addMember(new DeptMemberAddCommand(dept1_1.getId(), account.getId()));

        DepartmentPresentation dept1_2 = service.create(new DepartmentAddCommand(dept1.getId(), "test1_2"));
        service.addMember(new DeptMemberAddCommand(dept1_2.getId(), account.getId()));
        service.addMember(new DeptMemberAddCommand(dept1_2.getId(), account.getId()));
        service.addMember(new DeptMemberAddCommand(dept1_2.getId(), account.getId()));

        // When
        OrgPresentation tree = service.getTree();

        // Then
        System.out.println(tree.toString());
        log.info(tree.toString());
    }
}