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
@org.springframework.core.annotation.Order(5)
@Qualifier("progress")
public class ShippingOrderProcessor extends AbstractOrderProcessor {

    public ShippingOrderProcessor(OrderLogService orderLogService, OrderAdminService orderService) {
        super(orderLogService, orderService);
    }

    @Override
    public boolean supports(OrderStage stage) {
        return stage == OrderStage.SHIPPING;
    }

    @Override
    protected void validateCompletion(Order order, OrderStage nextStage) {
        return;
    }

    @Override
    protected OrderStage getNextStage(OrderType type) {
        return OrderStage.DELIVERED;
    }

}
