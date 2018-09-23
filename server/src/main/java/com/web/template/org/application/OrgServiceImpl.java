package com.web.template.org.application;

import com.web.template.org.application.data.DepartmentAddCommand;
import com.web.template.org.application.data.DepartmentPresentation;
import com.web.template.org.application.data.DeptMemberAddCommand;
import com.web.template.org.application.data.OrgPresentation;
import com.web.template.org.domain.dao.DepartmentDao;
import com.web.template.org.domain.dao.DeptMemberDao;
import com.web.template.user.domain.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class OrgServiceImpl implements OrgService {

    @Autowired
    private DepartmentDao deptRepository;

    @Autowired
    private DeptMemberDao deptMemberRepository;

    @Autowired
    private AccountDao accountRepository;

    @Override
    public DepartmentPresentation create(DepartmentAddCommand addCommand) {
        Map parent = null;
        if (addCommand.getParentId() != null) {
            parent = deptRepository.findById(addCommand.getParentId());
            assert parent != null;
        }


        LinkedHashMap<String, Object> addMap = new LinkedHashMap();
        addMap.put("name", addCommand.getName());
        addMap.put("parent_id", addCommand.getParentId());


        Map result = deptRepository.save(addMap);
        return new DepartmentPresentation((Long)result.get("id"), (String)result.get("name"), (Long)result.get("parent_id"));
    }

    @Override
    @Transactional
    public OrgPresentation getTree() {
        Map root = deptRepository.findByParentIsNull();

        List<LinkedHashMap> childrend = deptRepository.findByParent((Long)root.get("id"));

        List<OrgPresentation> orgTree = new ArrayList<>();
        for (LinkedHashMap departmentDto : childrend) {
            orgTree.add(this.recursiveDept(departmentDto));
        }

        boolean expanded = CollectionUtils.isEmpty(orgTree) ? false : true;
        return new OrgPresentation("ROOT", expanded, orgTree, false);
    }

    private OrgPresentation recursiveDept(LinkedHashMap department) {
        List<LinkedHashMap> departments = deptRepository.findByParent((Long) department.get("id"));

        OrgPresentation orgPresentation;
        if (!CollectionUtils.isEmpty(departments)) {
            orgPresentation = new OrgPresentation();

            List<OrgPresentation> children = new ArrayList<>();
            for (LinkedHashMap departmentDto : departments) {
                children.add(this.recursiveDept(departmentDto));
            }

            orgPresentation.setChildren(children);
            orgPresentation.setExpanded(true);

            return orgPresentation;
        }

        List<LinkedHashMap> deptMembers = deptMemberRepository.findByParent((Long) department.get("id"));
        List<OrgPresentation> members = new ArrayList<>();
        for (LinkedHashMap deptMember : deptMembers) {
            HashMap account = accountRepository.findById(deptMember.get("account_id"));
            members.add(new OrgPresentation((String) account.get("name"), false, null, true));
        }

        orgPresentation = new OrgPresentation((String) department.get("name"), false, members, true);

        return orgPresentation;
    }

    @Override
    public void addMember(DeptMemberAddCommand addCommand) {
        LinkedHashMap addMap =new LinkedHashMap();
        addMap.put("id", addCommand.getId());
        addMap.put("account_id", addCommand.getUserId());
        addMap.put("department_id", addCommand.getDeptId());
        deptMemberRepository.save(addMap);
    }
}
