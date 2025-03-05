package com.backend.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCancellationDeleteDTO {
    @NotEmpty
    private String description;
}
