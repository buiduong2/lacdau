package com.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.res.ProvinceDTO;
import com.backend.dto.res.ProvinceListDTO;
import com.backend.service.ProvinceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/provinces")
public class ProvinceController {

    private final ProvinceService service;

    @GetMapping()
    public List<ProvinceListDTO> findAll() {
        return service.findAllListDTO();
    }

    @GetMapping("{id}")
    public ProvinceDTO findbyId(@PathVariable long id) {
        return service.findDTOById(id);
    }
}
