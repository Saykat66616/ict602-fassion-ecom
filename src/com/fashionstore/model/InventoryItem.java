package com.fashionstore.model;

public class InventoryItem {
    // Encapsulation: stock can only change through controlled methods like reserveStock().
    private final String inventoryId;
    private int quantityAvailable;
    private final int reorderLevel;

    public InventoryItem(String inventoryId, int quantityAvailable, int reorderLevel) {
        this.inventoryId = inventoryId;
        this.quantityAvailable = quantityAvailable;
        this.reorderLevel = reorderLevel;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void reserveStock(int quantity) {
        // Business rule inside the object: stock cannot become negative.
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (quantity > quantityAvailable) {
            throw new IllegalStateException("Not enough stock for inventory " + inventoryId);
        }
        quantityAvailable -= quantity;
    }

    public boolean isLowStock() {
        return quantityAvailable <= reorderLevel;
    }
}
