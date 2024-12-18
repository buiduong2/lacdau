package com.backend.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryAdminUpdateDTO {

    @NotEmpty
    private String name;

    private Long parentId;

    @Override
    public String toString() {
        return "CategoryAdminUpdateDTO [name=" + name + ", parentId=" + parentId + "]";
    }

}
