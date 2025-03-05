package com.auth_server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.auth_server.dto.res.PermissionListDTO;
import com.auth_server.entity.User;
import com.auth_server.entity.UserPermission;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserPermissionRepositoryTest {

    @Autowired
    private UserPermissionRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindAllManager() {
        List<User> managers = repository.findAllManager();
        Long permissionCount = repository.count();

        assertThat(managers).hasSizeGreaterThan(0);
        assertThat(Hibernate.isInitialized(managers.get(0).getPermissions())).isTrue();
        assertThat(managers.stream().flatMap(u -> u.getPermissions().stream()).count()).isEqualTo(permissionCount);

    }

    @Test
    void findDTOsByUserId() {
        long perrmissionCount = repository.findAll().stream().filter(u -> u.getUser().getId().equals(60L)).count();
        List<PermissionListDTO> dtos = repository.findDTOsByUserId(60L);
        entityManager.clear();
        assertThat(dtos).hasSizeGreaterThan(0).hasSize((int) perrmissionCount);
        assertThat(dtos.get(0).getId()).isNotNull();
        assertThat(dtos.get(0).getCreatedBy().getId()).isNotNull();
        assertThat(dtos.get(0).getUpdatedBy().getId()).isNotNull();
    }

    @Test
    void testFindByUserId() {
        List<UserPermission> userPermissions = repository.findByUserId(60L);
        assertThat(userPermissions).isNotEmpty();

    }
}
