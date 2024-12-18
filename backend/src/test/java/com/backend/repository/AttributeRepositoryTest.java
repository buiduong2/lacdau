package com.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.backend.dto.res.AttributeAdminDTO;
import com.backend.entities.Attribute;
import com.backend.entities.AttributeValue;
import com.backend.entities.Category;
import com.backend.entities.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AttributeRepositoryTest {

    @Autowired
    public AttributeRepository repository;

    @Autowired
    public CategoryRepository categoryRepository;

    @Autowired
    public TestAttributeValueRepostiory attributeValueRepostiory;

    @Autowired
    public ProductRepository productRepository;

    @Autowired
    public TestEntityManager entityManager;

    @Test
    void testFindByIdIn() {

        List<Attribute> attributes = repository.findAllById(List.of(1L, 2L));
        Assertions.assertThat(attributes).extracting(Attribute::getId).containsAll(List.of(1L, 2L));
    }

    @Test
    void testFindByProductCategoryIn() {
        Category category = categoryRepository.findByName("BÀN PHÍM GAMING");
        List<Long> cateogryIds = categoryRepository.findIdsWithIdbyParentId(category.getId());

        entityManager.clear();
        List<Product> products = productRepository.findAll().stream()
                .filter(product -> cateogryIds.contains(product.getCategory().getId())).toList();

        List<AttributeValue> expected = products.stream().flatMap(p -> p.getAttributeValues().stream())
                .distinct()
                .toList();

        entityManager.clear();

        List<Attribute> actual = repository.findByProductCategoryInAndStatusIsActive(cateogryIds);
        entityManager.clear();
        assertThat(actual).hasSizeGreaterThan(0)
                .hasSize(3)
                .flatMap(attr -> attr.getAttributeValues())
                .hasSizeGreaterThan(0)
                .hasSize(expected.size())
                .containsExactlyInAnyOrderElementsOf(expected);

    }

    @Test
    void testFindAllAdminBy() {
        List<AttributeAdminDTO> dtos = repository.findAllAdminBy();
        List<AttributeValue> attributeValues = attributeValueRepostiory.findAll();

        Map<Long, Long> countByAttrId = attributeValues.stream()
                .collect(Collectors.groupingBy(
                        a -> Optional.ofNullable(a.getAttribute()).map(Attribute::getId).orElse(-1L),
                        Collectors.counting()));

        assertThat(dtos).isNotEmpty();
        assertThat(dtos).anyMatch(a -> countByAttrId.containsKey(a.getId()));
        assertThat(dtos).allMatch(a -> {
            if (countByAttrId.containsKey(a.getId())) {
                return countByAttrId.get(a.getId()).equals(a.getAttributeValueCount());
            }
            return false;
        });
    }
}
