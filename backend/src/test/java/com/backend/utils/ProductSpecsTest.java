package com.backend.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dto.req.FilterParam;
import com.backend.entities.AttributeValue;
import com.backend.entities.Product;
import com.backend.repository.CategoryRepository;
import com.backend.repository.TestAttributeValueRepostiory;
import com.backend.repository.TestBrandRepository;
import com.backend.repository.TestProductRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductSpecsTest {

    @Autowired
    private TestProductRepository repository;

    @Autowired
    private TestBrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestAttributeValueRepostiory attributeValueRepostiory;

    @Autowired
    private TestEntityManager entityManager;

    private ProductSpecs productSpecs;

    @BeforeEach
    public void inject() {
        productSpecs = new ProductSpecs(categoryRepository);
    }

    @Test
    void testIsBrandId() {
        long brandId = 7L;

        List<Product> products = repository.findAll(productSpecs.isBrandId(brandId));
        List<Product> products2 = repository.findByBrandId(brandId);

        assertThat(products)
                .hasSize(products2.size())
                .hasSizeGreaterThan(0)
                .allMatch(isBrandId(brandId))
                .containsAll(products2);

    }

    @Test
    void testIsBrandIdIn() {
        List<Long> brandIds = brandRepository.findByFirst7();

        List<Product> products = repository.findAll(productSpecs.isBrandIdIn(brandIds));

        List<Product> expected = repository.findByBrandIdIn(brandIds);

        assertThat(products).isEqualTo(expected);

    }

    @Test
    void testIsPriceBetween() {
        int min = 100000;
        int max = 200000;
        List<Product> products = repository.findAll(productSpecs.isPriceBetween(min, max));
        List<Product> products2 = repository.findByPriceBettween(min, max);

        assertThat(products)
                .hasSize(4)
                .containsAll(products2)
                .allMatch(isPriceBetween(min, max));
    }

    @Test
    void testIsPriceGreaterOrEqual() {
        int value = 5000000;
        List<Product> products = repository.findAll(productSpecs.isPriceGreaterOrEqual(value));
        List<Product> products2 = repository.findByPriceGreaterThanOrEqual(value);

        assertThat(products)
                .hasSize(34)
                .containsAll(products2)
                .extracting(p -> p.getSalePrice() != null ? p.getSalePrice() : p.getOriginalPrice())
                .allMatch(price -> price >= value);
    }

    @Transactional
    @Test
    void testIsAttributeValueIdIn() {
        List<Long> attributeValueIds = List.of(1L, 5L);
        List<Product> products = repository.findAll(productSpecs.isAttributeValueContainAll(attributeValueIds));

        assertThat(products)
                .hasSizeGreaterThan(0)
                .allMatch(byFilters(attributeValueIds));
    }

    @Test
    void testIsAttributeValueContainAnyId() {
        List<AttributeValue> attributeValues = attributeValueRepostiory.findFirst7By();
        List<Long> attributeIds = attributeValues.stream().map(AttributeValue::getId).toList();

        List<Product> products = repository.findAll(
                productSpecs.isAttributeValueContainAnyId(attributeIds));

        List<Product> expected = repository.findByAttributeValuesIdIn(attributeIds);

        assertThat(products)
                .hasSizeGreaterThan(0)
                .isEqualTo(expected);
    }

    @Transactional
    @Test
    void testByFilter_WithFullParam() {
        Long brandId = 7L;// AKKO
        Integer min = 2000000;
        Integer max = 5000000;
        List<Long> filterIds = List.of(1L, 5L); // 3Mode // 108 NUT

        FilterParam filterParam = new FilterParam();
        filterParam.setBrandId(brandId);
        filterParam.setMin(min);
        filterParam.setMax(max);
        filterParam.setCategoryId(2L);
        // Phải có đồng thời tất cả
        filterParam.setFilterIds(filterIds);

        List<Product> expected = entityManager.getEntityManager()
                .createQuery("FROM Product AS p JOIN FETCH p.brand", Product.class)
                .getResultList()
                .stream()
                .filter(isBrandId(brandId))
                .filter(isPriceBetween(min, max))
                .filter(byFilters(filterIds))
                .toList();

        List<Product> products = repository.findAll(productSpecs.byFilter(filterParam));

        assertThat(products)
                .hasSize(2)
                .isEqualTo(expected);
    }

    public static Predicate<Product> isPriceBetween(int min, int max) {
        return p -> {
            int price = p.getSalePrice() != null ? p.getSalePrice() : p.getOriginalPrice();
            return price >= min && price <= max;
        };
    }

    // Brand
    public static Predicate<Product> isBrandId(long id) {
        return p -> p.getBrand().getId() == id;
    }

    public static Predicate<Product> byFilters(List<Long> filterIds) {
        return product -> product.getAttributeValues()
                .stream()
                .map(value -> value.getId())
                .collect(Collectors.toSet())
                .containsAll(filterIds);
    }

    @Test
    void testIsCategoryId() {
        Long categoryId = 2L;
        List<Product> products = repository.findAll(productSpecs.isCategoryId(categoryId));
        assertThat(products).hasSize(902);

    }

    @Test
    void testIsCategoryIn() {
        List<Long> categoryIds = categoryRepository.findIdsWithIdbyParentId(23L);
        List<Product> products = repository.findAll(productSpecs.isCategoryIn(categoryIds));
        List<Product> expected = repository.findByCategoryIdIn(categoryIds);

        assertThat(products).hasSizeGreaterThan(0)
                .isEqualTo(expected);
    }

    @Test
    void testIsKeyWordContainIngoreCase_searchById() {
        String keyword = "ak";
        String upperKeyword = keyword.toUpperCase();
        List<Product> products = repository.findAll(productSpecs.isKeyWordContainIngoreCase(keyword));

        assertThat(products).hasSizeGreaterThan(0)
                .anyMatch(p -> !p.getProductCode().toUpperCase().contains(upperKeyword))
                .allMatch(p -> p.getProductCode().toUpperCase().contains(upperKeyword)
                        || p.getName().toUpperCase().contains(upperKeyword));

    }

}
