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

import com.backend.dto.res.AttributeValueAdminDTO;
import com.backend.entities.AttributeValue;
import com.backend.entities.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AttributeValueRepositoryTest {

    @Autowired
    private AttributeValueRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByProductCategoryIdIn() {
        List<Long> ids = categoryRepository.findIdsWithIdbyParentId(23L);
        List<AttributeValue> attributeValues = repository.findByProductsCategoryIdIn(ids);
        Assertions.assertThat(attributeValues)
                .hasSize(26)
                .hasSize((int) attributeValues.stream().distinct().count());
    }

    @Test
    void testFindIdsByProductIds() {
        Long productId = 1L;

        Product product = productRepository.findById(productId).orElseThrow();

        List<Long> attributeIds = repository.findIdsByProductIds(productId);

        Assertions.assertThat(attributeIds)
                .containsExactlyElementsOf(product.getAttributeValues().stream().map(AttributeValue::getId).toList());
    }

    @Test
    void testFindAllAdminDtosBy() {
        List<AttributeValueAdminDTO> dtos = repository.findAllAdminDtosBy();

        entityManager.clear();

        List<AttributeValue> attributeValues = repository.findAll();

        attributeValues.stream().filter(p-> p.getId() == 20L).findFirst().ifPresent(av -> System.out.println(av.getProducts().size()));

        Map<Long, Long> countProductByAVId = attributeValues.stream()
                .collect(Collectors.toMap(AttributeValue::getId,
                        av -> av.getProducts() == null ? 0L : (long) av.getProducts().size()));

        Assertions.assertThat(dtos).hasSize(attributeValues.size())
                .allMatch(dto -> countProductByAVId.get(dto.getId()).equals(dto.getProductCount()));

    }
}
