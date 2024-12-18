package com.backend.dto.res;

import java.time.LocalDateTime;

import com.backend.entities.ProductStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Product}
 */

@Getter
@Setter
public class ProductAdminDTO {
    private long natureId;
    private String id;
    private String name;
    private int originalPrice;
    private Integer salePrice;
    private int quantity;
    private ProductImageDTO mainImage;
    private Long categoryId;
    private Long brandId;
    private Long detailId;
    private Long relateInfoId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ProductStatus status;

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