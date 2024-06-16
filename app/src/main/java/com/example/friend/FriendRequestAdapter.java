package com.example.friend;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FriendRequestAdapter extends ArrayAdapter<FriendRequest> {

    private Context context;
    private List<FriendRequest> friendRequestsList;

    public FriendRequestAdapter(@NonNull Context context, @NonNull List<FriendRequest> objects) {
        super(context, 0, objects);
        this.context = context;
        this.friendRequestsList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_friend_request, parent, false);
        }

        TextView emailTextView = convertView.findViewById(R.id.emailTextView);
        Button acceptButton = convertView.findViewById(R.id.acceptButton);
        Button declineButton = convertView.findViewById(R.id.declineButton);

        FriendRequest request = friendRequestsList.get(position);

        // Fetch and display sender's email (you might want to optimize this)
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(request.userId);
        userRef.child("email").get().addOnSuccessListener(dataSnapshot -> {
            String email = dataSnapshot.getValue(String.class);
            emailTextView.setText(email);
        });

        acceptButton.setOnClickListener(v -> handleFriendRequest(request, true));
        declineButton.setOnClickListener(v -> handleFriendRequest(request, false));

        return convertView;
    }

    private void handleFriendRequest(FriendRequest request, boolean accept) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
        DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("users").child(request.userId);

        if (accept) {
            // Add to friends list
            currentUserRef.child("friendRequests").child("friends").child(request.userId).setValue(System.currentTimeMillis());
            senderRef.child("friendRequests").child("friends").child(currentUserId).setValue(System.currentTimeMillis());
        }

        // Remove from received requests
        currentUserRef.child("friendRequests").child("received").child(request.userId).removeValue();
        senderRef.child("friendRequests").child("sent").child(currentUserId).removeValue();

        Toast.makeText(context, accept ? "Friend request accepted" : "Friend request declined", Toast.LENGTH_SHORT).show();
    }
}
