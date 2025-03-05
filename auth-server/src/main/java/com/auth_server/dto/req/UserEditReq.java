package com.auth_server.dto.req;

import org.hibernate.validator.constraints.Length;

import com.auth_server.validation.IUniqueFields;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditReq implements IUniqueFields {
    @NotEmpty
    @Length(max = 100)
    private String displayName;

    @Email
    private String email;

}
