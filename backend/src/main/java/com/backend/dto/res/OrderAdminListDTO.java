package com.backend.dto.res;

import java.time.LocalDateTime;

import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderType;
import com.backend.utils.Payments.PaymentStatusable;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Order}
 */
@Getter
@Setter
public class OrderAdminListDTO {
    private long id;
    private OrderStage stage;
    private OrderType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PaymentDTO payment;
    private CustomerDTO customer;

    
    /**
     * DTO for {@link com.backend.entities.Customer}
     */
    @Getter
    @Setter
    public static class CustomerDTO {
        private Long id;
        private String firstName;
        private String lastName;
    }

    /**
     * DTO for {@link com.backend.entities.Payment}
     */
    @Getter
    @Setter
    public static class PaymentDTO implements PaymentStatusable{
        private int totalItem;
        private Double amountPaid;
        private double subTotalPrice;
        private Double shippingPrice;
        private Double totalPrice;
    }
}
