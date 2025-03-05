package com.backend.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.res.EmployeeAdminDTO;
import com.backend.dto.res.EmployeeAdminListDTO;
import com.backend.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/employees")
@RequiredArgsConstructor
@Validated
public class AdminEmployeeController {
    private final EmployeeService service;

    @GetMapping
    public List<EmployeeAdminListDTO> findAll() {
        return service.findAdminAll();
    }

    @GetMapping("{id}")
    public EmployeeAdminDTO findById(@PathVariable long id) {
        return service.findAdminById(id);
    }


}
