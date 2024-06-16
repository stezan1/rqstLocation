package com.example.friend;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {

    private ListView friendsListView;
    private List<User> friendsList;
    private FriendListAdapter friendListAdapter;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        friendsListView = findViewById(R.id.friendsListView);
        friendsList = new ArrayList<>();
        friendListAdapter = new FriendListAdapter(this, friendsList);
        friendsListView.setAdapter(friendListAdapter);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Load friends for the current user
        loadFriendsList();
    }

    private void loadFriendsList() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            Log.d("FriendsListActivity", "Loading friends for user: " + currentUserId);

            usersRef.child(currentUserId).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    friendsList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String friendUserId = snapshot.getKey();
                        // Fetch each friend's details
                        usersRef.child(friendUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User friend = dataSnapshot.getValue(User.class);
                                if (friend != null) {
                                    friend.setUserId(friendUserId); // Set userId
                                    friendsList.add(friend);
                                    friendListAdapter.notifyDataSetChanged();
                                    Log.d("FriendsListActivity", "Loaded friend: " + friend);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(FriendsListActivity.this, "Failed to load friend: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    Log.d("FriendsListActivity", "Loaded " + friendsList.size() + " friends");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FriendsListActivity", "Failed to load friends list: " + databaseError.getMessage());
                    Toast.makeText(FriendsListActivity.this, "Failed to load friends list: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
