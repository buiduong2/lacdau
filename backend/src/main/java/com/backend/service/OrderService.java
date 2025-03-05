package com.backend.service;

import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dto.req.FilterOrderCustomer;
import com.backend.dto.req.OrderCancellationDeleteDTO;
import com.backend.dto.req.OrderCreateDTO;
import com.backend.dto.res.OrderDTO;
import com.backend.dto.res.OrderListDTO;
import com.backend.dto.res.OrderLogDTO;
import com.backend.entities.Customer;
import com.backend.entities.Order;
import com.backend.entities.OrderAddress;
import com.backend.entities.OrderItem;
import com.backend.entities.Payment;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderType;
import com.backend.exception.ResourceNotFoundException;
import com.backend.exception.ValidationException;
import com.backend.mapper.OrderAddressMapper;
import com.backend.mapper.OrderItemMapper;
import com.backend.mapper.OrderMapper;
import com.backend.mapper.PaymentMapper;
import com.backend.repository.CustomerRepository;
import com.backend.repository.OrderRepository;
import com.backend.utils.OrderSpecs;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    private final CustomerRepository customerRepository;

    private final OrderMapper mapper;

    private final OrderItemMapper itemMapper;

    private final OrderAddressMapper addressMapper;

    private final PaymentMapper paymentMapper;

    private final OrderLogService orderLogService;

    private final OrderSpecs specs;

    public Page<OrderListDTO> findAll(FilterOrderCustomer filter, Customer customer, Pageable pageable) {
        return repository.findAll(specs.byCustomerFilter(filter, customer.getId()), pageable)
                .map(mapper::toListDTO);
    }

    public OrderDTO findById(long id, Customer customer) {
        return repository.findDetailByIdAndCustomerId(id, customer.getId())
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order not exists"));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public OrderDTO create(OrderCreateDTO dto, Customer c) {
        Order order = new Order();

        LinkedHashSet<OrderItem> orderItems = itemMapper.toEntities(dto.getOrderItems());
        OrderAddress orderAddress = addressMapper.toEntity(dto.getAddress());
        Payment payment = paymentMapper.toEntity(orderItems, dto.getPaymentMethod());
        Customer customer = customerRepository.getReferenceById(c.getId());

        order.setType(OrderType.ONLINE);
        order.setStage(OrderStage.PENDING);
        order.setCustomer(customer);

        order.setPayment(payment);
        order.setShipment(null);
        order.setOrderAddress(orderAddress);
        order.setOrderItems(orderItems);

        repository.saveAndFlush(order);
        orderLogService.createCustomerLog(order, "Customer has Created a new Order");

        return mapper.toDTO(order);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void cancel(long id, OrderCancellationDeleteDTO dto, Customer customer) {
        Order order = repository.findByIdAndCustomerId(id, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStage() != OrderStage.PENDING) {
            throw new ValidationException("order",
                    "Orer stage not valid to be canceling . Current Stage: " + order.getStage());
        }

        order.setStage(OrderStage.FAILURE);

        orderLogService.createCustomerLog(order, dto.getDescription());
    }

    public List<OrderLogDTO> findOrderLogByOrderId(long id, Customer customer) {
        return orderLogService.findCustomerByOrderId(id, customer.getId());
    }
}
