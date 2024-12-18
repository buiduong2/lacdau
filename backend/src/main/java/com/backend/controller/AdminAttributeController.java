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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.req.AttributeAdminCreateDTO;
import com.backend.dto.req.AttributeAdminUpdateDTO;
import com.backend.dto.res.AttributeAdminBasicDTO;
import com.backend.dto.res.AttributeAdminDTO;
import com.backend.service.AttributeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/attributes")
@RequiredArgsConstructor
@Validated
public class AdminAttributeController {

    private final AttributeService attributeService;

    @GetMapping
    public List<AttributeAdminDTO> findAll() {
        return attributeService.findAllAdmin();
    }

    @GetMapping("{id}")
    public AttributeAdminBasicDTO findById(@PathVariable long id) {
        return attributeService.findAdminBasicDTOById(id);
    }

    @PostMapping
    public AttributeAdminBasicDTO create(@RequestBody @Valid AttributeAdminCreateDTO dto) {
        return attributeService.create(dto);
    }

    @PutMapping("{id}")
    public AttributeAdminBasicDTO edit(@RequestBody @Valid AttributeAdminUpdateDTO dto, @PathVariable long id) {
        return attributeService.edit(dto, id);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id) {
        attributeService.deleteById(id);
    }

    @DeleteMapping
    public void deleteByIdIn(@RequestParam List<Long> id) {
        attributeService.deleteByIdIn(id);
    }
}
