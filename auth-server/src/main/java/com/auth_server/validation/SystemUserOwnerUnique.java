package com.auth_server.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ ElementType.METHOD })
@Constraint(validatedBy = SystemUserOwnerUniqueValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemUserOwnerUnique {
    String message() default "Value has already existed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Column[] column();

    public static enum Column {
        EMAIL,
        USERNAME
    }
}
