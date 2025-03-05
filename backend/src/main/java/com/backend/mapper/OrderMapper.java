package com.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.backend.dto.res.OrderAdminDTO;
import com.backend.dto.res.OrderAdminListDTO;
import com.backend.dto.res.OrderDTO;
import com.backend.dto.res.OrderListDTO;
import com.backend.dto.res.OrderProcessDTO;
import com.backend.entities.Order;
import com.backend.entities.Payment;
import com.backend.entities.enums.PaymentStatus;
import com.backend.utils.Payments;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class OrderMapper {

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "paymentId", source = "payment.id")
    @Mapping(target = "shipmentId", source = "shipment.id")
    @Mapping(target = "orderAddressId", source = "orderAddress.id")
    public abstract OrderProcessDTO toProcessDTO(Order entity);

    @Mapping(target = "payment.status", expression = "java( getPaymentStatus(payment) )")
    @Mapping(target = "orderAddress.district", source = "orderAddress.district.name")
    @Mapping(target = "orderAddress.province", source = "orderAddress.district.province.name")
    public abstract OrderDTO toDTO(Order entity);

    protected PaymentStatus getPaymentStatus(Payment payment) {
        return Payments.getPaymentStatus(payment);
    }

    public abstract OrderAdminListDTO toAdminListDTO(Order entity);

    @Mapping(target = "orderAddress.district", source = "orderAddress.district.name")
    @Mapping(target = "orderAddress.province", source = "orderAddress.district.province.name")
    @Mapping(target = "orderAddress.districtId", source = "orderAddress.district.id")
    @Mapping(target = "orderAddress.provinceId", source = "orderAddress.district.province.id")
    public abstract OrderAdminDTO toAdminDTO(Order entity);

    public abstract OrderListDTO toListDTO(Order entity);
}
