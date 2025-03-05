package com.backend.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStage {
    // progress
    PENDING("Tạo đơn hàng", false, false),
    CONFIRMATION("Xác nhận đơn hàng", false, false),
    PAYMENT("Thanh toán đơn hàng", false, false),
    PACKING("Đóng gói đơn hàng", false, false),
    SHIPPING("Vận chuyển đơn hàng", false, false),
    DELIVERED("Giao đơn hàng", false, false),
    SUCCESSFUL("Hoàn thành", false, false),
    COMPLETED("Kết thúc đơn hàng", false, true),

    // Cancelation
    FAILURE("Thất bại", true, false),
    CANCELED("Hủy đơn hàng", true, true);

    private final String description;

    private final boolean isCanceling;

    private final boolean isDone;

}
