package com.auth_server.repository;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.Query;

import com.auth_server.entity.User;

@Primary
public interface TestUserRepository extends UserRepository {

    @Query("""
            FROM User AS u
            WHERE u.permissions is EMPTY
            """)
    List<User> findByPermissionIsEmpty(Limit limit);

    @Query("""
            FROM User AS u
            WHERE u.permissions IS NOT EMPTY
            """)
    List<User> findByPermissionIsNotEmpty(Limit limit);

    default User findOneNotHavePermission() {
        return findByPermissionIsEmpty(Limit.of(1)).get(0);
    }

    default User findOneHavePermission() {
        return findByPermissionIsNotEmpty(Limit.of(1)).get(0);
    }
}
