package com.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import com.backend.dto.req.OrderProcessShipmentUpdateDTO;
import com.backend.entities.Shipment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShipmentMapper {

    @ToEntity
    Shipment toEntity(OrderProcessShipmentUpdateDTO.ShipmentDTO dto);

    @ToEntity
    void updateEntity(@MappingTarget Shipment entity, OrderProcessShipmentUpdateDTO.ShipmentDTO dto);
}
