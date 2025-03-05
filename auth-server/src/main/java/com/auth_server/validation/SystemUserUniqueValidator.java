package com.auth_server.validation;

import org.springframework.stereotype.Component;

import com.auth_server.repository.SystemUserRepository;
import com.auth_server.validation.SystemUserUnique.Column;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SystemUserUniqueValidator implements ConstraintValidator<SystemUserUnique, String> {

    private final SystemUserRepository repository;
    private Column column;

    @Override
    public void initialize(SystemUserUnique constraintAnnotation) {
        this.column = constraintAnnotation.column();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        switch (this.column) {
            case EMAIL:
                return isEmailvalid(value);
            case USERNAME:
                return isUsernameValid(value);
            default:
                throw new IllegalArgumentException("Column type not implemented Yet");
        }
    }

    private boolean isUsernameValid(String value) {
        return repository.existsByUsername(value) == false;
    }

    private boolean isEmailvalid(String value) {
        return repository.existsByEmail(value) == false;
    }

}
