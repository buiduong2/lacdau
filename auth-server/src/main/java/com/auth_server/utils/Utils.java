package com.auth_server.utils;

import org.springframework.security.core.Authentication;

import com.auth_server.security.CustomJwtAuthenticationToken;
import com.auth_server.security.CustomUserDetails;

public class Utils {
    public static Long getAuthId(Authentication auth) {
        if (auth instanceof CustomJwtAuthenticationToken token) {
            return token.getUserId();
        } else if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getId();
        } else {
            throw new RuntimeException("ERror sever");
        }
    }
}
