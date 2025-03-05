package com.auth_server.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.auth_server.service.OAuthUserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class FederatedIdentityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();

    private final OAuthUserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            UserDetails userDetails = null;
            if (authentication.getPrincipal() instanceof OidcUser) {
                userDetails = userService.updateOrRegisterThirdPartyUser((OAuth2AuthenticationToken) authentication,
                        (OidcUser) authentication.getPrincipal());
            } else if (authentication.getPrincipal() instanceof OAuth2User) {
                userDetails = userService
                        .updateOrRegisterThirdPartyUser((OAuth2AuthenticationToken) authentication,
                                (OAuth2User) authentication.getPrincipal());
            }
            if (userDetails != null) {
                Authentication appAuth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(appAuth);
            } else {
                throw new RuntimeException(
                        "FederatedIdentityAuthenticationSuccessHandler: we have a Principal  not implement yet");
            }
        }

        this.delegate.onAuthenticationSuccess(request, response, authentication);
    }

}
