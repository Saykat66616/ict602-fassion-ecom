package com.fashionstore.model;

import java.math.BigDecimal;

public class OrderItem {
    // Association: each OrderItem points to the purchased ProductVariant.
    private final ProductVariant variant;
    private final int quantity;
    private final BigDecimal unitPrice;

    public OrderItem(ProductVariant variant, int quantity) {
        this.variant = variant;
        this.quantity = quantity;
        this.unitPrice = variant.getPrice();
    }

    public BigDecimal calculateSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return variant + " x " + quantity + " = $" + calculateSubtotal();
    }
}
