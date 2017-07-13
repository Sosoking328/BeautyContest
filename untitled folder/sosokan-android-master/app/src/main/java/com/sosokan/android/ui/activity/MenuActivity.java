package com.sosokan.android.ui.activity;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.sosokan.android.BaseApp;
import com.sosokan.android.R;
import com.sosokan.android.adapter.AdvertiseNewAdapter;
import com.sosokan.android.adapter.CategoryPopularAdapter;
import com.sosokan.android.adapter.CircleTransform;
import com.sosokan.android.adapter.CitiesAdapter;
import com.sosokan.android.adapter.ListCategoryApiAdapter;
import com.sosokan.android.adapter.PagerAdapter;
import com.sosokan.android.control.RoundedImageView;
import com.sosokan.android.control.menu.ResideMenu;
import com.sosokan.android.control.menu.ResideMenuItem;
import com.sosokan.android.control.multi.level.menu.DataProviderCategoryApi;
import com.sosokan.android.control.multi.level.menu.ItemInfo;
import com.sosokan.android.control.multi.level.menu.MultiLevelListView;
import com.sosokan.android.control.multi.level.menu.OnItemCategoryClickListener;
import com.sosokan.android.events.Listener.HideShowScrollListener;
import com.sosokan.android.models.AdListApi;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.CategoryPopularCustomize;
import com.sosokan.android.models.City;
import com.sosokan.android.models.User;
import com.sosokan.android.models.UserInformationTokenApi;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.models.UserSelection;
import com.sosokan.android.mvp.advertise.AdvertisePresenter;
import com.sosokan.android.mvp.banner.BannerPresenter;
import com.sosokan.android.mvp.category.CategoryPresenter;
import com.sosokan.android.mvp.category.CategoryView;
import com.sosokan.android.mvp.home.HomePresenter;
import com.sosokan.android.mvp.home.HomeView;
import com.sosokan.android.mvp.userProfile.UserProfileResponse;
import com.sosokan.android.mvp.userProfile.UserProfileView;
import com.sosokan.android.networking.Service;
import com.sosokan.android.rest.UrlEndpoint;
import com.sosokan.android.ui.fragment.FavoriteFragment;
import com.sosokan.android.ui.fragment.HomeFragment;
import com.sosokan.android.ui.fragment.ConversationFragment;
import com.sosokan.android.ui.fragment.MyPostFragment;
import com.sosokan.android.ui.fragment.ProfileFragment;
import com.sosokan.android.ui.fragment.FollowingFragment;
import com.sosokan.android.ui.view.DialogCities;
import com.sosokan.android.ui.view.DialogSearchAdvance;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.DateUtils;
import com.sosokan.android.utils.GPSTracker;
import com.sosokan.android.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.crashlytics.android.Crashlytics;
import com.sosokan.android.utils.PermissionGrantedHelper;
import com.sosokan.android.utils.comparator.CategoryApiPopularComparator;
import com.sosokan.android.utils.comparator.CategoryApiSortComparator;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

/**
 * Created by AnhZin on 8/29/2016.
 */
public class MenuActivity extends BaseApp implements View.OnClickListener, BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener, UserProfileView, CategoryView {
    private static final String TAG = "MenuActivity";
    private static final String CHOOSE_LANGUAGE = "Choose.Language";
    private MenuActivity mContext;


    FrameLayout main_fragment;
    FirebaseUser mUser;
    private FirebaseAuth mAuth;
    Activity activity;

    public final static String ACTION_UPDATE_FAVORITE = "actionUpdateFavorite";
    public final static String ACTION_CALL_HOME_FRAGMENT = "callHome";
    public final static String ACTION_CALL_SEARCH_HOME_FRAGMENT = "callSearchHomeFragment";

    private final static String SAVED_ADAPTER_ITEMS = "SAVED_ADAPTER_ITEMS";
    private final static String SAVED_ADAPTER_KEYS = "SAVED_ADAPTER_KEYS";

    private final static String ACTION_WATCH_OF_SERVICE = "ACTION_WATCH_OF_SERVICE";
    //-----------------

    private DatabaseReference mDataRef;
    private FirebaseAnalytics mFirebaseAnalytics;

    String fragmentName;
    private HashMap<FirebaseDatabase, ValueEventListener> mListenerMap;

    //=================================================================== HOME PAGE
    RelativeLayout rlMainHome;
    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView ibMenuLeft, ibMenuRight;
    // LinearLayout contentRight;
    boolean showCategory = false;
    // MENU
    TextView tvTitleCategoryHome;
    LinearLayout llCategory;
    // Toolbar mToolbarHome;

    TextView edtSearchAdvertiseHome;
    boolean isChineseApp;
    //------------------
    // CATEGORY API
    public CategoryNew categorySelected;
    public String categorySelectedId = "";
    public int categorySelectedIdInt;
    CategoryNew categoryNew;
    // Map<String, CategoryNew> categoryMap;
    public CategoryNew categoryAll = null;
    private MultiLevelListView mListView;
    public List<CategoryNew> categories, categoriesChild, categoriesTabhost;
    public static Map<String, List<String>> mapCategoryChildren;
    public static Map<String, CategoryNew> mapCategory;
    ListCategoryApiAdapter listAdapter;

    // QUERY ADVERTISE WITH DISTANCE

    GPSTracker tracker;
    UrlEndpoint urlEndpoint;
    RecyclerView recyclerViewDistance;
    TextView tvTitleDistanceHome;

    //----- FILTER MENU
    ImageButton ibSearch;

    ImageButton ibViewHomeFragment;

    BroadcastReceiver updateReceiver;
    BroadcastReceiver updateReceiverCity;
    BroadcastReceiver updateReceiverUnselectCity;
    BroadcastReceiver updateReceiverUnselectSearchAdvance;
    public final static String ACTION_SEARCH_ADVANCE = "actionSearchAdvance";
    public final static String ACTION_UNSELECT_SEARCH_ADVANCE = "actionUnselectAdvanceSearch";
    public final static String ACTION_SELECT_CITY = "actionSelectCity";
    public final static String ACTION_UNSELECT_CITY = "actionUnselectCity";
    // CITIES
    ImageButton ibCities;
    //    TextView tvCities;
    LinearLayout llCities;
    RecyclerView rvCities;
    CitiesAdapter citiesAdapter;
    List<City> cities;

    // SEACH ADVANCE
    int distanceMax;
    int distanceMin;
    int priceMax;
    int priceMin;
    int dayMax;
    int dayMin;
    City city;
    boolean isAllDay;
    boolean isAllowDistance;
    boolean isAllPrice;
    String textSearch;
    boolean isSearchAdvance = false;

    private Animation animUp;
    private Animation animDown;
    private Animation animLeft;
    private Animation animRight;
    boolean isAdvance;

    public boolean showTypeGrid = true;
    CheckBox chbGridList;


    //------------------

    public String categoryAllId = "sosokanCategoryAll";
    //  Category categorySelected;
    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemMessages;
    private ResideMenuItem itemMyPosts;
    private ResideMenuItem itemFavorites;
    private ResideMenuItem itemSubscriptions;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemLogout;


    //LinearLayout searchHome;
    LinearLayout llTabHost;


    //========


    String idCategory;
    PermissionGrantedHelper permissionGrantedHelper;
    UserProfileApi user;
    List<String> categoryPopularIds;
    HorizontalScrollView hsvTabMenu;
    ViewPagerAdapter viewPagerAdapter;
    TextView tvAddCategory;


    PrefManager prefManager;

