package com.sosokan.android.ui.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sosokan.android.R;
import com.sosokan.android.adapter.AdvertiseAdapter;
import com.sosokan.android.adapter.AdvertiseNewAdapter;
import com.sosokan.android.control.menu.ResideMenu;
import com.sosokan.android.events.Listener.HideShowScrollListener;
//import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.AdvertiseApi;
//import com.sosokan.android.models.Category;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.Favorite;
import com.sosokan.android.models.User;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.ui.activity.AdvertiseDetailActivity;
import com.sosokan.android.ui.activity.MenuActivity;
import com.sosokan.android.ui.activity.PrefManager;
import com.sosokan.android.ui.activity.SignUpActivity;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by AnhZin on 8/30/2016.
 */
public class FavoriteFragment extends Fragment implements View.OnClickListener {

    private View parentView;


    private static final String TAG = "FavoriteFragment";

    StaggeredGridLayoutManager mLayoutManagerAdvertiseList, mLayoutManagerAdvertiseGrid;
    private DatabaseReference mDatabase;
    RelativeLayout llContainMain;
    private final static String SAVED_ADAPTER_ITEMS = "SAVED_ADAPTER_ITEMS";
    private final static String SAVED_ADAPTER_KEYS = "SAVED_ADAPTER_KEYS";
    private ArrayList<AdvertiseApi> mAdvertiseItems;

    private AdvertiseNewAdapter mAdapterAdvertise;
    List<CategoryNew> categories;
    RecyclerView recyclerView;
    Context appContext;

    //User user;
    UserProfileApi user;
    private DatabaseReference mQueryPost;
    private ArrayList<Favorite> favorites;

    BroadcastReceiver updateReceiver;
    public final static String ACTION_UPDATE_FAVORITE = "actionUpdateFavorite";
    MenuActivity parentActivity;
    TextView tvNoResultFound;
    List<AdvertiseApi> advertiseList;
    int index = 0;
    boolean showType;
    //  private SliderLayout mDemoSlider;
    FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataRef;
    Map<String, Object> advertisesFavorites, advertisesFavoritesOriginal;

    private ValueEventListener theListener;
    //======= SCROLL
    List<String> advertiseKeys;
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
        parentView = inflater.inflate(R.layout.activity_favorite, container, false);
        initView();
        advertiseKeys = new ArrayList<>();
        mAdvertiseItems = new ArrayList<>();
        parentActivity = (MenuActivity) getActivity();
        prefManager = new PrefManager(getActivity());
        categories = prefManager.getListCategoriesApi();
        //  categories = parentActivity.getCategories();
        mPageEndOffset = 0;
        mPageLimit = 20;
//        user = parentActivity.getUser();
        prefManager = new PrefManager(getActivity());
        user = prefManager.getUserProfile();

