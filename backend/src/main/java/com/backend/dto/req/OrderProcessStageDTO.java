package com.backend.dto.req;

import com.backend.entities.enums.OrderStage;
import com.backend.validation.EnumValue;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProcessStageDTO {

    @EnumValue(enumClass = OrderStage.class)
    @NotNull
    private String nextStage;

    private String description;
}
