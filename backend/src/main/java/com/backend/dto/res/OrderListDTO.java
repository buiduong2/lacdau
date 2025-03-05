package com.backend.dto.res;

import java.time.LocalDateTime;

import com.backend.entities.Payment;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderType;
import com.backend.entities.enums.PaymentMethod;
import com.backend.entities.enums.PaymentStatus;
import com.backend.utils.Payments.PaymentStatusable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderListDTO {

    private long id;
    private OrderType type;
    private OrderStage stage;
    private Payment payment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    public static class PaymentDTO implements PaymentStatusable {
        private int totalItem;
        private PaymentMethod method;
        private PaymentStatus status;
        private Double amountPaid;
        private Double shippingPrice;
        private double subTotalPrice;
        private Double totalPrice;
    }

}
