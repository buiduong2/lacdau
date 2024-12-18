package com.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import com.backend.dto.req.CategoryAdminUpdateDTO;
import com.backend.dto.res.CategoryAdminDTO;
import com.backend.entities.Category;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    @ToEntity
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "image", ignore = true)
    Category toEntity(CategoryAdminUpdateDTO dto);

    @ToEntity
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "image", ignore = true)
    void updateEntity(@MappingTarget Category category, CategoryAdminUpdateDTO dto);

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "imageSrc", source = "image.src")
    CategoryAdminDTO toAdminDTO(Category category);
}
