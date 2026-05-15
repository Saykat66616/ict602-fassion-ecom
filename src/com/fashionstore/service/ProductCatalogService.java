package com.fashionstore.service;

import com.fashionstore.model.Product;
import com.fashionstore.model.ProductVariant;
import com.fashionstore.model.Seller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProductCatalogService {
    // Service layer: product creation logic is kept separate from model classes.
    private final List<Product> products = new ArrayList<>();

    public Product createProduct(Seller seller, String productId, String name, String description, String brand, BigDecimal basePrice) {
        Product product = new Product(productId, seller, name, description, brand, basePrice);
        products.add(product);
        return product;
    }

    public ProductVariant addVariant(Product product, String variantId, String size, String color, String sku, BigDecimal price, int initialStock) {
        ProductVariant variant = new ProductVariant(variantId, product, size, color, sku, price, initialStock);
        product.addVariant(variant);
        return variant;
    }

    public Optional<Product> findProductById(String productId) {
        for (Product product : products) {
            if (product.getProductId().equalsIgnoreCase(productId)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    public List<Product> getProductsBySeller(Seller seller) {
        List<Product> sellerProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getSeller().getUserId().equals(seller.getUserId())) {
                sellerProducts.add(product);
            }
        }
        return Collections.unmodifiableList(sellerProducts);
    }

    public boolean updateProduct(Seller seller, String productId, String name, String description, String brand, BigDecimal basePrice) {
        Optional<Product> product = findProductById(productId);
        if (product.isEmpty() || !belongsToSeller(product.get(), seller)) {
            return false;
        }

        product.get().updateDetails(name, description, brand, basePrice);
        return true;
    }

    public boolean deleteProduct(Seller seller, String productId) {
        Optional<Product> product = findProductById(productId);
        if (product.isEmpty() || !belongsToSeller(product.get(), seller)) {
            return false;
        }

        return products.remove(product.get());
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    private boolean belongsToSeller(Product product, Seller seller) {
        return product.getSeller().getUserId().equals(seller.getUserId());
    }

    public void printCatalog() {
        for (Product product : products) {
            System.out.println(product);
            for (ProductVariant variant : product.getVariants()) {
                System.out.println("  - " + variant + " | $" + variant.getPrice()
                        + " | Stock: " + variant.getInventoryItem().getQuantityAvailable()
                        + (variant.getInventoryItem().isLowStock() ? " | LOW STOCK" : ""));
            }
        }
    }
}
