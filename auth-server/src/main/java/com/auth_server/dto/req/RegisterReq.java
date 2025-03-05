package com.auth_server.dto.req;

import org.hibernate.validator.constraints.Length;

import com.auth_server.validation.PasswordMatches;
import com.auth_server.validation.SystemUserUnique;
import com.auth_server.validation.SystemUserUnique.Column;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class RegisterReq implements PasswordConfirmable {
    @NotEmpty
    @Length(max = 100)
    private String displayName;

    @NotEmpty
    @Email
    @SystemUserUnique(column = Column.EMAIL)
    private String email;

    @NotEmpty
    @Length(min = 8, max = 100)
    @SystemUserUnique(column = Column.USERNAME)
    private String username;

    @NotEmpty
    @Length(min = 8, max = 100)
    private String password;

    @NotEmpty
    @Length(min = 8, max = 100)
    private String confirmPassword;
}
