package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.entities.Product;

@Repository
public interface TestProductRepository extends ProductRepository {

    List<Product> findByBrandId(Long id);

    @Query("""
            FROM Product p
            WHERE COALESCE(p.salePrice, p.originalPrice) BETWEEN :min AND :max
            """)
    List<Product> findByPriceBettween(int min, int max);

    @Query("""
            FROM Product p
            WHERE COALESCE(p.salePrice, p.originalPrice) >= :value
            """)
    List<Product> findByPriceGreaterThanOrEqual(int value);

    List<Product> findByAttributeValuesIdIn(List<Long> ids);

    List<Product> findByCategoryIdIn(List<Long> ids);

    List<Product> findByBrandIdIn(List<Long> ids);

    List<Product> findFirst2ByRelateInfoIsNull();

    List<Product> findFirst2ByRelateInfoIsNotNull();

    @Query("""
            SELECT COUNT(*) FROM Product WHERE category.id = ?1
                    """)
    Long countByCategoryId(long id);
}
