package com.sosokan.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.sosokan.android.R;
import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.Comment;
import com.sosokan.android.models.User;
import com.sosokan.android.ui.activity.EditProfileActivity;
import com.sosokan.android.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context mContext;
    private List<Comment> mObjects;
    private Listener listener;
    int widthBase;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDate, tvContent;
        ImageView imvAvatar;
        public MyViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
            imvAvatar = (ImageView) view.findViewById(R.id.imvAvatar);

        }


    }

    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_comment, parent, false);
        //    Log.e("onCreateViewHolder", Integer.toString(mObjects.size()));
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        widthBase = windowManager.getDefaultDisplay().getWidth();
        CommentAdapter.MyViewHolder holder = new CommentAdapter.MyViewHolder(itemView);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (mObjects != null && mObjects.size() > 0 && position < mObjects.size()) {
            final Comment comment = mObjects.get(position);
            if (comment != null) {
                holder.tvName.setText(comment.getName());

                holder.tvDate.setText(DateUtils.toDuration(comment.getCreatedAt(), mContext));
                holder.tvContent.setText(comment.getContent());
            }

            holder.imvAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("COMMENT ADAPTER", "viewHolder.imvAvatar.setOnClickListener");
                    gotoEditProfile();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }


    public interface Listener {
        void onItemClick(int position);
    }

    public CommentAdapter(Context context, int resource, List<Comment> objects, User user) {


        this.mContext = context;
        this.mObjects = objects;
    }



    private void gotoEditProfile() {
        Intent intent = new Intent(mContext, EditProfileActivity.class);
        mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}
