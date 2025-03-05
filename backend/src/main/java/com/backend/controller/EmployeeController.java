package com.backend.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.req.EmployeeRegisterDTO;
import com.backend.dto.res.EmployeeDTO;
import com.backend.security.CustomJwtAuthenticationToken;
import com.backend.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping("/check-register")
    public boolean existsbyUserId(CustomJwtAuthenticationToken authenticationToken) {
        return service.existsByUserId(authenticationToken.getPerson().getUserId());
    }

    @GetMapping
    public EmployeeDTO getProfile(CustomJwtAuthenticationToken authenticationToken) {
        return service.getProfile(authenticationToken);
    }

    @PostMapping
    public void register(@Valid @RequestBody EmployeeRegisterDTO data,
            CustomJwtAuthenticationToken authenticationToken) {
        service.createProfile(data, authenticationToken.getPerson().getUserId());
    }

    @PutMapping
    public EmployeeDTO editProfile(@Valid @RequestBody EmployeeRegisterDTO data,
            CustomJwtAuthenticationToken authenticationToken) {
        return service.updateProfile(data, authenticationToken.getPerson().getUserId());
    }
}
