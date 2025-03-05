package com.backend.dto.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProcessAddressUpdateDTO extends OrderProcessActionDTO {

    @Override
    public Object getPayload() {
        return this.payload;
    }

    @Valid
    private OrderAddressUpdateDTO payload;

    @Getter
    @Setter
    public static class OrderAddressUpdateDTO {
        @NotEmpty
        private String fullName;

        @NotEmpty
        private String phone;

        private long districtId;

        @NotEmpty
        private String detail;

        private String message;
    }

}
