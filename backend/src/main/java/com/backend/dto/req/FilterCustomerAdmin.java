package com.backend.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterCustomerAdmin {
    private String id;
    private String phone;
    private String name;
    private String email;
    private String addressProvince;
}
