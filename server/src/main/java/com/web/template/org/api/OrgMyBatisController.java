package com.web.template.org.api;

import com.web.template.org.application.OrgService;
import com.web.template.org.application.data.DepartmentAddCommand;
import com.web.template.org.application.data.DepartmentPresentation;
import com.web.template.org.application.data.OrgPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2")
public class OrgMyBatisController {

    @Autowired
    @Qualifier(value = "orgSercviceMyBatisImpl")
    private OrgService orgService;

    @PostMapping("/department")
    public DepartmentPresentation create(DepartmentAddCommand addCommand) {
        DepartmentPresentation departmentPresentation = orgService.create(addCommand);
        return departmentPresentation;
    }


    @GetMapping("/org")
    public OrgPresentation getOrg(){
        return orgService.getTree();
    }
}
