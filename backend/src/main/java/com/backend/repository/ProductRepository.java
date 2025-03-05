package com.backend.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.backend.dto.res.PriceInfo;
import com.backend.entities.Brand;
import com.backend.entities.Product;
import com.backend.entities.ProductStatus;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByProductCodeIn(List<String> ids);

    @EntityGraph(value = "Product.ProductInfo", type = EntityGraphType.LOAD)
    Optional<Product> findDetailByProductCodeAndStatus(String s, ProductStatus status);

    @Query("""
            SELECT DISTINCT p.brand
            FROM Product AS p
            WHERE p.status = 'ACTIVE' AND p.category.id IN ?1
                """)
    List<Brand> findAllBrandsByCategoryIdInAndProductStatusIsActive(List<Long> cateogryIds);

    @Query("""
            SELECT
                new com.backend.dto.res.PriceInfo(
                    COALESCE(MIN(COALESCE(p.salePrice, p.originalPrice)),0),
                    COALESCE(MAX(COALESCE(p.salePrice, p.originalPrice)),0)
            )
            FROM Product AS p
            WHERE p.status = 'ACTIVE' AND  p.category.id IN ?1
            """)
    PriceInfo findMinMaxPriceByCategoryIdInAndStatusIsActive(List<Long> cateogryIds);

    @EntityGraph(value = "Product.UpdateFull", type = EntityGraphType.LOAD)
    Optional<Product> findForUpdateFullByProductCode(String id);

    Optional<Product> findByProductCode(String code);

    Stream<Product> findByProductCodeIn(Collection<String> codes);

    @Query("""
            FROM Product AS p
            LEFT JOIN FETCH p.relateInfo AS i
            WHERE p.productCode IN ?1
            """)
    List<Product> findWithRelateByProductCodeIn(List<String> codes);

    @Modifying
    int deleteByProductCode(String code);

    @Modifying
    int deleteByProductCodeIn(String[] ids);

    @Query("""
            FROM Product AS p
            LEFT JOIN FETCH p.relateInfo AS i
            LEFT JOIN FETCH p.mainImage AS m

            WHERE p.productCode = ?1
                        """)
    Optional<Product> findWithRelateByProductCode(String id);

    @Query("""
            FROM Product AS p
            LEFT JOIN FETCH p.relateInfo AS i
            LEFT JOIN FETCH p.mainImage AS m
            WHERE i.relateGroup.id = ?1
            ORDER BY i.position ASC
                        """)
    List<Product> findWithRelateByGroupId(long id);

    @Query("""
            SELECT id FROM Product WHERE productCode = ?1
                        """)
    Long getIdByProductCode(String code);

    @Query("""
            SELECT id FROM Product WHERE productCode IN ?1
                        """)
    List<Long> getIdsByProductCodeIn(String[] codes);

    default Map<String, Product> findMapByProductCodeIn(Collection<String> productCodes) {
        return this.findByProductCodeIn(productCodes)
                .collect(Collectors.toMap(Product::getProductCode, Function.identity()));
    }

}
