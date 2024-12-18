package com.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.backend.dto.res.RelateDTO;
import com.backend.entities.RelateInfo;

public interface RelateInfoRepository extends JpaRepository<RelateInfo, Long> {

    @Query("""
               SELECT new com.backend.dto.res.RelateDTO(
                    i.id,
                   i.product.productCode,
                   i.name,
                   COALESCE(i.product.salePrice,i.product.originalPrice)
               )
               FROM RelateInfo  i
               WHERE i.relateGroup.id = (SELECT relateGroup.id FROM RelateInfo WHERE id = :id)
            """)
    List<RelateDTO> findByRelateById(Long id);

    @Query("""
            SELECT new com.backend.dto.res.RelateDTO(
                i.id,
                i.product.productCode,
                i.name,
                COALESCE(i.product.salePrice,i.product.originalPrice)
            )
            FROM RelateInfo  i
            WHERE i.relateGroup.id = ?1
            """)
    List<RelateDTO> findByRelateGroupId(Long id);

    Optional<RelateInfo> findByProductProductCode(String code);

    @Modifying
    @Query("""
            DELETE FROM RelateInfo AS r WHERE r.relateGroup.id =?1
            """)
    int deleteByRelateGroupId(long id);

    @Modifying
    @Query("""
            DELETE FROM RelateInfo AS r WHERE r.product.productCode NOT IN ?1 AND r.relateGroup.id = ?2
            """)
    int deleteByProductCodeNotInAndGroupId(List<String> codes, long groupId);

    @Modifying
    @Query("""
            DELETE FROM RelateInfo AS r WHERE r.relateGroup.id = ?1
            """)
    int deleteByGroupId(long groupId);
}
