package com.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.backend.repository.AttributeValueRepository;
import com.backend.repository.ProductRepository;
import com.backend.utils.ServiceTest;

@ServiceTest
public class AttributeValueServiceTest {

    @Autowired
    private AttributeValueService service;

    @Autowired
    private AttributeValueRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testDeleteById() {
        Long id = 1L;
        service.deleteById(id);
        entityManager.flush();
    }

    @Test
    void testDeleteById_itShouldMainProductEntity() {

        Long productCount = productRepository.count();

        entityManager.clear();

        Long id = 1L;
        assertThat(repository.existsById(id)).isTrue();
        service.deleteById(id);
        entityManager.flush();
        entityManager.clear();
        assertThat(repository.existsById(id)).isFalse();
        assertThat(productCount).isEqualTo(productRepository.count());

    }
}
