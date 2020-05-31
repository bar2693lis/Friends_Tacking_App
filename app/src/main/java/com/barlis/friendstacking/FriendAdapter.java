package com.barlis.friendstacking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<Friend> friends;

    public FriendAdapter(List<Friend> friends) {
        this.friends = friends;
    }

    interface FriendsListener
    {
        void onFriendClicked(int position, View view);
    }

    FriendsListener listener;

    public void setListener(FriendsListener listener)
    {
        this.listener = listener;
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder
    {
        ImageView friendIv;
        TextView nameTv;
        TextView bestTv;

        public FriendViewHolder( View itemView) {
            super(itemView);

            friendIv = itemView.findViewById(R.id.friend_image);
            nameTv = itemView.findViewById(R.id.friend_name);
            bestTv = itemView.findViewById(R.id.best_friend_or_not);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onFriendClicked(getAdapterPosition(), v);
                    }
                }
            });
        }
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_cell, parent, false);
        FriendViewHolder holder = new FriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.friendIv.setImageBitmap(friend.getPhoto());
        holder.nameTv.setText(friend.getName());
        if (friend.isBestFriend())
        {
            holder.bestTv.setText("Best Friend");
        }
        else
        {
            holder.bestTv.setText("Not bestie");
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}

