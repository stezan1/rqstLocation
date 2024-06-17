package com.example.friend;

public class Friend {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Friend(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }
    private String email; // Example field, adjust according to your Firebase structure

    // Constructor, getters, and setters
}
