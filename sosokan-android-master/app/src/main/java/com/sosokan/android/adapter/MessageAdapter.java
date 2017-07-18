package com.sosokan.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sosokan.android.R;
import com.sosokan.android.models.Conversation;
import com.sosokan.android.models.Message;
import com.sosokan.android.models.User;
import com.sosokan.android.ui.activity.EditProfileActivity;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.DateUtils;

import java.util.List;

/**
 * Created by AnhZin on 10/9/2016.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private Context mContext;
    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;
    private static final int RIGHT_MSG_IMG = 2;
    private static final int LEFT_MSG_IMG = 3;
    private List<Message> mListOfFireChat;
    User user, userOwner;
    private Listener listener;
    public void setListener(MessageAdapter.Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivUserChat;
        public LinearLayout llConversationItem;
        public TextView tvNameUser;
        public TextView txtLastMessage;
        public TextView tvTimestamp;

        public MyViewHolder(View view) {
            super(view);
            tvNameUser = (TextView) view.findViewById(R.id.tvNameUser);
            txtLastMessage = (TextView) view.findViewById(R.id.txtMessage);
            tvTimestamp = (TextView) view.findViewById(R.id.tvTimestamp);
            ivUserChat = (ImageView) view.findViewById(R.id.ivUserChat);
            // llConversationItem = (LinearLayout) view.findViewById(R.id.llConversationItem);

        }
    }

    public MessageAdapter(Context mContext, List<Message> messageList, User user, User userOwner) {
        this.mContext = mContext;
        this.mListOfFireChat = messageList;
        this.user = user;
        this.userOwner = userOwner;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType=0;
        Message message = mListOfFireChat.get(position);
        if(user != null) {
            if (message!= null && message.getUserId()!= null && message.getUserId().equals(user.getId())) {
                viewType = RIGHT_MSG;
            } else {
                viewType = LEFT_MSG;
            }
        }
        return viewType;
    }

    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages_send,parent,false);
            return new MessageAdapter.MyViewHolder(view);
        }else{

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages_receive,parent,false);
            return new MessageAdapter.MyViewHolder(view);
        }

        // view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages_send,parent,false);
        // return new MessageAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MessageAdapter.MyViewHolder holder,  int position) {
        if (mListOfFireChat != null && mListOfFireChat.size() > 0) {
            final Message message = mListOfFireChat.get(position);
            if (message != null && user!=null && userOwner!=null) {
                if (message.getUserId()!= null && message.getUserId().equals(user.getId())){
//                    holder.tvNameUser.setText(user.getUserName());
                    holder.txtLastMessage.setText(message.getContent());
                    holder.tvTimestamp.setText(DateUtils.getTimeForMessage(message.getTimestamp()));
                    if (holder.ivUserChat.getContext()!=null && user != null && user.getAvatar() != null && !user.getAvatar().getImageUrl().isEmpty()) {
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)holder.ivUserChat.getContext()).isDestroyed()) {

                        }*/
                        try
                        {
                            Glide.with(holder.ivUserChat.getContext()).load(user.getAvatar().getImageUrl()).into(holder.ivUserChat);
                        }catch (Exception ex)
                        {

                        }
                    }
                    holder.ivUserChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           if(holder.ivUserChat.getContext()!= null && message.getConversationId()!= null && !message.getConversationId().isEmpty())
                           {

                               FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.CONVERSATIONS).child(message.getConversationId()).addValueEventListener(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(DataSnapshot dataSnapshot) {
                                       Conversation    conversation = dataSnapshot.getValue(Conversation.class);
                                       if (conversation != null) {
                                           Intent intent = new Intent(mContext, EditProfileActivity.class);
                                           intent.putExtra(Constant.TITTLE_POST, conversation.getPostName());
                                           intent.putExtra(Constant.CATEGORYID, conversation.getPostCategoryId());
                                           intent.putExtra(Constant.ID_USER_OWNER, conversation.getPostOwnerId());
                                           intent.putExtra(Constant.ADVERTISEID, conversation.getPostId());
                                           intent.putExtra(Constant.CONVERSATIONID, conversation.getId());
                                           intent.putExtra(Constant.ADVERTISE_CREATED_AT, conversation.getCreatedAt());
                                           intent.putExtra(Constant.FROM_MESSAGE, true);
                                           mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                                       }
                                   }

                                   @Override
                                   public void onCancelled(DatabaseError databaseError) {

                                   }
                               });

                           }
                        }
                    });

                }else {
                    holder.tvNameUser.setText(userOwner.getUserName());
                    holder.txtLastMessage.setText(message.getContent());
                    holder.tvTimestamp.setText(DateUtils.getTimeForMessage(message.getTimestamp()));
                    if (holder.ivUserChat.getContext()!=null && userOwner != null && userOwner.getAvatar() != null && !userOwner.getAvatar().getImageUrl().isEmpty()) {
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)holder.ivUserChat.getContext()).isDestroyed()) {

                        }*/
                        try
                        {
                            Glide.with(holder.ivUserChat.getContext()).load(userOwner.getAvatar().getImageUrl()).into(holder.ivUserChat);
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
        return mListOfFireChat==null?0: mListOfFireChat.size();
    }
}

