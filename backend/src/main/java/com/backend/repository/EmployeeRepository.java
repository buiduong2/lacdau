package com.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUserId(long userId);

    boolean existsByUserId(long userId);

    @EntityGraph(value = "Employee.AdminDTO", type = EntityGraphType.LOAD)
    Optional<Employee> findProfileByUserId(long userId);
}
