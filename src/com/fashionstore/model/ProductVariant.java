package com.fashionstore.model;

import java.math.BigDecimal;

public class ProductVariant {
    private final String variantId;
    private final Product product;
    private final String size;
    private final String color;
    private final String sku;
    private BigDecimal price;
    // Composition: each ProductVariant owns its InventoryItem.
    private final InventoryItem inventoryItem;

    public ProductVariant(String variantId, Product product, String size, String color, String sku, BigDecimal price, int initialStock) {
        this.variantId = variantId;
        this.product = product;
        this.size = size;
        this.color = color;
        this.sku = sku;
        this.price = price;
        this.inventoryItem = new InventoryItem("INV-" + variantId, initialStock, 3);
    }

    public String getVariantId() {
        return variantId;
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public void adjustPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return product.getName() + " [" + size + ", " + color + ", SKU: " + sku + "]";
    }
}
