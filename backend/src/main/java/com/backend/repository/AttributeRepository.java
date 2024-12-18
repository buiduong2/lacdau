package com.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.dto.res.AttributeAdminDTO;
import com.backend.entities.Attribute;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    @Query("""
            SELECT DISTINCT attr
            FROM Attribute AS attr
            JOIN FETCH attr.attributeValues AS attrVal
            INNER JOIN attrVal.products AS p ON p.category.id IN ?1
            WHERE p.status = 'ACTIVE'
            """)
    List<Attribute> findByProductCategoryInAndStatusIsActive(List<Long> ids);

    @Query("""

            SELECT new com.backend.dto.res.AttributeAdminDTO(
                a.id ,
                a.name ,
                a.createdAt ,
                a.updatedAt,
                COUNT(av.id)
            )
            FROM Attribute AS a
            LEFT JOIN a.attributeValues AS av
            GROUP BY a
            """)
    List<AttributeAdminDTO> findAllAdminBy();

    @Query("""
            FROM Attribute AS a
            JOIN FETCH a.attributeValues AS av
            WHERE a.id = ?1
                """)
    Optional<Attribute> findWithAttrValById(long id);

}
