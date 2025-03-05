package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entities.OrderLog;

public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {

    List<OrderLog> findByOrderIdAndOrderCustomerId(long orderId, long customerId);

    List<OrderLog> findAdminByOrderId(long orderId);

}
