package com.example.friend;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsActivity extends AppCompatActivity {

    private ListView friendRequestsListView;
    private DatabaseReference friendRequestsRef;
    private List<FriendRequest> friendRequestsList;
    private FriendRequestAdapter friendRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        friendRequestsListView = findViewById(R.id.friendRequestsListView);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendRequestsRef = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUserId).child("friendRequests").child("received");

        friendRequestsList = new ArrayList<>();
        friendRequestAdapter = new FriendRequestAdapter(this, friendRequestsList);

        friendRequestsListView.setAdapter(friendRequestAdapter);

        loadFriendRequests();
    }

    private void loadFriendRequests() {
        friendRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendRequestsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String senderId = snapshot.getKey();
                    FriendRequest request = snapshot.getValue(FriendRequest.class);
                    request.userId = senderId;  // Add senderId to FriendRequest
                    friendRequestsList.add(request);
                }
                friendRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendRequestsActivity.this, "Failed to load friend requests: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
