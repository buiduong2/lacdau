package com.auth_server.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {
    private Long id;

    private String displayName;

    private String email;

    private String avatarUrl;

}
