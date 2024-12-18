package com.backend.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import com.backend.dto.req.AttributeAdminCreateDTO;
import com.backend.dto.req.AttributeAdminUpdateDTO;
import com.backend.dto.res.AttributeAdminBasicDTO;
import com.backend.entities.Attribute;
import com.backend.entities.AttributeValue;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, //
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED//
)
@DecoratedWith(AttributeMapperDecorator.class)
public interface AttributeMapper {

    @Mapping(target = "attributeValueIds", expression = "java( toEntityId(entity.getAttributeValues()) )")
    AttributeAdminBasicDTO toBasicDTO(Attribute entity);

    default List<Long> toEntityId(Set<AttributeValue> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream().map(AttributeValue::getId).toList();
    }

    @ToEntity
    Attribute toEntity(AttributeAdminCreateDTO dto);

    @ToEntity
    void updateEntity(@MappingTarget Attribute entity, AttributeAdminUpdateDTO dto);

    @ToEntity
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "attribute", ignore = true)
    AttributeValue toAttributeValue(AttributeAdminCreateDTO.AttributeValueDTO dto);

    @ToEntity
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "attribute", ignore = true)
    AttributeValue toAttributeValue(AttributeAdminUpdateDTO.AttributeValueDTO dto);

}
