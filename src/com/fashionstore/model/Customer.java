package com.fashionstore.model;

// Inheritance: Customer reuses common fields and methods from User.
public class Customer extends User {
    private int loyaltyPoints;
    // Composition: a Customer owns one ShoppingCart.
    private final ShoppingCart shoppingCart;

    public Customer(String userId, String name, String email) {
        super(userId, name, email);
        this.shoppingCart = new ShoppingCart("CART-" + userId);
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void addLoyaltyPoints(int points) {
        loyaltyPoints += points;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}
