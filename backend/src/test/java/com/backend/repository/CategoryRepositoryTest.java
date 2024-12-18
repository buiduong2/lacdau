package com.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dto.res.CategoryDTO;
import com.backend.entities.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTest {

    @Autowired
    private TestCategoryRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindDtooBy() {
        List<CategoryDTO> categoryDtos = repository.findAllDtosBy();
        assertThat(categoryDtos)
                .hasSize(132)
                .allMatch(c -> c.getName() != null)
                .anyMatch(c -> c.getParentId() != null);

    }

    @Transactional
    @Test
    void testFindIdsWithIdbyParentId() {
        List<Long> ids = repository.findIdsWithIdbyParentId(2L);

        assertThat(ids).hasSize(49);
        Category lastChild = repository.findById(ids.get(ids.size() - 1)).get();
        Category root = lastChild;

        while (root.getParent() != null) {
            root = root.getParent();
        }
        assertThat(root.getId()).isEqualTo(2L);
        assertThat(root.getId()).isEqualTo(2L);

    }

    @Test
    void testGetDepthToRoot() {

        Category category = repository.findById(77L).get();

        List<Category> pathToRoot = new ArrayList<>();
        Category parent = category;
        pathToRoot.add(parent);
        while (parent.getParent() != null) {
            parent = parent.getParent();
            pathToRoot.add(parent);
        }
        assertThat(pathToRoot).hasSize(3);

        long depthLeaf = repository.getDepthToRoot(pathToRoot.get(0).getId());
        long depthMiddle = repository.getDepthToRoot(pathToRoot.get(1).getId());
        long depthRoot = repository.getDepthToRoot(pathToRoot.get(2).getId());

        assertThat(depthLeaf).isEqualTo(2L);
        assertThat(depthMiddle).isEqualTo(1L);
        assertThat(depthRoot).isEqualTo(0L);
    }

    @Test
    void testGetDepthToLeaf() {
        Category category = repository.findById(77L).get();
        LinkedList<Category> pathToLeaf = new LinkedList<>();
        Category parent = category;
        pathToLeaf.add(parent);
        while (parent.getParent() != null) {
            parent = parent.getParent();
            pathToLeaf.offerFirst(parent);
        }
        assertThat(pathToLeaf).hasSize(3);

        entityManager.clear();

        long depthLeaf = repository.getDepthToLeaf(pathToLeaf.get(0).getId());
        long depthMiddle = repository.getDepthToLeaf(pathToLeaf.get(1).getId());
        long depthRoot = repository.getDepthToLeaf(pathToLeaf.get(2).getId());

        assertThat(depthLeaf).isEqualTo(2L);
        assertThat(depthMiddle).isEqualTo(1L);
        assertThat(depthRoot).isEqualTo(0L);
    }

}
