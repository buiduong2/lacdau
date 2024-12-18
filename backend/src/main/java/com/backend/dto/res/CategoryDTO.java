package com.backend.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Category}
 */
@Getter
@Setter
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String imageSrc;
    private Long parentId;
}
