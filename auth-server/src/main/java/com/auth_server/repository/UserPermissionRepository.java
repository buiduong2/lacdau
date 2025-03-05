package com.auth_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.auth_server.dto.res.PermissionListDTO;
import com.auth_server.entity.User;
import com.auth_server.entity.UserPermission;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    @Query("""
            FROM User AS u
            INNER JOIN FETCH u.permissions AS p
            """)
    List<User> findAllManager();

    @Query("""
            SELECT
                new com.auth_server.dto.res.PermissionListDTO(
                    p.id,
                    p.permission,
                    p.revokedAt,
                    p.createdAt,
                    p.updatedAt,
                    u.id,
                    u.displayName,
                    c.id,
                    c.displayName
                )
            FROM UserPermission AS p
            LEFT JOIN p.createdBy AS c
            LEFT JOIN p.updatedBy AS u
            WHERE p.user.id = ?1
            """)
    List<PermissionListDTO> findDTOsByUserId(Long id);

    @Query("""
            FROM UserPermission
            WHERE user.id = ?1
            """)
    List<UserPermission> findByUserId(long id);

}
