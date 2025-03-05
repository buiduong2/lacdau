package com.auth_server.dto.req;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilter {
    private String username;
    private String displayName;

    @Size(min = 1, max = 2)
    @Valid
    private List<@Pattern(regexp = "^(user|admin)$") String> morderates;

    @Size(min = 1, max = 3)
    @Valid
    private List<@Pattern(regexp = "^(SYS|OAUTH-(GOOGLE|GITHUB))$") String> providers;

    @Override
    public String toString() {
        return "UserFilter [username=" + username + ", displayName=" + displayName + ", morderate=" + morderates
                + ", provider=" + providers + "]";
    }

}
