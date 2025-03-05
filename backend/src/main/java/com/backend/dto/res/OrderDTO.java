package com.backend.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import com.backend.entities.enums.OrderItemStatus;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderType;
import com.backend.entities.enums.PaymentMethod;
import com.backend.entities.enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {

    private long id;
    private List<OrderItemDTO> orderItems;
    private OrderStage stage;
    private OrderType type;
    private PaymentDTO payment;
    private ShipmentDTO shipment;
    private OrderAddressDTO orderAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    public static class OrderItemDTO {
        private ProductDTO product;
        private int requestedQuantity;
        private int suppliedQuantity;
        private int unitPrice;
        private Integer salePrice;
        private OrderItemStatus status;
    }

    @Getter
    @Setter
    public static class ProductDTO {
        private String name;
        private String productCode;
        private ImageDTO mainImage;
    }

    @Getter
    @Setter
    public static class ImageDTO {
        private String src;
        private String alt;
    }

    @Getter
    @Setter
    public static class PaymentDTO {
        private Double amountPaid;

        private LocalDateTime paymentDate;

        private PaymentMethod method;

        private PaymentStatus status;

        private Double shippingPrice;

        private double subTotalPrice;

        private Double totalPrice;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    public static class ShipmentDTO {
        private LocalDateTime estimatedDelivery;
        private double shippingPrice;
        private String trackingDetail;
        private String carrier;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    public static class OrderAddressDTO {
        private String fullName;
        private String phone;
        private String province;
        private String district;
        private String detail;
        private String message;
    }
}
