package com.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.dto.req.OrderCreateDTO;
import com.backend.dto.req.OrderProcessAddressUpdateDTO.OrderAddressUpdateDTO;
import com.backend.entities.District;
import com.backend.entities.OrderAddress;
import com.backend.repository.DistrictRepository;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class OrderAddressMapper {

    @Autowired
    private DistrictRepository districtRepository;

    @ToEntity
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "district", expression = "java( getDistrictId(dto.getDistrictId()) )")
    public abstract OrderAddress toEntity(OrderCreateDTO.AddressDTO dto);

    @ToEntity
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "district", expression = "java( getDistrictId(dto.getDistrictId()) )")
    public abstract void updateEntity(@MappingTarget OrderAddress orderAddress, OrderAddressUpdateDTO dto);

    protected District getDistrictId(long id) {
        return districtRepository.getReferenceById(id);
    }

}
