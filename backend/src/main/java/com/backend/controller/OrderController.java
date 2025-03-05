package com.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.req.FilterOrderCustomer;
import com.backend.dto.req.OrderCancellationDeleteDTO;
import com.backend.dto.req.OrderCreateDTO;
import com.backend.dto.res.OrderDTO;
import com.backend.dto.res.OrderListDTO;
import com.backend.dto.res.OrderLogDTO;
import com.backend.entities.Customer;
import com.backend.security.CustomJwtAuthenticationToken;
import com.backend.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService service;

    @GetMapping
    public Page<OrderListDTO> findAll(FilterOrderCustomer filter, CustomJwtAuthenticationToken token,
            Pageable pageable) {
        return service.findAll(filter, (Customer) token.getPerson(), pageable);
    }

    @GetMapping("{id}")
    public OrderDTO findById(@PathVariable long id, CustomJwtAuthenticationToken token) {
        return service.findById(id, (Customer) token.getPerson());
    }

    @GetMapping("{id}/log")
    public List<OrderLogDTO> findOrderLogByOrderId(@PathVariable long id, CustomJwtAuthenticationToken token) {
        return service.findOrderLogByOrderId(id, (Customer) token.getPerson());
    }

    @PostMapping
    public OrderDTO createOrder(@Valid @RequestBody OrderCreateDTO dto, CustomJwtAuthenticationToken token) {
        return service.create(dto, (Customer) token.getPerson());
    }

    @DeleteMapping("{id}")
    public void deleteOrder(@PathVariable long id,
            @Valid @RequestBody OrderCancellationDeleteDTO dto,
            CustomJwtAuthenticationToken token) {
        service.cancel(id, dto, (Customer) token.getPerson());
    }

}
