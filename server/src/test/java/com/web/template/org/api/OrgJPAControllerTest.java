package com.web.template.org.api;

import com.web.template.TemplateApplication;
import com.web.template.common.AbstractServiceHelper;
import com.web.template.common.MockMvcHelper;
import com.web.template.org.domain.Department;
import com.web.template.org.domain.DepartmentRepository;
import com.web.template.org.domain.DeptMember;
import com.web.template.user.application.AccountService;
import com.web.template.user.domain.Account;
import com.web.template.user.domain.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class})
public class OrgJPAControllerTest{

    @Autowired
    private MockMvcHelper mockMvcHelper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void create() {
    }

    @Test
    public void getOrg() throws Exception {
        // Given
        Account account = accountRepository.save(new Account("test1","test1","1234", "USER"));
        Department root = departmentRepository.save(new Department("ROOT", null));
        Department sub1 = departmentRepository.save(new Department("sub1", root));
        sub1.addMember(new DeptMember(account));
        Department sub2 = departmentRepository.save(new Department("sub2", root));
        sub2.addMember(new DeptMember(account));
        Department sub3 = departmentRepository.save(new Department("sub3", root));

        Department sub1_1 = departmentRepository.save(new Department("sub1_1", sub1));
        sub1_1.addMember(new DeptMember(account));
        Department sub1_2 = departmentRepository.save(new Department("sub1_2", sub1));

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        get("/v1/org"));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }
}