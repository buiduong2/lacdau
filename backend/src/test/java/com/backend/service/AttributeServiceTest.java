package com.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.backend.dto.req.AttributeAdminCreateDTO;
import com.backend.dto.req.AttributeAdminCreateDTO.AttributeValueDTO;
import com.backend.dto.req.AttributeAdminUpdateDTO;
import com.backend.entities.Attribute;
import com.backend.entities.AttributeValue;
import com.backend.entities.Attribute_;
import com.backend.repository.AttributeRepository;
import com.backend.repository.TestAttributeValueRepostiory;
import com.backend.utils.ServiceTest;

@ServiceTest
public class AttributeServiceTest {

    @Autowired
    private AttributeService service;

    @Autowired
    private AttributeRepository repository;

    @Autowired
    private TestAttributeValueRepostiory attributeValueRepostiory;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testCreate() {
        AttributeAdminCreateDTO dto = new AttributeAdminCreateDTO();
        dto.setName("DTO NAME");
        List<AttributeValueDTO> avs = List.of(new AttributeValueDTO(), new AttributeValueDTO());
        avs.forEach(av -> av.setName("AV NAME"));
        dto.setAttributeValues(avs);

        var result = service.create(dto);

        entityManager.flush();
        entityManager.clear();
        Attribute a = repository.findById(result.getId()).get();

        assertThat(a)
                .isNotNull()
                .hasFieldOrPropertyWithValue(Attribute_.NAME, "DTO NAME");

        assertThat(a.getAttributeValues()).hasSizeGreaterThan(0)
                .allMatch(av -> av.getName().equals("AV NAME"));
    }

    @Test
    void testEdit() {
        Long id = 1L;
        Long avIdToBeremove;
        AttributeAdminUpdateDTO dto = new AttributeAdminUpdateDTO();
        dto.setName("DTO NAME");
        List<AttributeAdminUpdateDTO.AttributeValueDTO> avs = List.of(
                new AttributeAdminUpdateDTO.AttributeValueDTO(),
                new AttributeAdminUpdateDTO.AttributeValueDTO());
        avs.forEach(av -> av.setName("AV NAME"));
        dto.setAttributeValues(avs);

        Attribute original = repository.findById(id).get();

        assertThat(original).isNotNull();
        assertThat(original.getAttributeValues()).isNotNull();
        assertThat(original.getAttributeValues()).isNotNull();

        AttributeValue av = original.getAttributeValues().stream().findFirst().get();
        avIdToBeremove = av.getId();
        assertThat(av).isNotNull();

        dto.setRemoveAttributeValueId(List.of(av.getId()));

        entityManager.flush();
        entityManager.clear();

        service.edit(dto, id);

        entityManager.flush();
        entityManager.clear();

        Attribute actual = repository.findById(id).get();

        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue(Attribute_.NAME, "DTO NAME");

        assertThat(actual.getAttributeValues()).isNotEmpty()
                .hasSize(original.getAttributeValues().size() + avs.size() - 1)
                .doesNotContain(av);

        assertThat(attributeValueRepostiory.findById(avIdToBeremove))
                .isEmpty();
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        assertThat(repository.existsById(id)).isTrue();
        service.deleteById(id);
        entityManager.flush();
        assertThat(repository.existsById(id)).isFalse();
    }
}
