package com.web.template.user.application.data;

import lombok.Value;

@Value
public class AccountAddCommand {

    private String name;

    private String username;

    private String password;

    private String role;

}

