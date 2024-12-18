package com.backend.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.req.BrandAdminUpdateDTO;
import com.backend.dto.res.BrandAdminBasicDTO;
import com.backend.dto.res.BrandAdminDTO;
import com.backend.service.BrandService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/brands")
@RequiredArgsConstructor
@Validated
public class AdminBrandController {

    private final BrandService service;

    @GetMapping
    public List<BrandAdminDTO> findAll() {
        return service.findAllAdminDtos();
    }

    @GetMapping("{id}")
    public BrandAdminBasicDTO findById(@PathVariable long id) {
        return service.findBasicById(id);
    }

    @PostMapping
    public BrandAdminBasicDTO create(@Valid @RequestBody BrandAdminUpdateDTO dto) {
        return service.create(dto);
    }

    @PutMapping("{id}")
    public BrandAdminBasicDTO update(@Valid @RequestBody BrandAdminUpdateDTO dto, @PathVariable long id) {
        return service.update(dto, id);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id) {
        service.deleteById(id);
    }

}
