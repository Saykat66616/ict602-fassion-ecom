package com.fashionstore.model;

// Abstraction: User is an abstract parent class for common user data and behavior.
public abstract class User {
    // Encapsulation: fields are private and accessed through public methods.
    private final String userId;
    private String name;
    private String email;

    protected User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void updateProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
