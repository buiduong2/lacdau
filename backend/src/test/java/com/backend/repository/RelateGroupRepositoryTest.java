package com.backend.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.backend.dto.res.RelateGroupAdminDTO;
import com.backend.entities.RelateGroup;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RelateGroupRepositoryTest {

    @Autowired
    private RelateGroupRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindAllAdminDtosBy() {

        List<RelateGroupAdminDTO> dtos = repository.findAllAdminDtosBy();

        entityManager.flush();
        List<RelateGroup> relateGroups = repository.findAll();
        Map<Long, Long> countRelateInfos = relateGroups.stream().collect(
                Collectors.toMap(RelateGroup::getId, r -> r.getRelateInfos() != null ? r.getRelateInfos().size() : 0L));

        Assertions.assertThat(dtos).hasSize(relateGroups.size())
                .allMatch(dto -> countRelateInfos.get(dto.getId()).equals(dto.getRelateInfoCount()));

    }
}
