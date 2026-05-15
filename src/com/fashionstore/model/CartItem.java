package com.fashionstore.model;

import java.math.BigDecimal;

public class CartItem {
    // Association: each CartItem refers to one selected ProductVariant.
    private final ProductVariant variant;
    private int quantity;

    public CartItem(ProductVariant variant, int quantity) {
        this.variant = variant;
        updateQuantity(quantity);
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Cart item quantity must be greater than zero.");
        }
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return variant.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