        setupRecycleView();
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(getActivity());
        }
        handleInstanceState(savedInstanceState);
        setupFirebase();
        favorites = new ArrayList<>();
        advertisesFavorites = new HashMap<>();
        advertisesFavoritesOriginal = new HashMap<>();
        if (user != null) {
            Log.e("FAVORITE user id ", String.valueOf(user.getId()));

            //TODO GET FAVORITE
            // advertisesFavoritesOriginal = advertisesFavorites = user.getFavorites();
            Log.e(TAG, "FAVORITE advertisesFavoritesOriginal " + String.valueOf(advertisesFavoritesOriginal));
        }
        updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String advertiseId = intent.getStringExtra(Constant.ADVERTISEID);
                index = 0;
                Log.e(TAG, "updateReceiver advertiseId " + String.valueOf(advertiseId));
                //  advertiseList = new ArrayList<>(getListAd());
                //TODO PROCESS FAVORITE
                /*if (mAdvertiseItems != null && mAdvertiseItems.size() > 0) {
                    for (Advertise advertise1 : mAdvertiseItems) {
                        if (advertise1.getId().equals(advertiseId)) {
                            mAdvertiseItems.remove(advertise1);
                            Log.e(TAG, "updateReceiver  mAdvertiseItems.remove "+ String.valueOf(advertiseId));
                            break;
                        }
                    }
                    updateAdvertiseAdapterInFavorite();
                }*/

            }
        };
        parentActivity.registerReceiver(updateReceiver, new IntentFilter(ACTION_UPDATE_FAVORITE));
        return parentView;

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
    }


    // Restoring the item list and the keys of the items: they will be passed to the adapter
    private void handleInstanceState(Bundle savedInstanceState) {

        mAdvertiseItems = new ArrayList<AdvertiseApi>();
    }

    private void setupFirebase() {
        //TODO GET FAVORITES
        /*  advertisesFavoritesOriginal = user.getFavorites();
        advertisesFavorites = new HashMap<String, Object>();
        */
        initAndGetCategory();
    }

    public void initAndGetCategory() {
        if (user != null) {
            //TODO GET FAVORITE
            //   advertisesFavorites = user.getFavorites();
            mAdvertiseItems = new ArrayList<>();
            advertiseKeys = new ArrayList<>();
            if (advertisesFavorites != null && advertisesFavorites.size() > 0) {
                index = 0;
                mAdvertiseItems = new ArrayList<>();
                for (Map.Entry<String, Object> entry : advertisesFavorites.entrySet()) {
                    String key = entry.getKey();
                    //  advertiseIds.add(key);
                    advertiseKeys.add(key);
                }
                queryAdvertiseByPaging();

            } else {
                ApplicationUtils.closeMessage();
            }

        } else {
            ApplicationUtils.closeMessage();
        }
    }

    private void queryAdvertiseByPaging() {

        Log.e("queryAdvertiseByPaging ", "advertiseKeys " + String.valueOf(advertiseKeys));
        List<String> subAdvertises = null;
        if (advertiseKeys != null && advertiseKeys.size() > mPageEndOffset + mPageLimit) {
            try {
                //   subAdvertises = advertiseKeys.subList(mPageEndOffset, mPageEndOffset + mPageLimit);
                List<String> list1 = new LinkedList<>(advertiseKeys.subList(0, mPageEndOffset));
                List<String> list2 = new LinkedList<>(advertiseKeys.subList(mPageEndOffset, mPageEndOffset + mPageLimit));
                subAdvertises = list2;
            } catch (Exception e) {
                Log.e("queryAdvertiseByPaging ", "e " + String.valueOf(e));
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
                Log.e("key", key);
                final List<String> finalSubAdvertises = subAdvertises;
                //TODO PROCESS FAVORITE
                /*theListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ApplicationUtils.closeMessage();

                        try {
                            Advertise advertise = dataSnapshot.getValue(Advertise.class);
                            if (advertise != null) {
                               // Log.e(TAG,"advertise "+ String.valueOf(advertise));
                                setListAd(advertise);

                            }
                        } catch (Exception ex) {
                            Log.e("Exception", ex.toString());
                        }
                        //  Log.e("index ndw", Integer.toString(index));
                        index++;
                        if (finalSubAdvertises == null || index < finalSubAdvertises.size()) {
                            return;
                        }
                       // advertiseList = new ArrayList<>(getListAd());
                        updateAdvertiseAdapterInFavorite();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        ApplicationUtils.closeMessage();
                    }
                };
                mDataRef.child(Constant.ADVERTISES).child(key).addListenerForSingleValueEvent(theListener);*/
            }

        } else {
            Log.e(TAG, "subAdvertises === null");
        }
    }

    private void updateAdvertiseAdapterInFavorite() {

        advertiseList = new ArrayList<>(getListAd());
        Log.e(TAG, "updateAdvertiseAdapterInFavorite advertiseList " + String.valueOf(advertiseList));


        Collections.reverse(advertiseList);
        if (advertiseList != null && advertiseList.size() > 0) {
            tvNoResultFound.setVisibility(View.GONE);
            llContainMain.setVisibility(View.VISIBLE);
            mAdapterAdvertise = new AdvertiseNewAdapter(parentActivity, advertiseList, categories, user, favorites, showType);
            recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
            mAdapterAdvertise.setListener(new AdvertiseNewAdapter.Listener() {
                @Override
                public void onItemClick(int position) {
                    if (advertiseList != null && advertiseList.size() > 0 && advertiseList.size() > position) {

                        AdvertiseApi advertiseNew = advertiseList.get(position);
                        Log.e(TAG, "advertiseNew pos " + String.valueOf(position));

                        String advertiseId = advertiseList.get(position).getLegacy_id();
                        Log.e(TAG, "advNew advertiseId " + String.valueOf(advertiseId));
                        callDetailPage(advertiseNew.getLegacy_id(), advertiseNew.getCategoryId(), position);
                    }
                }
            });
            mAdapterAdvertise.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapterAdvertise);
            Log.e(TAG, "updated ");

            //   recyclerView.scrollToPosition(mScrollPosition);
        } else {
            Log.e(TAG, "tvNoResultFound ");
            tvNoResultFound.setVisibility(View.VISIBLE);
            llContainMain.setVisibility(View.GONE);
        }
    }

    private void setupRecycleView() {

        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        mAdapterAdvertise = new AdvertiseNewAdapter(parentActivity, advertiseList, categories, user, favorites, showType);
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
                            queryAdvertiseByPaging();
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
                            queryAdvertiseByPaging();
                        }
                    } else {
                        mScrollQueryInProgress = false;
                    }

                }

            }
        });

        recyclerView.setPreserveFocusAfterLayout(true);
        recyclerView.scrollToPosition(mScrollPosition);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        parentActivity.unregisterReceiver(updateReceiver);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appContext = getActivity();

    }

    @Override
    public void onClick(View v) {

    }


    public void callDetailPage(String adId, String idCategory, int index) {
        if (adId != null && !adId.isEmpty() && idCategory != null && !idCategory.isEmpty()) {


            Intent intent = new Intent(appContext, AdvertiseDetailActivity.class);

            intent.putExtra(Constant.INDEX_FRAGMENT, 3); //FAVORITE
            intent.putExtra(Constant.POSSITION, index);
            intent.putExtra(Constant.ADVERTISEID, adId);
            intent.putExtra(Constant.CATEGORYID, idCategory);
            Log.e("Favorite ", "=========== Call Ads from Favorite");
            Log.e("Favorite ", "=========== Call Ads from Favorite idCategory " + idCategory);
            Log.e("Favorite ", "=========== Call Ads from Favorite advertiseId " + adId);
            Log.e("Favorite ", "=========== Call Ads from Favorite indexAd " + index);
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }
    }


    public void showMessageError(String title, String message) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) getContext()).isDestroyed()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());

            if (dlgAlert != null)
                dlgAlert.create().dismiss();
            dlgAlert.setMessage(message);
            dlgAlert.setTitle(title);
            dlgAlert.setPositiveButton(getResources().getString(R.string.SignUp), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(getContext(), SignUpActivity.class);
                    startActivity(intent);
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


    private synchronized void setListAd(AdvertiseApi advertise) {
        if (mAdvertiseItems == null)
            mAdvertiseItems = new ArrayList<>();
        mAdvertiseItems.add(advertise);
    }

    // make sure no R/W conflict since we're getting this from a callback
    private synchronized List<AdvertiseApi> getListAd() {
        return mAdvertiseItems;
    }

}
