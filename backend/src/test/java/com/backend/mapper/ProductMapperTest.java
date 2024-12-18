package com.backend.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.entities.Brand;
import com.backend.entities.Category;
import com.backend.entities.Product;
import com.backend.entities.ProductDetail;
import com.backend.entities.RelateGroup;
import com.backend.entities.RelateInfo;
import com.backend.repository.AttributeValueRepository;
import com.backend.repository.RelateInfoRepository;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

    @Mock
    private AttributeValueRepository attributeValueRepository;

    @Mock
    private RelateInfoRepository relateInfoRepository;

    @InjectMocks
    private ProductMapper mapper = new ProductMapperImpl();

    @Test
    void testToUpdateFullDTO() {
        var category = new Category();
        var brand = new Brand();
        var productDetail = new ProductDetail();
        var relateInfo = new RelateInfo();
        var relateGroup = new RelateGroup();

        String[] specficiations = new String[] { "1", "2" };
        List<Long> attributeValueIds = List.of(1L, 2L, 3L, 4L, 5L);
        String relateName = "relateINfo";

        long categoryId = 1L;
        category.setId(categoryId);
        long brandId = 2L;
        brand.setId(brandId);
        long relateGroupId = 3L;
        relateGroup.setId(relateGroupId);

        relateInfo.setName(relateName);
        relateInfo.setRelateGroup(relateGroup);
        productDetail.setSpecifications(String.join("\n", specficiations));

        var product = new Product();
        String name = "Product Name";
        product.setName(name);
        product.setProductCode("BAK123");
        int originalPrice = 10;
        product.setOriginalPrice(originalPrice);
        int salePrice = 11;
        product.setSalePrice(salePrice);
        int quantity = 13;
        product.setQuantity(quantity);

        product.setDetail(productDetail);
        product.setBrand(brand);
        product.setCategory(category);
        product.setRelateInfo(relateInfo);

        Mockito.when(attributeValueRepository.findIdsByProductIds(null)).thenReturn(attributeValueIds);
        var productUpdateFullDTO = mapper.toUpdateFullDTO(product);
        assertThat(productUpdateFullDTO.getName()).isEqualTo(name);
        assertThat(productUpdateFullDTO.getPrefixProductCode()).isEqualTo("BAK");
        assertThat(productUpdateFullDTO.getOriginalPrice()).isEqualTo(originalPrice);
        assertThat(productUpdateFullDTO.getSalePrice()).isEqualTo(salePrice);
        assertThat(productUpdateFullDTO.getQuantity()).isEqualTo(quantity);

        assertThat(productUpdateFullDTO.getDetail().getSpecifications()).isEqualTo(specficiations);

        assertThat(productUpdateFullDTO.getBrandId()).isEqualTo(brandId);
        assertThat(productUpdateFullDTO.getCategoryId()).isEqualTo(categoryId);
        assertThat(productUpdateFullDTO.getAttributeValueIds()).isEqualTo(attributeValueIds);

        assertThat(productUpdateFullDTO.getRelateInfo().getName()).isEqualTo(relateName);
        assertThat(productUpdateFullDTO.getRelateInfo().getRelateGroupId()).isEqualTo(relateGroupId);

    }

}
