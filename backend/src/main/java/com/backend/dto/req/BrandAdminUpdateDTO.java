package com.backend.dto.req;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Brand}
 */

@Getter
@Setter
public class BrandAdminUpdateDTO {
    @NotEmpty
    @Length(min = 1, max = 50)
    private String name;
}
