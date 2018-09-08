package com.web.template.org.application;

import com.web.template.org.application.data.DepartmentAddCommand;
import com.web.template.org.application.data.DepartmentPresentation;
import com.web.template.org.application.data.DeptMemberAddCommand;
import com.web.template.org.application.data.OrgPresentation;
import com.web.template.org.domain.Department;
import com.web.template.org.domain.DepartmentRepository;
import com.web.template.org.domain.DeptMember;
import com.web.template.org.domain.dao.DepartmentDao;
import com.web.template.org.domain.dao.DeptMemberDao;
import com.web.template.org.domain.dto.DepartmentDto;
import com.web.template.org.domain.dto.DeptMemberDto;
import com.web.template.user.domain.Account;
import com.web.template.user.domain.AccountRepository;
import com.web.template.user.domain.dao.AccountDao;
import com.web.template.user.domain.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Service(value = "orgSercviceMyBatisImpl")
public class OrgServiceMyBatisImpl implements OrgService {

    @Autowired
    private DepartmentDao deptRepository;

    @Autowired
    private DeptMemberDao deptMemberRepository;

    @Autowired
    private AccountDao accountRepository;

    @Override
    public DepartmentPresentation create(DepartmentAddCommand addCommand){
        DepartmentDto parent = null;
        if(addCommand.getParentId() != null){
            parent = deptRepository.findById(addCommand.getParentId()).orElseThrow(() -> new NoSuchElementException());
        }

        DepartmentDto result = deptRepository.save(new DepartmentDto(addCommand.getName(), addCommand.getParentId()));
        return new DepartmentPresentation(result.getId(), result.getName(), result.getParentId());
    }

    @Override
    @Transactional
    public OrgPresentation getTree(){
        DepartmentDto root = deptRepository.findByParentIsNull();

        List<DepartmentDto> childrend = deptRepository.findByParent(root.getId());
        List<OrgPresentation> orgTree = new ArrayList<>();
        for(DepartmentDto departmentDto : childrend){
            orgTree.add(this.recursiveDept(departmentDto));
        }

        boolean expanded = CollectionUtils.isEmpty(orgTree) ? false : true;
        return new OrgPresentation("ROOT", expanded, orgTree, false);
    }

    private OrgPresentation recursiveDept(DepartmentDto department){
        List<DepartmentDto> departments = deptRepository.findByParent(department.getId());

        OrgPresentation orgPresentation;
        if(!CollectionUtils.isEmpty(departments)){
            orgPresentation = new OrgPresentation();

            List<OrgPresentation> children = new ArrayList<>();
            for(DepartmentDto departmentDto : departments) {
                children.add(this.recursiveDept(departmentDto));
            }

            orgPresentation.setChildren(children);
            orgPresentation.setExpanded(true);

            return orgPresentation;
        }

        List<DeptMemberDto> deptMembers = deptMemberRepository.findByParent(department.getId());
        List<OrgPresentation> members = new ArrayList<>();
        for(DeptMemberDto deptMember : deptMembers) {
            AccountDto account = accountRepository.findById(deptMember.getAccount_id());
            members.add(new OrgPresentation(account.getName(), false, null, true));
        }

        orgPresentation = new OrgPresentation(department.getName(), false, members, true);

        return orgPresentation;
    }

    @Override
    public void addMember(DeptMemberAddCommand addCommand) {
        deptMemberRepository.save(new DeptMemberDto(addCommand.getId(), addCommand.getUserId(), addCommand.getDeptId()));
    }
}
