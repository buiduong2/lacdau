package com.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.req.FilterOrderAdmin;
import com.backend.dto.res.OrderAdminDTO;
import com.backend.dto.res.OrderAdminListDTO;
import com.backend.dto.res.OrderLogAdminDTO;
import com.backend.service.OrderAdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderAdminService service;

    @GetMapping
    public Page<OrderAdminListDTO> findAll(@Valid FilterOrderAdmin filter, Pageable pageable) {
        return service.findAll(filter, pageable);
    }

    @GetMapping("{id}")
    public OrderAdminDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("{id}/log")
    public List<OrderLogAdminDTO> findLogByOrderId(@PathVariable long id) {
        return service.findOrderLogByOrderId(id);
    }
}
