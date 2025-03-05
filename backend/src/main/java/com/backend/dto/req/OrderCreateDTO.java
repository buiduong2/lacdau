package com.backend.dto.req;

import java.util.LinkedHashSet;

import com.backend.entities.enums.PaymentMethod;
import com.backend.validation.EnumValue;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateDTO {

    @Valid
    @NotEmpty
    private LinkedHashSet<OrderItemDTO> orderItems;

    @NotNull
    @Valid
    private AddressDTO address;

    @NotNull
    @EnumValue(enumClass = PaymentMethod.class)
    private String paymentMethod;

    @Getter
    @Setter
    public static class OrderItemDTO {
        @NotEmpty
        private String productId;

        @Positive
        private int quantity;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((productId == null) ? 0 : productId.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            OrderItemDTO other = (OrderItemDTO) obj;
            if (productId == null) {
                if (other.productId != null)
                    return false;
            } else if (!productId.equals(other.productId))
                return false;
            return true;
        }

    }

    @Setter
    @Getter
    public static class AddressDTO {
        @NotEmpty
        private String fullName;

        @Email
        @NotEmpty
        private String email;

        @NotEmpty
        private String phone;

        private long districtId;

        @NotEmpty
        private String detail;

        private String message;
    }

}
