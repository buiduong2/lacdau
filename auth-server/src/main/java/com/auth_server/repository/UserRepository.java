package com.auth_server.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.auth_server.dto.res.IdAndPermissionCount;
import com.auth_server.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByIsSuperuserIsTrue();

    default Map<Long, Boolean> findIsModerateByIdIn(List<Long> ids) {
        return getIdAndIsMorderate(ids).stream()
                .collect(Collectors.toMap(IdAndPermissionCount::getId, IdAndPermissionCount::isNotEmpty));
    };

    @Query("""
            SELECT
               new com.auth_server.dto.res.IdAndPermissionCount(
                u.id,
                u.permissions IS NOT EMPTY
               )
            FROM User AS u
            WHERE u.id IN ?1
            """)
    List<IdAndPermissionCount> getIdAndIsMorderate(List<Long> ids);
}
