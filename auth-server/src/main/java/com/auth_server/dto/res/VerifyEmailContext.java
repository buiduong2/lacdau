package com.auth_server.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailContext extends BaseEmail {
    private String displayName;
    private String tokenUrl;
}
