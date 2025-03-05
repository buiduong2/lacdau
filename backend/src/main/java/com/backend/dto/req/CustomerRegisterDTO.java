package com.backend.dto.req;

import java.time.LocalDate;

import com.backend.entities.enums.Gender;
import com.backend.validation.EnumValue;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRegisterDTO {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private AddressDTO address;

    @EnumValue(enumClass = Gender.class)
    @NotNull
    private String gender;

    @Past
    @NotNull
    private LocalDate dob;

    @NotEmpty
    private String phone;

    @Email
    @NotNull
    private String email;

    @Getter
    @Setter
    public static class AddressDTO {

        @NotBlank
        private long districtId;

        @NotBlank
        private String detail;
    }
}
