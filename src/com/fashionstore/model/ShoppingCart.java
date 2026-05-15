package com.fashionstore.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCart {
    private final String cartId;
    // Composition: ShoppingCart owns CartItem objects.
    private final List<CartItem> items = new ArrayList<>();

    public ShoppingCart(String cartId) {
        this.cartId = cartId;
    }

    public void addItem(ProductVariant variant, int quantity) {
        for (CartItem item : items) {
            if (item.getVariant().getVariantId().equals(variant.getVariantId())) {
                item.updateQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(variant, quantity));
    }

    public List<CartItem> getItems() {
        // Encapsulation: callers can read cart items but cannot replace the internal list.
        return Collections.unmodifiableList(items);
    }

    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public void clear() {
        items.clear();
    }

    public void printCart() {
        System.out.println("Cart: " + cartId);
        for (CartItem item : items) {
            System.out.println("- " + item.getVariant() + " x " + item.getQuantity() + " = $" + item.getSubtotal());
        }
        System.out.println("Total: $" + calculateTotal());
    }
}
