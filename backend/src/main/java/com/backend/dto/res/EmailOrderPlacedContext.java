package com.backend.dto.res;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailOrderPlacedContext extends BaseEmailContext {
    private String customerName;
    private long orderId;
    private String orderDetailUrl;
    private LocalDateTime orderCreatedAt;
}
