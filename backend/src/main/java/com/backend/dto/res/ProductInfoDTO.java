package com.backend.dto.res;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Product}
 */
@Setter
@Getter
public class ProductInfoDTO implements Serializable {
    private String id;
    private String name;
    private int viewCount;
    private String[] specifications;
    private Integer salePrice;
    private int quantity;
    private int originalPrice;
    private int categoryId;
    private List<ThumbnailDTO> thumbnails;
    private List<RelateDTO> relatedProducts;

    /**
     * DTO for {@link com.backend.entities.Image}
     */
    @Getter
    @Setter
    public static class ThumbnailDTO {
        private Long id;
        private String src;
        private String alt;
        private int position;
    }

}
