package com.backend.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import com.backend.dto.req.AttributeValueAdminUpdateDTO;
import com.backend.dto.res.AttributeValueAdminBasic;
import com.backend.entities.AttributeValue;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttributeValueMapper {

    @ToEntity
    @Mapping(target = "attribute", ignore = true)
    @Mapping(target = "products", ignore = true)
    AttributeValue toEntity(AttributeValueAdminUpdateDTO dto);

    @InheritConfiguration(name = "toEntity")
    void updateEntity(@MappingTarget AttributeValue entity, AttributeValueAdminUpdateDTO dto);

    @Mapping(target = "attributeId", source = "attribute.id")
    AttributeValueAdminBasic toBasicDTO(AttributeValue attributeValue);
}
