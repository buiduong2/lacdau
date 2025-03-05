package com.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomCustomerRepository {

    Optional<Customer> findByUserId(Long userId);

    boolean existsByUserId(long userId);

    @EntityGraph(value = "Customer.AdminDTO", type = EntityGraphType.LOAD)
    Optional<Customer> findAdminById(long id);

    @EntityGraph(value = "Customer.AdminDTO", type = EntityGraphType.LOAD)
    Optional<Customer> findProfileByUserId(long userId);
}
