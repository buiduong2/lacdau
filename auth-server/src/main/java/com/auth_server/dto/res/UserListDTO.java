package com.auth_server.dto.res;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link com.auth_server.entity.User}
 */
@Getter
@Setter
public class UserListDTO {
    private long id;
    private String displayName;
    private String avatarUrl;
    private String username;
    private String email;
    private boolean emailVerified;
    private String userType;
    private String provider;
    private boolean isModerate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
