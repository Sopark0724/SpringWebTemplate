package com.web.template.org.application;

import com.web.template.org.application.data.DepartmentAddCommand;
import com.web.template.org.application.data.DepartmentPresentation;
import com.web.template.org.application.data.DeptMemberAddCommand;
import com.web.template.org.application.data.OrgPresentation;
import com.web.template.org.domain.Department;
import com.web.template.org.domain.DepartmentRepository;
import com.web.template.org.domain.DeptMember;
import com.web.template.user.domain.Account;
import com.web.template.user.domain.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.*;

@Service(value = "orgSercviceJPAImpl")
public class OrgServiceJPAImpl implements OrgService {

    @Autowired
    private DepartmentRepository deptRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public DepartmentPresentation create(DepartmentAddCommand addCommand){
        Department parent = null;
        if(addCommand.getParentId() != null){
            parent = deptRepository.findById(addCommand.getParentId()).orElseThrow(() -> new NoSuchElementException());
        }

        Department result = deptRepository.save(new Department(addCommand.getName(), parent));
        return new DepartmentPresentation(result.getId(), result.getName(), result.getParentId());
    }

    @Override
    @Transactional
    public OrgPresentation getTree(){
        Department root = deptRepository.findByParentIsNull();

        List<OrgPresentation> orgTree = root.getChildren().stream()
                .map(this::recursiveDept)
                .collect(toList());
        boolean expanded = CollectionUtils.isEmpty(orgTree) ? false : true;
        return new OrgPresentation("ROOT", expanded, orgTree, false);
    }

    private OrgPresentation recursiveDept(Department department){
        List<Department> departments = department.getChildren();
        OrgPresentation orgPresentation;
        if(!CollectionUtils.isEmpty(departments)){
            orgPresentation = new OrgPresentation();
            List<OrgPresentation> children = departments.stream()
                    .map(this::recursiveDept)
                    .collect(toList());
            orgPresentation.setChildren(children);
            orgPresentation.setExpanded(true);

            return orgPresentation;
        }

        List<OrgPresentation> members = department.getMembers().stream()
                .map((this::convetMember))
                .collect(toList());

        orgPresentation = new OrgPresentation(department.getName(), false, members, true);

        return orgPresentation;
    }

    private OrgPresentation convetMember(DeptMember deptMember){
        return new OrgPresentation(deptMember.getName(), null, null, true);
    }

    @Override
    public void addMember(DeptMemberAddCommand addCommand) {
        Department department = deptRepository.findById(addCommand.getDeptId()).orElseThrow(() -> new NoSuchElementException());
        Account user = accountRepository.findById(addCommand.getUserId()).orElseThrow(() -> new NoSuchElementException());
        DeptMember member = new DeptMember(user);
        department.addMember(member);
        deptRepository.save(department);
    }
}
