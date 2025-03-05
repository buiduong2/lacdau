package com.auth_server.config;

import static com.auth_server.entity.Permission.USER_MANAGE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import com.auth_server.security.CustomAuthenticationProvider;
import com.auth_server.security.FederatedIdentityAuthenticationSuccessHandler;
import com.auth_server.security.JwtAuthenticationConverter;
import com.auth_server.service.UserManagerService;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class SecurityConfig {

    final JwtAuthenticationConverter converter;

    final FederatedIdentityAuthenticationSuccessHandler federatedIdentityAuthenticationSuccessHandler;

    final UserManagerService userDetailsService;

    final Validator validator;

    final CorsConfigurationSource configurationSource;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder(), validator);
    }

    @Bean
    @Order(2)
    SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {

        String[] devUrls = new String[] { "/error/**", "/test/**" };
        String[] publishUrls = new String[] { "/verify-email" };
        String[] resourceUrls = new String[] { "/img/**", "/css/**", "/js/**" };
        String[] guestOnlyUrls = new String[] {
                "/register", "/login", "/send-email", "/forgot-password", "/reset-password"
        };
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/api/profile/**").authenticated()
                .requestMatchers("/api/admin/users").hasAuthority(USER_MANAGE.toString())
                .requestMatchers("/api/admin/user-permissions").hasAuthority(USER_MANAGE.toString())
                .requestMatchers(devUrls).permitAll() // TODO: TEST
                .requestMatchers(publishUrls).permitAll()
                .requestMatchers(guestOnlyUrls).anonymous()
                .requestMatchers(resourceUrls).permitAll()
                .anyRequest().authenticated())
                .cors(c -> c.configurationSource(configurationSource))
                .formLogin(c -> c.loginPage("/login")
                        .failureUrl("/login?error"))
                .oauth2Login(c -> c.successHandler(federatedIdentityAuthenticationSuccessHandler)
                        .loginPage("/login"))
                .oauth2ResourceServer(c -> c.jwt(j -> j.jwtAuthenticationConverter(converter)))
                .logout(Customizer.withDefaults())
                .exceptionHandling(c -> c.accessDeniedPage("/"));

        return http.build();
    }
}
