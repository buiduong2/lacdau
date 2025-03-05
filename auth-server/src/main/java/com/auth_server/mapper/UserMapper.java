package com.auth_server.mapper;

import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import com.auth_server.dto.req.UserEditReq;
import com.auth_server.dto.res.UserListDTO;
import com.auth_server.dto.res.UserProfile;
import com.auth_server.entity.OAuthUser;
import com.auth_server.entity.SystemUser;
import com.auth_server.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserProfile toProfileDTO(User user);

    @Mapping(target = "userType", expression = "java( getUserType(user) )")
    @Mapping(target = "provider", expression = "java( getUserProvider(user) )")
    @Mapping(target = "username", expression = "java( getUsername(user) )")
    @Mapping(target = "moderate", ignore = true)
    UserListDTO toListDTO(User user);

    @ToEntity
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "isSuperuser", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    User updateEntity(@MappingTarget User user, UserEditReq req);

    default void updateEmailEntity(User user, String email) {
        if (email == null) {
            return;
        }
        if (user.getEmail().equals(email)) {
            return;
        } else {
            user.setEmail(email);
            user.setEmailVerified(true);
        }
    }

    default UserListDTO updateModerateListDTO(UserListDTO dto, Map<Long, Boolean> idAndIsModerate) {
        if (idAndIsModerate.containsKey(dto.getId())) {
            dto.setModerate(idAndIsModerate.get(dto.getId()));
            return dto;
        }
        throw new IllegalArgumentException("map must contains user Id ");
    }

    default String getUserProvider(User user) {
        if (user instanceof OAuthUser oAuthUser) {
            return oAuthUser.getProvider().toString();
        }
        return null;
    }

    default String getUserType(User user) {

        if (user instanceof SystemUser) {
            return "SYS";
        }
        if (user instanceof OAuthUser) {
            return "OAUTH";
        }

        return null;
    }

    default String getUsername(User user) {

        if (user instanceof SystemUser systemUser) {
            return systemUser.getUsername();
        }
        if (user instanceof OAuthUser oAuthUser) {
            return oAuthUser.getUsername();
        }

        return null;
    }

}
