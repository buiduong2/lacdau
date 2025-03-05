package com.backend.dto.res;

import java.time.LocalDateTime;

import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderStageAction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderLogDTO {

    private long id;

    private long orderId;

    private OrderStage stage;

    private OrderStageAction action;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
