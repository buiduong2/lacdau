package com.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.backend.entities.ProductCodeGenerator;
import com.backend.repository.ProductCodeGeneratorRepository;
import com.backend.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCodeGeneratorService {

    private final ProductCodeGeneratorRepository repository;

    private ProductCodeGenerator findOrCreateByKey(String key) {
        return repository.findById(key).orElseGet(() -> {
            ProductCodeGenerator entity = new ProductCodeGenerator();
            entity.setCounter(1L);
            entity.setName(key);
            return repository.save(entity);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public String getNextCounterByKey(String key) {
        ProductCodeGenerator entity = this.findOrCreateByKey(key);
        long counter = entity.getCounter();
        entity.setCounter(counter + 1);
        return Utils.getFormatProductCode(key, counter);
    }

    public List<ProductCodeGenerator> findAll() {
        return repository.findAll();
    }
}
