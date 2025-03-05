package com.backend.mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.backend.dto.req.OrderProcessPaymentPaidUpdateDTO.PaymentPaidUpdateDTO;
import com.backend.dto.req.OrderProcessPaymentUpdateDTO.PaymentUpdateDTO;
import com.backend.entities.OrderItem;
import com.backend.entities.Payment;
import com.backend.entities.enums.PaymentMethod;
import com.backend.utils.Payments;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    default Payment toEntity(LinkedHashSet<OrderItem> orderItems, String method) {
        Payment payment = new Payment();

        payment.setMethod(PaymentMethod.valueOf(method));
        payment.setTotalPrice(null);
        payment.setShippingPrice(null);
        payment.setAmountPaid(null);
        
        updateTotalItem(payment, orderItems);   
        updateSubTotalPrice(payment, orderItems);

        return payment;
    }

    default void updateTotalItem(Payment payment, Collection<OrderItem> orderItems) {
        payment.setTotalItem(
                orderItems.stream().map(OrderItem::getSuppliedQuantity).reduce(0, (sum, quantity) -> sum + quantity));
    }

    default void updateSubTotalPrice(Payment payment, Collection<OrderItem> orderItems) {
        payment.setSubTotalPrice(Payments.calculateSubTotalPrice(orderItems));
    }

    default void updateTotalPrice(Payment payment) {
        payment.setTotalPrice(payment.getSubTotalPrice() + payment.getShippingPrice());
    }

    default void updateShippingPrice(Payment entity, PaymentUpdateDTO dto) {
        entity.setMethod(dto.getMethod());
        entity.setShippingPrice(dto.getShippingPrice());
    }

    default void updatePaid(Payment entity, PaymentPaidUpdateDTO dto) {
        entity.setAmountPaid(dto.getAmountPaid());
        entity.setPaymentDate(LocalDateTime.now());
    }

}
