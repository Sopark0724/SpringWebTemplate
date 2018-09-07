package com.web.template.org.application.data;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class DepartmentPresentation {
    private Long id;

    private String name;

    private Long parentId;
}
