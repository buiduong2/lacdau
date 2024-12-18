package com.backend.dto.res;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO for {@link com.backend.entities.AttributeValue}
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttributeValueAdminDTO {
    private Long id;

    private String name;

    private Long attributeId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private long productCount;

}
