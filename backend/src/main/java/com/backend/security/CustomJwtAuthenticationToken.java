package com.backend.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.backend.entities.Person;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {
    private Person person;

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, Person person) {
        super(jwt, authorities);
        this.person = person;
    }
}