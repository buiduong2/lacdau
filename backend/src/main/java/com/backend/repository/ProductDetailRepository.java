package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entities.ProductDetail;

public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {
    
}
