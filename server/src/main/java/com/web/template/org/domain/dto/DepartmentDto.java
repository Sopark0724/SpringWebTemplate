package com.web.template.org.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DepartmentDto {

    private Long id;

    private String name;

    private Long parent_id;

    private Date created_at;

    private Date updated_at;

    public DepartmentDto(String name, Long parent_id) {
        this.name = name;
        this.parent_id = parent_id;
    }

    public Long getParentId(){
        return this.parent_id;
    }
}
