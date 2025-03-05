package com.backend.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailOrderThankYouContext extends BaseEmailContext {
    private String clientHomePage;
    private String customerName;
    private long orderId;
}
