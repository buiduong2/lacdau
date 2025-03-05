package com.backend.processor.progress;

import static com.backend.entities.enums.OrderStageAction.CREATE_SHIPMENT;
import static com.backend.entities.enums.OrderStageAction.UPDATE_SHIPMENT;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.backend.dto.req.OrderProcessShipmentUpdateDTO;
import com.backend.dto.req.OrderProcessStageDTO;
import com.backend.entities.Employee;
import com.backend.entities.Order;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderStageAction;
import com.backend.entities.enums.OrderType;
import com.backend.exception.ValidationException;
import com.backend.processor.AbstractOrderProcessor;
import com.backend.processor.OrderActionConsummer;
import com.backend.service.MailSenderService;
import com.backend.service.OrderAdminService;
import com.backend.service.OrderLogService;

@Component
@org.springframework.core.annotation.Order(4)
@Qualifier("progress")
public class PackingOrderProcessor extends AbstractOrderProcessor {

    private final MailSenderService mailSenderService;

    private final Map<OrderStageAction, OrderActionConsummer> actionHandlers = Map.of(
            CREATE_SHIPMENT, this::handleCreateShipment,
            UPDATE_SHIPMENT, this::handleUpdateShipment

    );

    public PackingOrderProcessor(OrderLogService orderLogService, OrderAdminService orderService,
            MailSenderService mailSenderService) {
        super(orderLogService, orderService);
        this.mailSenderService = mailSenderService;
    }

    @Override
    protected void postProcessStage(Order order, OrderProcessStageDTO dto, Employee employee) {
        this.mailSenderService.sendOrderMailPlaced(order);
    }

    public void handleCreateShipment(Order order, Employee employee, Object payload) {
        if (order.getType() == OrderType.IN_STORE) {
            throw new ValidationException("type", "Order INS_STORE can't not create shipment");
        }
        if (payload instanceof OrderProcessShipmentUpdateDTO.ShipmentDTO dto) {
            this.orderService.createOrUpdate(order, employee, dto);
        }
    }

    public void handleUpdateShipment(Order order, Employee employee, Object payload) {
        if (order.getType() == OrderType.IN_STORE) {
            throw new ValidationException("type", "Order INS_STORE can't not create shipment");
        }
        if (payload instanceof OrderProcessShipmentUpdateDTO.ShipmentDTO dto) {
            this.orderService.createOrUpdate(order, employee, dto);
        }
    }

    @Override
    protected Map<OrderStageAction, OrderActionConsummer> getActionHandlers() {
        return this.actionHandlers;
    }

    @Override
    public boolean supports(OrderStage stage) {
        return stage == OrderStage.PACKING;
    }

    @Override
    protected void validateCompletion(Order order, OrderStage nextStage) {
        if (nextStage.isCanceling()) {
            return;
        }

        if (nextStage == OrderStage.SHIPPING && order.getShipment() == null) {
            throw new ValidationException("nextStage", "Must be Create A shipment before perform process");
        }
    }

    @Override
    protected OrderStage getNextStage(OrderType type) {
        switch (type) {
            case IN_STORE:
                return OrderStage.COMPLETED;
            case ONLINE:
                return OrderStage.SHIPPING;
            default:
                throw new RuntimeException("Unimplemented OrderType " + type);
        }
    }

}
