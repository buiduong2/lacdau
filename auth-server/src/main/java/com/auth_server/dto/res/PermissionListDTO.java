package com.auth_server.dto.res;

import java.time.LocalDateTime;

import com.auth_server.entity.Permission;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link com.auth_server.entity.UserPermission}
 */
@Getter
@Setter
@NoArgsConstructor
public class PermissionListDTO {

    public PermissionListDTO(Long id,Permission permission, LocalDateTime revokedAt, LocalDateTime createdAt, LocalDateTime updatedAt,
            Long updatedById, String updatedByDisplayName,
            Long createdById, String createdByDisplayName) {
        this.id = id;
        this.permission = permission;
        this.revokedAt = revokedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        if (updatedById != null) {
            this.updatedBy = new UserAuditDTO();
            this.updatedBy.setId(updatedById);
            this.updatedBy.setDisplayName(updatedByDisplayName);

        }
        if (createdById != null) {
            this.createdBy = new UserAuditDTO();
            this.createdBy.setId(createdById);
            this.createdBy.setDisplayName(createdByDisplayName);
        }
    }

    private Long id;
    private Permission permission;
    private LocalDateTime revokedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserAuditDTO updatedBy;
    private UserAuditDTO createdBy;

    /**
     * DTO for {@link com.auth_server.entity.User}
     */
    @Getter
    @Setter
    public static class UserAuditDTO {
        private Long id;
        private String displayName;
    }
}
