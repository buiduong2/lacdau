package com.auth_server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.auth_server.dto.req.RegisterReq;
import com.auth_server.entity.SystemUser;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SystemUserMapper {

    @ToEntity
    @Mapping(target = "isSuperuser", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    SystemUser toEntity(RegisterReq dto);
}
