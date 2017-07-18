package com.sosokan.android.ui.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sosokan.android.R;
import com.sosokan.android.adapter.ConversationAdapter;
import com.sosokan.android.control.menu.ResideMenu;
import com.sosokan.android.models.Category;
import com.sosokan.android.models.Conversation;
import com.sosokan.android.models.User;
import com.sosokan.android.ui.activity.MenuActivity;
import com.sosokan.android.ui.activity.MessageActivity;
import com.sosokan.android.ui.activity.PrefManager;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AnhZin on 8/30/2016.
 */
public class ConversationFragment extends android.support.v4.app.Fragment {

    private View parentView;
    //private DatabaseReference mDatabase;
    LinearLayout llContainMain;
    private final static String SAVED_ADAPTER_ITEMS = "SAVED_ADAPTER_ITEMS";
    private final static String SAVED_ADAPTER_KEYS = "SAVED_ADAPTER_KEYS";
    private ArrayList<Conversation> mConversationItems;
    private Map<String, User> mapUser;
    private ConversationAdapter mAdapterConversation;
    List<Category> categories;
    RecyclerView recyclerView;
    Context appContext;
    FirebaseUser mUser;
    User user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // private DatabaseReference mQueryConversation;
    // private DatabaseReference mQueryUser;
    private ResideMenu resideMenu;
    TextView tvNoResultFound;
    private ValueEventListener theListener;
    private PrefManager prefManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_my_message, container, false);
        initView();
        mConversationItems = new ArrayList<>();
        mapUser = new HashMap<>();
        MenuActivity parentActivity = (MenuActivity) getActivity();
        prefManager = new PrefManager(getActivity());
        categories = prefManager.getListCategoriesFirebase();
     //   categories = parentActivity.getCategories();
        setupRecycleView();
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(getActivity());
        }
        handleInstanceState(savedInstanceState);

        setupFirebase();

        return parentView;
    }


    private void initView() {

        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        parentView.findViewById(R.id.ibMenuLeftMessage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        tvNoResultFound = (TextView) parentView.findViewById(R.id.tvNoResultFoundMessage);
    }

    // Restoring the item list and the keys of the items: they will be passed to the adapter
    private void handleInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(SAVED_ADAPTER_ITEMS) &&
                savedInstanceState.containsKey(SAVED_ADAPTER_KEYS)) {
        } else if (savedInstanceState != null) {
            // savedInstanceState.putString("categoryId", categoryId);
        } else {
            mConversationItems = new ArrayList<Conversation>();
        }
    }

    private void setupFirebase() {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();
            }
        };
        if (mUser != null) {
            if (!ApplicationUtils.isShow) {
                ApplicationUtils.showProgress(getActivity());
            }

            theListener =  new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        mapUser.put(user.getId(), user);
                     //   theListener =
                        FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(mUser.getUid()).child(Constant.CONVERSATIONS).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                                // for each group, fetch the name and print it
                                String groupKey = snapshot.getKey();

                                FirebaseDatabase.getInstance()
                                        .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.CONVERSATIONS).child(groupKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        Conversation conversation = snapshot.getValue(Conversation.class);
                                        if (conversation != null) {
                                            if (conversation.getWaitingUsers() == null) {
                                                mConversationItems.add(conversation);
                                            }

                                            FirebaseDatabase.getInstance()
                                                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.CONVERSATIONS).child(conversation.getId()).child(Constant.USERS).addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                                                    // for each group, fetch the name and print it
                                                    String groupKey = snapshot.getKey();
                                                    setListenerForConversationInUser(groupKey);
                                                }

                                                @Override
                                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                                }

                                                @Override
                                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                }

                                                @Override
                                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } else {
                        ApplicationUtils.closeMessage();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };

            FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(mUser.getUid()).addListenerForSingleValueEvent( theListener);

        }
        ApplicationUtils.closeMessage();
    }

    private void setListenerForConversationInUser(String groupKey) {
        theListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);
                mapUser.put(user1.getId(), user1);
                mAdapterConversation = new ConversationAdapter(getActivity(), mConversationItems, mapUser, user);
                mAdapterConversation.notifyDataSetChanged();
                recyclerView.setAdapter(mAdapterConversation);
                mAdapterConversation.setListener(new ConversationAdapter.Listener() {
                    @Override
                    public void onItemClick(int position) {
                        callDetailMessage(position);
                    }
                });
                if (mConversationItems != null && mConversationItems.size() > 0) {
                    tvNoResultFound.setVisibility(View.GONE);
                } else {
                    tvNoResultFound.setVisibility(View.VISIBLE);
                }
                ApplicationUtils.closeMessage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(groupKey).addListenerForSingleValueEvent(theListener);
    }


    private void setupRecycleView() {
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        mAdapterConversation = new ConversationAdapter(appContext, mConversationItems, mapUser, user);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(appContext, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapterConversation);
        mAdapterConversation.setListener(new ConversationAdapter.Listener() {
            @Override
            public void onItemClick(int position) {
                callDetailMessage(position);
            }
        });

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Conversation conversation = mConversationItems.get(position);
                if(conversation!= null && user!= null)
                {
                    Map<String, Object> waitingUsers = conversation.getWaitingUsers();
                    if (waitingUsers == null) {
                        waitingUsers = new HashMap<>();
                    }
                    long timeUpdate = DateUtils.getDateInformation();
                    waitingUsers.put(user.getId(), 0 - timeUpdate);
                    conversation.setUpdatedAt(timeUpdate);
                    conversation.setWaitingUsers(waitingUsers);
                    FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.CONVERSATIONS).child(conversation.getId()).setValue(conversation, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                            } else {
                                if (user != null) {
                                    Log.e("Delete Message","=================== SUCCESS ===============" + conversation.getId());
                                    mConversationItems.remove(position);
                                    mAdapterConversation.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
                // Do Stuff

            }

        });
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);

        /*if (mConversationItems != null && mConversationItems.size() > 0) {
            tvNoResultFound.setVisibility(View.GONE);
        }else {
            tvNoResultFound.setVisibility(View.VISIBLE);
        }*/

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // mAdapterPost.destroy();

    }


