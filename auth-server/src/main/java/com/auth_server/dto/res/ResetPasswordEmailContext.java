package com.auth_server.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordEmailContext extends BaseEmail {
    private String email;
    private String tokenUrl;
    private String displayName;
    private String username;
}
