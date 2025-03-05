package com.auth_server.dto.req;

import org.hibernate.validator.constraints.Length;

import com.auth_server.entity.TokenType;
import com.auth_server.validation.PasswordMatches;
import com.auth_server.validation.TokenValid;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class ResetPasswordReq implements PasswordConfirmable {

    @TokenValid(type = TokenType.FORGOT_PASSWORD)
    private String token;

    @NotEmpty
    @Length(min = 8, max = 100)
    private String password;

    @NotEmpty
    @Length(min = 8, max = 100)
    private String confirmPassword;
}
