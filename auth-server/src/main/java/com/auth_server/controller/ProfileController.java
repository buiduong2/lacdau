package com.auth_server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth_server.dto.res.UserProfile;
import com.auth_server.service.UserService;
import com.auth_server.utils.Utils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService service;

    @GetMapping
    public UserProfile getProfile(Authentication auth) {
        Long userId = Utils.getAuthId(auth);
        return service.getProfileById(userId);
    }

}
