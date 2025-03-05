package com.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.backend.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @EntityGraph(value = "Order.orderDetail", type = EntityGraphType.LOAD)
    Optional<Order> findDetailByIdAndCustomerId(long orderId, Long customerId);

    Optional<Order> findByIdAndCustomerId(long orderId, long customerId);

    @EntityGraph(value = "Order.AdminDTO", type = EntityGraphType.LOAD)
    Optional<Order> findAdminById(Long id);

}