//    public DatabaseReference getmDatabase() {
//        return mDatabase;
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appContext = getActivity();

    }


    public void callDetailMessage(int position) {
        Log.e("callMessageActivity ", "fragement");
        if (mConversationItems != null && mConversationItems.size() > 0) {
            Conversation conversation = mConversationItems.get(position);
            if (conversation != null) {
                Intent intent = new Intent(appContext, MessageActivity.class);

                String idOwnerAd = "";

                if (idOwnerAd == null || idOwnerAd.isEmpty()) {

                }

                if (conversation.getPostOwnerId() != null && !conversation.getPostOwnerId().isEmpty()) {
                    idOwnerAd = conversation.getPostOwnerId();
                } else {
                    Map<String, Object> mapUser = conversation.getUsers();
                    if (mapUser != null) {
                        for (Map.Entry<String, Object> entry : mapUser.entrySet()) {
                            String userId = entry.getKey();
                            if (user != null) {
                                if (!userId.equals(user.getId())) {
                                    idOwnerAd = userId;
                                    break;
                                }
                            }
                        }
                    }
                }
                intent.putExtra(Constant.POSSITION, position);
                intent.putExtra(Constant.TITTLE_POST, conversation.getPostName());
                intent.putExtra(Constant.CATEGORYID, conversation.getPostCategoryId());
                intent.putExtra(Constant.ID_USER_OWNER, idOwnerAd);
                intent.putExtra(Constant.ADVERTISEID, conversation.getPostId());
                intent.putExtra(Constant.CONVERSATIONID, conversation.getId());
                intent.putExtra(Constant.ADVERTISE_CREATED_AT, conversation.getPostCreatedAt());
                intent.putExtra(Constant.FROM_DETAIL, false);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }

    }


}

