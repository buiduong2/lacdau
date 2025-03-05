package com.backend.processor.progress;

import static com.backend.entities.enums.OrderStageAction.UPDATE_PAYMENT;
import static com.backend.entities.enums.OrderStageAction.UPDATE_PAYMENT_AMOUNT_PAID;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.backend.dto.req.OrderProcessPaymentPaidUpdateDTO;
import com.backend.dto.req.OrderProcessPaymentUpdateDTO;
import com.backend.entities.Employee;
import com.backend.entities.Order;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderStageAction;
import com.backend.entities.enums.OrderType;
import com.backend.entities.enums.PaymentStatus;
import com.backend.exception.ValidationException;
import com.backend.processor.AbstractOrderProcessor;
import com.backend.processor.OrderActionConsummer;
import com.backend.service.OrderAdminService;
import com.backend.service.OrderLogService;
import com.backend.utils.Payments;

@Component
@org.springframework.core.annotation.Order(3)
@Qualifier("progress")
public class PaymentOrderProcessor extends AbstractOrderProcessor {

    private final Map<OrderStageAction, OrderActionConsummer> actionHandlers = Map.of(
            UPDATE_PAYMENT, this::handleUpdatePayment,
            UPDATE_PAYMENT_AMOUNT_PAID, this::handleUpdatePaymentAmountPaid

    );

    public PaymentOrderProcessor(OrderLogService orderLogService, OrderAdminService orderService) {
        super(orderLogService, orderService);
    }

    private void handleUpdatePayment(Order order, Employee employee, Object payload) {
        if (payload instanceof OrderProcessPaymentUpdateDTO.PaymentUpdateDTO dto) {
            this.orderService.updatePayment(order, employee, dto);
        }
    }

    private void handleUpdatePaymentAmountPaid(Order order, Employee employee, Object payload) {
        if (payload instanceof OrderProcessPaymentPaidUpdateDTO.PaymentPaidUpdateDTO dto) {
            this.orderService.updatePaymentPaid(order, employee, dto);
        }
    }

    @Override
    protected Map<OrderStageAction, OrderActionConsummer> getActionHandlers() {
        return actionHandlers;
    }

    @Override
    public boolean supports(OrderStage stage) {
        return stage == OrderStage.PAYMENT;
    }

    @Override
    protected void validateCompletion(Order order, OrderStage nextStage) {
        PaymentStatus status = Payments.getPaymentStatus(order.getPayment());

        if (nextStage.isCanceling()) {
            return;
        } else {
            if (status == PaymentStatus.PAID) {
                return;
            }
            if (status == PaymentStatus.PROCESSING) {
                throw new ValidationException("payment", "Should update infomation Payment");
            }

            if (status == PaymentStatus.PENDING) {
                throw new ValidationException("payment", "Should wait for customer paid");
            }

            throw new RuntimeException("Status not found");
        }

    }

    @Override
    protected OrderStage getNextStage(OrderType type) {
        return OrderStage.PACKING;
    }

}
