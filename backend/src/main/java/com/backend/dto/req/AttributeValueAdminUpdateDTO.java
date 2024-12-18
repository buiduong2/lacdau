package com.backend.dto.req;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeValueAdminUpdateDTO {
    @NotEmpty
    @Length(max = 50)
    private String name;

    private Long attributeId;
}
