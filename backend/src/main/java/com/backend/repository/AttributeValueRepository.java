package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.dto.res.AttributeValueAdminDTO;
import com.backend.entities.AttributeValue;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {

    List<AttributeValue> findByProductsCategoryIdIn(List<Long> ids);

    @Query("""
            SELECT av.id
            FROM AttributeValue AS av
            INNER JOIN av.products AS p ON p.id = ?1
            """)
    List<Long> findIdsByProductIds(Long id);

    @Query("""
            SELECT new com.backend.dto.res.AttributeValueAdminDTO (
                av.id,
                av.name,
                av.attribute.id,
                av.createdAt,
                av.updatedAt,
                COUNT(p.id)
            )
            FROM AttributeValue AS av
            LEFT JOIN av.products AS p
            GROUP BY av
            """)
    List<AttributeValueAdminDTO> findAllAdminDtosBy();
}
