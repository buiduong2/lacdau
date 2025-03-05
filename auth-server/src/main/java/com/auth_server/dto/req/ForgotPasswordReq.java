package com.auth_server.dto.req;

import com.auth_server.validation.EmailExistedAndVerified;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordReq {

    @NotEmpty
    @Email
    @EmailExistedAndVerified
    private String email;
}
