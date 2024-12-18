package com.backend.dto.res;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Brand}
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandAdminBasicDTO {
    private Long id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
