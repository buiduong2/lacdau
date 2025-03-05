package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.entities.District;

public interface DistrictRepository extends JpaRepository<District, Long> {

    @Query("""
            SELECT 
                province.name
            FROM District 
            WHERE id = ?1
            """)
    String findProvinceNameById(long id);
}
