package com.backend.security;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.backend.entities.Person;
import com.backend.repository.CustomerRepository;
import com.backend.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements Converter<Jwt, CustomJwtAuthenticationToken> {

    private final CustomerRepository customerRepository;

    private final EmployeeRepository employeeRepository;

    @Override
    @Nullable
    public CustomJwtAuthenticationToken convert(@SuppressWarnings("null") Jwt source) {
        List<String> authorities = source.getClaim("authorities");
        List<String> scopes = source.getClaim("scope");
        Long userId = source.getClaim("userId");
        Person person = getPrincipal(scopes, userId);
        Set<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(scopes, authorities);

        return new CustomJwtAuthenticationToken(source, grantedAuthorities, person);
    }

    private Set<GrantedAuthority> getGrantedAuthorities(List<String> scopes, List<String> authorities) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (String scope : scopes) {
            grantedAuthorities.add(new SimpleGrantedAuthority("SCOPE_" + scope));
        }

        for (String authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));

        }
        return grantedAuthorities;
    }

    private Person getPrincipal(List<String> scopes, long userId) {
        Optional<? extends Person> perOptional;
        if (scopes.contains("CLIENT")) {
            perOptional = customerRepository.findByUserId(userId);
        } else {
            perOptional = employeeRepository.findByUserId(userId);
        }

        if (perOptional.isEmpty()) {
            Person person = new AnymousPerson();
            person.setUserId(userId);
            return person;
        }

        return perOptional.get();
    }

}
