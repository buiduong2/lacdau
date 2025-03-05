package com.backend.dto.req;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProcessPaymentPaidUpdateDTO extends OrderProcessActionDTO {

    @Valid
    private PaymentPaidUpdateDTO payload;

    @Override
    public Object getPayload() {
        return this.payload;
    }

    @Getter
    @Setter
    public static class PaymentPaidUpdateDTO {

        private double amountPaid;
    }

}
