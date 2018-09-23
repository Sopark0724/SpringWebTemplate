package com.web.template.org.api;

import com.web.template.TemplateApplication;
import com.web.template.common.MockMvcHelper;
import com.web.template.org.domain.dao.DepartmentDao;
import com.web.template.org.domain.dao.DeptMemberDao;
import com.web.template.user.domain.dao.AccountDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TemplateApplication.class, MockMvcHelper.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class OrgControllerTest {

    @Autowired
    private MockMvcHelper mockMvcHelper;

    @Autowired
    private AccountDao accountRepository;

    @Autowired
    private DepartmentDao departmentRepository;

    @Autowired
    private DeptMemberDao deptMemberDao;

    @Test
    public void create() {
    }

    @Test
    public void getOrg() throws Exception {
        // Given
        LinkedHashMap<String, Object> accountMap = new LinkedHashMap();
        accountMap.put("name", "test1");
        accountMap.put("username", "test1");
        accountMap.put("password", "1234");
        accountMap.put("role", "USER");

        HashMap account = accountRepository.save(accountMap);
        Long accountId = (Long) account.get("id");

        LinkedHashMap<String, Object> addMap =new LinkedHashMap();
        LinkedHashMap<String, Object> memberAddMap =new LinkedHashMap();
        addMap.put("name", "ROOT");

        LinkedHashMap root = departmentRepository.save(addMap);
        addMap = new LinkedHashMap<>();
        addMap.put("name", "sub1");
        addMap.put("parent_id", (Long)root.get("id"));

        LinkedHashMap sub1 = departmentRepository.save(addMap);

        memberAddMap.put("account_id", accountId);
        memberAddMap.put("department_id", (Long)sub1.get("id"));
        deptMemberDao.save(memberAddMap);

        addMap = new LinkedHashMap<>();
        addMap.put("parent_id", (Long)root.get("id"));
        addMap.put("name", "sub2");
        LinkedHashMap sub2 = departmentRepository.save(addMap);

        memberAddMap = new LinkedHashMap<>();
        memberAddMap.put("account_id", accountId);
        memberAddMap.put("department_id", (Long)sub2.get("id"));
        deptMemberDao.save(memberAddMap);

        addMap = new LinkedHashMap<>();
        addMap.put("parent_id", (Long)root.get("id"));
        addMap.put("name", "sub3");
        LinkedHashMap sub3 = departmentRepository.save(addMap);


        addMap = new LinkedHashMap<>();
        addMap.put("parent_id", (Long)sub1.get("id"));
        addMap.put("name", "sub1_1");

        LinkedHashMap sub1_1 = departmentRepository.save(addMap);

        memberAddMap = new LinkedHashMap<>();
        memberAddMap.put("account_id", accountId);
        memberAddMap.put("department_id", (Long)sub1_1.get("id"));
        deptMemberDao.save(memberAddMap);

        addMap = new LinkedHashMap<>();
        addMap.put("parent_id", (Long)sub1.get("id"));
        addMap.put("name", "sub1_2");
        LinkedHashMap sub1_2 = departmentRepository.save(addMap);

        // When
        ResultActions resultAction =
                mockMvcHelper.perform(
                        get("/org"));

        // Then
        resultAction
                .andDo(print())
                .andExpect(status().isOk());

    }
}