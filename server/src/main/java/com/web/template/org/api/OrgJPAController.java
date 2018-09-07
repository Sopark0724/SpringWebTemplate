package com.web.template.org.api;

import com.web.template.org.application.OrgServiceJPAImpl;
import com.web.template.org.application.data.DepartmentAddCommand;
import com.web.template.org.application.data.DepartmentPresentation;
import com.web.template.org.application.data.OrgPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class OrgJPAController {

    @Autowired
    private OrgServiceJPAImpl orgServiceJPA;

    @PostMapping("/department")
    public DepartmentPresentation create(DepartmentAddCommand addCommand) {
        DepartmentPresentation departmentPresentation = orgServiceJPA.create(addCommand);
        return departmentPresentation;
    }


    @GetMapping("/org")
    public OrgPresentation getOrg(){
        return orgServiceJPA.getTree();
    }
}
