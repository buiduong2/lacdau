package com.backend.dto.req;

import java.util.LinkedHashSet;

import com.backend.dto.req.OrderCreateDTO.OrderItemDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProcessOrderItemUpdateDTO extends OrderProcessActionDTO {

    @Valid
    private OrderItemUpdateDTO payload;

    @Getter
    @Setter
    public class OrderItemUpdateDTO {

        @Valid
        @NotEmpty
        private LinkedHashSet<OrderItemDTO> orderItems;
    }

}
