package com.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entities.Province;

public interface ProvinceRepository extends JpaRepository<Province, Long> {

    @EntityGraph(value = "Province.DTO", type = EntityGraphType.LOAD)
    Optional<Province> findDTOById(long id);
}
