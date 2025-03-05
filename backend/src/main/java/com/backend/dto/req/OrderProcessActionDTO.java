package com.backend.dto.req;

import com.backend.entities.enums.OrderStageAction;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class OrderProcessActionDTO {

    @Null
    @JsonIgnore
    private OrderStageAction action;

    @NotEmpty
    private String description;

    public abstract Object getPayload();
}
