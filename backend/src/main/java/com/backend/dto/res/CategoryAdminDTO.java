package com.backend.dto.res;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Category}
 */
@Getter
@Setter
public class CategoryAdminDTO {
    private Long id;
    private String name;
    private String imageSrc;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryAdminDTO(Long id, String name, String imageSrc, Long parentId, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.imageSrc = imageSrc;
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
