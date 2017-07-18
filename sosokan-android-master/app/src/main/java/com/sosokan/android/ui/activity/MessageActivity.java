package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sosokan.android.BuildConfig;
import com.sosokan.android.R;
import com.sosokan.android.adapter.CircleTransform;
import com.sosokan.android.adapter.MessageAdapter;
import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.Category;
import com.sosokan.android.models.Conversation;
import com.sosokan.android.models.Image;
import com.sosokan.android.models.Message;
import com.sosokan.android.models.User;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.DateUtils;
import com.sosokan.android.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

import static com.sosokan.android.utils.FireBaseUtils.generateId;

/**
 * Created by AnhZin on 9/2/2016.
 */
public class MessageActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MessageActivity.class.getSimpleName();

    private RecyclerView mChatRecyclerView;
    private ChildEventListener mMessageChatListener;
    /* unique Firebase ref for this chat */
    // private DatabaseReference mQueryMessage;
    Query queryMessage, queryConversation, queryuserUserOwnerAdvertise, queryUserOwnerAdvertise;
    FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    User user, userUserOwnerAdvertise;
    //==================
    // ChatFirebaseAdapter adapterMessage;
    MessageAdapter adapterMessage;
    private ArrayList<Message> messageMessageList;
    private DatabaseReference mDatabase;
    private ArrayList<String> mAdapterKeys;
    // private UserModel userModel;
    private LinearLayoutManager mLinearLayoutManager;
    int position;
    String idUserOwnerAdvertise;
    boolean isChineseApp;
    boolean fromDetail;
    String advertiseId, conversationId, categoryId;
    String tittlePost;
    Conversation conversation;
    long advertiseCreatedAt;
    Advertise advertise;

    TextView tvOwnerAdvertise;
    TextView tvTittleAdvertiseMessage;
    ImageButton ibSendMessage;
    RelativeLayout llCoverMessage;
    TextView tvCreatedDateMassage;

    ImageView ivUserOwner;
    Map<String, Object> mapMessages;
    Image iconCategory;
    ImageView ivCoverMessage;
    int indexAdvertise, indexFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }*/
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_messages);
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(MessageActivity.this);
        }
        messageMessageList = new ArrayList<Message>();
        mAdapterKeys = new ArrayList<String>();
        mapMessages = new HashMap<>();
        String languageToLoad = LocaleHelper.getLanguage(MessageActivity.this);
        isChineseApp = languageToLoad.toString().equals("zh");
        if (getIntent() != null) {
            idUserOwnerAdvertise = getIntent().getStringExtra(Constant.ID_USER_OWNER);
            fromDetail = getIntent().getBooleanExtra(Constant.FROM_DETAIL, false);

            position = getIntent().getIntExtra(Constant.POSSITION, -1);
            conversationId = getIntent().getStringExtra(Constant.CONVERSATIONID);
            tittlePost = getIntent().getStringExtra(Constant.TITTLE_POST);
            advertiseId = getIntent().getStringExtra(Constant.ADVERTISEID);
            advertiseCreatedAt = getIntent().getLongExtra(Constant.ADVERTISE_CREATED_AT, 0);
            advertiseId = getIntent().getStringExtra(Constant.ADVERTISEID);
            categoryId = getIntent().getStringExtra(Constant.CATEGORYID);
            if (idUserOwnerAdvertise != null) {
                Log.e("idUserOwnerAdvertise ", idUserOwnerAdvertise);
            }
            Log.e("fromDetail ", String.valueOf(fromDetail));
            Log.e("position ", String.valueOf(position));

            Log.e("conversationId ", String.valueOf(conversationId));
            Log.e("tittlePost ", String.valueOf(tittlePost));

            Log.e("advertiseId ", String.valueOf(advertiseId));
            Log.e("advertiseCreatedAt ", String.valueOf(advertiseCreatedAt));

        }
        initView();
        setupChat();
        handleInstanceState(savedInstanceState);
        setupFirebase();


    }

    private void initView() {
        if (user != null)
            setTitle("Chatting as " + user.getUserName());

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.edtChatMessage);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });
        ibSendMessage = (ImageButton) findViewById(R.id.ibSendMessage);
        ibSendMessage.setOnClickListener(this);
        llCoverMessage = (RelativeLayout) findViewById(R.id.llCoverMessage);
        tvCreatedDateMassage = (TextView) findViewById(R.id.tvCreatedDateMassage);

        findViewById(R.id.ibBackMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToPostDetail();
            }
        });

        if (conversationId != null && !conversationId.isEmpty()) {
            ibSendMessage.setEnabled(false);
        }
        tvTittleAdvertiseMessage = (TextView) findViewById(R.id.tvTittleAdvertiseMessage);
        tvTittleAdvertiseMessage.setText(tittlePost);
        tvOwnerAdvertise = (TextView) findViewById(R.id.tvOwnerAdvertise);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        ivUserOwner = (ImageView) findViewById(R.id.ivUserOwner);
        ivCoverMessage = (ImageView) findViewById(R.id.ivCoverMessage);
        ivCoverMessage.setOnClickListener(this);
        llCoverMessage.setOnClickListener(this);

    }

    public void backToPostDetail() {
        if (fromDetail) {
            gotoAdvertiseDetail();
        } else {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("Fragment", "Message");
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.start_activity_left_to_right, 0);
            finish();
        }
    }

    public ArrayList<String> getKeysArray() {
        return mAdapterKeys;
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    private void handleInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            savedInstanceState.putInt(Constant.INDEX_FRAGMENT, indexFragment);
            savedInstanceState.putString(Constant.ID_USER_OWNER, idUserOwnerAdvertise);
            savedInstanceState.putBoolean(Constant.FROM_DETAIL, fromDetail);
            savedInstanceState.putInt(Constant.POSSITION, position);
            savedInstanceState.putString(Constant.CONVERSATIONID, conversationId);
            savedInstanceState.putString(Constant.TITTLE_POST, tittlePost);
            savedInstanceState.putString(Constant.ADVERTISEID, advertiseId);
            savedInstanceState.putLong(Constant.ADVERTISE_CREATED_AT, advertiseCreatedAt);
            savedInstanceState.putString(Constant.CATEGORYID, categoryId);

        } else {
            messageMessageList = new ArrayList<Message>();
            mAdapterKeys = new ArrayList<String>();
            mapMessages = new HashMap<>();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.putBoolean(Constant.IS_CHINESE_APP, isChineseApp);
            handleInstanceState(savedInstanceState);
            Log.e("Mess SaveInstance", "===================");
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            isChineseApp = savedInstanceState.getBoolean(Constant.IS_CHINESE_APP);
            idUserOwnerAdvertise = savedInstanceState.getString(Constant.ID_USER_OWNER);
            fromDetail = savedInstanceState.getBoolean(Constant.FROM_DETAIL);
            position = savedInstanceState.getInt(Constant.POSSITION);
            conversationId = savedInstanceState.getString(Constant.CONVERSATIONID);
            tittlePost = savedInstanceState.getString(Constant.TITTLE_POST);
            advertiseId = savedInstanceState.getString(Constant.ADVERTISEID);
            advertiseCreatedAt = savedInstanceState.getLong(Constant.ADVERTISE_CREATED_AT);
            categoryId = savedInstanceState.getString(Constant.CATEGORYID);
            indexFragment = savedInstanceState.getInt(Constant.INDEX_FRAGMENT, 0);
            Log.e("Mess RestoreInstance", "===================");

        } else {
            messageMessageList = new ArrayList<Message>();
            mAdapterKeys = new ArrayList<String>();
            mapMessages = new HashMap<>();
        }

    }

    private void setupFirebase() {

        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        getInformationAdvertise();

        if (mUser != null) {
            mDatabase.child(Constant.USERS).child(mUser.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                Log.e("User ", "SUCCESS");
                                getInformationUserOwnerAdvertise();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }


    }

    public void getInformationUserOwnerAdvertise() {
        if (idUserOwnerAdvertise != null && !idUserOwnerAdvertise.isEmpty()) {

            mDatabase.child(Constant.USERS).child(idUserOwnerAdvertise).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userUserOwnerAdvertise = dataSnapshot.getValue(User.class);
                            if (userUserOwnerAdvertise != null) {
                                Log.e("User Owner", "SUCCESS");
                                checkAndCreateInformationConversation();
                                tvOwnerAdvertise.setText(userUserOwnerAdvertise.getUserName());
                                if (userUserOwnerAdvertise.getAvatar() != null && userUserOwnerAdvertise.getAvatar().getImageUrl() != null && !userUserOwnerAdvertise.getAvatar().getImageUrl().isEmpty()) {
                                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !(MessageActivity.this).isDestroyed()) {

                                    }*/
                                    try {
                                        Glide.with(ivUserOwner.getContext()).load(userUserOwnerAdvertise.getAvatar().getImageUrl()).centerCrop()
                                                .transform(new CircleTransform(ivUserOwner.getContext())).override(40, 40).into(ivUserOwner);
                                    } catch (Exception ex) {

                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
        ApplicationUtils.closeMessage();
    }

    @Override
    protected void onDestroy() {
        Glide.with(getApplicationContext()).pauseRequests();
        Glide.get(getApplicationContext()).clearMemory();
        super.onDestroy();
    }

    public void getInformationAdvertise() {
        if (advertiseId != null && !advertiseId.isEmpty()) {
            mDatabase.child(Constant.ADVERTISES).child(advertiseId).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            advertise = dataSnapshot.getValue(Advertise.class);
                            if (advertise != null) {
                                if (tittlePost == null || tittlePost.isEmpty()) {
                                    if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty() && advertise.getName().contains("-")) {
                                        String[] separated = advertise.getName().toString().split("-");
                                        String stChinese = separated != null && separated.length > 0 ? separated[0] : "";
                                        String stEnglish = separated != null && separated.length > 1 ? separated[1] : "";

                                        if (isChineseApp) {
                                            tvTittleAdvertiseMessage.setText(stChinese);
                                        } else {
                                            tvTittleAdvertiseMessage.setText(stEnglish);
                                        }
                                    } else if (advertise.getName() != null) {
                                        tvTittleAdvertiseMessage.setText(advertise.getName());
                                    }
                                }
                                if (advertiseCreatedAt == 0) {
                                    tvCreatedDateMassage.setText(DateUtils.toDate(advertise.getCreatedAt() * 1000));
                                } else {
                                    tvCreatedDateMassage.setText(DateUtils.toDate(advertiseCreatedAt * 1000));
                                }
                                if (advertise.getImageHeader() != null && advertise.getImageHeader().getImageUrl() != null && !advertise.getImageHeader().getImageUrl().isEmpty()) {
                                    setCoverAdvertise(advertise.getImageHeader().getImageUrl());
                                } else if (advertise != null && advertise.getCategoryId() != null && !advertise.getCategoryId().isEmpty()) {
                                    categoryId = advertise.getCategoryId();
                                    queryCategory();
                                }


                            } else {
                                tvTittleAdvertiseMessage.setText("");
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
        ApplicationUtils.closeMessage();
    }

    private void queryCategory() {
        mDatabase.child(Constant.CATEGORIES).child(advertise.getCategoryId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Category category = dataSnapshot.getValue(Category.class);
                    if (category != null) {
                        if (isChineseApp && category.getIcons().getIconChinese() != null) {
                            iconCategory = category.getIcons().getIconChinese();
                            if (iconCategory.getImageUrl() != null && !iconCategory.getImageUrl().isEmpty()) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) llCoverMessage.getContext()).isDestroyed()) {
//
//                                }
                                try {
                                    Glide.with(llCoverMessage.getContext()).load(iconCategory.getImageUrl()).asBitmap().into(new SimpleTarget<Bitmap>(llCoverMessage.getWidth(), llCoverMessage.getHeight()) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                            Drawable drawable = new BitmapDrawable(resource);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                llCoverMessage.setBackground(drawable);
                                            }
                                        }
                                    });
                                } catch (Exception ex) {

                                }
                            }

                        } else {
                            iconCategory = category.getIcons().getIconEnglish();
                            if (iconCategory.getImageUrl() != null && !iconCategory.getImageUrl().isEmpty()) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) llCoverMessage.getContext()).isDestroyed()) {
//
//                                }
                                try {
                                    Glide.with(llCoverMessage.getContext()).load(iconCategory.getImageUrl()).asBitmap().into(new SimpleTarget<Bitmap>(llCoverMessage.getWidth(), llCoverMessage.getHeight()) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                            Drawable drawable = new BitmapDrawable(resource);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                llCoverMessage.setBackground(drawable);
                                            }
                                        }
                                    });
                                } catch (Exception ex) {

                                }
                            }
                        }

                    }
                } catch (Exception ex) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void checkAndCreateInformationConversation() {
        if (conversationId == null || conversationId.isEmpty()) {
            initConversation();

        } else {
            queryConversation();
        }

        ApplicationUtils.closeMessage();
    }

    private void initConversation() {
        String generationId = generateId();
        conversation = new Conversation();
        conversation.setId(generationId);
        conversation.setConversationId(generationId);
        conversation.setPostId(advertiseId);
        conversation.setPostName(tittlePost);
        Long tsLong = System.currentTimeMillis() / 1000;
        conversation.setCreatedAt(tsLong);
        conversation.setUpdatedAt(tsLong);
        conversation.setPostCategoryId(categoryId);
        conversation.setLastMessageTimestamp(tsLong);

        Map<String, Object> mapUser = new HashMap<>();

        if (user != null) {

            if(user.getId()!=null && !user.getId().isEmpty())
            {
                conversation.setCreatedBy(user.getId());
                mapUser.put(user.getId(), true);
            }

            conversation.setUsers(mapUser);
            if (idUserOwnerAdvertise != null) {
                mapUser.put(idUserOwnerAdvertise, true);

            } else if (userUserOwnerAdvertise != null) {
                mapUser.put(userUserOwnerAdvertise.getId(), true);
            }
            String idConversation = conversationId == null || conversationId.isEmpty() ? (conversation != null ? conversation.getId() : conversation.getConversationId()) : conversationId;
            if (idConversation != null && !idConversation.isEmpty()) {
                Log.e("idConversation ", idConversation);
                Log.e("conversation ", String.valueOf(conversation));
                mDatabase.child(Constant.CONVERSATIONS).child(idConversation).setValue(conversation, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                        } else {
                            if (user != null) {
                                conversationId = conversation.getId();
                                EditText inputText = (EditText) findViewById(R.id.edtChatMessage);
                                String input = inputText.getText().toString();
                                if (!input.equals("")) {
                                    sendMessageToFirebase(inputText, input);
                                }
                                saveConversationForUser();
                            }
                        }
                    }
                });
            }
        }

    }

    private void queryConversation() {
        if (conversationId != null && !conversationId.isEmpty()) {
            queryConversation = mDatabase.child(Constant.CONVERSATIONS).child(conversationId);
            queryConversation.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    conversation = dataSnapshot.getValue(Conversation.class);
                    if (conversation != null && conversationId != null) {
                        if (conversation.getPostHeaderImageURL() != null && !conversation.getPostHeaderImageURL().isEmpty()) {
                            setCoverAdvertise(conversation.getPostHeaderImageURL());
                        }
                        queryMessages();

                    }
                    ibSendMessage.setEnabled(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void setCoverAdvertise(String urlCover) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !(this).isDestroyed()) {
        Log.e("setCoverAdvertise ", urlCover);
        try {
            Glide.with(llCoverMessage.getContext()).load(urlCover).asBitmap().into(new SimpleTarget<Bitmap>(llCoverMessage.getWidth(), llCoverMessage.getHeight()) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        Log.e("setCoverAdvertise ", "SUCCESS");
                        llCoverMessage.setBackground(drawable);
                    }
                }
            });
        } catch (Exception ex) {

        }
