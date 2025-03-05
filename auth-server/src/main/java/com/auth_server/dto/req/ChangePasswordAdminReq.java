package com.auth_server.dto.req;

import org.hibernate.validator.constraints.Length;

import com.auth_server.validation.PasswordMatches;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class ChangePasswordAdminReq implements PasswordConfirmable {

    @NotEmpty
    @Length(min = 8, max = 100)
    private String password;

    @NotEmpty
    @Length(min = 8, max = 100)
    private String confirmPassword;
}
