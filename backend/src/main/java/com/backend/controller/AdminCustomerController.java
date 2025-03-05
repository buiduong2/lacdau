package com.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.req.FilterCustomerAdmin;
import com.backend.dto.res.CustomerAdminDTO;
import com.backend.dto.res.CustomerAdminListDTO;
import com.backend.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/customers")
@RequiredArgsConstructor
@Validated
public class AdminCustomerController {

    private final CustomerService service;

    @GetMapping
    public Page<CustomerAdminListDTO> findAll(FilterCustomerAdmin filter, Pageable pageable) {
        return service.findAdminAll(filter, pageable);
    }

    @GetMapping("{id}")
    public CustomerAdminDTO findById(@PathVariable long id) {
        return service.findAdminById(id);
    }
}
