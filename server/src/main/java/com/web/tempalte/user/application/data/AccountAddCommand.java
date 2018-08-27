package com.web.tempalte.user.application.data;

import lombok.Value;

@Value
public class AccountAddCommand {

    private String name;

    private String username;

    private String password;
}

