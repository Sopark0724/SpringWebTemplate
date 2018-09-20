package com.web.template.org.api;

import com.web.template.TemplateApplication;
import com.web.template.common.MockMvcHelper;
import com.web.template.org.domain.dao.DepartmentDao;
import com.web.template.org.domain.dao.DeptMemberDao;
import com.web.template.org.domain.dto.DepartmentDto;
import com.web.template.org.domain.dto.DeptMemberDto;
import com.web.template.user.domain.dao.AccountDao;
import com.web.template.user.domain.dto.AccountDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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
        AccountDto account = accountRepository.save(new AccountDto("test1", "test1", "1234", "USER"));
        DepartmentDto root = departmentRepository.save(new DepartmentDto("ROOT", null));
        DepartmentDto sub1 = departmentRepository.save(new DepartmentDto("sub1", root.getId()));
        deptMemberDao.save(new DeptMemberDto(null, account.getId(), sub1.getId()));
        DepartmentDto sub2 = departmentRepository.save(new DepartmentDto("sub2", root.getId()));
        deptMemberDao.save(new DeptMemberDto(null, account.getId(), sub2.getId()));
        DepartmentDto sub3 = departmentRepository.save(new DepartmentDto("sub3", root.getId()));

        DepartmentDto sub1_1 = departmentRepository.save(new DepartmentDto("sub1_1", sub1.getId()));
        deptMemberDao.save(new DeptMemberDto(null, account.getId(), sub1_1.getId()));
        DepartmentDto sub1_2 = departmentRepository.save(new DepartmentDto("sub1_2", sub1.getId()));

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