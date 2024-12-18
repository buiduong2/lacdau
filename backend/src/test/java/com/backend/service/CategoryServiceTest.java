package com.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.backend.entities.Category;
import com.backend.exception.ValidationException;
import com.backend.repository.TestCategoryRepository;
import com.backend.repository.TestProductRepository;
import com.backend.utils.ServiceTest;

@ServiceTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService service;

    @Autowired
    private TestCategoryRepository repository;

    @Autowired
    private TestProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testRepository() {
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testValidateCategory_whenAddParentLeaf_shouldThrown() {
        Category category = new Category();
        category.setName("NAME");
        category.setParent(repository.getReferenceById(77L));
        repository.save(category);
        flushAndClear();

        assertThat(category.getId()).isNotNull();

        Assertions.assertThatThrownBy(() -> {
            service.validateCategory(category.getId());
        }).isInstanceOf(ValidationException.class);

    }

    @Test
    void testValidateCategory_whenAddParentCircle_shouldThrown() {
        Category category = repository.findById(77L).get();
        category.setParent(category);
        repository.save(category);

        flushAndClear();

        Assertions.assertThatThrownBy(() -> {
            service.validateCategory(category.getId());
        }).isInstanceOf(ValidationException.class);
    }

    @Test
    void testValidateCategory_whenDetachLeaf_shouldWork() {
        Category category = repository.findById(77L).get();
        category.setParent(null);
        repository.save(category);

        flushAndClear();

        Category category2 = repository.findById(77L).get();

        assertThat(category2.getParent()).isEqualTo(null);

        service.validateCategory(category2.getId());
    }

    @Test
    void testValidateCategory_whenDetachMiddle_shouldWork() {
        Category category = repository.findById(77L).get().getParent();
        category.setParent(null);
        repository.save(category);

        flushAndClear();

        Category category2 = repository.findById(category.getId()).get();

        assertThat(category2.getParent()).isEqualTo(null);

        service.validateCategory(category2.getId());
    }

    @Test
    void testDeleteById() {
        long productCount = productRepository.count();
        long categoryCount = repository.count();
        long categoryId = 23L;

        Category category = repository.findById(categoryId).get();

        assertThat(category).isNotNull();
        assertThat(category.getParent()).isNotNull();
        assertThat(category.getChildren()).hasSizeGreaterThan(0);

        List<Category> children = category.getChildren();

        flushAndClear();

        service.deleteById(categoryId);

        flushAndClear();

        List<Category> root = repository.findAllById(children.stream().map(Category::getId).toList());

        assertThat(repository.existsById(categoryId)).isFalse();
        assertThat(categoryCount - 1).isEqualTo(repository.count());
        assertThat(productCount).isEqualTo(productRepository.count());
        assertThat(root).allMatch(c -> c.getParent() == null);
    }
}
