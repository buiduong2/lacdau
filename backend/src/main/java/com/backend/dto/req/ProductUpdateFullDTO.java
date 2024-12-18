package com.backend.dto.req;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.backend.entities.ProductStatus;
import com.backend.validation.EnumValue;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Product}
 */
@Setter
@Getter
public class ProductUpdateFullDTO {

    @NotEmpty
    @Length(max = 150)
    private String name;

    @NotEmpty
    @Length(max = 10)
    private String prefixProductCode;

    @Positive
    private int originalPrice;
    @Positive
    private Integer salePrice;

    @Positive
    private int quantity;

    @NotNull
    @EnumValue(enumClass = ProductStatus.class)
    private String status;

    @Valid
    @NotNull
    private DetailUpdateDTO detail;

    @Valid
    private RelateUpdateDTO relateInfo;

    private List<Long> attributeValueIds;
    private Long categoryId;
    private Long brandId;

    @Setter
    @Getter
    public static class DetailUpdateDTO {
        private String[] specifications;
    }

    @Getter
    @Setter
    public static class RelateUpdateDTO {

        @NotEmpty
        private String name;

        @NotNull
        @Positive
        private Long relateGroupId;
    }

}