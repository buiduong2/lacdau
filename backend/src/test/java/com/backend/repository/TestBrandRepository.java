package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

public interface TestBrandRepository extends BrandRepository {

    @Query(value = """
            SELECT b.id
            FROM brand AS b
            LIMIT 7
            """, nativeQuery = true)
    List<Long> findByFirst7();
}
