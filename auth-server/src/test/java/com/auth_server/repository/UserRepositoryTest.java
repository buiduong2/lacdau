package com.auth_server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.auth_server.dto.res.IdAndPermissionCount;
import com.auth_server.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void testFindIsModerateByIdIn() {
        List<User> users = repository.findAll();

        Map<Long, Boolean> map = repository.findIsModerateByIdIn(users.stream().map(User::getId).toList());

        Map<Long, Boolean> expectedMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user.getPermissions().size() > 0));

        assertThat(map).hasSize(expectedMap.size()).hasSize(users.size());

        map.entrySet().forEach(entry -> {
            assertThat(map.get(entry.getKey())).isEqualTo(entry.getValue());
        });

    }

    @Test
    void testGetIdAndIsMorderate() {
        List<User> users = repository.findAll();
        List<IdAndPermissionCount> list = repository.getIdAndIsMorderate(users.stream().map(User::getId).toList());

        assertThat(list).hasSize(users.size());
        assertThat(list).anyMatch(u -> u.isNotEmpty());
    }

}
