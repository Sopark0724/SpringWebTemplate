package com.web.template.user.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccountDto {

    private Long id;
    private String name;
    private String username;
    private String password;
    private String role;

    public AccountDto(String name, String username, String password, String role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public AccountDto encryptPassword(PasswordEncoder passwordEncoder) {
        if (StringUtils.isEmpty(this.password)) {
            return this;
        }
        this.password = passwordEncoder.encode(this.password);
        return this;
    }
}
