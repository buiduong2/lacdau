package com.backend.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.backend.dto.res.OrderLogAdminDTO;
import com.backend.dto.res.OrderLogDTO;
import com.backend.entities.OrderLog;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderLogMapper {

    List<OrderLogDTO> toDTOs(List<OrderLog> entities);

    List<OrderLogAdminDTO> toAdminDTOS(List<OrderLog> entities);

    @Mapping(target = "orderId", source = "order.id")
    OrderLogDTO toDTO(OrderLog entity);

    @Mapping(target = "orderId", source = "order.id")
    OrderLogAdminDTO toAdminDTO(OrderLog entity);

}
