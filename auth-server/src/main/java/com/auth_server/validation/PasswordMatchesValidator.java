package com.auth_server.validation;

import com.auth_server.dto.req.PasswordConfirmable;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordConfirmable> {

    @Override
    public boolean isValid(PasswordConfirmable value, ConstraintValidatorContext context) {
        String password = value.getPassword();
        String confirmPassword = value.getConfirmPassword();
        if (password == null || confirmPassword == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Passwords do not match")
                .addPropertyNode("confirmPassword") // Hoặc để trống nếu muốn lỗi toàn cục
                .addConstraintViolation();

        return password.equals(confirmPassword);
    }

}
