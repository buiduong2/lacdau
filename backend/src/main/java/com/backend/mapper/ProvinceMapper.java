package com.backend.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.backend.dto.res.ProvinceDTO;
import com.backend.dto.res.ProvinceListDTO;
import com.backend.entities.Province;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProvinceMapper {

    List<ProvinceListDTO> toListDTO(List<Province> entities);

    ProvinceDTO toDTO(Province entity);

}
