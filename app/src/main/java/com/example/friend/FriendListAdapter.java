package com.example.friend;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
public class FriendListAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> friendsList;

    public FriendListAdapter(Context context, List<User> friendsList) {
        super(context, R.layout.item_friends, friendsList);
        this.context = context;
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false);
        }

        TextView friendEmailTextView = view.findViewById(R.id.emailTextView);

        User friend = friendsList.get(position);
        if (friend != null) {
            friendEmailTextView.setText(friend.getEmail());
        }

        return view;
    }
}
