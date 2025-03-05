package com.backend.utils;

import java.util.Collection;

import com.backend.entities.OrderItem;
import com.backend.entities.enums.PaymentStatus;

public class Payments {

    public static interface PaymentStatusable {
        Double getShippingPrice();

        Double getTotalPrice();

        Double getAmountPaid();

        default PaymentStatus getStatus() {
            return Payments.getPaymentStatus(this);
        }
    }

    public static PaymentStatus getPaymentStatus(PaymentStatusable payment) {
        if (payment.getShippingPrice() == null || payment.getTotalPrice() == null) {
            return PaymentStatus.PROCESSING;
        }

        if (payment.getAmountPaid() == null) {
            return PaymentStatus.PENDING;
        }

        if (payment.getAmountPaid() >= payment.getTotalPrice()) {
            return PaymentStatus.PAID;
        }

        if (payment.getAmountPaid() <= 0) {
            return PaymentStatus.REFUNDED;
        }

        return PaymentStatus.PENDING;
    }

    public static double calculateSubTotalPrice(Collection<OrderItem> orderItems) {
        double res = 0;

        for (OrderItem orderItem : orderItems) {
            Integer salePrice = orderItem.getSalePrice();
            int unitPrice = orderItem.getUnitPrice();
            int quantity = orderItem.getSuppliedQuantity();
            double price = salePrice != null ? salePrice : unitPrice;
            res += quantity * price;
        }

        return res;
    }
}
