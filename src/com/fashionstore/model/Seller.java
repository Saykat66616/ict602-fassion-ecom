package com.fashionstore.model;

// Inheritance: Seller is a specialized type of User.
public class Seller extends User {
    private final String storeName;
    private String verificationStatus;

    public Seller(String userId, String name, String email, String storeName) {
        super(userId, name, email);
        this.storeName = storeName;
        this.verificationStatus = "APPROVED";
    }

    public String getStoreName() {
        return storeName;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
