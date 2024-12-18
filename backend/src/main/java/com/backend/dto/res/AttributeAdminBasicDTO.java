package com.backend.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Attribute}
 */

@Getter
@Setter
public class AttributeAdminBasicDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Long> attributeValueIds;
}
