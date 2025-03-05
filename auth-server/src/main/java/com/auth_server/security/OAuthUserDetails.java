package com.auth_server.security;

import com.auth_server.entity.OAuthUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class OAuthUserDetails extends CustomUserDetails {

    private String username;

    public OAuthUserDetails(OAuthUser user) {
        super(user);
        this.username = user.getUsername();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return "";
    }

}
