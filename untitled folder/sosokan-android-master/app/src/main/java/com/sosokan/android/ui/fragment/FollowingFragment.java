package com.sosokan.android.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sosokan.android.R;
import com.sosokan.android.adapter.AdvertiseFollowingAdapter;
import com.sosokan.android.control.menu.ResideMenu;
import com.sosokan.android.events.Listener.HideShowScrollListener;
import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.Category;
import com.sosokan.android.models.Favorite;
import com.sosokan.android.models.User;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.ui.activity.AdvertiseDetailActivity;
import com.sosokan.android.ui.activity.MenuActivity;
import com.sosokan.android.ui.activity.NewAdvertiseActivity;
import com.sosokan.android.ui.activity.PrefManager;
import com.sosokan.android.ui.activity.SignUpActivity;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by AnhZin on 8/30/2016.
 */
public class FollowingFragment extends Fragment implements Observer, View.OnClickListener {

    private View parentView;


    private static final String TAG = "FollowingFragment";
      RelativeLayout llContainMain;
    private ArrayList<Advertise> mAdvertiseItems;

    private AdvertiseFollowingAdapter mAdapterAdvertise;
    List<Category> categories;
    RecyclerView recyclerView;
    Context appContext;
    //User user;
    UserProfileApi user;
    private ArrayList<Favorite> favorites;
    //List<String> advertiseIds;
    List<Advertise> advertiseList;
    RecyclerView.LayoutManager mLayoutManager;
    boolean isChineseApp;
    int index = 0;
    boolean showType;
    MenuActivity parentActivity;
    StaggeredGridLayoutManager mLayoutManagerAdvertiseList, mLayoutManagerAdvertiseGrid;
    TextView tvNoResultFound;
    int count = 0;
    List<User> usersInfor;
    Map<String, String> imagesUser;
    List<String> advertiseKeys;
    private ValueEventListener theListener;
    private DatabaseReference mDataRef;
    private Map<String, Boolean> mAdvertiseKeys;

    Map<String, Object> advertisesFollowing;


    //======= SCROLL
    int mPageEndOffset, mPageLimit;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int mScrollPosition = 0;
    boolean mScrollQueryInProgress = false;
    private final static int INIT_DOWNLOADS = 10;
    private final static int MAX_AD_LIST = 100;
    private final static int DOWNLOAD_SIZE = 25;
    private final static int MIN_AD_LIST = MAX_AD_LIST - DOWNLOAD_SIZE;

