package com.backend.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.dto.req.ProductAdminFullDTO;
import com.backend.dto.req.ProductUpdateFullDTO;
import com.backend.dto.res.FilterDTO.FilterGroup;
import com.backend.dto.res.FilterDTO.FilterItemDTO;
import com.backend.dto.res.ProductAdminDTO;
import com.backend.dto.res.ProductInfoDTO;
import com.backend.dto.res.ProductInfoDTO.ThumbnailDTO;
import com.backend.dto.res.ProductRelateInfoAdminDTO;
import com.backend.dto.res.ProductSummaryDTO;
import com.backend.dto.res.RelateDTO;
import com.backend.entities.Attribute;
import com.backend.entities.AttributeValue;
import com.backend.entities.Brand;
import com.backend.entities.Image;
import com.backend.entities.Product;
import com.backend.entities.ProductDetail;
import com.backend.repository.AttributeValueRepository;
import com.backend.repository.RelateInfoRepository;

import lombok.NoArgsConstructor;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@NoArgsConstructor
public abstract class ProductMapper {

    @Autowired
    private RelateInfoRepository productRelateInfoRepository;

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    @Mapping(target = "thumbnails", expression = "java( toThumbnailDto(product) )")
    @Mapping(target = "relatedProducts", expression = "java( getRelateDtos(product) )")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "id", source = "productCode")
    public abstract ProductInfoDTO toProductInfoDto(Product product);

    public abstract ThumbnailDTO fromImagetoThumbnailDTO(Image productImage);

    @Mapping(target = "id", source = "productCode")
    public abstract ProductSummaryDTO toProductSummaryDto(Product product);

    @Mapping(target = "natureId", source = "id")
    @Mapping(target = "id", source = "productCode")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "detailId", source = "detail.id")
    @Mapping(target = "relateInfoId", source = "relateInfo.id")
    public abstract ProductAdminDTO toProductAdminDto(Product product);

    @Mapping(target = "relateInfo.relateGroupId", source = "relateInfo.relateGroup.id")
    @Mapping(target = "id", source = "productCode")
    public abstract ProductRelateInfoAdminDTO toProductRelateInfoAdminDTO(Product product);

    public abstract FilterItemDTO toFilterItemDto(Brand productBrand);

    @Mapping(target = "attributeValues", source = "attributeValues")
    public abstract FilterGroup toFilterAttributeDto(Attribute productAttribute);

    public abstract FilterGroup[] toFilterAttributeDto(List<Attribute> productAttribute);

    public abstract FilterItemDTO toFilterAttributeValueDto(AttributeValue productAttributeValue);

    public abstract FilterItemDTO[] toFilterAttributeValueDtos(List<AttributeValue> attributeValues);

    public List<ThumbnailDTO> toThumbnailDto(Product product) {
        ProductDetail productDetail = product.getDetail();
        return productDetail.getThumbnails().stream().map(this::fromImagetoThumbnailDTO).toList();
    }

    public List<RelateDTO> getRelateDtos(Product product) {
        if (product.getRelateInfo() == null) {
            return null;
        }
        return productRelateInfoRepository.findByRelateById(product.getRelateInfo().getId());
    }

    @ToEntity
    @Mapping(target = "detail.specifications", expression = "java( String.join(\"\\n\",detailUpdateDTO.getSpecifications()))")
    public abstract void updateFullEntity(@MappingTarget Product entity, ProductUpdateFullDTO dto);

    @Mapping(target = "id", source = "productCode")
    @Mapping(target = "relateInfo.relateGroupId", source = "relateInfo.relateGroup.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "attributeValueIds", qualifiedByName = { "AttributeValuesToId" }, source = ".")
    @Mapping(target = "prefixProductCode", qualifiedByName = { "ProductCodeToPrefixCode" }, source = ".")
    public abstract ProductAdminFullDTO toUpdateFullDTO(Product product);

    public String[] toSpecifications(String speccifications) {
        if (speccifications == null || speccifications.isEmpty()) {
            return new String[] {};
        }

        return speccifications.split("\n");
    };

    @Named("AttributeValuesToId")
    public List<Long> getAttributeIds(Product product) {
        return attributeValueRepository.findIdsByProductIds(product.getId());
    }

    @Named("ProductCodeToPrefixCode")
    public String getPrefixCode(Product product) {

        StringBuilder sb = new StringBuilder();
        char[] chars = product.getProductCode().toCharArray();

        for (char c : chars) {
            if (c >= '0' && c <= '9') {
                return sb.toString();
            }
            sb.append(c);
        }

        return sb.toString();
    }
}
