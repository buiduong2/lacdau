package com.backend.dto.res;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.backend.entities.enums.Gender;
import com.backend.entities.enums.PersonStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeAdminDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private AddressDTO address;
    private PersonStatus status;
    private Gender gender;
    private LocalDate dob;
    private String phone;
    private String email;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    public static class AddressDTO {
        private Long id;
        private String province;
        private String district;
        private String detail;
    }
}
