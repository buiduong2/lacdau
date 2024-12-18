package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.dto.res.BrandAdminDTO;
import com.backend.entities.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("""
            SELECT new com.backend.dto.res.BrandAdminDTO(
                b.id,
                b.name,
                b.createdAt,
                b.updatedAt,
                COUNT(p.id)
            )
            FROM Brand AS b
            LEFT JOIN b.products AS p
            GROUP BY b
            """)
    List<BrandAdminDTO> findAllAdminDtosBy();




}
