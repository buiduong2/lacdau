package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.backend.dto.res.OrderLogAdminDTO;
import com.backend.dto.res.OrderLogDTO;
import com.backend.entities.Employee;
import com.backend.entities.Order;
import com.backend.entities.OrderLog;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderStageAction;
import com.backend.exception.ResourceNotFoundException;
import com.backend.mapper.OrderLogMapper;
import com.backend.repository.OrderLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderLogService {

    private final OrderLogRepository repository;

    private final OrderLogMapper mapper;

    @Transactional(propagation = Propagation.MANDATORY)
    public void createCustomerLog(Order order, String description) {
        OrderStage stage = order.getStage();
        OrderLog orderLog = new OrderLog();
        orderLog.setDescription(description);
        orderLog.setStage(stage);
        orderLog.setOrder(order);
        orderLog.setEmployee(null);
        repository.save(orderLog);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void create(OrderStageAction action, String description, Order order, Employee employee) {
        OrderLog orderLog = new OrderLog();
        orderLog.setStage(order.getStage());
        orderLog.setOrder(order);
        orderLog.setEmployee(employee);
        orderLog.setAction(action);
        orderLog.setDescription(description);

        repository.save(orderLog);
    }

    public List<OrderLogDTO> findCustomerByOrderId(long orderId, long customerId) {
        List<OrderLog> entities = repository.findByOrderIdAndOrderCustomerId(orderId, customerId);
        return mapper.toDTOs(entities);
    }

    public List<OrderLogAdminDTO> findAdminByOrderId(long orderId) {
        List<OrderLog> entities = repository.findAdminByOrderId(orderId);
        if (entities.isEmpty()) {
            throw new ResourceNotFoundException("Order not found");
        }
        return mapper.toAdminDTOS(entities);
    }

}
