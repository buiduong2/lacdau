package com.auth_server.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.auth_server.entity.TokenType;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = TokenValidValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenValid {

    String message() default "Token is not existed or expirated";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    TokenType type();

}
