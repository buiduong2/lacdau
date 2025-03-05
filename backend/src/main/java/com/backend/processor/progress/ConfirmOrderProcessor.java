package com.backend.processor.progress;

import static com.backend.entities.enums.OrderStageAction.UPDATE_ADDRESS;
import static com.backend.entities.enums.OrderStageAction.UPDATE_ORDER_ITEM;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.backend.dto.req.OrderProcessAddressUpdateDTO.OrderAddressUpdateDTO;
import com.backend.dto.req.OrderProcessOrderItemUpdateDTO.OrderItemUpdateDTO;
import com.backend.entities.Employee;
import com.backend.entities.Order;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderStageAction;
import com.backend.entities.enums.OrderType;
import com.backend.processor.AbstractOrderProcessor;
import com.backend.processor.OrderActionConsummer;
import com.backend.service.OrderAdminService;
import com.backend.service.OrderLogService;

@Component
@org.springframework.core.annotation.Order(2)
@Qualifier("progress")
public class ConfirmOrderProcessor extends AbstractOrderProcessor {

    private final Map<OrderStageAction, OrderActionConsummer> actionHandlers = Map.of(
            UPDATE_ADDRESS, this::handleEditAddress,
            UPDATE_ORDER_ITEM, this::handleEditOrderItem

    );

    public ConfirmOrderProcessor(OrderLogService orderLogService, OrderAdminService orderService) {
        super(orderLogService, orderService);
    }

    private void handleEditAddress(Order order, Employee employee, Object payload) {
        if (payload instanceof OrderAddressUpdateDTO dto) {
            orderService.updateOrerAddress(order, employee, dto);
        }
    }

    private void handleEditOrderItem(Order order, Employee employee, Object payload) {
        if (payload instanceof OrderItemUpdateDTO dto) {
            orderService.updateOrderItems(order, employee, dto);
        }
    }

    @Override
    protected Map<OrderStageAction, OrderActionConsummer> getActionHandlers() {
        return actionHandlers;
    }

    @Override
    protected void validateCompletion(Order order, OrderStage nextStage) {
        return;
    }

    @Override
    public boolean supports(OrderStage stage) {
        return stage == OrderStage.CONFIRMATION;
    }

    @Override
    protected OrderStage getNextStage(OrderType type) {
        return OrderStage.PAYMENT;
    }

}
