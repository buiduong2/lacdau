package com.backend.exception;

import com.backend.entities.enums.OrderStage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderActionNotAllowedInStageException extends RuntimeException {
    public OrderActionNotAllowedInStageException(OrderStage currentStage, String action) {
        super("Action: " + action + "Not Allow in " + currentStage.toString());
    }
}
