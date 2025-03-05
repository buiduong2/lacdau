package com.auth_server.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.auth_server.entity.User}
 */
@Getter
@Setter
public class UserPermissionDTO {
    private Long id;
    private String displayName;
    private String avatarUrl;
    private String email;
    private String provider;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private List<PermissionListDTO> permissions;

    
}
