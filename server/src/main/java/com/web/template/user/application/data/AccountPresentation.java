package com.web.template.user.application.data;

import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class AccountPresentation {
    private Long id;

    private String name;

    private String username;

    public static AccountPresentation convertFrom(Object from) {
        return new ModelMapper().map(from, AccountPresentation.class);
    }
}
