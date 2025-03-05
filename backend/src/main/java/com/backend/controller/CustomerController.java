package com.backend.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.req.CustomerRegisterDTO;
import com.backend.dto.res.CustomerDTO;
import com.backend.security.CustomJwtAuthenticationToken;
import com.backend.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService service;

    @GetMapping("/check-register")
    public boolean existsbyUserId(CustomJwtAuthenticationToken authenticationToken) {
        return service.existsByUserId(authenticationToken.getPerson().getUserId());
    }

    @GetMapping
    public CustomerDTO getProfile(CustomJwtAuthenticationToken authenticationToken) {
        return service.getProfile(authenticationToken);
    }

    @PostMapping
    public CustomerDTO register(@Valid @RequestBody CustomerRegisterDTO data,
            CustomJwtAuthenticationToken authenticationToken) {
        return service.createProfile(data, authenticationToken.getPerson().getUserId());
    }

    @PutMapping
    public CustomerDTO editProfile(@Valid @RequestBody CustomerRegisterDTO data,
            CustomJwtAuthenticationToken authenticationToken) {
        return service.updateProfile(data, authenticationToken.getPerson().getUserId());
    }
}
