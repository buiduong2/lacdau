package com.auth_server.validation;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.auth_server.repository.SystemUserRepository;
import com.auth_server.validation.SystemUserOwnerUnique.Column;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import lombok.RequiredArgsConstructor;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
@Component
@RequiredArgsConstructor
public class SystemUserOwnerUniqueValidator implements ConstraintValidator<SystemUserOwnerUnique, Object[]> {

    private final SystemUserRepository systemUserRepository;

    private Column[] columns;

    @Override
    public void initialize(SystemUserOwnerUnique constraintAnnotation) {
        this.columns = constraintAnnotation.column();
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        Long id = (Long) value[0];
        IUniqueFields req = (IUniqueFields) value[1];

        for (Column column : this.columns) {
            switch (column) {
                case EMAIL:
                    if (!isEmailValid(id, req)) {
                        context.buildConstraintViolationWithTemplate(
                                String.format("Email has already exited"))
                                .addPropertyNode("email")
                                .addConstraintViolation();
                        return false;
                    }
                    break;
                case USERNAME:
                    if (!isUsernameValid(id, req)) {
                        context.buildConstraintViolationWithTemplate(
                                String.format("Username has already exited"))
                                .addPropertyNode("username")
                                .addConstraintViolation();
                        return false;
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unexpected enum  not impelemented yet: " + column);
            }
        }

        return true;
    }

    private boolean isEmailValid(Long userId, IUniqueFields req) {
        if (Strings.isBlank(req.getEmail())) {
            return true;
        }
        System.out.println(systemUserRepository.existsByEmailAndIdNot(req.getEmail(), userId));
        return systemUserRepository.existsByEmailAndIdNot(req.getEmail(), userId) == false;
    }

    private boolean isUsernameValid(Long userId, IUniqueFields req) {
        if (Strings.isBlank(req.getUsername())) {
            return true;
        }
        return systemUserRepository.existsByUsernameAndIdNot(req.getUsername(), userId) == false;
    }

}
