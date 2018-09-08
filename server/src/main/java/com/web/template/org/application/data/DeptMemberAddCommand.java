package com.web.template.org.application.data;


import lombok.Value;

@Value
public class DeptMemberAddCommand {

    private Long id;

    private Long deptId;

    private Long userId;

    public DeptMemberAddCommand(Long id, Long deptId, Long userId) {
        this.id = id;
        this.deptId = deptId;
        this.userId = userId;
    }

    public DeptMemberAddCommand(Long deptId, Long userId) {
        this.id = null;
        this.deptId = deptId;
        this.userId = userId;
    }
}
