package com.web.template.org.application;

import com.web.template.TemplateApplication;
import com.web.template.common.AbstractServiceHelper;
import com.web.template.common.AccountTestService;
import com.web.template.common.MockMvcHelper;
import com.web.template.org.application.data.DepartmentAddCommand;
import com.web.template.org.application.data.DepartmentPresentation;
import com.web.template.org.application.data.DeptMemberAddCommand;
import com.web.template.org.application.data.OrgPresentation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class, AccountTestService.class})
@Slf4j
public class OrgServiceTest extends AbstractServiceHelper {

    @Autowired
    private OrgService service;

    @Autowired
    private AccountTestService accountService;

    @Test
    public void create() {
        // Given
        DepartmentAddCommand command = new DepartmentAddCommand(null, "SINGLE_ROOT");

        // When
        DepartmentPresentation dept = service.create(command);

        // Then
        Assert.assertNotNull(dept);
    }

    @Test
    public void getTree() {
        // Given
        Map account = accountService.getTestAccount();

        DepartmentPresentation dept = service.create(new DepartmentAddCommand(null, "test"));
        service.addMember(new DeptMemberAddCommand(dept.getId(), (Long)account.get("id")));

        DepartmentPresentation dept1 = service.create(new DepartmentAddCommand(dept.getId(), "test1"));

        DepartmentPresentation dept2 = service.create(new DepartmentAddCommand(dept.getId(), "test2"));
        service.addMember(new DeptMemberAddCommand(dept2.getId(), (Long)account.get("id")));

        DepartmentPresentation dept1_1 = service.create(new DepartmentAddCommand(dept1.getId(), "test1_1"));
        service.addMember(new DeptMemberAddCommand(dept1_1.getId(), (Long)account.get("id")));
        service.addMember(new DeptMemberAddCommand(dept1_1.getId(), (Long)account.get("id")));

        DepartmentPresentation dept1_2 = service.create(new DepartmentAddCommand(dept1.getId(), "test1_2"));
        service.addMember(new DeptMemberAddCommand(dept1_2.getId(), (Long)account.get("id")));
        service.addMember(new DeptMemberAddCommand(dept1_2.getId(), (Long)account.get("id")));
        service.addMember(new DeptMemberAddCommand(dept1_2.getId(), (Long)account.get("id")));

        // When
        OrgPresentation tree = service.getTree();

        // Then
        System.out.println(tree.toString());
        log.info(tree.toString());
    }
}