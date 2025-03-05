package com.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dto.req.OrderProcessActionDTO;
import com.backend.dto.req.OrderProcessStageDTO;
import com.backend.dto.res.OrderProcessDTO;
import com.backend.entities.Employee;
import com.backend.entities.Order;
import com.backend.entities.enums.OrderStage;
import com.backend.exception.ResourceNotFoundException;
import com.backend.exception.ValidationException;
import com.backend.mapper.OrderMapper;
import com.backend.processor.AbstractOrderProcessor;
import com.backend.repository.EmployeeRepository;
import com.backend.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProcessService {

    private final OrderRepository repository;

    private final EmployeeRepository employeeRepository;

    @Qualifier("progress")
    private final List<AbstractOrderProcessor> progressOrderProcessors;

    @Qualifier("cancel")
    private final List<AbstractOrderProcessor> cancelOrderProcessors;

    private final OrderMapper orderMapper;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public OrderProcessDTO handleProcess(long orderId, OrderProcessStageDTO dto, long employeeId) {

        Order order = getOrderById(orderId);
        Employee employee = getEmployee(employeeId);
        AbstractOrderProcessor processor = getProcessor(order);
        processor.processStage(order, dto, employee);

        return orderMapper.toProcessDTO(order);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public OrderProcessDTO handleAction(long orderId, OrderProcessActionDTO dto, long employeeId) {
        Order order = getOrderById(orderId);
        Employee employee = getEmployee(employeeId);
        AbstractOrderProcessor processor = getProcessor(order);
        processor.handleAction(order, dto, employee);

        return orderMapper.toProcessDTO(order);
    }

    private AbstractOrderProcessor getProcessor(Order order) {
        OrderStage stage = order.getStage();
        List<AbstractOrderProcessor> processors;
        if (stage.isDone()) {
            throw new ValidationException("order", "Order has done can't not change");
        }
        if (stage.isCanceling()) {
            processors = cancelOrderProcessors;
        } else {
            processors = progressOrderProcessors;
        }
        for (AbstractOrderProcessor processor : processors) {
            if (processor.supports(stage)) {
                return processor;
            }
        }
        throw new ResourceNotFoundException("Processor not found");
    }

    private Order getOrderById(long orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    private Employee getEmployee(long employeeId) {
        return employeeRepository.getReferenceById(employeeId);
    }

}
