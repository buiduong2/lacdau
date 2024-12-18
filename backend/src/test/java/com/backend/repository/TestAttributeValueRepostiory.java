package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.entities.AttributeValue;

@Repository
public interface TestAttributeValueRepostiory extends JpaRepository<AttributeValue, Long> {

    List<AttributeValue> findByIdIn(List<Long> id);

    List<AttributeValue> findFirst7By();
}
