package com.backend.service;

import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dto.req.FilterOrderAdmin;
import com.backend.dto.req.OrderProcessAddressUpdateDTO.OrderAddressUpdateDTO;
import com.backend.dto.req.OrderProcessOrderItemUpdateDTO.OrderItemUpdateDTO;
import com.backend.dto.req.OrderProcessPaymentPaidUpdateDTO.PaymentPaidUpdateDTO;
import com.backend.dto.req.OrderProcessPaymentUpdateDTO.PaymentUpdateDTO;
import com.backend.dto.req.OrderProcessShipmentUpdateDTO.ShipmentDTO;
import com.backend.dto.res.OrderAdminDTO;
import com.backend.dto.res.OrderAdminListDTO;
import com.backend.dto.res.OrderLogAdminDTO;
import com.backend.entities.Employee;
import com.backend.entities.Order;
import com.backend.entities.OrderItem;
import com.backend.entities.Payment;
import com.backend.entities.Shipment;
import com.backend.entities.enums.OrderItemStatus;
import com.backend.exception.ResourceNotFoundException;
import com.backend.mapper.OrderAddressMapper;
import com.backend.mapper.OrderItemMapper;
import com.backend.mapper.OrderMapper;
import com.backend.mapper.PaymentMapper;
import com.backend.mapper.ShipmentMapper;
import com.backend.repository.OrderItemRepository;
import com.backend.repository.OrderRepository;
import com.backend.utils.OrderSpecs;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderAdminService {

    private final OrderMapper mapper;

    private final OrderAddressMapper addressMapper;

    private final OrderItemMapper itemMapper;

    private final OrderRepository repository;

    private final OrderItemRepository itemRepository;

    private final PaymentMapper paymentMapper;

    private final ShipmentMapper shipmentMapper;

    private final OrderLogService orderLogService;
    private final OrderSpecs orderSpecs;

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateOrerAddress(Order order, Employee employee, OrderAddressUpdateDTO dto) {
        addressMapper.updateEntity(order.getOrderAddress(), dto);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updateOrderItems(Order order, Employee employee, OrderItemUpdateDTO dto) {
        List<OrderItem> oldItems = itemRepository.findByOrderId(order.getId());
        LinkedHashSet<OrderItem> updatedItems = itemMapper.updateEntities(oldItems, dto.getOrderItems());

        for (OrderItem orderItem : order.getOrderItems()) {
            if (!updatedItems.contains(orderItem)) {
                order.removeOrderItem(orderItem);
            }
        }

        for (OrderItem orderItem : updatedItems) {
            order.addOrderItem(orderItem);
        }

        Payment payment = order.getPayment();
        paymentMapper.updateSubTotalPrice(payment, order.getOrderItems());
        paymentMapper.updateTotalItem(payment, order.getOrderItems());

        repository.save(order);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updatePayment(Order order, Employee employee, PaymentUpdateDTO dto) {
        Payment payment = order.getPayment();
        paymentMapper.updateShippingPrice(payment, dto);
        paymentMapper.updateTotalPrice(payment);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void updatePaymentPaid(Order order, Employee employee, PaymentPaidUpdateDTO dto) {
        Payment payment = order.getPayment();
        paymentMapper.updatePaid(payment, dto);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void createOrUpdate(Order order, Employee employee, ShipmentDTO dto) {
        Shipment shipment = order.getShipment();
        if (shipment == null) {
            order.setShipment(shipmentMapper.toEntity(dto));
        } else {
            shipmentMapper.updateEntity(shipment, dto);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void cancelOrder(Order order) {
        List<OrderItem> orderItems = itemRepository.findByOrderId(order.getId());
        for (OrderItem orderItem : orderItems) {
            itemMapper.releaseProductQuantity(orderItem);
            orderItem.setStatus(OrderItemStatus.NOT_SUPPLIED);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void refund(Order order, Employee employee, PaymentPaidUpdateDTO dto) {
        Payment payment = order.getPayment();
        payment.setAmountPaid(dto.getAmountPaid());
    }

    public Page<OrderAdminListDTO> findAll(FilterOrderAdmin filter, Pageable pageable) {
        return repository.findAll(orderSpecs.byAdminFilter(filter), pageable)
                .map(mapper::toAdminListDTO);
    }

    public OrderAdminDTO findById(Long id) {
        return repository.findAdminById(id).map(mapper::toAdminDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public List<OrderLogAdminDTO> findOrderLogByOrderId(long id) {
        return orderLogService.findAdminByOrderId(id);
    }

}
