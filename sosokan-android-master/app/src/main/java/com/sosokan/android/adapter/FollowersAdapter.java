package com.sosokan.android.adapter;

import android.app.Activity;
import android.content.Context;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.sosokan.android.R;
import com.sosokan.android.models.User;

import java.util.List;
import java.util.Map;

/**
 * Created by AnhZin on 10/8/2016.
 */

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.MyViewHolder> {

    private Context mContext;
    private List<User> userList;
    private FollowersAdapter.Listener listener;
    private DatabaseReference mQueryUser;
    Map<String, User> mapUser;
    User user;
    public void setListener(FollowersAdapter.Listener listener) {
        this.listener = listener;
    }



    public interface Listener {
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;

        public ImageView ivUser;
        public LinearLayout llUserItem;
        public TextView tvNameUser;

        public MyViewHolder(View view) {
            super(view);
            this.v = view;
            tvNameUser = (TextView) view.findViewById(R.id.tvNameUser);
            ivUser = (ImageView) view.findViewById(R.id.ivUser);
            llUserItem = (LinearLayout) view.findViewById(R.id.llUserItem);

        }
    }

    public FollowersAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }


    @Override
    public FollowersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new FollowersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FollowersAdapter.MyViewHolder holder, final int position) {
        if (userList != null && userList.size() > 0) {
            final User user = userList.get(position);
            if (user != null) {
                if(user.getUserName()!= null && !user.getUserName().isEmpty())
                {
                    holder.tvNameUser.setText(user.getUserName());
                }

                if(user.getAvatar()!= null && user.getAvatar().getImageUrl()!= null && !user.getAvatar().getImageUrl().isEmpty())
                {
                    if (holder.ivUser.getContext() != null) {
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)holder.ivUser.getContext()).isDestroyed())
                        {


                        }*/
                        try
                        {
                            Glide.with(holder.ivUser.getContext()).load(user.getAvatar().getImageUrl()).centerCrop()
                                    .transform(new CircleTransform(holder.ivUser.getContext())).override(40, 40).into(holder.ivUser);
                        }catch (Exception ex)
                        {

                        }
                    }
                }


            }

        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

