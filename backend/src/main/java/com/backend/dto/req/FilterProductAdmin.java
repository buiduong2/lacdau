package com.backend.dto.req;

import java.util.List;

import com.backend.entities.ProductStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterProductAdmin {
    private String search;
    private List<Long> categoryIds;
    private List<Long> brandIds;
    private List<Long> attributeValueIds;
    private List<Integer> minMaxPrice;
    private List<ProductStatus> status;
}
