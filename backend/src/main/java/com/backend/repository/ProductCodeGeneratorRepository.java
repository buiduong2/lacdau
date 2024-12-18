package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entities.ProductCodeGenerator;

public interface ProductCodeGeneratorRepository extends JpaRepository<ProductCodeGenerator, String> {
    
}
