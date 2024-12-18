package com.backend.dto.req;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilterParam {
    private List<Long> filterIds;
    private long categoryId;
    private Integer min;
    private Integer max;
    private Long brandId;
}
