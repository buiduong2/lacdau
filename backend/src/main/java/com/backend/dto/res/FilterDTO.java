package com.backend.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class FilterDTO {

    private PriceInfo price;

    private FilterGroup brand;

    private FilterGroup[] attributes;

    @Setter
    @Getter
    public static class FilterItemDTO {
        private String name;
        private long id;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FilterGroup {
        private String name;
        private FilterItemDTO[] attributeValues;
    }
}
