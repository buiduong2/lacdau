package com.auth_server.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.auth_server.dto.req.UpdatePermissionReq;
import com.auth_server.entity.Permission;
import com.auth_server.entity.User;
import com.auth_server.entity.UserPermission;
import com.auth_server.repository.TestUserRepository;
import com.auth_server.utils.ServiceTest;

@ServiceTest
public class UserPermissionServiceTest {

    @Autowired
    private UserPermissionService service;

    @Autowired
    private TestUserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("When Create New Manager Should Work")
    void testUpdatePermission() {
        User user = userRepository.findOneNotHavePermission();
        assertThat(user).isNotNull();
        assertThat(user.getPermissions()).hasSize(0);

        entityManager.clear();

        System.out.println("===============TESST==========");

        Set<String> permissions = Set.of(Permission.CUSTOMER_MANAGE.toString(), Permission.ORDER_MANAGE.toString());
        updatePermission(user.getId(), permissions);

        User actual = userRepository.findById(user.getId()).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getPermissions()).hasSize(permissions.size())
                .allMatch(p -> p.getRevokedAt() == null);
        assertThat(actual.getPermissions().stream().map(UserPermission::getPermission).toList())
                .containsAll(List.of(Permission.CUSTOMER_MANAGE, Permission.ORDER_MANAGE));
    }

    @Test
    @DisplayName("When add new Permission to an Manager should work And not update existed Permissions")
    void testUpdatePermission2() {
        User user = userRepository.findOneHavePermission();
        assertThat(user).isNotNull();
        assertThat(user.getPermissions())
                .hasSizeGreaterThan(0)
                .hasSizeLessThan(Permission.values().length);

        entityManager.clear();

        System.out.println("===============TESST==========");

        Set<Permission> existedPermissions = user
                .getPermissions()
                .stream()
                .map(UserPermission::getPermission)
                .collect(Collectors.toSet());

        Set<String> permissionToBeAdds = new HashSet<>();
        permissionToBeAdds.addAll(existedPermissions.stream().map(Permission::toString).toList());

        for (Permission permission : Permission.values()) {
            if (!existedPermissions.contains(permission)) {
                permissionToBeAdds.add(permission.toString());
            }
        }
        assertThat(permissionToBeAdds)
                .hasSize(Permission.values().length);

        updatePermission(user.getId(), permissionToBeAdds);

        User actual = userRepository.findById(user.getId()).get();
        assertThat(actual).isNotNull();
        assertThat(actual.getPermissions().stream().filter(p -> user.getPermissions().contains(p)).toList())
                .hasSize(existedPermissions.size())
                .allMatch(p -> p.getUpdatedAt() == null || p.getUpdatedAt().isBefore(LocalDateTime.now()));
        assertThat(actual.getPermissions()).hasSize(Permission.values().length);
        assertThat(actual.getPermissions().stream().map(UserPermission::getPermission).toList())
                .containsAll(Arrays.asList(Permission.values()));
    }

    @Test
    @DisplayName("When req omit some permissions should revoked it")
    void testUpdatePermission3() {
        User user = userRepository.findOneHavePermission();
        assertThat(user).isNotNull();
        assertThat(user.getPermissions())
                .hasSizeGreaterThan(0)
                .hasSizeLessThan(Permission.values().length);

        entityManager.clear();

        System.out.println("===============TESST==========");
        Set<Permission> existedPermissions = user
                .getPermissions()
                .stream()
                .map(UserPermission::getPermission)
                .collect(Collectors.toSet());

        Set<String> permissionToBeAdds = new HashSet<>();

        for (Permission permission : Permission.values()) {
            if (!existedPermissions.contains(permission)) {
                permissionToBeAdds.add(permission.toString());
            }
        }
        assertThat(permissionToBeAdds)
                .hasSize(Permission.values().length - existedPermissions.size());

        updatePermission(user.getId(), permissionToBeAdds);

        User actual = userRepository.findById(user.getId()).get();

        assertThat(actual).isNotNull();
        assertThat(actual.getPermissions()).hasSize(Permission.values().length);
        assertThat(actual.getPermissions().stream().filter(p -> user.getPermissions().contains(p)))
                .allMatch(p -> p.getUpdatedAt().isAfter(LocalDateTime.now().minusMinutes(2L)))
                .allMatch(p -> p.getRevokedAt() != null)
                .allMatch(p -> p.getRevokedAt().isAfter(LocalDateTime.now().minusMinutes(2L)));

    }

    private void updatePermission(long userId, Set<String> permissionToBeAdds) {
        UpdatePermissionReq req = new UpdatePermissionReq();
        req.setPermissions(permissionToBeAdds);

        service.updatePermission(userId, req);
        entityManager.flush();
        entityManager.clear();
    }
}
