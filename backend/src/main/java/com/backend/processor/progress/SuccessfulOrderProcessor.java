package com.backend.processor.progress;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.backend.dto.req.OrderProcessStageDTO;
import com.backend.entities.Employee;
import com.backend.entities.Order;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderType;
import com.backend.processor.AbstractOrderProcessor;
import com.backend.service.MailSenderService;
import com.backend.service.OrderAdminService;
import com.backend.service.OrderLogService;

@Component
@org.springframework.core.annotation.Order(7)
@Qualifier("progress")
public class SuccessfulOrderProcessor extends AbstractOrderProcessor {

    private final MailSenderService mailSenderService;

    public SuccessfulOrderProcessor(OrderLogService orderLogService, OrderAdminService orderService,
            MailSenderService mailSenderService) {
        super(orderLogService, orderService);
        this.mailSenderService = mailSenderService;
    }

    @Override
    protected void postProcessStage(Order order, OrderProcessStageDTO dto, Employee employee) {
        this.mailSenderService.sendOrderMailThankYou(order);
    }

    @Override
    public boolean supports(OrderStage stage) {
        return stage == OrderStage.SUCCESSFUL;
    }

    @Override
    protected void validateCompletion(Order order, OrderStage nextStage) {
        return;
    }

    @Override
    protected OrderStage getNextStage(OrderType type) {
        return OrderStage.COMPLETED;
    }

}
