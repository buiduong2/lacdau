package com.backend.dto.req;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Product}
 */
@Setter
@Getter
public class ProductAdminFullDTO {
    
    private String id;

    private String name;

    private String prefixProductCode;

    private int originalPrice;
    private Integer salePrice;

    private int quantity;

    private String status;

    private DetailUpdateDTO detail;

    private RelateUpdateDTO relateInfo;

    private List<Long> attributeValueIds;
    private Long categoryId;
    private Long brandId;

    private ImageDTO mainImage;

    @Getter
    @Setter
    public static class ImageDTO {
        private long id;
        private String src;
        private String alt;
        private int position;
    }

    @Setter
    @Getter
    public static class DetailUpdateDTO {
        private String[] specifications;
        private List<ImageDTO> thumbnails;
    }

    @Getter
    @Setter
    public static class RelateUpdateDTO {

        private String name;

        private Long relateGroupId;
    }

}