//        }
    }

    private void queryMessages() {
        queryMessage = mDatabase.child(Constant.MESSAGES).child(conversationId);
        queryMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Log.e("queryMessages ", "SUCCESS");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Message message = snapshot.getValue(Message.class);
                        if (message != null) {
                            messageMessageList.add(message);
                            mapMessages.put(message.getId(), message);
                        }

                    }
                    Log.e("queryMessages ", String.valueOf(messageMessageList));

                    mChatRecyclerView = (RecyclerView) findViewById(R.id.lvChatMessage);
                    adapterMessage = new MessageAdapter(MessageActivity.this, messageMessageList, user, userUserOwnerAdvertise);

                    adapterMessage.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeInserted(int positionStart, int itemCount) {
                            super.onItemRangeInserted(positionStart, itemCount);
                            int friendlyMessageCount = adapterMessage.getItemCount();
                            int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                            if (lastVisiblePosition == -1 ||
                                    (positionStart >= (friendlyMessageCount - 1) &&
                                            lastVisiblePosition == (positionStart - 1))) {
                                mChatRecyclerView.scrollToPosition(positionStart);
                            }
                        }
                    });
                    mChatRecyclerView.setLayoutManager(mLinearLayoutManager);
                    mChatRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mChatRecyclerView.setAdapter(adapterMessage);
                }

                // setupChat();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setupChat() {
        mChatRecyclerView = (RecyclerView) findViewById(R.id.lvChatMessage);
        adapterMessage = new MessageAdapter(this, messageMessageList, user, userUserOwnerAdvertise);
        adapterMessage.notifyDataSetChanged();
        adapterMessage.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapterMessage.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mChatRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        mChatRecyclerView.setLayoutManager(mLinearLayoutManager);
        mChatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mChatRecyclerView.setAdapter(adapterMessage);

    }

    @Override
    public void onStop() {
        super.onStop();

        //  adapterMessage.cleanUp();
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.edtChatMessage);
        String input = inputText.getText().toString();
        if (!input.equals("")) {

            if (conversation == null || conversation.getWaitingUsers() != null) {
                initConversation();
            } else {
                sendMessageToFirebase(inputText, input);
            }

        }
    }

    private void sendMessageToFirebase(EditText inputText, String input) {
        Long tsLong = System.currentTimeMillis() / 1000;
        //String ts = tsLong.toString();
        final Message message = new Message();
        if (user != null) {
            message.setUserId(user.getId());
            message.setUserDisplayName(user.getUserName());
            if (user.getAvatar() != null && user.getAvatar().getImageUrl() != null) {
                message.setImageURL(user.getAvatar().getImageUrl());
            }
            String generationId = generateId();
            message.setId(generationId);
            message.setMessageId(generationId);
            message.setCreatedAt(tsLong);
            message.setUpdatedAt(tsLong);
            message.setTimestamp(tsLong);

            message.setSeen(false);
            message.setContent(input);
            mapMessages.put(message.getId(), message);
            // Create a new, auto-generated child of that MessageMessage location, and save our MessageMessage data there
            String idConversation = conversationId == null || conversationId.isEmpty() ? (conversation != null ? conversation.getId() : conversation.getConversationId()) : conversationId;

            if (idConversation != null && !idConversation.isEmpty()) {
                message.setConversationId(idConversation);
                mDatabase.child(Constant.MESSAGES).child(idConversation).setValue(mapMessages, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                        } else {
                            saveConversation(message);
                        }
                    }
                });
                inputText.setText("");
                messageMessageList.add(message);
                mChatRecyclerView = (RecyclerView) findViewById(R.id.lvChatMessage);
                adapterMessage = new MessageAdapter(getApplicationContext(), messageMessageList, user, userUserOwnerAdvertise);

                adapterMessage.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        int friendlyMessageCount = adapterMessage.getItemCount();
                        int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                        if (lastVisiblePosition == -1 ||
                                (positionStart >= (friendlyMessageCount - 1) &&
                                        lastVisiblePosition == (positionStart - 1))) {
                            mChatRecyclerView.scrollToPosition(positionStart);
                        }
                    }
                });
                adapterMessage.notifyDataSetChanged();
                mChatRecyclerView.setLayoutManager(mLinearLayoutManager);
                mChatRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mChatRecyclerView.setAdapter(adapterMessage);
            }
        }

    }

    public void saveConversation(Message message) {
        if (conversation != null) {
            Long tsLong = System.currentTimeMillis() / 1000;
            conversation.setLastMessageId(message.getId());
            conversation.setLastMessageTimestamp(message.getTimestamp());
            conversation.setLastMessageSentBy(message.getUserDisplayName());
            conversation.setLastMessageContent(message.getContent());
            if (advertise != null) {
                conversation.setPostCreatedAt(advertise.getCreatedAt());
            }
            if (conversation.getPostCategoryId() == null || conversation.getPostCategoryId().isEmpty()) {
                conversation.setPostCategoryId(categoryId);
            }
            Map<String, Object> mapUnreadMess;
            mapUnreadMess = conversation.getUnreadLastMessageUsers();
            if (mapUnreadMess == null) {
                mapUnreadMess = new HashMap<>();

            }
            mapUnreadMess.put(idUserOwnerAdvertise, true);
            conversation.setUnreadLastMessageUsers(mapUnreadMess);
            // String imgUrl = advertise.getImageHeader() == null ? null : advertise.getImageHeader().getImageUrl();
            if (conversation.getPostHeaderImageURL() == null || conversation.getPostHeaderImageURL().isEmpty()) {
                if (advertise.getImageHeader() != null && advertise.getImageHeader().getImageUrl() != null && !advertise.getImageHeader().getImageUrl().isEmpty()) {
                    conversation.setPostHeaderImageURL(advertise.getImageHeader().getImageUrl());
                } else if (iconCategory != null && iconCategory.getImageUrl() != null && !iconCategory.getImageUrl().isEmpty()) {
                    conversation.setPostHeaderImageURL(iconCategory.getImageUrl());
                }
            }
            if (conversation.getPostId() == null || conversation.getPostId().isEmpty() && advertise != null) {
                conversation.setPostId(advertise.getId());
            }

            conversation.setUpdatedAt(tsLong);
            String idConversation = conversationId == null || conversationId.isEmpty() ? (conversation != null ? conversation.getId() : conversation.getConversationId()) : conversationId;

            if (idConversation != null && !idConversation.isEmpty()) {
                mDatabase.child(Constant.CONVERSATIONS).child(idConversation).setValue(conversation, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                        } else {
                            if (user != null) {
                                saveConversationForUser();
                            }
                        }
                    }
                });
            }
        }
    }

    public void saveConversationForUser() {
        Map<String, Object> mapConversationOfUserCurrent;
        String idConversation = conversationId == null || conversationId.isEmpty() ? (conversation != null ? conversation.getId() : conversation.getConversationId()) : conversationId;
        if (user != null && idConversation != null && !idConversation.isEmpty()) {
            mapConversationOfUserCurrent = user.getConversations();
            if (mapConversationOfUserCurrent == null) {
                mapConversationOfUserCurrent = new HashMap<>();
            }
            mapConversationOfUserCurrent.put(idConversation, true);
            user.setConversations(mapConversationOfUserCurrent);
            PrefManager prefManager = new PrefManager(getApplicationContext());
            prefManager.setUser(user);
            mDatabase.child(Constant.USERS).child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                    } else {

                    }
                }
            });

        }


        Map<String, Object> mapConversationOfUserOwner;
        if (userUserOwnerAdvertise != null) {
            mapConversationOfUserOwner = userUserOwnerAdvertise.getConversations();
            if (mapConversationOfUserOwner == null) {
                mapConversationOfUserOwner = new HashMap<>();
            }

            if (idConversation != null && !idConversation.isEmpty()) {
                mapConversationOfUserOwner.put(idConversation, true);
                userUserOwnerAdvertise.setConversations(mapConversationOfUserOwner);
                mDatabase.child(Constant.USERS).child(userUserOwnerAdvertise.getId()).setValue(userUserOwnerAdvertise, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                        } else {

                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibSendMessage:
                sendMessage();
                break;
            case R.id.llCoverMessage:
                gotoAdvertiseDetail();
                break;
        }
    }

    private void gotoAdvertiseDetail() {

        String idCate = "";
        if (categoryId == null && advertise != null) {

            idCate = advertise.getCategoryId();
        } else if (!categoryId.isEmpty()) {

            idCate = categoryId;
        }
        if (advertiseId != null && !advertiseId.isEmpty() && idCate != null && !idCate.isEmpty() && conversationId != null && !conversationId.isEmpty()) {
            Intent intent = new Intent(this, AdvertiseDetailApiActivity.class);
            intent.putExtra("Position", position);
            intent.putExtra(Constant.ADVERTISEID, advertiseId);
            intent.putExtra(Constant.CATEGORYID, idCate);
            intent.putExtra(Constant.INDEX_FRAGMENT, indexFragment);
            intent.putExtra(Constant.FROM_MESSAGE, true);
            intent.putExtra(Constant.CONVERSATIONID, conversationId);
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.start_activity_left_to_right, 0);
            finish();
        }
    }
}