    //======== MENU
    private ResideMenu resideMenu;
    private PrefManager prefManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_following, container, false);
        initView();
        mAdvertiseItems = new ArrayList<>();
        favorites = new ArrayList<>();
        //  advertiseIds = new ArrayList<>();
        usersInfor = new ArrayList<>();
        if (getContext() != null) {
            String languageToLoad = LocaleHelper.getLanguage(getContext());
            isChineseApp = languageToLoad.toString().equals("zh");
        }
        parentActivity = (MenuActivity) getActivity();
        prefManager = new PrefManager(getActivity());
        categories = prefManager.getListCategoriesFirebase();
       // categories = parentActivity.getCategories();
       // user = parentActivity.getUser();
        prefManager = new PrefManager(getActivity());
        user = prefManager.getUserProfile();

        imagesUser = new HashMap<>();
        mAdvertiseKeys = new HashMap<>();
        //  Toast.makeText(getContext(), "sortBy " + sortBy, Toast.LENGTH_LONG).show();
        advertiseKeys = new ArrayList<>();
        mAdvertiseItems = new ArrayList<>();
        advertisesFollowing = new HashMap<>();
        favorites = new ArrayList<>();
        mPageEndOffset = 0;
        mPageLimit = 20;
        mDataRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Config.FIREBASE_URL);
        setupRecycleView();
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(getActivity());
        }
        handleInstanceState(savedInstanceState);
        setupFirebase();

        return parentView;
    }

    public void onViewCreated(final View view, Bundle saved) {
        super.onViewCreated(view, saved);
        /*view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                // get width and height of the view
            }
        });*/
    }


    private void initView() {
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        parentView.findViewById(R.id.ibMenuLeftFragmentOther).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        tvNoResultFound = (TextView) parentView.findViewById(R.id.tvNoResultFound);


        llContainMain = (RelativeLayout) parentView.findViewById(R.id.llContainMain);
        tvNoResultFound.setVisibility(View.VISIBLE);
        llContainMain.setVisibility(View.GONE);
    }

    // Restoring the item list and the keys of the items: they will be passed to the adapter
    private void handleInstanceState(Bundle savedInstanceState) {

        mAdvertiseItems = new ArrayList<>();
    }

    private void setupFirebase() {
        initAndGetAdvertise();


    }

    public void initAndGetAdvertise() {

        if (user != null) {
            //TODO PROCESS FOLLOWING
            /*final Map<String, Object> subscriptions = user.getFollowingUsers();

            if (subscriptions != null) {
                for (Map.Entry<String, Object> entry : subscriptions.entrySet()) {
                    String keySubscriptions = entry.getKey();
                    Log.e("keySubscriptions ", String.valueOf(keySubscriptions));
                    String value = entry.getValue().toString();
                    // boolean isUser = Boolean.parseBoolean(value);
                    // if (isUser) {
                    FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(keySubscriptions).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            try {
                                User userOwner = dataSnapshot.getValue(User.class);
                                if (userOwner != null) {
                                    Map<String, Object> advertisesUser = userOwner.getAdvertises();
                                    advertisesFollowing.putAll(advertisesUser);
                                    for (Map.Entry<String, Object> entry : advertisesUser.entrySet()) {
                                        String key = entry.getKey();
                                        //  advertiseIds.add(key);
                                        advertiseKeys.add(key);
                                    }
                                    usersInfor.add(userOwner);
                                    if (userOwner.getAvatar() != null && userOwner.getAvatar().getImageUrl() != null
                                            && !userOwner.getAvatar().getImageUrl().isEmpty() && userOwner.getUserId() != null) {
                                        imagesUser.put(userOwner.getId(), userOwner.getAvatar().getImageUrl());
                                    }
                                }
                                // callListAdvertise();
                                queryAdvertiseByCategoryPaging();
                            } catch (Exception ex) {
                                System.out.print("ERRORR " + ex);
                            }


                            count++;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            } else {
                ApplicationUtils.closeMessage();
            }*/

        } else {
            ApplicationUtils.closeMessage();
        }
    }

    private void queryAdvertiseByCategoryPaging() {

        Log.e("queryAdvertiseByPaging ", "advertiseKeys " +String.valueOf(advertiseKeys));
        List<String> subAdvertises = null;
        if (advertiseKeys != null && advertiseKeys.size() > mPageEndOffset + mPageLimit) {
            try {
                //   subAdvertises = advertiseKeys.subList(mPageEndOffset, mPageEndOffset + mPageLimit);
                List<String> list1 = new LinkedList<>(advertiseKeys.subList(0, mPageEndOffset));
                List<String> list2 = new LinkedList<>(advertiseKeys.subList(mPageEndOffset, mPageEndOffset + mPageLimit));
                subAdvertises = list2;
            } catch (Exception e) {
                // subAdvertises = new ArrayList<>();
            }
        } else {
            if (advertiseKeys != null && mPageLimit > advertiseKeys.size()) {
                subAdvertises = advertiseKeys;
            } else {
                if (advertiseKeys != null && mPageEndOffset + mPageLimit > advertiseKeys.size() && advertiseKeys.size() > mPageEndOffset) {
//                    subAdvertises = advertiseKeys.subList(mPageEndOffset, advertiseKeys.size() - 1);
                    List<String> list1 = new LinkedList<>(advertiseKeys.subList(0, mPageEndOffset));
                    List<String> list2 = new LinkedList<>(advertiseKeys.subList(mPageEndOffset, advertiseKeys.size() - 1));
                    subAdvertises = list2;
                }
            }

        }

        if (subAdvertises != null && subAdvertises.size() > 0) {
            index = 0;
            Log.e("index", Integer.toString(index));
            for (String key : subAdvertises) {

                final List<String> finalSubAdvertises = subAdvertises;

                theListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ApplicationUtils.closeMessage();

                        try {
                            Advertise advertise = dataSnapshot.getValue(Advertise.class);
                            if (advertise != null) {

                                if (mAdvertiseKeys != null && mAdvertiseKeys.get(advertise.getId()) != null
                                        && mAdvertiseKeys.get(advertise.getId()) != null) {
                                    Log.e("mAdvertiseKeys", "double adver");
                                } else {
                                    mAdvertiseKeys.put(advertise.getId(), true);

                                    if ((advertise.isChinese() && isChineseApp)) {
                                        setListAd(advertise);
                                    } else if (!advertise.isChinese() && !isChineseApp) {
                                        setListAd(advertise);
                                    }
                                }

                            }
                        } catch (Exception ex) {
                            Log.e("Exception", ex.toString());
                        }
                        //  Log.e("index ndw", Integer.toString(index));
                        index++;
                        if (finalSubAdvertises == null || index < finalSubAdvertises.size()) {
                            return;
                        }
                        advertiseList = new ArrayList<>(getListAd());
                        updateAdvertiseAdapterInSubscription();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        ApplicationUtils.closeMessage();
                    }
                };
                mDataRef.child(Constant.ADVERTISES).child(key).addListenerForSingleValueEvent(theListener);
            }

        }
    }

    private void updateAdvertiseAdapterInSubscription() {
        if (advertiseList != null && advertiseList.size() > 0) {
            tvNoResultFound.setVisibility(View.GONE);
            llContainMain.setVisibility(View.VISIBLE);
            Collections.reverse(advertiseList);
            Log.e("imagesUser ", String.valueOf(imagesUser));
            mAdapterAdvertise = new AdvertiseFollowingAdapter(getActivity(), advertiseList, categories, user, favorites, showType, imagesUser);
            recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
            mAdapterAdvertise.setListener(new AdvertiseFollowingAdapter.Listener() {
                @Override
                public void onItemClick(int position) {
                    if (advertiseList != null && advertiseList.size() > 0 && advertiseList.size() > position) {
                        Advertise advertiseNew = advertiseList.get(position);
                        callDetailPage(advertiseNew.getId(), advertiseList.get(position).getCategoryId(), position);
                    }
                }
            });
            mAdapterAdvertise.notifyDataSetChanged();
            mLayoutManager = new GridLayoutManager(appContext, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapterAdvertise);
            recyclerView.setPreserveFocusAfterLayout(true);
            recyclerView.scrollToPosition(mScrollPosition);
        } else {
            tvNoResultFound.setVisibility(View.VISIBLE);
            llContainMain.setVisibility(View.GONE);
        }

    }

    private void setupRecycleView() {
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        mAdapterAdvertise = new AdvertiseFollowingAdapter(parentActivity, advertiseList, categories, user, favorites, showType, imagesUser);
        mLayoutManagerAdvertiseList = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManagerAdvertiseGrid = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManagerAdvertiseGrid);
        recyclerView.setAdapter(mAdapterAdvertise);

        recyclerView.addOnScrollListener(new HideShowScrollListener() {
            @Override
            public void onHide() {

            }

            @Override
            public void onShow() {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //  Log.e("...", "onScrolled!");
                visibleItemCount = mLayoutManagerAdvertiseGrid.getChildCount();
                totalItemCount = mLayoutManagerAdvertiseGrid.getItemCount();


                //   Log.e("mPageEndOffset", Integer.toString(mPageEndOffset));
                int scrollCount = mPageEndOffset == 0 ? mPageLimit : mPageEndOffset;
                if (dy > 0 && dy % 10 == 0 && advertiseKeys != null && mPageEndOffset < advertiseKeys.size()) //check for scroll down
                {
                    visibleItemCount = mLayoutManagerAdvertiseGrid.getChildCount();
                    totalItemCount = mLayoutManagerAdvertiseGrid.getItemCount();
                    int[] viewsIds = mLayoutManagerAdvertiseGrid.findFirstCompletelyVisibleItemPositions(null);
                    int[] lastViewId = mLayoutManagerAdvertiseGrid.findLastVisibleItemPositions(null);
                    pastVisiblesItems = viewsIds == null ? scrollCount : viewsIds[0] <= 0 ? scrollCount : viewsIds[0];
                    //========
                    Log.d(TAG, "total = " + totalItemCount);

                          /*  for (int id : lastViewId) {
                                Log.d(TAG, "anylastViewId = " + id);
                            }*/

                    int last = Math.max(lastViewId[0], lastViewId[1]);
                    float percent = (float) last / (float) totalItemCount;
                    Log.d(TAG, "scroll percent = " + (percent * 100));
                    if ((percent > 0.7) && (!mScrollQueryInProgress)) {

                        if (advertiseKeys != null) {
                            int position = last - DOWNLOAD_SIZE;
                            if (position < 0) {
                                position = 0;
                            }
                            mScrollPosition = position;
                            mScrollQueryInProgress = true;
                        }
                        mPageEndOffset = mPageLimit + mPageEndOffset;
                        if (mPageEndOffset < advertiseKeys.size()) {
                            queryAdvertiseByCategoryPaging();
                        }
                    } else {
                        mScrollQueryInProgress = false;
                    }

                } else if (dy < 0 && dy % 10 == 0) {        //check for scroll down
                    totalItemCount = mLayoutManagerAdvertiseGrid.getItemCount();
                    int[] lastViewId = mLayoutManagerAdvertiseGrid.findLastVisibleItemPositions(null);
                    Log.d(TAG, "total = " + totalItemCount);
                    int last = Math.min(lastViewId[0], lastViewId[1]);
                    float percent = (float) last / (float) totalItemCount;
                    Log.d(TAG, "scroll percent = " + (percent * 100));
                    if ((percent > 0.7) && (!mScrollQueryInProgress)) {

                        if (advertiseKeys != null) {
                            int position = last - DOWNLOAD_SIZE;
                            if (position < 0) {
                                position = 0;
                            }
                            mScrollPosition = position;
                            mScrollQueryInProgress = true;
                        }
                        mPageEndOffset = mPageLimit + mPageEndOffset;
                        if (mPageEndOffset < advertiseKeys.size()) {
                            queryAdvertiseByCategoryPaging();
                        }
                    } else {
                        mScrollQueryInProgress = false;
                    }

                }

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        if (parentActivity != null) {
            prefManager = new PrefManager(getActivity());
            user = prefManager.getUserProfile();

           /* user = parentActivity.getUser();*/
            initAndGetAdvertise();
        }
        //  mAdapterPost.destroy();
        // mAdapterAdvertise.des
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appContext = getActivity();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imbAdd:
                String alert;
                String message;
                if (user != null) {
                    Intent intent = new Intent(appContext, NewAdvertiseActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
        }
    }


    public void callDetailPage(String advertiseId, String idCategory, int index) {
        if (advertiseId != null && !advertiseId.isEmpty() && idCategory != null && !idCategory.isEmpty()) {


            Intent intent = new Intent(appContext, AdvertiseDetailActivity.class);

            intent.putExtra(Constant.INDEX_FRAGMENT, 3); //FAVORITE
            intent.putExtra(Constant.POSSITION, index);
            intent.putExtra(Constant.ADVERTISEID, advertiseId);
            intent.putExtra(Constant.CATEGORYID, idCategory);
            Log.e("FOLLOWING ", "=========== Call Ads from FOLLOWING");
            Log.e("FOLLOWING ", "=========== Call Ads from FOLLOWING idCategory " + idCategory);
            Log.e("FOLLOWING ", "=========== Call Ads from FOLLOWING advertiseId " + advertiseId);
            Log.e("FOLLOWING ", "=========== Call Ads from FOLLOWING indexAd " + index);
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }
    }


    public void showMessageError(String title, String message) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)getContext()).isDestroyed()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
            if (dlgAlert != null)
                dlgAlert.create().dismiss();

            dlgAlert.setMessage(message);
            dlgAlert.setTitle(title);
            dlgAlert.setPositiveButton(getResources().getString(R.string.SignUp), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(getContext(), SignUpActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }


            });
            dlgAlert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
//            }

        } catch (Exception ex) {

        }


    }

    @Override
    public void update(Observable observable, Object data) {

    }

    public void broadcastIntentSearchHome() {
        Intent intent = new Intent(MenuActivity.ACTION_CALL_SEARCH_HOME_FRAGMENT);
        appContext.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  mAdapterPost.destroy();
        // mAdapterAdvertise.des
        if (theListener != null) {
            mDataRef.removeEventListener(theListener);
        }

        Log.e("FollowingFragment ", "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //  mAdapterPost.destroy();
        // mAdapterAdvertise.des
        advertiseKeys = new ArrayList<>();

        mAdvertiseItems = new ArrayList<>();
        advertiseList = new ArrayList<>();
        if (theListener != null && mDataRef != null) {
            mDataRef.removeEventListener(theListener);
        }

        Log.e("FollowingFragment ", "onDestroyView");
    }

    private synchronized void setListAd(Advertise advertise) {
        if (mAdvertiseItems == null)
            mAdvertiseItems = new ArrayList<>();
        mAdvertiseItems.add(advertise);
    }

    // make sure no R/W conflict since we're getting this from a callback
    private synchronized List<Advertise> getListAd() {
        return mAdvertiseItems;
    }

}
