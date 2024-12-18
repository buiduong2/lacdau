package com.backend.dto.req;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeAdminUpdateDTO {
    @NotEmpty
    @Length(max = 255)
    private String name;

    @Size(min = 1)
    @Valid
    private List<AttributeValueDTO> attributeValues;

    @Size(min = 1)
    private List<Long> removeAttributeValueId;

    @Getter
    @Setter
    public static class AttributeValueDTO {
        @NotEmpty
        private String name;
    }
}
