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

import com.backend.dto.req.AttributeValueAdminUpdateDTO;
import com.backend.dto.res.AttributeValueAdminBasic;
import com.backend.dto.res.AttributeValueAdminDTO;
import com.backend.service.AttributeValueService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/attribute-values")
@RequiredArgsConstructor
@Validated
public class AdminAttributeValueController {

    private final AttributeValueService service;

    @GetMapping
    public List<AttributeValueAdminDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public AttributeValueAdminBasic findById(@PathVariable long id) {
        return service.findAdminBasicDTOById(id);
    }

    @PostMapping
    public AttributeValueAdminBasic create(@Valid @RequestBody AttributeValueAdminUpdateDTO dto) {
        return service.create(dto);
    }

    @PutMapping("{id}")
    public AttributeValueAdminBasic edit(@Valid @RequestBody AttributeValueAdminUpdateDTO dto, @PathVariable long id) {
        return service.edit(dto, id);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id) {
        service.deleteById(id);
    }

}
