package com.backend.dto.req;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Shipment}
 */
@Getter
@Setter
public class OrderProcessShipmentUpdateDTO extends OrderProcessActionDTO {

    @Valid
    private ShipmentDTO payload;

    @Override
    public Object getPayload() {
        return this.payload;
    }

    @Getter
    @Setter
    public static class ShipmentDTO {

        @NotNull
        @Future
        private LocalDateTime estimatedDelivery;

        @PositiveOrZero
        private double shippingPrice;

        @NotEmpty
        private String trackingDetail;

        @NotEmpty
        private String carrier;
    }

}
