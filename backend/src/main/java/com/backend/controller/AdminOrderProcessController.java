package com.backend.controller;

import static com.backend.entities.enums.OrderStageAction.CREATE_SHIPMENT;
import static com.backend.entities.enums.OrderStageAction.UPDATE_PAYMENT;
import static com.backend.entities.enums.OrderStageAction.UPDATE_PAYMENT_AMOUNT_PAID;
import static com.backend.entities.enums.OrderStageAction.UPDATE_PAYMENT_REFUND;
import static com.backend.entities.enums.OrderStageAction.UPDATE_SHIPMENT;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dto.req.OrderProcessAddressUpdateDTO;
import com.backend.dto.req.OrderProcessOrderItemUpdateDTO;
import com.backend.dto.req.OrderProcessPaymentPaidUpdateDTO;
import com.backend.dto.req.OrderProcessPaymentUpdateDTO;
import com.backend.dto.req.OrderProcessShipmentUpdateDTO;
import com.backend.dto.req.OrderProcessStageDTO;
import com.backend.dto.res.OrderProcessDTO;
import com.backend.entities.enums.OrderStageAction;
import com.backend.security.CustomJwtAuthenticationToken;
import com.backend.service.OrderProcessService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/orders/process")
@Validated
@RequiredArgsConstructor
public class AdminOrderProcessController {

    private final OrderProcessService orderService;

    @PostMapping("{id}")
    public OrderProcessDTO processOrder(
            @PathVariable long id,
            @Valid @RequestBody OrderProcessStageDTO dto,
            CustomJwtAuthenticationToken token) {

        return orderService.handleProcess(id, dto, token.getPerson().getId());
    }

    @PutMapping("{id}/order-item")
    public OrderProcessDTO editOrder(@PathVariable long id,
            @Valid @RequestBody OrderProcessOrderItemUpdateDTO dto,
            CustomJwtAuthenticationToken token) {
        dto.setAction(OrderStageAction.UPDATE_ORDER_ITEM);

        return orderService.handleAction(id, dto, token.getPerson().getId());
    }

    @PutMapping("{id}/order-address")
    public OrderProcessDTO editOrderAddress(
            @PathVariable long id,
            @Valid @RequestBody OrderProcessAddressUpdateDTO dto,
            CustomJwtAuthenticationToken token) {
        dto.setAction(OrderStageAction.UPDATE_ADDRESS);

        return orderService.handleAction(id, dto, token.getPerson().getId());
    }

    @PutMapping("{id}/payment")
    public OrderProcessDTO editPayment(
            @PathVariable long id,
            @Valid @RequestBody OrderProcessPaymentUpdateDTO dto,
            CustomJwtAuthenticationToken token) {
        dto.setAction(UPDATE_PAYMENT);
        return orderService.handleAction(id, dto, token.getPerson().getId());
    }

    @PutMapping("{id}/payment/amount-paid")
    public OrderProcessDTO editPaymentAmountPaid(
            @PathVariable long id,
            @Valid @RequestBody OrderProcessPaymentPaidUpdateDTO dto,
            CustomJwtAuthenticationToken token) {

        dto.setAction(UPDATE_PAYMENT_AMOUNT_PAID);
        return orderService.handleAction(id, dto, token.getPerson().getId());
    }

    @PutMapping("{id}/payment/refund")
    public OrderProcessDTO editPaymentRefund(
            @PathVariable long id,
            @Valid @RequestBody OrderProcessPaymentPaidUpdateDTO dto,
            CustomJwtAuthenticationToken token) {
        dto.setAction(UPDATE_PAYMENT_REFUND);
        return orderService.handleAction(id, dto, token.getPerson().getId());
    }

    @PostMapping("{id}/shipment")
    public OrderProcessDTO createShipment(@PathVariable long id,
            @Valid @RequestBody OrderProcessShipmentUpdateDTO dto,
            CustomJwtAuthenticationToken token) {
        dto.setAction(CREATE_SHIPMENT);
        return orderService.handleAction(id, dto, token.getPerson().getId());
    }

    @PutMapping("{id}/shipment")
    public OrderProcessDTO editShipment(@PathVariable long id,
            @Valid @RequestBody OrderProcessShipmentUpdateDTO dto,
            CustomJwtAuthenticationToken token) {
        dto.setAction(UPDATE_SHIPMENT);
        return orderService.handleAction(id, dto, token.getPerson().getId());
    }

}
