package com.backend.processor.progress;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.backend.entities.Order;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderType;
import com.backend.processor.AbstractOrderProcessor;
import com.backend.service.OrderAdminService;
import com.backend.service.OrderLogService;

@Component
@org.springframework.core.annotation.Order(1)
@Qualifier("progress")
public class PendingOrderProcessor extends AbstractOrderProcessor {

    public PendingOrderProcessor(OrderLogService orderLogService, OrderAdminService orderService) {
        super(orderLogService, orderService);
    }

    @Override
    public boolean supports(OrderStage stage) {
        return stage == OrderStage.PENDING;
    }

    @Override
    protected void validateCompletion(Order order, OrderStage stage) {
        return;
    }

    @Override
    protected OrderStage getNextStage(OrderType type) {
        return OrderStage.CONFIRMATION;
    }

}
