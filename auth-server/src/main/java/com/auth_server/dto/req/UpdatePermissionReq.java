package com.auth_server.dto.req;

import java.util.Set;

import com.auth_server.entity.Permission;
import com.auth_server.validation.EnumValue;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePermissionReq {

    @NotNull
    @Valid
    private Set<@EnumValue(enumClass = Permission.class) String> permissions;
}
