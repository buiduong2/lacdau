package com.backend.repository;

import com.backend.dto.res.RelateGroupAdminDTO;
import com.backend.entities.RelateGroup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RelateGroupRepository extends JpaRepository<RelateGroup, Long> {

    @Query("""
            SELECT new com.backend.dto.res.RelateGroupAdminDTO(
                r.id,
                r.name,
                r.createdAt,
                r.updatedAt,
                COUNT(ri)
            )
            FROM RelateGroup  AS r
            LEFT JOIN r.relateInfos AS ri
            GROUP BY r
            """)
    List<RelateGroupAdminDTO> findAllAdminDtosBy();

}
