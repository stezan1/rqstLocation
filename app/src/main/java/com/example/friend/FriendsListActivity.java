package com.example.friend;

import android.os.Bundle;
import android.util.Log;
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

public class FriendsListActivity extends AppCompatActivity {

    private static final String TAG = "FriendsListActivity";
    private ListView friendsListView;
    private DatabaseReference friendRequestsRef;
    private DatabaseReference usersRef;
    private List<User> friendsList;
    private FriendListAdapter friendListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        Log.d(TAG, "onCreate: started");

        friendsListView = findViewById(R.id.friendsListView);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "onCreate: currentUserId = " + currentUserId);

        friendRequestsRef = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUserId).child("friendRequests").child("friends");
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        Log.d(TAG, "onCreate: Firebase references initialized");

        friendsList = new ArrayList<>();
        friendListAdapter = new FriendListAdapter(this, friendsList);

        friendsListView.setAdapter(friendListAdapter);

        Log.d(TAG, "onCreate: ListView adapter set");

        loadFriends();
    }

    private void loadFriends() {
        Log.d(TAG, "loadFriends: started");

        friendRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: friendRequestsRef dataSnapshot received");

                friendsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String friendUserId = snapshot.getKey();
                    Log.d(TAG, "onDataChange: friendUserId = " + friendUserId);

                    usersRef.child(friendUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange: usersRef dataSnapshot received for userId = " + friendUserId);

                            User friend = dataSnapshot.getValue(User.class);
                            if (friend != null) {
                                friend.setUserId(friendUserId);
                                friendsList.add(friend);
                                Log.d(TAG, "onDataChange: friend added to friendsList = " + friend.getEmail());
                                friendListAdapter.notifyDataSetChanged();
                            } else {
                                Log.w(TAG, "onDataChange: User data is null for userId = " + friendUserId);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled: usersRef listener cancelled for userId = " + friendUserId, databaseError.toException());
                            Toast.makeText(FriendsListActivity.this, "Failed to load friend details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: friendRequestsRef listener cancelled", databaseError.toException());
                Toast.makeText(FriendsListActivity.this, "Failed to load friends: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
