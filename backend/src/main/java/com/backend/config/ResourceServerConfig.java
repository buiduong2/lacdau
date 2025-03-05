package com.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.cors.CorsConfigurationSource;

import com.backend.entities.Customer;
import com.backend.entities.Employee;
import com.backend.security.AnymousPerson;
import com.backend.security.CustomJwtAuthenticationToken;
import com.backend.security.JwtAuthenticationConverter;
import com.backend.security.Permission;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ResourceServerConfig {
    @Value("${custom.auth.key_set_uri}")
    private String keySetUri;

    final JwtAuthenticationConverter converter;

    final CorsConfigurationSource configurationSource;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(c -> c.jwt(jwt -> jwt.jwkSetUri(keySetUri).jwtAuthenticationConverter(converter)));
        http.authorizeHttpRequests(r -> r
                .requestMatchers("/api/categories/**").permitAll()
                .requestMatchers("/api/products/**").permitAll()
                .requestMatchers("/api/provinces/**").permitAll()
                .requestMatchers("/api/employees/check-register").authenticated()
                .requestMatchers("/api/customers/check-register").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/customers").access(this.isAnymousPerson())
                .requestMatchers(HttpMethod.POST, "/api/employees").access(this.isAnymousPerson())
                .requestMatchers("/api/customer/**").access(this.isCustomer())
                .requestMatchers("/api/employees/**").access(this.isEmployee())
                .requestMatchers("/api/admin/orders/**").hasAuthority(Permission.ORDER_MANAGE.toString())
                .requestMatchers("/api/admin/**").hasAuthority(Permission.PRODUCT_MANAGE.toString())
                .anyRequest().authenticated()

        );
        http.cors(c -> c.configurationSource(configurationSource));
        http.sessionManagement(s -> s.disable());

        http.csrf(c -> c.disable());
        return http.build();
    }

    private AuthorizationManager<RequestAuthorizationContext> isCustomer() {
        return (authentication, context) -> {
            if (authentication instanceof CustomJwtAuthenticationToken token) {
                if (token.getPerson() instanceof Customer) {
                    return new AuthorizationDecision(true);
                }
            }
            return new AuthorizationDecision(false);
        };
    }

    private AuthorizationManager<RequestAuthorizationContext> isAnymousPerson() {
        return (authentication, context) -> {
            if (authentication.get() instanceof CustomJwtAuthenticationToken token) {
                if (token.getPerson() instanceof AnymousPerson) {
                    return new AuthorizationDecision(true);
                }
            }

            return new AuthorizationDecision(false);
        };

    }

    private AuthorizationManager<RequestAuthorizationContext> isEmployee() {
        return (authentication, context) -> {
            if (authentication.get() instanceof CustomJwtAuthenticationToken token) {
                if (token.getPerson() instanceof Employee && authentication.get().getAuthorities().size() > 0) {
                    return new AuthorizationDecision(true);
                }
            }
            return new AuthorizationDecision(false);
        };
    }
}
