package com.auth_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.auth_server.entity.OAUthProvider;
import com.auth_server.entity.OAuthUser;

public interface OAuthUserRepository extends JpaRepository<OAuthUser, Long> {
    @EntityGraph(value = "auth", type = EntityGraphType.LOAD)
    Optional<OAuthUser> findByProviderUserIdAndProvider(String providerUserId, OAUthProvider provider);
}
