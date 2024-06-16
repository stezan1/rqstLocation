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

public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> userList;

    public UserAdapter(@NonNull Context context, @NonNull List<User> objects) {
        super(context, 0, objects);
        this.context = context;
        this.userList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        TextView emailTextView = convertView.findViewById(R.id.emailTextView);
        Button sendRequestButton = convertView.findViewById(R.id.sendRequestButton);

        User user = userList.get(position);
        emailTextView.setText(user.email);

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFriendRequest(user);
            }
        });

        return convertView;
    }

    private void sendFriendRequest(User user) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference sentRef = FirebaseDatabase.getInstance().getReference("users")
                .child(currentUserId).child("friendRequests").child("sent").child(user.userId);
        DatabaseReference receivedRef = FirebaseDatabase.getInstance().getReference("users")
                .child(user.userId).child("friendRequests").child("received").child(currentUserId);

        FriendRequest friendRequest = new FriendRequest(System.currentTimeMillis(), "pending");

        sentRef.setValue(friendRequest);
        receivedRef.setValue(friendRequest);

        Toast.makeText(context, "Friend request sent", Toast.LENGTH_SHORT).show();
    }
}
