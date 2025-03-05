package com.backend.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.backend.entities.Customer;
import com.backend.entities.Employee;
import com.backend.entities.Person;
import com.backend.security.AnymousPerson;
import com.backend.security.CustomJwtAuthenticationToken;
import com.backend.security.Permission;

public class CustomSecurityContextFactory implements WithSecurityContextFactory<WithJwtAuthenticationToken> {

    @Override
    public SecurityContext createSecurityContext(WithJwtAuthenticationToken annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Class<? extends Person> principalType = annotation.principalType();
        Person principal;

        if (principalType == Employee.class) {
            principal = new Employee();
        } else if (principalType == Customer.class) {
            principal = new Customer();
        } else {
            principal = new AnymousPerson();
        }

        Set<GrantedAuthority> authorities = Arrays.stream(annotation.authorities())
                .map(Permission::toString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user")
                .claim("scope", "read")
                .build();

        CustomJwtAuthenticationToken token = new CustomJwtAuthenticationToken(jwt, authorities, principal);

        context.setAuthentication(token);

        return context;
    }

}
