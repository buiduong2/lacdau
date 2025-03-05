package com.auth_server.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.auth_server.entity.User;

@Component
class SpringSecurityAuditorAware implements AuditorAware<User> {

    @SuppressWarnings("null")
    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(auth -> {
                    if (auth instanceof CustomJwtAuthenticationToken jwt) {
                        User user = new User();
                        user.setId(jwt.getUserId());
                        return user;
                    }
                    if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
                        User user = new User();
                        user.setId(userDetails.getId());
                        return user;
                    }

                    throw new RuntimeException("Soime thing wrong with Authenticaiton aware");
                });
    }
}