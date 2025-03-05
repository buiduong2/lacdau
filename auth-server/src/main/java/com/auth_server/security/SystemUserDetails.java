package com.auth_server.security;

import com.auth_server.entity.SystemUser;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class SystemUserDetails extends CustomUserDetails {

    private String password;
    private String username;

    public SystemUserDetails(SystemUser user) {
        super(user);
        this.password = user.getPassword();
        this.username = user.getUsername();
    }

}
