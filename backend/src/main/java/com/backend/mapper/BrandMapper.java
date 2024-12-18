package com.backend.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import com.backend.dto.req.BrandAdminUpdateDTO;
import com.backend.dto.res.BrandAdminBasicDTO;
import com.backend.entities.Brand;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BrandMapper {

    BrandAdminBasicDTO toBasicDTO(Brand brand);

    @ToEntity
    @Mapping(target = "products", ignore = true)
    Brand toEntity(BrandAdminUpdateDTO dto);

    @InheritConfiguration(name = "toEntity")
    void updateEntity(@MappingTarget Brand brand, BrandAdminUpdateDTO dto);
}
