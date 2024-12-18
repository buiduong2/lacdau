package com.backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.backend.dto.req.ProductUpdateFullDTO;
import com.backend.dto.req.ProductUpdateFullDTO.RelateUpdateDTO;
import com.backend.dto.res.ProductAdminDTO;
import com.backend.entities.AttributeValue;
import com.backend.entities.Product;
import com.backend.entities.ProductStatus;
import com.backend.entities.Product_;
import com.backend.repository.TestProductRepository;
import com.backend.utils.ServiceTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@ServiceTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private TestProductRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private Validator validator;

    @MockBean
    private ProductCodeGeneratorService codeGeneratorService;

    @Test
    void testEditWithFullAssociationsById_whenRemoveAllAssociations_ShouldWork() {
        ProductUpdateFullDTO dto = new ProductUpdateFullDTO();
        Set<ConstraintViolation<ProductUpdateFullDTO>> error = validator.validate(dto);
        assertThat(error).isNotEmpty();
        String name = "Duong1";
        int originalPrice = 1000;
        int salePrice = 800;
        int quantity = 100;
        String id = "BKAK0001";
        String prefix = "BKAK";

        dto.setPrefixProductCode(prefix);
        dto.setName(name);
        dto.setOriginalPrice(originalPrice);
        dto.setSalePrice(salePrice);
        dto.setQuantity(quantity);
        dto.setStatus("ACTIVE");

        error = validator.validate(dto);
        assertThat(error).isEmpty();

        productService.editById(id, dto);

        entityManager.flush();
        entityManager.clear();

        Product actual = productService.findById(id);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue(Product_.PRODUCT_CODE, id)
                .hasFieldOrPropertyWithValue(Product_.SALE_PRICE, salePrice)
                .hasFieldOrPropertyWithValue(Product_.ORIGINAL_PRICE, originalPrice)
                .hasFieldOrPropertyWithValue(Product_.QUANTITY, quantity)
                .hasFieldOrPropertyWithValue(Product_.NAME, name)
                .hasFieldOrPropertyWithValue(Product_.BRAND, null)
                .hasFieldOrPropertyWithValue(Product_.CATEGORY, null)
                .hasFieldOrPropertyWithValue(Product_.MAIN_IMAGE, null)
                .hasFieldOrPropertyWithValue(Product_.DETAIL, null)
                .hasFieldOrPropertyWithValue(Product_.RELATE_INFO, null)
                .hasFieldOrPropertyWithValue(Product_.ATTRIBUTE_VALUES, Set.of());

        // Associate Relation has a should REMOVE
        Long countProductDetail = entityManager.getEntityManager()
                .createQuery("SELECT COUNT(e) FROM ProductDetail AS e  WHERE e.product IS NULL",
                        Long.class)
                .getSingleResult();

        Long countProductRelateInfo = entityManager.getEntityManager()
                .createQuery("SELECT COUNT(e) FROM RelateInfo AS e WHERE e.product IS NULL", Long.class)
                .getSingleResult();
        Long countMainImage = entityManager.getEntityManager()
                .createQuery(
                        "SELECT COUNT(i) FROM Image AS i  WHERE i.productDetail IS NOT NULL AND i.id NOT IN (SELECT c.image.id FROM Category AS c) ",
                        Long.class)
                .getSingleResult();
        assertThat(countProductDetail).isEqualTo(0);
        assertThat(countProductRelateInfo).isEqualTo(0);
        assertThat(countMainImage).isEqualTo(0);

    }

    @Test
    void testEditWithFullAssociationsById_whenChangeAllAssociations_ShouldWork() {
        ProductUpdateFullDTO dto = new ProductUpdateFullDTO();
        Set<ConstraintViolation<ProductUpdateFullDTO>> error = validator.validate(dto);
        assertThat(error).isNotEmpty();
        String productCode = "BKAK";
        String id = "BKAK0001";
        String name = "Duong1";
        int originalPrice = 1000;
        int salePrice = 800;
        int quantity = 100;
        Long brandId = 3L;
        Long categoryId = 78L;
        Long relateGroupId = 109L;
        List<Long> attributeValueIds = List.of(1L, 2L, 3L);
        dto.setPrefixProductCode(productCode);

        dto.setName(name);
        dto.setOriginalPrice(originalPrice);
        dto.setSalePrice(salePrice);
        dto.setQuantity(quantity);
        dto.setStatus("ACTIVE");
        dto.setBrandId(brandId);
        dto.setCategoryId(categoryId);

        RelateUpdateDTO relate = new RelateUpdateDTO();
        relate.setName("Abc");
        relate.setRelateGroupId(relateGroupId);
        dto.setRelateInfo(relate);
        dto.setAttributeValueIds(attributeValueIds);

        error = validator.validate(dto);
        assertThat(error).isEmpty();

        productService.editById(id, dto);

        entityManager.flush();
        entityManager.clear();

        Product actual = productService.findById(id);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue(Product_.PRODUCT_CODE, id)
                .hasFieldOrPropertyWithValue(Product_.SALE_PRICE, salePrice)
                .hasFieldOrPropertyWithValue(Product_.ORIGINAL_PRICE, originalPrice)
                .hasFieldOrPropertyWithValue(Product_.QUANTITY, quantity)
                .hasFieldOrPropertyWithValue(Product_.NAME, name)
                .matches(p -> p.getBrand().getId() == brandId)
                .matches(p -> p.getCategory().getId() == categoryId)
                .matches(p -> p.getMainImage() == null)
                .matches(p -> p.getAttributeValues().size() == attributeValueIds.size())
                .matches(p -> p.getAttributeValues()
                        .stream()
                        .map(AttributeValue::getId)
                        .allMatch(attributeValueIds::contains));

    }

    @Test
    void testCreate() {

        Mockito.when(codeGeneratorService.getNextCounterByKey("BKAK")).thenReturn("BAKABA001");

        ProductUpdateFullDTO dto = new ProductUpdateFullDTO();
        Set<ConstraintViolation<ProductUpdateFullDTO>> error = validator.validate(dto);
        assertThat(error).isNotEmpty();
        String name = "Duong1";
        int originalPrice = 1000;
        int salePrice = 800;
        int quantity = 100;
        Long brandId = 3L;
        Long categoryId = 78L;
        Long relateGroupId = 109L;
        List<Long> attributeValueIds = List.of(1L, 2L, 3L);
        String prefix = "BKAK";
        // Mockito.when(null)

        dto.setPrefixProductCode(prefix);
        dto.setName(name);
        dto.setOriginalPrice(originalPrice);
        dto.setSalePrice(salePrice);
        dto.setQuantity(quantity);
        dto.setBrandId(brandId);
        dto.setCategoryId(categoryId);
        dto.setStatus("ACTIVE");

        RelateUpdateDTO relate = new RelateUpdateDTO();
        relate.setName("Abc");
        relate.setRelateGroupId(relateGroupId);
        dto.setRelateInfo(relate);
        dto.setAttributeValueIds(attributeValueIds);

        error = validator.validate(dto);
        assertThat(error).isEmpty();

        ProductAdminDTO result = productService.create(dto);

        entityManager.flush();
        entityManager.clear();

        Product actual = productService.findById(result.getId());

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue(Product_.PRODUCT_CODE, "BAKABA001")
                .hasFieldOrPropertyWithValue(Product_.PRODUCT_CODE, result.getId())
                .hasFieldOrPropertyWithValue(Product_.SALE_PRICE, salePrice)
                .hasFieldOrPropertyWithValue(Product_.ORIGINAL_PRICE, originalPrice)
                .hasFieldOrPropertyWithValue(Product_.QUANTITY, quantity)
                .hasFieldOrPropertyWithValue(Product_.NAME, name)
                .hasFieldOrPropertyWithValue(Product_.STATUS, ProductStatus.ACTIVE)
                .matches(p -> p.getBrand().getId() == brandId)
                .matches(p -> p.getCategory().getId() == categoryId)
                .matches(p -> p.getMainImage() == null)
                .matches(p -> p.getAttributeValues().size() == attributeValueIds.size())
                .matches(p -> p.getAttributeValues()
                        .stream()
                        .map(AttributeValue::getId)
                        .allMatch(attributeValueIds::contains));

    }

    @Test
    void testDeleteByProductCode() {
        String productCode = "BPEDR0076";
        Product p = repository.findByProductCode(productCode).get();
        assertThat(p).isNotNull();
        entityManager.flush();
        entityManager.clear();
        productService.deleteByProductCode(productCode);
        entityManager.flush();
        entityManager.clear();

        assertThat(repository.existsById(p.getId())).isFalse();

    }
}
