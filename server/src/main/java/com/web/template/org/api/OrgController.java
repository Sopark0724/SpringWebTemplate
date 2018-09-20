package com.web.template.org.api;

import com.web.template.org.application.OrgService;
import com.web.template.org.application.data.DepartmentAddCommand;
import com.web.template.org.application.data.DepartmentPresentation;
import com.web.template.org.application.data.OrgPresentation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrgController {

    @Autowired
    private OrgService orgService;

    @ApiOperation(value = "부서 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addCommand", value = "부서등록 정보", required = true)
    })
    @PostMapping("/department")
    public DepartmentPresentation create(DepartmentAddCommand addCommand) {
        DepartmentPresentation departmentPresentation = orgService.create(addCommand);
        return departmentPresentation;
    }

    @ApiOperation(value = "조직도 트리 구조 조회")
    @GetMapping("/org")
    public OrgPresentation getOrg() {
        log.info("orb Test");
        return orgService.getTree();
    }
}
