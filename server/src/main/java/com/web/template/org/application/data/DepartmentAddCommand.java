package com.web.template.org.application.data;

import lombok.Value;

@Value
public class DepartmentAddCommand {
    private Long parentId;
    private String name;
}
