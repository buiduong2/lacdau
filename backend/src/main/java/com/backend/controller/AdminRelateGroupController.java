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

import com.backend.dto.req.RelateGroupAdminUpdateDTO;
import com.backend.dto.res.RelateGroupAdminBasicDTO;
import com.backend.dto.res.RelateGroupAdminDTO;
import com.backend.dto.res.RelateGroupInfoDTO;
import com.backend.service.RelateGroupService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/relate-groups")
@RequiredArgsConstructor
@Validated
public class AdminRelateGroupController {

    private final RelateGroupService service;

    @GetMapping
    public List<RelateGroupAdminDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}/relate-infos")
    public RelateGroupInfoDTO findRelateInfosById(@PathVariable Long id) {
        return service.findRelateInfosById(id);
    }

    @GetMapping("{id}")
    public RelateGroupAdminBasicDTO findById(@PathVariable long id) {
        return service.findBasicById(id);
    }

    @PostMapping
    public RelateGroupAdminBasicDTO create(@Valid @RequestBody RelateGroupAdminUpdateDTO dto) {
        return service.create(dto);
    }

    @PutMapping("{id}")
    public RelateGroupAdminBasicDTO edit(@Valid @RequestBody RelateGroupAdminUpdateDTO dto, @PathVariable long id) {
        return service.editById(dto, id);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id) {
        service.deleteById(id);
    }
}
