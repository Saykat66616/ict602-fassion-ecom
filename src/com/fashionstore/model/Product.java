package com.fashionstore.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Product {
    private final String productId;
    private final Seller seller;
    private String name;
    private String description;
    private String brand;
    private BigDecimal basePrice;
    // Composition: a Product is made up of one or more ProductVariant objects.
    private final List<ProductVariant> variants = new ArrayList<>();

    public Product(String productId, Seller seller, String name, String description, String brand, BigDecimal basePrice) {
        this.productId = productId;
        this.seller = seller;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.basePrice = basePrice;
    }

    public void addVariant(ProductVariant variant) {
        variants.add(variant);
    }

    public List<ProductVariant> getVariants() {
        // Encapsulation: return read-only view so outside code cannot directly modify the list.
        return Collections.unmodifiableList(variants);
    }

    public String getProductId() {
        return productId;
    }

    public Seller getSeller() {
        return seller;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void updateDetails(String name, String description, String brand, BigDecimal basePrice) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return productId + " - " + name + " by " + brand + " (" + seller.getStoreName() + ")";
    }
}
