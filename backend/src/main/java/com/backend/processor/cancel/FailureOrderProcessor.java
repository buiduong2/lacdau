package com.backend.processor.cancel;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.backend.dto.req.OrderProcessPaymentPaidUpdateDTO.PaymentPaidUpdateDTO;
import com.backend.dto.req.OrderProcessStageDTO;
import com.backend.entities.Employee;
import com.backend.entities.Order;
import com.backend.entities.Payment;
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
@org.springframework.core.annotation.Order(1)
@Qualifier("cancel")
public class FailureOrderProcessor extends AbstractOrderProcessor {

    private final Map<OrderStageAction, OrderActionConsummer> actionHandlers = Map.of(
            OrderStageAction.UPDATE_PAYMENT_AMOUNT_PAID, this::handleUpdatePaymentRefund);

    public FailureOrderProcessor(OrderLogService orderLogService, OrderAdminService orderService) {
        super(orderLogService, orderService);
    }

    @Override
    protected void postProcessStage(Order order, OrderProcessStageDTO dto, Employee employee) {
        this.orderService.cancelOrder(order);
    }

    private void handleUpdatePaymentRefund(Order order, Employee employee, Object payload) {
        if (order.getPayment() == null) {
            throw new ValidationException("payload", "Payment not exists");
        }
        PaymentStatus status = Payments.getPaymentStatus(order.getPayment());

        if (status == PaymentStatus.PAID || status == PaymentStatus.PROCESSING) {
            throw new ValidationException("amountPaid", "Payment status is: " + status + " is not valid to refund");
        }

        if (payload instanceof PaymentPaidUpdateDTO dto) {
            this.orderService.refund(order, employee, dto);
        }
    }

    @Override
    protected Map<OrderStageAction, OrderActionConsummer> getActionHandlers() {
        return actionHandlers;
    }

    @Override
    public boolean supports(OrderStage stage) {
        return stage == OrderStage.FAILURE;
    }

    @Override
    protected void validateCompletion(Order order, OrderStage nextStage) {
        Payment payment = order.getPayment();
        if (payment == null) {
            return;
        }

        PaymentStatus status = Payments.getPaymentStatus(payment);

        if (status != PaymentStatus.PAID) {
            throw new ValidationException("payment", "Payment must refuned to customer before continue");
        }
    }

    @Override
    protected OrderStage getNextStage(OrderType type) {
        return OrderStage.CANCELED;
    }

}