    //========= CATEGORY POPULAR
    CategoryPopularAdapter categoryPopularAdapter;
    RecyclerView rvCategoryPopular;
    List<CategoryPopularCustomize> categoryPopularCustomizeArrayList;
    RelativeLayout rlMainHomeViewPager;
    LinearLayout llSelectCategoryPopular;
    ImageButton ibBackSelectCategoryPopular;
    TextView tvSaveSelectCategoryPopular;
    List<String> categoryIds;
    public UserSelection userSelection;


    @Inject
    public Service service;
    BannerPresenter bannerPresenter;
    CategoryPresenter categoryPresenter;
    AdvertisePresenter advertisePresenter;
    // HomePresenter homePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final long currentTime = System.currentTimeMillis();
        Log.e("currentTime onCreate", String.valueOf(currentTime));

        getDeps().injectMenu(this);
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.main_menu);
        initValue();
        setUpMenu();

        initView();
        confViews();

        registerBroadcast();

    }

    private void initView() {
        main_fragment = (FrameLayout) findViewById(R.id.main_fragment);
        main_fragment.setVisibility(View.VISIBLE);
        rlMainHome = (RelativeLayout) findViewById(R.id.rlMainHome);

        fragmentName = getIntent().getStringExtra(Constant.FRAGMENT);
        if (fragmentName != null) {
            processFragment();
        } else {
            gotoHomeFragment();

        }

        ibMenuRight = (ImageView) findViewById(R.id.ibMenuRight);
        ibMenuLeft = (ImageView) findViewById(R.id.ibMenuLeft);
        if (ibMenuLeft != null) {
            ibMenuLeft.setOnClickListener(this);
        }
        if (ibMenuRight != null) {
            ibMenuRight.setOnClickListener(this);
        }

        // contentRight = (LinearLayout) findViewById(R.id.contentRight);
        //===========
        findViewById(R.id.tvTitleCategoryHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showCategory = !showCategory;

                showHideDropdownCategory();
            }
        });
        findViewById(R.id.ibMenuRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                showHideViewSearch();
            }
        });
        findViewById(R.id.ibMenuLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        //  mToolbarHome = (Toolbar) findViewById(R.id.toolbar);
        llCategory = (LinearLayout) findViewById(R.id.llCategory);


        tvTitleCategoryHome = (TextView) findViewById(R.id.tvTitleCategoryHome);
        tvTitleCategoryHome.setText(getResources().getString(R.string.all));

        //======== FILTER MENU
        ibSearch = (ImageButton) findViewById(R.id.ibSearch);
        ibSearch.setOnClickListener(this);
        ibViewHomeFragment = (ImageButton) findViewById(R.id.ibViewHomeFragment);
        ibViewHomeFragment.setOnClickListener(this);


        //======= CITIES
       /* tvCities = (TextView)  findViewById(R.id.tvCities);
        tvCities.setOnClickListener(this);*/
        ibCities = (ImageButton) findViewById(R.id.ibCities);
        ibCities.setOnClickListener(this);

        llCities = (LinearLayout) findViewById(R.id.llCities);
        rvCities = (RecyclerView) findViewById(R.id.rvCities);

        //======= VIEW PAGER

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        hsvTabMenu = (HorizontalScrollView) findViewById(R.id.hsvTabMenu);

        //================= ADD CATEGORY POPULAR
        tvAddCategory = (TextView) findViewById(R.id.tvAddCategory);
        tvAddCategory.setOnClickListener(this);
        rlMainHomeViewPager = (RelativeLayout) findViewById(R.id.rlMainHomeViewPager);
        llSelectCategoryPopular = (LinearLayout) findViewById(R.id.llSelectCategoryPopular);
        ibBackSelectCategoryPopular = (ImageButton) findViewById(R.id.ibBackSelectCategoryPopular);
        ibBackSelectCategoryPopular.setOnClickListener(this);
        tvSaveSelectCategoryPopular = (TextView) findViewById(R.id.tvSaveSelectCategoryPopular);
        tvSaveSelectCategoryPopular.setOnClickListener(this);
    }

    private void initValue() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        try {
            if (getApplicationContext() != null) {
                String languageToLoad = LocaleHelper.getLanguage(getApplicationContext());
                isChineseApp = languageToLoad.toString().equals("zh");

            }
        } catch (Exception ex) {
            System.out.println("languageToLoad failed: " + ex.getMessage());
        }
        mListenerMap = new HashMap<>();


        mContext = this;
        activity = this;

        mDataRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Config.FIREBASE_URL);
        prefManager = new PrefManager(this);
        user = prefManager.getUserProfile();
        //====================================== HOME PAGE
        if (getApplicationContext() != null) {
            String languageToLoad = LocaleHelper.getLanguage(getApplicationContext());
            isChineseApp = languageToLoad.toString().equals("zh");
            Log.e("isChineseApp", String.valueOf(isChineseApp));
        }
        resideMenu = new ResideMenu(this, "", "");
        categories = new ArrayList<>();
        mapCategory = new HashMap<>();
        categoriesTabhost = new ArrayList<>();
        categoriesChild = new ArrayList<>();

        if (permissionGrantedHelper == null)
            permissionGrantedHelper = new PermissionGrantedHelper(this);
        if (permissionGrantedHelper.checkPermissionForMap()) {
            tracker = new GPSTracker(this);
            if (!tracker.canGetLocation()) {
                tracker.showSettingsAlert();
            }
        } else {
            permissionGrantedHelper.checkAndRequestPermissionForMap();
        }
        permissionGrantedHelper = new PermissionGrantedHelper(this);
        categoryPresenter = new CategoryPresenter(service, this, this);
        categoryPresenter.getCategoryList();
        //  homePresenter = new HomePresenter(service, this, this);
        userSelection = new UserSelection();
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e("MENU ACTIVITY", "onPause");
        Intent i = new Intent(this, WatchServiceReceiver.class);

        i.setAction(ACTION_WATCH_OF_SERVICE);
        startService(i);

    }

    @Override
    protected void onDestroy() {
        if (getApplicationContext() != null) {
            Glide.with(getApplicationContext()).pauseRequests();
            Glide.get(getApplicationContext()).clearMemory();
        }
        super.onDestroy();
        Log.e("MENU ACTIVITY", "onDestroy");
        categoryPresenter.onStop();
        unregisterReceiver(updateReceiver);
        unregisterReceiver(updateReceiverCity);
        unregisterReceiver(updateReceiverUnselectCity);
        unregisterReceiver(updateReceiverUnselectSearchAdvance);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MENU ACTIVITY", "onResum");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // getIntent() should always return the most recent
        setIntent(intent);

        //I added here this part to receive the intent from onPostExecute //
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ArrayList<Integer> c = extras
                    .getIntegerArrayList("stop_route");
            for (int item : c) {
                System.out.println("The Intent is not empty: "
                        + item);
            }
        }
    }

    private void processFragment() {
        switch (fragmentName) {
            case Constant.Profile:
            case Constant.Setting:
                gotoProfile();
                break;
            case Constant.Favorite:
                gotoFavorite();
                break;
            case Constant.Message:
                gotoMessage();
                break;
            case Constant.Following:
                gotoFollowing();
                break;
            case Constant.MyPost:
                gotoMyPost();
                break;
            case Constant.HomeFragment:
                gotoHomeFragment();
                break;
            default:
                gotoHomeFragment();
                break;
        }
    }

    private void gotoMyPost() {
        String alert;
        String message;
        if (user != null) {
           /* goneContentRight();
            edtSearchAdvertiseHome.setVisibility(View.GONE);
            setMenuOtherVisibleAndHomeGone(getResources().getString(R.string.my_post_menu));*/
            changeFragment(new MyPostFragment());
        } else {
            alert = getResources().getString(R.string.we_are_sorry);
            message = getResources().getString(R.string.you_need_to_login_your_sosokan);
            showMessageError(alert, message);
        }
    }

    private void gotoHomeFragment() {
       /* Bundle bundle = new Bundle();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        changeFragment(homeFragment);*/
        rlMainHome.setVisibility(View.VISIBLE);
        main_fragment.setVisibility(View.GONE);

    }


    public void setLocale(Context context, String language) {
        if (context != null) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            context.getApplicationContext().getResources().updateConfiguration(config, null);
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

        }
    }


    public void setSaveLanguage(int position) {
        if (mContext != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putInt(CHOOSE_LANGUAGE, position);
            editor.commit();
            editor.apply();

        }
    }

    public void setLanguageByPosition(int position) {
        Intent intent = new Intent(this, MenuActivity.class);
        Log.d("setLanguageByPosition", Integer.toString(position));
        switch (position) {
            case 0:
                if (!LocaleHelper.getLanguage(mContext).equals("en")) {
                    setLocale(mContext, "en");
                    LocaleHelper.onCreate(mContext, "en");
                    intent.putExtra(Constant.FRAGMENT, Constant.Profile);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    MenuActivity.this.overridePendingTransition(R.anim.nothing, R.anim.nothing);
                    finish();
                }

                break;
            case 1:
                if (!LocaleHelper.getLanguage(mContext).equals("zh")) {
                    setLocale(mContext, "zh");
                    LocaleHelper.onCreate(mContext, "zh");
                    setSaveLanguage(position);
                    intent.putExtra(Constant.FRAGMENT, Constant.Profile);

                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    MenuActivity.this.overridePendingTransition(R.anim.nothing, R.anim.nothing);
                    finish();
                }

                break;
        }


    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.putBoolean(Constant.IS_CHINESE_APP, isChineseApp);
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            isChineseApp = savedInstanceState.getBoolean(Constant.IS_CHINESE_APP);
        }
        Log.e("MENU RestoreInstance", "===================");
    }

    private void setUpMenu() {
        String nameUser = "";
        UserInformationTokenApi userInformationTokenApi = null;
        UserProfileApi userProfileApi = null;
        //TODO EDIT API GET USER TO MVP
        PrefManager prefManager = new PrefManager(getApplication());
        if (prefManager != null && (prefManager.getUserInformationToken() != null || prefManager.getUserProfile() != null)) {
            if (prefManager.getUserInformationToken() != null) {
                userInformationTokenApi = prefManager.getUserInformationToken();
                nameUser = userInformationTokenApi.getUsername();
            }
            if (prefManager.getUserProfile() != null)
                userProfileApi = prefManager.getUserProfile();
            if (userProfileApi != null) {
                nameUser = userProfileApi.getDisplay_name();
            }
        }
        user = prefManager.getUserProfile();
        if (user != null) {


            if (user.getDisplay_name() != null && !user.getDisplay_name().isEmpty()) {
                nameUser = user.getDisplay_name();
            } else if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
                nameUser = user.getPhoneNumber();// String.format("%s %s",user.getPhoneCode(), user.getPhoneNumber());

            }
            if (user.getImage_url() != null && !user.getImage_url().isEmpty()) {
                resideMenu = new ResideMenu(this, user.getImage_url(), nameUser);

            } else {
                resideMenu = new ResideMenu(this, "", nameUser);
            }
            Log.e("user nameUser ", "=================== " + nameUser);
            View scrollViewLeftMenu = resideMenu.getLeftMenuView();
            RoundedImageView profileImage = (RoundedImageView) scrollViewLeftMenu.findViewById(R.id.profile_image_menu);

            if (profileImage != null && profileImage.getContext() != null) {
                if (user.getImage_url() != null && !user.getImage_url().isEmpty()) {

                    try {
                        Glide.with(profileImage.getContext()).load(user.getImage_url()).centerCrop()
                                .transform(new CircleTransform(profileImage.getContext())).override(40, 40).into(profileImage);
                    } catch (Exception ex) {

                    }
                } else {
                    try {
                        Glide.with(profileImage.getContext()).load(R.drawable.no_avatar).centerCrop()
                                .transform(new CircleTransform(profileImage.getContext())).override(40, 40).into(profileImage);
                    } catch (Exception ex) {

                    }
                }

            }

        } else {
            resideMenu = new ResideMenu(this, "", nameUser);
            View scrollViewLeftMenu = resideMenu.getLeftMenuView();
            RoundedImageView profileImage = (RoundedImageView) scrollViewLeftMenu.findViewById(R.id.profile_image_menu);
            if (profileImage != null && profileImage.getContext() != null) {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !mContext.isDestroyed()) {

                }*/
                try {
                    Glide.with(profileImage.getContext()).load(R.drawable.no_avatar).centerCrop()
                            .transform(new CircleTransform(profileImage.getContext())).override(40, 40).into(profileImage);
                } catch (Exception ex) {

                }

            }
        }


        resideMenu.setBackground(R.drawable.splash_tranparency);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        //  resideMenu.setScaleValue(0.6f);

        itemHome = new ResideMenuItem(this, R.drawable.ic_home_white_24dp, getResources().getString(R.string.home_menu), false, "");
        //TODO GET CONVERSATION
        /*int messageCount = user == null ? 0 : user.getConversations() == null ? 0 : user.getConversations().size();
        itemMessages = new ResideMenuItem(this, R.drawable.ic_chat_bubble_outline_black_24dp, getResources().getString(R.string.mess_menu), false, Integer.toString(messageCount));
*/
        itemMessages = new ResideMenuItem(this, R.drawable.ic_chat_bubble_outline_black_24dp, getResources().getString(R.string.mess_menu), false, "0");
        itemMyPosts = new ResideMenuItem(this, R.drawable.ic_note_add_white_24dp, getResources().getString(R.string.my_post_menu), false, "");
        itemFavorites = new ResideMenuItem(this, R.drawable.ic_favorite_border_white_30dp, getResources().getString(R.string.favorite_menu), false, "");
        itemSubscriptions = new ResideMenuItem(this, R.drawable.ic_subscriptions_white_24dp, getResources().getString(R.string.following), false, "");
        itemSettings = new ResideMenuItem(this, R.drawable.ic_settings_applications_white_24dp, getResources().getString(R.string.setting_menu), false, "");
        if (user != null) {
            itemLogout = new ResideMenuItem(this, R.mipmap.ic_log_out, getResources().getString(R.string.logout_menu), false, "");
        } else {
            itemLogout = new ResideMenuItem(this, R.mipmap.ic_log_out, getResources().getString(R.string.logout_menu), false, "");
        }


        itemHome.setOnClickListener(this);
        itemMessages.setOnClickListener(this);
        itemMyPosts.setOnClickListener(this);
        itemFavorites.setOnClickListener(this);
        itemSubscriptions.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemLogout.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemMessages, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemMyPosts, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFavorites, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSubscriptions, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogout, ResideMenu.DIRECTION_LEFT);
        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);


        // RoundedImageView profileImage = resideMenu.getProfileView();

        TextView tvNameUserMenu = resideMenu.getNameUser();
        TextView tvLikeUserMenu = resideMenu.getLikeUser();
        LinearLayout llUserProfileMenuLeft = resideMenu.getLinearLayoutUserProfile();

        if (user != null) {
            llUserProfileMenuLeft.setVisibility(View.VISIBLE);
            tvNameUserMenu.setText(nameUser);
            tvLikeUserMenu.setText(String.format(mContext.getString(R.string.class_field), user.getMyAdvertiseCount()));
        } else {
            tvNameUserMenu.setText("");
            tvLikeUserMenu.setText(String.format(mContext.getString(R.string.class_field), 0));
        }
        if (llUserProfileMenuLeft != null) {
            llUserProfileMenuLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoProfile();
                }
            });
        }

    }

    private void gotoProfile() {
        changeFragment(new ProfileFragment());
        try {
            if (resideMenu != null && resideMenu.isOpened())
                resideMenu.closeMenu();
        } catch (Exception ex) {

        }
    }

    private void gotoFavorite() {
        if (user != null) {
            changeFragment(new FavoriteFragment());
            try {
                if (resideMenu != null && resideMenu.isOpened())
                    resideMenu.closeMenu();
            } catch (Exception ex) {

            }
        } else {
            String alert = getResources().getString(R.string.we_are_sorry);
            String message = getResources().getString(R.string.you_need_to_login_your_sosokan);
            showMessageError(alert, message);
        }

    }

    private void gotoMessage() {
        if (user != null) {
            changeFragment(new ConversationFragment());
            try {
                if (resideMenu.isOpened())
                    resideMenu.closeMenu();
            } catch (Exception ex) {
            }

        } else {
            String alert = getResources().getString(R.string.we_are_sorry);
            String message = getResources().getString(R.string.you_need_to_login_your_sosokan);
            showMessageError(alert, message);
        }

    }


    private void gotoFollowing() {
        if (user != null) {
            changeFragment(new FollowingFragment());
            try {
                if (resideMenu != null && resideMenu.isOpened())
                    resideMenu.closeMenu();
            } catch (Exception ex) {
            }

        } else {
            String alert = getResources().getString(R.string.we_are_sorry);
            String message = getResources().getString(R.string.you_need_to_login_your_sosokan);
            showMessageError(alert, message);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        // return gestureDetector.onTouchEvent(ev);
        if (resideMenu != null)
            try {
                return resideMenu.dispatchTouchEvent(ev);
            } catch (Exception ex) {
                return false;
            }
        else {
            return false;
        }
    }

    public void showMessageError(String title, String message) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)getApplicationContext()).isDestroyed()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MenuActivity.this);

            if (dlgAlert != null)
                dlgAlert.create().dismiss();
            dlgAlert.setMessage(message);
            dlgAlert.setTitle(title);
            dlgAlert.setPositiveButton(getResources().getString(R.string.SignUp), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
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
    public void onClick(View view) {
        String alert;
        String message;

        if (view == itemHome) {
            //  setMenuHomeVisibleAndOtherGone();
            // changeFragment(new HomeFragment());
            //TODO SHOW HOME
            rlMainHome.setVisibility(View.VISIBLE);
            main_fragment.setVisibility(View.GONE);
            /*Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();*/
        } else if (view == itemMessages) {
            if (user != null) {
                changeFragment(new ConversationFragment());
            } else {
                alert = getResources().getString(R.string.we_are_sorry);
                message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                showMessageError(alert, message);
            }

        } else if (view == itemMyPosts) {
            gotoMyPost();

        } else if (view == itemFavorites) {
            if (user != null) {
                gotoFavorite();
            } else {
                alert = getResources().getString(R.string.we_are_sorry);
                message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                showMessageError(alert, message);
            }

        } else if (view == itemSubscriptions) {
            if (user != null) {
                //  goneContentRight();
                //  setMenuOtherVisibleAndHomeGone(getResources().getString(R.string.following));
                Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtra(Constant.FRAGMENT, Constant.Following);

                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                MenuActivity.this.overridePendingTransition(R.anim.nothing, R.anim.nothing);
                finish();
                // changeFragment(new FollowingFragment());
            } else {
                alert = getResources().getString(R.string.we_are_sorry);
                message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                showMessageError(alert, message);
            }
        } else if (view == itemSettings) {
            //  goneContentRight();
            gotoProfile();
        } else if (view == itemLogout) {
            LoginManager.getInstance().logOut();
            if (prefManager == null)
                prefManager = new PrefManager(this);
            prefManager.deleteUser();
            prefManager.deleteUserFirebase();
            prefManager.deleteUserInformationTokenApi();
            prefManager.deleteUserProfile();

            if (user != null) {
                //  goneContentRight();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }

        }
        if (resideMenu != null && resideMenu.isOpened())
            resideMenu.closeMenu();
        android.app.FragmentManager fm = getFragmentManager();
        switch (view.getId()) {


            case R.id.ibSearch:
               /* DialogSearchAdvance dialogSearchAdvance = new DialogSearchAdvance();
                dialogSearchAdvance.showDialog(this, categorySelected);*/
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                android.app.Fragment prev = getFragmentManager().findFragmentByTag("map");
                if (prev != null) {
                    ft.remove(prev);
                    ft.commit();
                }
                //  ft.addToBackStack(null);

                DialogSearchAdvance dialogSearchAdvance = DialogSearchAdvance.newInstance("dialog_search_advance", false);
                dialogSearchAdvance.show(fm, "fragment_edit_name");
                ibSearch.setEnabled(false);
                break;

            case R.id.ibViewHomeFragment:
                showTypeGrid = !showTypeGrid;

                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (showTypeGrid) {

                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ibViewHomeFragment.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_grid));
                    } else {
                        ibViewHomeFragment.setBackground(getResources().getDrawable(R.drawable.view_grid));
                    }
                } else {
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ibViewHomeFragment.setBackgroundDrawable(getResources().getDrawable(R.drawable.view_list));
                    } else {
                        ibViewHomeFragment.setBackground(getResources().getDrawable(R.drawable.view_list));
                    }
                }
                HomeFragment homeFragment = (HomeFragment) viewPagerAdapter.getItem(viewPager.getCurrentItem());
                if (homeFragment != null && homeFragment.isVisible()) {

                    homeFragment.showTypeOfAdapter();
                }
                break;

            case R.id.ibCities:
                android.app.FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                android.app.Fragment prev1 = getFragmentManager().findFragmentByTag("map");
                if (prev1 != null) {
                    ft1.remove(prev1);
                    ft1.commit();
                }

                DialogCities dialogCities = DialogCities.newInstance("Some Title");
                dialogCities.show(fm, "fragment_edit_name");
                ibCities.setEnabled(false);
               /* DialogCities dialogCities = new DialogCities();
                dialogCities.showDialog(getActivity(), getActivity().getFragmentManager());*/
                break;

            case R.id.tvAddCategory:
                rlMainHomeViewPager.setVisibility(View.GONE);
                llSelectCategoryPopular.setVisibility(View.VISIBLE);
                break;
            case R.id.ibBackSelectCategoryPopular:
                rlMainHomeViewPager.setVisibility(View.VISIBLE);
                llSelectCategoryPopular.setVisibility(View.GONE);
                break;
            case R.id.tvSaveSelectCategoryPopular:
                rlMainHomeViewPager.setVisibility(View.VISIBLE);
                llSelectCategoryPopular.setVisibility(View.GONE);

                if (categoryPopularAdapter != null) {
                    categoryPopularCustomizeArrayList = categoryPopularAdapter.categoryList;
                    categoriesTabhost = new ArrayList<>();
                    categoryIds = new ArrayList<>();
                    for (int i = 0; i < categoryPopularCustomizeArrayList.size(); i++) {
                        if (categoryPopularCustomizeArrayList.get(i).isSelected()) {
                            categoriesTabhost.add(categoryPopularCustomizeArrayList.get(i).getCategory());
                            categoryIds.add(categoryPopularCustomizeArrayList.get(i).getCategory().getLegacy_id());
                        }
                    }

                    llTabHost = (LinearLayout) findViewById(R.id.llTabHost);
                    llTabHost.removeAllViews();
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    final int width = size.x - tvAddCategory.getWidth();

                    Log.e(TAG, "width " + width);
                    hsvTabMenu.setLayoutParams(new RelativeLayout.LayoutParams(width, tvAddCategory.getHeight()));
                    tvAddCategory.setPadding(30, 15, 30, 15);
                    Gson gson = new Gson();
                    if (prefManager == null) prefManager = new PrefManager(this);
                    prefManager.setCategoriesPopular(gson.toJson(categoryPopularCustomizeArrayList));
                    if (categoryAll != null) {
                        categoriesTabhost.add(0, categoryAll);
                    }
                    initTabshot();
                }
                break;

        }
    }


    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            // Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            //  Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment) {
        if (resideMenu != null) {
            resideMenu.clearIgnoredViewList();
        }
       /* if (llCategory.getVisibility() == View.VISIBLE) {
            llCategory.setVisibility(View.GONE);
        }*/
        rlMainHome.setVisibility(View.GONE);
        main_fragment.setVisibility(View.VISIBLE);

        final Activity activity = this;
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (activity != null && !activity.isFinishing()) {
            getSupportFragmentManager().beginTransaction().remove(targetFragment).commitAllowingStateLoss();
        }
        if (getSupportFragmentManager().findFragmentById(R.id.main_fragment) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, targetFragment, Constant.FRAGMENT)
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_fragment, targetFragment)
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commitAllowingStateLoss();
        }


    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }
