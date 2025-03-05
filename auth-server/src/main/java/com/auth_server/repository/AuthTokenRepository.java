package com.auth_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth_server.entity.AuthToken;
import com.auth_server.entity.TokenType;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByTokenValueAndType(String tokenValue, TokenType type);
}
