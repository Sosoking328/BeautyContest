package com.sosokan.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sosokan.android.R;
import com.sosokan.android.models.ChatModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.firebase.database.Query;

/**
 * Created by AnhZin on 9/4/2016.
 */
public class ChatFirebaseAdapter extends FirebaseRecyclerAdapter<ChatFirebaseAdapter.MyChatViewHolder,ChatModel> {

    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;
    private static final int RIGHT_MSG_IMG = 2;
    private static final int LEFT_MSG_IMG = 3;
    private List<ChatModel> mListOfFireChat;
    private Context mContext;

   // private ClickListenerChatFirebase mClickListenerChatFirebase;

    private String idUser;

    private String avatarUserSender,avatarUserReceive;
    public ChatFirebaseAdapter(Context mContext, Query query, Class<ChatModel> itemClass,
                               @Nullable ArrayList<ChatModel> items,
                               @Nullable ArrayList<String> keys,String idUser) {
        super(query, itemClass, items, keys);
        mListOfFireChat = items;
        // mStorage = FirebaseStorage.getInstance().getReference();
        this.mContext = mContext;
        this.idUser = idUser;
    }
    public ChatFirebaseAdapter(Context mContext, Query query, Class<ChatModel> itemClass,
                              @Nullable ArrayList<ChatModel> items,
                              @Nullable ArrayList<String> keys,String idUser, String avatarUserSender,String avatarUserReceive) {
        super(query, itemClass, items, keys);
        mListOfFireChat = items;
       // mStorage = FirebaseStorage.getInstance().getReference();
        this.mContext = mContext;
        this.idUser = idUser;
        this.avatarUserSender = avatarUserSender;
        this.avatarUserReceive = avatarUserReceive;
    }
    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages_send,parent,false);
            return new MyChatViewHolder(view);
        }else if (viewType == LEFT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages_receive,parent,false);
            return new MyChatViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages_send,parent,false);
        return new MyChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyChatViewHolder viewHolder, int position) {
        ChatModel model=mListOfFireChat.get(position);
        viewHolder.setIvUser(model.getUserModel().getPhotoProfile());
        viewHolder.setTvNameUser(model.getUserModel().getName());
        viewHolder.setTxtMessage(model.getMessage());
        viewHolder.setTvTimestamp(com.sosokan.android.utils.DateUtils.toDuration(model.getTimeStamp(),mContext));

        //selectedItem = mListOfFireChat.size() -1;
        if(position == mListOfFireChat.size() -1)
            viewHolder.itemView.setSelected(true);
        viewHolder.itemView.requestFocus();
    }
    public static int selectedItem = -1;

    public void setSelectedItem(int position)
    {
        selectedItem = position;
    }
    @Override
    protected void itemAdded(ChatModel item, String key, int position) {

    }

    @Override
    protected void itemChanged(ChatModel oldItem, ChatModel newItem, String key, int position) {
        Log.d("itemChanged",Integer.toString(position));
        selectedItem = mListOfFireChat.size() -1;

    }

    @Override
    protected void itemRemoved(ChatModel item, String key, int position) {

    }

    @Override
    protected void itemMoved(ChatModel item, String key, int oldPosition, int newPosition) {

    }
    public void refillAdapter(ChatModel newFireChatMessage){

        /*add new message chat to list*/
       // mListOfFireChat.add(newFireChatMessage);

        /*refresh view*/
        notifyItemInserted(getItemCount()-1);
    }

    public void cleanUp() {
        mListOfFireChat.clear();
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel model = getItem(position);
        if (model.getMapModel() != null){
            if (model.getUserModel().getUserId().equals(idUser)){
                return RIGHT_MSG_IMG;
            }else{
                return LEFT_MSG_IMG;
            }
        }else if (model.getFile() != null){
            if (model.getFile().getType().equals("img") && model.getUserModel().getName().equals(idUser)){
                return RIGHT_MSG_IMG;
            }else{
                return LEFT_MSG_IMG;
            }
        }else if (model.getUserModel().getUserId().equals(idUser)){
            return RIGHT_MSG;
        }else{
            return LEFT_MSG;
        }
    }


    public class MyChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTimestamp;
        ImageView ivUser;
        TextView txtMessage,tvNameUser;
        public MyChatViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = (TextView)itemView.findViewById(R.id.tvTimestamp);
            txtMessage = (TextView)itemView.findViewById(R.id.txtMessage);
            tvNameUser = (TextView)itemView.findViewById(R.id.tvNameUser);
            ivUser = (ImageView)itemView.findViewById(R.id.ivUserChat);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ChatModel model = getItem(position);
           /* if (model.getMapModel() != null){
                mClickListenerChatFirebase.clickImageMapChat(view,position,model.getMapModel().getLatitude(),model.getMapModel().getLongitude());
            }else{
                mClickListenerChatFirebase.clickImageChat(view,position,model.getUserModel().getName(),model.getUserModel().getPhoto_profile(),model.getFile().getUrl_file());
            }*/
        }
        public void setTvNameUser(String nameUser){
            if (tvNameUser == null)return;
            tvNameUser.setText(nameUser);
        }

        public void setTxtMessage(String message){
            if (txtMessage == null)return;
            txtMessage.setText(message);
        }

        public void setIvUser(String urlPhotoUser){
            if (ivUser == null)return;
            if( urlPhotoUser!= null &&ivUser.getContext()!=null && !urlPhotoUser.isEmpty())
            {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)mContext).isDestroyed()) {
//
//                }
                try
                {
                    Glide.with(ivUser.getContext()).load(urlPhotoUser).centerCrop().transform(new CircleTransform(ivUser.getContext())).override(40, 40).into(ivUser);
                }catch (Exception ex)
                {

                }
            }

        }

        public void setTvTimestamp(String timestamp){
            if (tvTimestamp == null)return;
            Long times= Long.parseLong(timestamp);
            Date d = new Date(times);
            DateFormat df = new SimpleDateFormat("HH:mm, MMM d");
            String reportDate = df.format(d);

            tvTimestamp.setText(reportDate);
           /* tvTimestamp.setText(converteTimestamp(timestamp));*/
        }


    }

}
