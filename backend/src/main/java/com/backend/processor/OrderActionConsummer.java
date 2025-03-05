package com.backend.processor;

import com.backend.entities.Employee;
import com.backend.entities.Order;

@FunctionalInterface
public interface OrderActionConsummer {
    void handle(Order order, Employee employee, Object payload);
}
