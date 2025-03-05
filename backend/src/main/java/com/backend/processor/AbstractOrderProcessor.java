package com.backend.processor;

import java.util.Map;

import com.backend.dto.req.OrderProcessActionDTO;
import com.backend.dto.req.OrderProcessStageDTO;
import com.backend.entities.Employee;
import com.backend.entities.Order;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderStageAction;
import com.backend.entities.enums.OrderType;
import com.backend.exception.OrderActionNotAllowedInStageException;
import com.backend.exception.ValidationException;
import com.backend.service.OrderAdminService;
import com.backend.service.OrderLogService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractOrderProcessor {

    private final OrderLogService orderLogService;

    protected final OrderAdminService orderService;

    public void processStage(Order order, OrderProcessStageDTO dto, Employee employee) {
        OrderStage nextStage = OrderStage.valueOf(dto.getNextStage());
        this.validateNextStage(order, nextStage);
        this.validateCompletion(order, nextStage);
        preProcessStage(order, dto, employee);
        order.setStage(nextStage);
        logOrderProcess(dto, order, employee);
        postProcessStage(order, dto, employee);
    };

    protected void preProcessStage(Order order, OrderProcessStageDTO dto, Employee employee) {
        return;
    }

    protected void postProcessStage(Order order, OrderProcessStageDTO dto, Employee employee) {
        return;
    }

    protected Map<OrderStageAction, OrderActionConsummer> getActionHandlers() {
        return Map.of();
    }

    public void handleAction(Order order, OrderProcessActionDTO dto, Employee employee) {

        OrderActionConsummer handler = getActionHandlers().get(dto.getAction());
        if (handler == null) {
            throw new OrderActionNotAllowedInStageException(order.getStage(), dto.getAction().toString());
        }

        handler.handle(order, employee, dto.getPayload());
        logOrderAction(dto, order, employee);
    }

    protected void logOrderProcess(OrderProcessStageDTO dto, Order order, Employee employee) {
        orderLogService.create(null, dto.getDescription(), order, employee);

    }

    protected void logOrderAction(OrderProcessActionDTO dto, Order order, Employee employee) {
        orderLogService.create(dto.getAction(), dto.getDescription(), order, employee);
    }

    public abstract boolean supports(OrderStage stage);

    abstract protected void validateCompletion(Order order, OrderStage nextStage);

    protected void validateNextStage(Order order, OrderStage nextStage) {
        if (order.getStage().isCanceling() == nextStage.isCanceling()) {
            if (getNextStage(order.getType()) == nextStage) {
                return;
            } else {
                throw new ValidationException("nextStage", "Next stage not allowed");
            }
        } else if (nextStage.isCanceling()) {
            if (nextStage == OrderStage.FAILURE) {
                return;
            } else {
                throw new ValidationException("nextStage", "Next stage not allowed. Suggest: FAILURE");
            }
        } else {
            throw new ValidationException("nextStage",
                    " Next Stage not allowed. Can't not tranisition from canceling to process");
        }
    }

    abstract protected OrderStage getNextStage(OrderType type);

}
