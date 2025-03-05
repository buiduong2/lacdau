package com.backend.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

import com.backend.entities.Person;
import com.backend.security.Permission;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomSecurityContextFactory.class)
public @interface WithJwtAuthenticationToken {
    Class<? extends Person> principalType();

    Permission[] authorities();
}
