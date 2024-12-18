package com.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.dto.res.CategoryAdminDTO;
import com.backend.dto.res.CategoryDTO;
import com.backend.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<CategoryDTO> findAllDtosBy();

    List<CategoryAdminDTO> findAllAdminDtosBy();

    Optional<CategoryAdminDTO> findDTOById(long id);

    @Query(nativeQuery = true, value = """
            WITH RECURSIVE category_cte AS (
                SELECT id FROM category WHERE id = :id
                UNION
                SELECT c.id
                FROM category_cte AS cte
                INNER JOIN category AS c ON  cte.id = c.parent_id
            )
            SELECT * FROM category_cte;
                        """)
    List<Long> findIdsWithIdbyParentId(Long id);

    Category findByName(String name);

    @Query(nativeQuery = true, value = """
            WITH RECURSIVE cat_tree AS (
                SELECT id , parent_id, 0 as depth FROM category WHERE id = ?1
                UNION ALL
                SELECT c.id, c.parent_id, a.depth + 1
                FROM category as c
                INNER JOIN cat_tree AS a ON a.parent_id = c.id
                WHERE a.depth  < 5
            )
            SELECT MAX(depth)  FROM cat_tree
            """)
    long getDepthToRoot(long categoryId);

    @Query(nativeQuery = true, value = """
            WITH RECURSIVE cat_tree AS (
                SELECT id,parent_id, 0 as depth FROM category WHERE id = ?1
                UNION
                SELECT c.id, c.parent_id, a.depth + 1 as depth
                FROM category AS c
                INNER JOIN cat_tree AS a  ON a.id = c.parent_id
                WHERE a.depth < 5
            )
            SELECT MAX(depth) FROM cat_tree
                """)
    long getDepthToLeaf(long categoryId);
}