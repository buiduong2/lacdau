package com.auth_server.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

import com.auth_server.dto.res.UserManagerListDTO;
import com.auth_server.dto.res.UserPermissionDTO;
import com.auth_server.entity.OAuthUser;
import com.auth_server.entity.Permission;
import com.auth_server.entity.SystemUser;
import com.auth_server.entity.User;
import com.auth_server.entity.UserPermission;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserPermissionMapper {

    List<UserManagerListDTO> toManagerDTOs(List<User> users);

    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "provider", expression = "java( getProvider(u) )")
    UserPermissionDTO toUserDTO(User u);

    default String getProvider(User u) {
        if (u instanceof SystemUser) {
            return "SYS";
        }
        if (u instanceof OAuthUser oauth) {
            return oauth.getProvider().toString();
        }
        return null;
    }

    default Set<UserPermission> toEntitys(Set<String> permissons, long userId) {
        return permissons.stream().map(p -> this.toEntity(p, userId))
                .collect(Collectors.toSet());
    }

    default UserPermission toEntity(String permission, long userId) {
        UserPermission userPermission = new UserPermission();
        User user = new User();
        user.setId(userId);

        userPermission.setPermission(toEnum(permission));
        userPermission.setUser(user);
        return userPermission;
    }

    @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = MappingConstants.ANY_REMAINING)
    Permission toEnum(String str);

}
