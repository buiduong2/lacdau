package com.auth_server.security;

import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;

import com.auth_server.dto.req.LoginReq;
import com.auth_server.exception.AuthValidationException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private final Validator validator;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                                        Validator validator) {
        super(passwordEncoder);
        super.setUserDetailsService(userDetailsService);
        this.validator = validator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        LoginReq loginReq = new LoginReq();
        loginReq.setUsername(username);
        loginReq.setPassword(password);

        Set<ConstraintViolation<LoginReq>> violations = validator.validate(loginReq);

        if (!violations.isEmpty()) {
            List<FieldError> fieldErrors = violations.stream()
                    .map(vi -> new FieldError("loginReq", vi.getPropertyPath().toString(), vi.getMessage())).toList();

            throw new AuthValidationException(password, fieldErrors);
        }

        return super.authenticate(authentication);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
