package com.backend.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({ ElementType.METHOD })
@Constraint(validatedBy = CategoryIdParamsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryIdParams {

    String message() default "Parent Id not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
