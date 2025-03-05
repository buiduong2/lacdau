package com.auth_server.validation;

import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.auth_server.entity.SystemUser;
import com.auth_server.exception.ResourceNotFoundException;
import com.auth_server.repository.SystemUserRepository;
import com.auth_server.security.CustomUserDetails;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordOldMatchesValidator implements ConstraintValidator<PasswordOldMatches, String> {

    private final SystemUserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Strings.isBlank(value)) {
            return true;
        }
        Long id = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(CustomUserDetails.class::cast)
                .map(CustomUserDetails::getId)
                .orElseThrow(() -> new RuntimeException("Authentication get somethign wrong"));

        SystemUser user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user is not exists"));

        return passwordEncoder.matches(value, user.getPassword());
    }

}
