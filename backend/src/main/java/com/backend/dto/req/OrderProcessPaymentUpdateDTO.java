package com.backend.dto.req;

import com.backend.entities.enums.PaymentMethod;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProcessPaymentUpdateDTO extends OrderProcessActionDTO {

    @Valid
    private PaymentUpdateDTO payload;

    @Override
    public Object getPayload() {
        return this.payload;
    }

    @Getter
    @Setter
    public static class PaymentUpdateDTO {

        @Enumerated(EnumType.STRING)
        @NotNull
        private PaymentMethod method;

        @PositiveOrZero
        private double shippingPrice;
    }

}
