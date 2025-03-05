package com.auth_server.validation;

import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.auth_server.entity.SystemUser;
import com.auth_server.repository.SystemUserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

// Support SystemUser Only
@Component
@RequiredArgsConstructor
public class EmailExistedAndVerifiedValidator implements ConstraintValidator<EmailExistedAndVerified, String> {

    private final SystemUserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (Strings.isBlank(email)) {
            return true;
        }

        Optional<SystemUser> opt = userRepository.findByEmail(email);

        if (opt.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "The email does not exist or your account belongs to a 3rd party")
                    .addConstraintViolation();
            return false;
        }

        SystemUser user = opt.get();
        boolean isVerified = user.isEmailVerified();
        if (!isVerified) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "The email is not verified")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
