package com.backend.dto.res;

import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProcessDTO {
    private long orderId;
    private OrderType type;
    private OrderStage stage;
    private Long customerId;
    private Long paymentId;
    private Long shipmentId;
    private Long orderAddressId;
}
