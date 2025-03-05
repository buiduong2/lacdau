package com.auth_server.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import com.auth_server.entity.Permission;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.auth_server.entity.User}
 */
@Getter
@Setter
public class UserManagerListDTO {
    private Long id;
    private String displayName;
    private String avatarUrl;
    private String email;
    private List<PermissionDTO> permissions;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    /**
     * DTO for {@link com.auth_server.entity.UserPermission}
     */
    @Getter
    @Setter
    public static class PermissionDTO {
        private Long id;
        private Permission permission;
        private LocalDateTime revokedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
