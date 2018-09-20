package com.web.template.org.domain.dto;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeptMemberDto {

    private Long id;

    private Long account_id;

    private Long department_id;
}
