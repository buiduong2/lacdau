package com.backend.dto.req;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * @link com.backend.entities.RelateGroup
 */

@Getter
@Setter
public class RelateGroupAdminUpdateDTO {
    @NotEmpty
    private String name;

    @Valid
    private List<RelateInfoDTO> relateInfos;

    @Getter
    @Setter
    public static class RelateInfoDTO {

        @NotEmpty
        private String productId;

        @NotEmpty
        private String name;

        @Positive
        @NotNull
        private Integer position;
    }
}
