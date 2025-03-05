package com.auth_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.auth_server.entity.SystemUser;
import com.auth_server.entity.TokenType;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    @EntityGraph("auth")
    Optional<SystemUser> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNot(String username, Long ownerId);

    boolean existsByEmailAndIdNot(String email, Long ownerId);

    Optional<SystemUser> findByEmail(String email);

    @Query("""
            FROM SystemUser AS u
            INNER JOIN FETCH u.tokens AS t
            WHERE t.tokenValue = ?1 AND t.type = ?2
            """)
    Optional<SystemUser> findUserByTokenValueAndTokenType(String tokenValue, TokenType tokenType);
}
