package com.backend.dto.res;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Product}
 */
@Setter
@Getter
public class ProductRelateInfoAdminDTO {
    private String id;
    private String name;
    private int originalPrice;
    private Integer salePrice;
    private int quantity;
    private ProductImageDTO mainImage;
    private RelateDTO relateInfo;

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

    @Setter
    @Getter
    public static class RelateDTO {
        private Long id;

        private String name;

        private Long relateGroupId;
    }

}