/*

    public User getUser() {
        return user;
    }
*/


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    void setServiceWatchdogTimer(boolean set, int timeout) {
        Intent intent;
        PendingIntent alarmIntent;
        intent = new Intent(); // forms and creates appropriate Intent and pass it to AlarmManager
        intent.setAction(ACTION_WATCH_OF_SERVICE);
        intent.setClass(this, WatchServiceReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (set)
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeout, alarmIntent);
        else
            am.cancel(alarmIntent);
    }

    @Override
    public void onUserProfileFailure(String appErrorMessage) {
        if (prefManager == null) prefManager = new PrefManager(this);
        user = prefManager.getUserProfile();
    }

    @Override
    public void getUserProfileSuccess(UserProfileResponse userProfileResponse) {
        if (userProfileResponse != null) {
            if (userProfileResponse.getResults() != null && userProfileResponse.getResults().size() > 0) {
                user = userProfileResponse.getResults().get(0);

            }

        }
    }


    public class WatchServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("onReceive  ", "onReceive");
            if (intent.getAction().equals(ACTION_WATCH_OF_SERVICE)) {
                // check your flag and
                // restart your service if it's necessary
                setServiceWatchdogTimer(true, 60000 * 5); // restart the watchdogtimer
            }
        }

    }

    //========================================================
    private void registerBroadcast() {
        updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                distanceMax = intent.getIntExtra("distanceMax", 500);
                if (distanceMax == 0) distanceMax = 500;
                distanceMin = intent.getIntExtra("distanceMin", 0);
                priceMax = intent.getIntExtra("priceMax", 0);
                priceMin = intent.getIntExtra("priceMin", 0);
                dayMax = intent.getIntExtra("dayMax", 0);
                dayMin = intent.getIntExtra("dayMin", 0);
                city = (City) intent.getSerializableExtra("citySelected");
                isAllDay = intent.getBooleanExtra("isAllDay", false);
                isAllowDistance = intent.getBooleanExtra("isAllowDistance", false);
                isAllPrice = intent.getBooleanExtra("isAllPrice", false);
                textSearch = intent.getStringExtra("textSearch");
                categorySelectedId = intent.getStringExtra("categorySelectedId");
                if (categorySelectedId != null && !categorySelectedId.isEmpty() && mapCategory != null) {
                    categorySelected = mapCategory.get(categorySelectedId);
                }

                //distance = distanceMax;

                Log.e("distanceMax", String.valueOf(distanceMax));
                Log.e("distanceMin", String.valueOf(distanceMin));
                Log.e("priceMax", String.valueOf(priceMax));
                Log.e("priceMin", String.valueOf(priceMin));
                Log.e("dayMax", String.valueOf(dayMax));
                Log.e("dayMin", String.valueOf(dayMin));
                Log.e("city", String.valueOf(city));
                Log.e("isAllDay", String.valueOf(isAllDay));
                Log.e("isAllowDistance", String.valueOf(isAllowDistance));
                Log.e("isAllPrice", String.valueOf(isAllPrice));
                Log.e("textSearch", String.valueOf(textSearch));
                Log.e("categorySelectedId", String.valueOf(categorySelectedId));
                isSearchAdvance = true;
                ibSearch.setEnabled(true);
                //TODO CHECK ADS
                HomeFragment homeFragment = (HomeFragment) viewPagerAdapter.getItem(viewPager.getCurrentItem());
                if (homeFragment != null && homeFragment.isVisible()) {
                    Log.e(TAG, "getListAdsWithParamAdvance");
                    userSelection = new UserSelection();
                    userSelection.setAllDay(isAllDay);
                    userSelection.setAllPrice(isAllPrice);
                    userSelection.setAllowDistance(isAllowDistance);
                    userSelection.setDistanceMax(distanceMax);
                    userSelection.setDistanceMin(distanceMin);
                    userSelection.setPriceMax(priceMax);
                    userSelection.setPriceMin(priceMin);
                    userSelection.setDayMax(dayMax);
                    userSelection.setDayMin(dayMin);
                    userSelection.setCity(city);
                    userSelection.setCategorySelectedId(categorySelectedId);
                    userSelection.setCategory(categorySelected);
                    userSelection.setTextSearch(textSearch);
                    homeFragment.getListAdsWithParamAdvance();
                }
                //getListAdsWithParamAdvance();
                // queryAdvertiseWithDistance();
            }
        };
        registerReceiver(updateReceiver, new IntentFilter(ACTION_SEARCH_ADVANCE));

        updateReceiverCity = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                city = (City) intent.getSerializableExtra("citySelected");
                if (city != null) {
                    Log.e(TAG, city.getCity());
                    //TODO CHECK ADS
                    HomeFragment homeFragment = (HomeFragment) viewPagerAdapter.getItem(viewPager.getCurrentItem());
                    if (homeFragment != null && homeFragment.isVisible()) {

                        homeFragment.getAdvertiseWithDistanceAdvanceParam(300, city.getLatitude(), city.getLongitude());
                    }
                    //getAdvertiseWithDistanceAdvanceParam(300, city.getLatitude(), city.getLongitude());
                }
                ibCities.setEnabled(true);
            }
        };
        registerReceiver(updateReceiverCity, new IntentFilter(ACTION_SELECT_CITY));

        updateReceiverUnselectCity = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ibCities.setEnabled(true);
            }
        };

        registerReceiver(updateReceiverUnselectCity, new IntentFilter(ACTION_UNSELECT_CITY));

        updateReceiverUnselectSearchAdvance = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ibSearch.setEnabled(true);
            }
        };

        registerReceiver(updateReceiverUnselectSearchAdvance, new IntentFilter(ACTION_UNSELECT_SEARCH_ADVANCE));
    }


    @Override
    public void onFailure(String appErrorMessage) {
        /*mapCategoryChildren = new HashMap<>();
        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        prefManager = new PrefManager(this);
        categories = prefManager.getListCategoriesApi();
        if (categories != null && categories.size() > 0) {
            categoriesTabhost = new ArrayList<>();
            categoriesChild = new ArrayList<>();
            processDataMenu();
        }*/
    }


    //========= MENU
    public void showHideViewSearch() {
       /* if (contentRight.getVisibility() == View.VISIBLE) {
            contentRight.setVisibility(View.GONE);


            ibMenuRight.setSelected(false);
        } else {
            ibMenuRight.setSelected(true);
            contentRight.setVisibility(View.VISIBLE);

            showCategory = false;
            if (llCategory.getVisibility() == View.VISIBLE) {
                llCategory.setVisibility(View.GONE);

            }
            // main_fragment.setVisibility(View.VISIBLE);
        }*/
    }

    public void showViewSearch() {
        ibMenuRight.setSelected(true);
        // contentRight.setVisibility(View.VISIBLE);
        // main_fragment.setVisibility(View.VISIBLE);
        showCategory = false;
        if (llCategory.getVisibility() == View.VISIBLE) {
            llCategory.setVisibility(View.GONE);
        }
    }


    public void setMenuHomeVisibleAndOtherGone() {
        //   mToolbarHome.setVisibility(View.VISIBLE);
        ibMenuRight.setVisibility(View.VISIBLE);
        edtSearchAdvertiseHome.setVisibility(View.VISIBLE);
        if (categorySelected != null) {
            tvTitleCategoryHome.setText(categorySelected.getName());
            if (!isChineseApp) {
                tvTitleCategoryHome.setText(categorySelected.getName());
            } else {
                if (categorySelected.getNameChinese() != null && !categorySelected.getNameChinese().isEmpty()) {
                    tvTitleCategoryHome.setText(categorySelected.getNameChinese());
                } else {
                    tvTitleCategoryHome.setText(categorySelected.getName());
                }
            }
            tvTitleCategoryHome.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_orange_24dp, 0);
        }
    }

    private void showHideDropdownCategory() {
        // setMenuHomeVisibleAndOtherGone();
        if (llCategory.getVisibility() == View.GONE) {
            llCategory.setVisibility(View.VISIBLE);
            //  searchHome.setVisibility(View.GONE);
            /*if (contentRight.getVisibility() == View.VISIBLE) {
                contentRight.setVisibility(View.GONE);
            }*/
            goneContentRight();
        } else {
            llCategory.setVisibility(View.GONE);
            sendCategoryIdToHomeFragment();


        }

    }

    private void goneContentRight() {
       /* if (contentRight.getVisibility() == View.VISIBLE) {
            contentRight.setVisibility(View.GONE);
        }*/
    }

    public void processDataMenu() {
        mapCategory = new HashMap<>();
        if (categories != null && categories.size() > 0) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i) != null && categories.get(i).getLegacy_id() != null && categories.get(i).getLegacy_id().equals(Constant.sosokanCategoryAll)) {
                    categoryAll = categories.get(i);
                    //categoryAllId = category.getLegacy_id();
                }
                if (categories.get(i) != null) {
                    mapCategory.put(String.valueOf(categories.get(i).getLegacy_id()), categories.get(i));
                }
                if (categories.get(i).getPopular() > 0) {
                    categoriesTabhost.add(categories.get(i));
                }
                if (categories.get(i) != null && categories.get(i).getName() != null && categories.get(i).getName().equals(Constant.News)) {
                    categoryNew = categories.get(i);
                }
                /*if (categories.get(i).getSort() > 0) {


                }*/
                if (categories.get(i) != null && categories.get(i).getLegacy_id() != null
                        && !categories.get(i).getLegacy_id().equals(Constant.sosokanCategoryAll)) {
                    if (categories.get(i).getParentId() == null || categories.get(i).getParentId().isEmpty() || categories.get(i).getParent() == null || categories.get(i).getLevel() == 0) {
                        categoriesChild.add(categories.get(i));
                        //  Log.e(TAG, "categories.get(i).getLegacy_id() " + categories.get(i).getLegacy_id());
                    }

                }
            }
        }
        //    categoriesTabhost.add(0, categoryAll);
        Collections.sort(categoriesTabhost, new CategoryApiPopularComparator());
        Collections.reverse(categoriesTabhost);
        Collections.sort(categoriesChild, new CategoryApiSortComparator());
        categoriesChild.add(0, categoryAll);
        /*if (categoryNew != null) {
            categoriesChild.add(0, categoryNew);
        }*/
        // List<CategoryNew> listCategoryChildren = new ArrayList<>();
        mapCategoryChildren = new HashMap<>();
        if (categories != null) {
            for (CategoryNew category : categories) {
                if (category != null && category.getLegacy_id().equals(Constant.sosokanCategoryAll)) {
                    categoryAll = category;
                    categoryAllId = category.getLegacy_id();
                    categorySelectedId = categoryAllId;
                }
                List<String> categoriesId;
                if (category.getParentId() == null) {
                    categoriesId = mapCategoryChildren.get("0");
                    if (categoriesId == null) {
                        categoriesId = new ArrayList<>();
                    }
                    categoriesId.add(category.getLegacy_id());
                    mapCategoryChildren.put("0", categoriesId);
                    //listCategoryChildren.add(category);
                } else {
                    categoriesId = mapCategoryChildren.get(category.getParentId());
                    if (categoriesId == null) {
                        categoriesId = new ArrayList<>();
                    }
                    categoriesId.add(category.getLegacy_id());
                    mapCategoryChildren.put(category.getParentId(), categoriesId);
                }
            }
        }
        DataProviderCategoryApi.initData(mapCategory, mapCategoryChildren, categories);
        //Log.e("categoryId Menu", String.valueOf(categoriesChild));
        if (categoriesChild != null && categoriesChild.size() > 0)
            listAdapter.setDataItems(categoriesChild);
        mListView.setAdapter(listAdapter);
        if (categoriesChild != null && categoriesChild.size() > 0) {
            categorySelected = categoriesChild.get(0);
        }


        if (prefManager == null) prefManager = new PrefManager(this);

        processForTabhostMenu();
        updateUIForTabMenuWhenUserClick(0);
        setupRecycleViewCategoryPopular();
        //TODO GET ADS
        //initAndGetAdvertiseInCategory();
    }

    private void processForTabhostMenu() {
        if (prefManager.getListCategoriesPopular() == null || prefManager.getListCategoriesPopular().size() == 0) {
            categoryPopularCustomizeArrayList = new ArrayList<>();
            categoryIds = new ArrayList<>();
            CategoryPopularCustomize categoryPopularCustomize;
            if (categoriesTabhost != null && categoriesTabhost.size() > 0) {
                for (int i = 0; i < categoriesTabhost.size(); i++) {
                    categoryPopularCustomize = new CategoryPopularCustomize();
                    categoryPopularCustomize.setCategory(categoriesTabhost.get(i));
                    categoryPopularCustomize.setSelected(true);
                    categoryPopularCustomizeArrayList.add(categoryPopularCustomize);
                    categoryIds.add(categoriesTabhost.get(i).getLegacy_id());
                }
                Gson gson = new Gson();
                prefManager.setCategoriesPopular(gson.toJson(categoryPopularCustomizeArrayList));
                llTabHost = (LinearLayout) findViewById(R.id.llTabHost);
                llTabHost.removeAllViews();

            }
        } else {
            categoryPopularCustomizeArrayList = prefManager.getListCategoriesPopular();

            categoriesTabhost = new ArrayList<>();
            categoryIds = new ArrayList<>();
            for (int i = 0; i < categoryPopularCustomizeArrayList.size(); i++) {
                if (categoryPopularCustomizeArrayList.get(i).isSelected()) {
                    //  categoryIds.add(categoryPopularCustomizeArrayList.get(i).getCategory().getLegacy_id());
                    categoriesTabhost.add(categoryPopularCustomizeArrayList.get(i).getCategory());
                }
            }
            Collections.sort(categoriesTabhost, new CategoryApiPopularComparator());
            Collections.reverse(categoriesTabhost);
            for (int i = 0; i < categoriesTabhost.size(); i++) {
                categoryIds.add(categoriesTabhost.get(i).getLegacy_id());
            }
            //
            llTabHost = (LinearLayout) findViewById(R.id.llTabHost);
            llTabHost.removeAllViews();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            final int width = size.x - tvAddCategory.getWidth();
            Log.e(TAG, "width " + width);

            hsvTabMenu.setLayoutParams(new RelativeLayout.LayoutParams(width, tvAddCategory.getHeight()));
            tvAddCategory.setPadding(30, 15, 30, 15);
/*
            // llTabHost
            Gson gson = new Gson();
            if (prefManager == null) prefManager = new PrefManager(this);
            prefManager.setCategoriesPopular(gson.toJson(categoryPopularCustomizeArrayList));*/
            // initTabshot();
        }
        if (categoryAll != null) {
            categoriesTabhost.add(0, categoryAll);
        }
        initTabshot();
    }

    private OnItemCategoryClickListener mOnItemClickListener = new OnItemCategoryClickListener() {

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            CategoryNew category = ((CategoryNew) item);
          /*  if(category!=null)
            {*/
            categorySelected = category;

            if (category != null) {
                categorySelectedId = category.getLegacy_id();

                // homePresenter.setCategoryChange(categorySelectedId);
                Log.e(TAG, "mOnItemClickListener  categorySelectedId = " + categorySelectedId);
                idCategory = categorySelectedId;
                callOnChangeCategoryListener(viewPager.getCurrentItem(), idCategory);
                if (categoryPopularIds.indexOf(idCategory) >= 0) {
                    updateUIForTabMenuWhenUserClick(categoryPopularIds.indexOf(idCategory));
                }
                // viewPager.setCurrentItem(categoryIds.indexOf(v.getTag().toString()));
                tvTitleCategoryHome.setText(category.getName());

                if (!isChineseApp) {
                    tvTitleCategoryHome.setText(category.getName());
                } else {
                    if (category.getNameChinese() != null && !category.getNameChinese().isEmpty()) {
                        tvTitleCategoryHome.setText(category.getNameChinese());
                    } else {
                        tvTitleCategoryHome.setText(category.getName());
                    }
                }

                if (categorySelected != null && categorySelected.getLegacy_id().equals(Constant.sosokanCategoryAll)) {
                    showHideDropdownCategory();
                } else {
                    boolean hadSub = false;
                    if (categories != null) {
                        for (CategoryNew cate : categories) {
                            if (cate.getParentId() != null && cate.getParentId().equals(categorySelectedId)) {
                                hadSub = true;
                                break;
                            }
                        }

                        if (hadSub == false) {
                            llCategory.setVisibility(View.GONE);
                            sendCategoryIdToHomeFragment();
                        }
                    }
                }
            }

            /*}*/

        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            CategoryNew category = ((CategoryNew) item);
            if (category != null) {
                categorySelectedId = category.getLegacy_id();
                idCategory = categorySelectedId;
                callOnChangeCategoryListener(viewPager.getCurrentItem(), idCategory);
                if (categoryPopularIds.indexOf(idCategory) >= 0) {
                    updateUIForTabMenuWhenUserClick(categoryPopularIds.indexOf(idCategory));
                }
                Log.e(TAG, "mOnItemClickListener  categorySelectedId = " + categorySelectedId);
                categorySelected = category;
                if (!isChineseApp) {
                    tvTitleCategoryHome.setText(category.getName());
                } else {
                    if (category.getNameChinese() != null && !category.getNameChinese().isEmpty()) {
                        tvTitleCategoryHome.setText(category.getNameChinese());
                    } else {
                        tvTitleCategoryHome.setText(category.getName());
                    }
                }
            }

            if (categorySelected != null && categorySelected.getLegacy_id().equals(Constant.sosokanCategoryAll)) {
                showHideDropdownCategory();

            } else {
                boolean hadSub = false;
                if (categories != null) {
                    for (CategoryNew cate : categories) {
                        if (cate.getParentId() != null && cate.getParentId().equals(categorySelectedId)) {
                            hadSub = true;
                            break;
                        }
                    }

                    if (hadSub == false) {
                        llCategory.setVisibility(View.GONE);
                        sendCategoryIdToHomeFragment();
                    }
                }

            }
        }
    };


    public void sendCategoryIdToHomeFragment() {
        llCategory.setVisibility(View.GONE);
        //   searchHome.setVisibility(View.VISIBLE);
        //TODO GET ADS
        // initAndGetAdvertiseInCategory();
        // queryAdvertiseByCategory();
    }

    private void confViews() {
        mListView = (MultiLevelListView) findViewById(R.id.listViewCategory);
        listAdapter = new ListCategoryApiAdapter(this);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mapCategoryChildren = new HashMap<>();
        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        categoriesTabhost = new ArrayList<>();
        categories = prefManager.getListCategoriesApi();

        //processForTabhostMenu();
        rlMainHomeViewPager.setVisibility(View.VISIBLE);
        llSelectCategoryPopular.setVisibility(View.GONE);

        if (categoryPopularAdapter != null) {
            categoryPopularCustomizeArrayList = categoryPopularAdapter.categoryList;
            categoriesTabhost = new ArrayList<>();
            categoryIds = new ArrayList<>();
            for (int i = 0; i < categoryPopularCustomizeArrayList.size(); i++) {
                if (categoryPopularCustomizeArrayList.get(i).isSelected()) {
                    categoriesTabhost.add(categoryPopularCustomizeArrayList.get(i).getCategory());
                    categoryIds.add(categoryPopularCustomizeArrayList.get(i).getCategory().getLegacy_id());
                }
            }

            llTabHost = (LinearLayout) findViewById(R.id.llTabHost);

            llTabHost.removeAllViews();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            final int width = size.x - tvAddCategory.getWidth();

            Log.e(TAG, "width " + width);
            hsvTabMenu.setLayoutParams(new RelativeLayout.LayoutParams(width, tvAddCategory.getHeight()));
            tvAddCategory.setPadding(30, 15, 30, 15);
            Gson gson = new Gson();
            if (prefManager == null) prefManager = new PrefManager(this);
            prefManager.setCategoriesPopular(gson.toJson(categoryPopularCustomizeArrayList));
            if (categoryAll != null) {
                categoriesTabhost.add(0, categoryAll);
            }
            initTabshot();
        }
    }

    private void initTabshot() {
        categoryPopularIds = new ArrayList<>();
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        llTabHost = (LinearLayout) findViewById(R.id.llTabHost);

        Log.e(TAG, "initTabshot ");
        if (prefManager == null) prefManager = new PrefManager(this);
        if (prefManager.getListCategoriesPopular() != null) {
            Log.e(TAG, "prefManager.getListCategoriesPopular() ");
            // categoryPopularCustomizeArrayList = prefManager.getListCategoriesPopular();
            for (int i = 0; i < categoriesTabhost.size(); i++) {
                CategoryNew categoryNew = categoriesTabhost.get(i);
                String nameCategory = isChineseApp ? categoryNew.getNameChinese() : categoryNew.getName();
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.categorySelectedId = categoriesTabhost.get(i).getLegacy_id();
                viewPagerAdapter.addFragment(homeFragment, nameCategory);
                categoryPopularIds.add(categoriesTabhost.get(i).getLegacy_id());
                TextView valueTV = new TextView(this);
                TextView viewLine = new TextView(this);
                viewLine.setBackgroundColor(getResources().getColor(R.color.white));
                Log.d(TAG, "nameCategory i = " + i + "   nameCategory: " + nameCategory);

                viewLine.setLayoutParams(new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
                viewLine.setPadding(5, 10, 5, 10);
                if (categorySelected != null && categorySelected.getLegacy_id().equals(categoriesTabhost.get(i).getLegacy_id())) {
                    valueTV.setBackgroundResource(R.drawable.textlines);
                    valueTV.setPadding(30, 15, 30, 15);
                }
                if (isChineseApp) {
                    if (categoriesTabhost.get(i).getNameChinese() != null && !categoriesTabhost.get(i).getNameChinese().isEmpty()) {
                        valueTV.setText(categoriesTabhost.get(i).getNameChinese());
                    } else {
                        valueTV.setText(categoriesTabhost.get(i).getName());
                    }
                } else {
                    valueTV.setText(categoriesTabhost.get(i).getName());
                }
                valueTV.setId(i);
                valueTV.setTextColor(getResources().getColor(R.color.grey_text_category));
                valueTV.setTag(categoriesTabhost.get(i).getLegacy_id());
                valueTV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                valueTV.setGravity(Gravity.CENTER);
                valueTV.setAllCaps(true);
                valueTV.setPadding(30, 15, 30, 15);

                valueTV.setOnClickListener(new MenuActivity.OnTabHotsClick());

                valueTV.setGravity(Gravity.BOTTOM);
                llTabHost.addView(valueTV);
                tabLayout.addTab(tabLayout.newTab().setText(nameCategory));
                final long currentTime = System.currentTimeMillis();
                Log.e("currentTime foreach", String.valueOf(currentTime));

            }
            final long currentTime = System.currentTimeMillis();
            Log.e("currentTime end for", String.valueOf(currentTime));

            //viewPager.setAdapter(adapter);
        }
        //llTabHost.setTa
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x - tvAddCategory.getWidth();
        Log.e(TAG, "width " + width);

        hsvTabMenu.setLayoutParams(new RelativeLayout.LayoutParams(width, tvAddCategory.getHeight()));
        tvAddCategory.setPadding(30, 15, 30, 15);
        // PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.e("onPageScrolled", "position " + position);
                String idCategory = categoryPopularIds.get(position);
                CategoryNew cate = mapCategory.get(idCategory);
                if (cate != null) {
                    idCategory = cate.getLegacy_id();
                    categorySelectedId = idCategory;
                    categorySelected = cate;
                    Log.e("onPageSelected", "idCategory " + idCategory);
                    final long currentTime = DateUtils.getDateInformation();
                    Log.e("currentTime ", String.valueOf(currentTime));

                    //homePresenter.setCategoryChange(idCategory);
                    //TODO CHECK HERE


                }
                callOnChangeCategoryListener(position, idCategory);

                updateUIForTabMenuWhenUserClick(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        final long currentTime = System.currentTimeMillis();
        Log.e("currentTime initTabshot", String.valueOf(currentTime));

        //Adding onTabSelectedListener to swipe views
        //  tabLayout.setOnTabSelectedListener(this);

    }

    private void updateUIForTabMenuWhenUserClick(int position) {
        resetTabhots();
        TextView tv = ((TextView) llTabHost.findViewById(position));
        tv.setBackgroundResource(R.drawable.btn_tab_host_red);
        //  tv.setTextColor(Color.parseColor("#ffffff"));
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setPadding(30, 15, 30, 15);

        int x, y;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int width = displayMetrics.widthPixels;
        x = tv.getLeft() - width / 3;
        y = tv.getTop();
        hsvTabMenu.scrollTo(x, y);
    }

    private void callOnChangeCategoryListener(int position, String idCategory) {
        HomeFragment homeFragment = (HomeFragment) viewPagerAdapter.getItem(position);
        if (homeFragment != null && homeFragment.isVisible()) {
            Log.e("homeFragment", idCategory);
            homeFragment.OnCategoryChange(idCategory);
        }
    }


    public class OnTabHotsClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            CategoryNew cate = mapCategory.get(v.getTag().toString());
            if (cate != null) {
                idCategory = cate.getLegacy_id();
                categorySelectedId = idCategory;
                categorySelected = cate;
                Log.e("OnTabHotsClick", categorySelected.getLegacy_id());
                //TODO CHECK HERE
               /* nextURL = null;
                initAndGetAdvertiseInCategory();*/
                if (categoryIds != null)
                    viewPager.setCurrentItem(categoryIds.indexOf(v.getTag().toString()));
                resetTabhots();
                TextView tv = ((TextView) v.findViewById(v.getId()));
                if (isChineseApp) {
                    if (cate.getNameChinese().equals(tv.getText())) {
                        tv.setBackgroundResource(R.drawable.btn_tab_host_red);
                        tv.setTextColor(getResources().getColor(R.color.white));
                        tv.setPadding(30, 15, 30, 15);

                    }
                } else {
                    if (cate.getName().equals(tv.getText())) {
                        tv.setBackgroundResource(R.drawable.btn_tab_host_red);
                        tv.setTextColor(getResources().getColor(R.color.white));
                        tv.setPadding(30, 15, 30, 15);
                    }
                }
            }
            //viewPager.getCurrentItem();
            //callOnChangeCategoryListener(viewPager.getCurrentItem(), idCategory);


            animLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_activity_left_to_right);
            animDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_activity_right_to_left);

        }
    }

    public void resetTabhots() {
        for (int i = 0; i < categoriesTabhost.size(); i++) {
            TextView tv = ((TextView) llTabHost.findViewById(i));
            tv.setBackgroundResource(R.color.white);

            tv.setTextColor(getResources().getColor(R.color.grey_text_category));
            tv.setAllCaps(true);
            tv.setPadding(30, 15, 30, 15);
            // tv.setTextColor(getResources().getColor(R.color.orange));
        }
    }


    @Override
    public void getCategoryListSuccess(List<CategoryNew> categoryListResponse) {


        categories = new ArrayList<>();
        mapCategory = new HashMap<>();
        categoriesChild = new ArrayList<>();
        categories = categoryListResponse;
        mapCategoryChildren = new HashMap<>();
        categoriesChild = new ArrayList<>();
        categoriesTabhost = new ArrayList<>();
        processDataMenu();
        Log.e(TAG, "getCategoryListSuccess ");

    }

    public interface OnCategoryChangeListener {
        public void OnCategoryChange(String categoryId);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupRecycleViewCategoryPopular() {
        Log.d(TAG, "setupRecycleViewAdvertiseApi()");

        rvCategoryPopular = (RecyclerView) findViewById(R.id.rvCatgoryPopular);
        rvCategoryPopular.setHasFixedSize(false);
        categoryPopularAdapter = new CategoryPopularAdapter(this, categoryPopularCustomizeArrayList);

        StaggeredGridLayoutManager mLayoutManagerAdvertiseGrid = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvCategoryPopular.setHasFixedSize(false);
        rvCategoryPopular.setItemAnimator(null);

        rvCategoryPopular.setAdapter(categoryPopularAdapter);
        rvCategoryPopular.setLayoutManager(mLayoutManagerAdvertiseGrid);

    }
}
