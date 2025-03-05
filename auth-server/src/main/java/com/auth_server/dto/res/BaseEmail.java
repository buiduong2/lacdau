package com.auth_server.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseEmail {
    private String to;
    private String from;
    private String subject;
    private String template;
    private long expirateMin;
}
