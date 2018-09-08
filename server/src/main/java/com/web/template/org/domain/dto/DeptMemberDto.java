package com.web.template.org.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeptMemberDto {

    private Long id;

    private Long account_id;

    private Long department_id;
}
