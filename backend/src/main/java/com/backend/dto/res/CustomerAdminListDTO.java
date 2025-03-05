package com.backend.dto.res;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.backend.entities.Customer}
 */
@Getter
@Setter
@AllArgsConstructor
public class CustomerAdminListDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private long totalOrder;
    private double totalPaymentAmountPaid;
    private String addressProvince;
    private LocalDateTime lastOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
