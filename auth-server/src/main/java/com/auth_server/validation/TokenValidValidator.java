package com.auth_server.validation;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.auth_server.entity.AuthToken;
import com.auth_server.entity.TokenType;
import com.auth_server.repository.AuthTokenRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenValidValidator implements ConstraintValidator<TokenValid, String> {

    private final AuthTokenRepository tokenRepository;

    private TokenType type;

    @Override
    public void initialize(TokenValid constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Strings.isBlank(value)) {
            return true;
        }

        Optional<AuthToken> tokenOpt = tokenRepository.findByTokenValueAndType(value, this.type);

        if (tokenOpt.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Token does not exist")
                    .addConstraintViolation();
            return false;
        }

        AuthToken token = tokenOpt.get();

        if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Token has expired")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }

}
