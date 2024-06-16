package com.example.friend;

public class FriendRequest {
    public String userId;  // Add this line
    public long timestamp;
    public String status;

    public FriendRequest() {
        // Default constructor required for calls to DataSnapshot.getValue(FriendRequest.class)
    }

    public FriendRequest(long timestamp, String status) {
        this.timestamp = timestamp;
        this.status = status;
    }
}
