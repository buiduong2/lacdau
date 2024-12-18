package com.backend.dto.req;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeAdminCreateDTO {
    @NotEmpty
    @Length(max = 255)
    private String name;

    @NotEmpty
    @Valid
    private List<AttributeValueDTO> attributeValues;

    @Getter
    @Setter
    public static class AttributeValueDTO {
        @NotEmpty
        private String name;
    }
}
