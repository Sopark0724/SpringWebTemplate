package com.web.template.user.application.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountAddCommand {

    private String name;

    private String username;

    private String password;

    private String role;

}

