package com.backend.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductOutOfStockException extends RuntimeException {
    private String productCode;
    private int quantity;

    public ProductOutOfStockException(String productCode, int quantity) {
        super("Product out of stock");
        this.productCode = productCode;
        this.quantity = quantity;

    }
}
