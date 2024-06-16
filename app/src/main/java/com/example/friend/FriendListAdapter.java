package com.example.friend;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class FriendListAdapter extends BaseAdapter {

    private Context context;
    private List<User> friendsList;

    public FriendListAdapter(Context context, List<User> friendsList) {
        this.context = context;
        this.friendsList = friendsList;
    }

    @Override
    public int getCount() {
        return friendsList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false);
            holder = new ViewHolder();
            holder.emailTextView = convertView.findViewById(R.id.emailTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User friend = friendsList.get(position);
        holder.emailTextView.setText(friend.getEmail());

        return convertView;
    }

    static class ViewHolder {
        TextView emailTextView;
    }
}
