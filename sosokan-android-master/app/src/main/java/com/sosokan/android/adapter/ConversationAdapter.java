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
import com.google.firebase.database.DatabaseReference;
import com.sosokan.android.R;
import com.sosokan.android.models.Conversation;
import com.sosokan.android.models.User;
import com.sosokan.android.ui.activity.MessageActivity;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.DateUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by AnhZin on 10/8/2016.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewHolder>  implements View.OnClickListener {

    private Context mContext;
    private List<Conversation> conversationList;
    private ConversationAdapter.Listener listener;
    private DatabaseReference mQueryUser;
    Map<String, User> mapUser;
    User user;

    public void setListener(ConversationAdapter.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

    }

    public interface Listener {
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View v;

        public ImageView ivUserChat;
        public LinearLayout llConversationItem;
        public TextView tvNameUser;
        public TextView txtLastMessage;
        public TextView tvTimestamp;
        public TextView tvNameCategoryConversation;

        public MyViewHolder(View view) {
            super(view);
            this.v = view;
            tvNameUser = (TextView) view.findViewById(R.id.tvNameUserConversation);
            tvNameCategoryConversation = (TextView) view.findViewById(R.id.tvNameCategoryConversation);
            txtLastMessage = (TextView) view.findViewById(R.id.txtLastMessageConversation);
            tvTimestamp = (TextView) view.findViewById(R.id.tvTimestampConversation);
            ivUserChat = (ImageView) view.findViewById(R.id.ivUserChatConversation);
            llConversationItem = (LinearLayout) view.findViewById(R.id.llConversationItem);


        }
        public void bind(final int position, final ConversationAdapter.Listener listener) {

            v.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {

                    listener.onItemClick(position);

                }

            });

        }
    }

    public ConversationAdapter(Context mContext, List<Conversation> conversationList, Map<String, User> mapUser, User user) {
        this.mContext = mContext;
        this.conversationList = conversationList;
        this.mapUser = mapUser;
        this.user = user;
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(int position);
    }


    @Override
    public ConversationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conversation, parent, false);

        return new ConversationAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ConversationAdapter.MyViewHolder holder, final int position) {
        holder.bind(position,listener);
        if (conversationList != null && conversationList.size() > 0) {
            final Conversation conversation = conversationList.get(position);
            if (conversation != null) {
                if (conversation.getId() != null && !conversation.getId().isEmpty())
                Log.e("conver ID", conversation.getId());
                if (conversation.getPostName() != null && !conversation.getPostName().isEmpty())
                    Log.e("getPostName ", conversation.getPostName());
                String stDisplay = "";
                if (conversation.getLastMessageSentBy()!= null && !conversation.getLastMessageSentBy().isEmpty()) {

                    stDisplay = conversation.getLastMessageSentBy();
                }
                if(conversation.getLastMessageContent()!= null && !conversation.getLastMessageContent().isEmpty() && !stDisplay.isEmpty())
                {
                    stDisplay = stDisplay+ " : " + conversation.getLastMessageContent();
                }    else if(conversation.getLastMessageContent()!= null && !conversation.getLastMessageContent().isEmpty() ){
                    stDisplay =  conversation.getLastMessageContent();
                }

                holder.tvNameUser.setText(stDisplay);
                holder.tvTimestamp.setText(DateUtils.toHours(conversation.getLastMessageTimestamp()));
                holder.tvNameCategoryConversation.setText(conversation.getPostName());
                if (conversation != null && mContext != null && conversation.getPostHeaderImageURL() != null && !conversation.getPostHeaderImageURL().isEmpty()) {
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) mContext).isDestroyed()) {
                        //Glide.with(mContext).load(conversation.getPostHeaderImageURL()).into(holder.ivUserChat);
                    }*/
                    try{
                        Glide.with(holder.ivUserChat.getContext()).load(conversation.getPostHeaderImageURL()).centerCrop()
                                .transform(new CircleTransform(holder.ivUserChat.getContext())).override(50, 50).into(holder.ivUserChat);

                    }catch (Exception ex)
                    {

                    }
                }else{

                }

            }

        }
    }

    private void callMessageActivity(Conversation conversation, User user, int position) {
        Log.e("callMessageActivity ", "zin");
        Intent intent = new Intent(mContext, MessageActivity.class);

        String idOwnerAd = "";
        Map<String, Object> mapUser = conversation.getUsers();
        if (mapUser != null) {
            for (Map.Entry<String, Object> entry : mapUser.entrySet()) {
                String userId = entry.getKey();
                if (userId != null && user != null && !userId.equals(user.getId())) {
                    idOwnerAd = userId;
                    break;
                }
            }
        }
        intent.putExtra(Constant.POSSITION, position);
        intent.putExtra(Constant.TITTLE_POST, conversation.getPostName());
        intent.putExtra(Constant.CATEGORYID, conversation.getPostCategoryId());
        intent.putExtra(Constant.ID_USER_OWNER, idOwnerAd);
        intent.putExtra(Constant.ADVERTISEID, conversation.getPostId());
        intent.putExtra(Constant.CONVERSATIONID, conversation.getId());
        intent.putExtra(Constant.ADVERTISE_CREATED_AT, conversation.getCreatedAt());
        intent.putExtra(Constant.FROM_DETAIL, false);
        mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }
}

