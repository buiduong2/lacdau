package com.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entities.ProductCodeGenerator;
import com.backend.service.ProductCodeGeneratorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/product-codes")
@RequiredArgsConstructor
public class AdminProductCodeController {
    private final ProductCodeGeneratorService service;

    @GetMapping
    public List<ProductCodeGenerator> findAll() {
        return service.findAll();
    }
}
