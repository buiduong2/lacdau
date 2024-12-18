package com.backend.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Product}
 */
@Setter
@Getter
@AllArgsConstructor
public class ProductSummaryDTO {
    private String id;
    private String name;
    private int originalPrice;
    private Integer salePrice;
    private int quantity;
    private ProductImageDTO mainImage;

    /**
     * DTO for {@link com.backend.entities.Image}
     */
    @Setter
    @Getter
    public static class ProductImageDTO {
        private Long id;

        private String src;

        private String alt;

    }
}
