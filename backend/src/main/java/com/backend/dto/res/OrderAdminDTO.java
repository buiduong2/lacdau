package com.backend.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import com.backend.entities.enums.Gender;
import com.backend.entities.enums.OrderItemStatus;
import com.backend.entities.enums.OrderStage;
import com.backend.entities.enums.OrderType;
import com.backend.entities.enums.PaymentMethod;
import com.backend.utils.Payments.PaymentStatusable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderAdminDTO {

    private long id;
    private OrderType type;
    private OrderStage stage;
    private CustomerDTO customer;
    private PaymentDTO payment;
    private ShipmentDTO shipment;
    private OrderAddressDTO orderAddress;
    private List<OrderItemDTO> orderItems;

    @Getter
    @Setter
    public static class CustomerDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private Gender gender;
        private String phone;
        private String email;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    public static class PaymentDTO implements PaymentStatusable {
        private Long id;
        private PaymentMethod method;
        private Double amountPaid;
        private Double shippingPrice;
        private double subTotalPrice;
        private Double totalPrice;
        private LocalDateTime paymentDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    public static class ShipmentDTO {
        private Long id;
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
        private Long id;
        private String fullName;
        private String phone;
        private long provinceId;
        private long districtId;
        private String province;
        private String district;
        private String detail;
        private String message;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    public static class OrderItemDTO {
        private Long id;
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
        private String productCode;
        private Long id;
        private String name;
        private ImageDTO mainImage;
    }

    @Getter
    @Setter
    public static class ImageDTO {
        private String src;
        private String alt;
    }
}
