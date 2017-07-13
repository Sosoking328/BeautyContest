package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sosokan.android.BaseApp;
import com.sosokan.android.R;
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
import com.sosokan.android.models.Category;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.CategoryPopularCustomize;
import com.sosokan.android.models.City;
import com.sosokan.android.models.UserInformationTokenApi;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.mvp.advertise.AdvertisePresenter;
import com.sosokan.android.mvp.banner.BannerPresenter;
import com.sosokan.android.mvp.category.CategoryPresenter;
import com.sosokan.android.mvp.category.CategoryView;
import com.sosokan.android.networking.Service;
import com.sosokan.android.rest.UrlEndpoint;
import com.sosokan.android.ui.fragment.ConversationFragment;
import com.sosokan.android.ui.fragment.FavoriteFragment;
import com.sosokan.android.ui.fragment.FollowingFragment;
import com.sosokan.android.ui.fragment.HomeFragment;
import com.sosokan.android.ui.fragment.ProfileFragment;
import com.sosokan.android.ui.view.DialogCities;
import com.sosokan.android.ui.view.DialogSearchAdvance;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.GPSTracker;
import com.sosokan.android.utils.LocaleHelper;
import com.sosokan.android.utils.PermissionGrantedHelper;
import com.sosokan.android.utils.comparator.CategoryApiPopularComparator;
import com.sosokan.android.utils.comparator.CategoryApiSortComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by macintosh on 3/24/17.
 */

public class HomeActivity extends BaseApp implements View.OnClickListener, TabLayout.OnTabSelectedListener, CategoryView {
    private static final String TAG = "HomeActivity";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView ibMenuLeft, ibMenuRight;
    // LinearLayout contentRight;
    boolean showCategory = false;
    // MENU
    TextView tvTitleCategoryHome;
    LinearLayout llCategory;
    Toolbar mToolbarHome;

    TextView edtSearchAdvertiseHome;
    boolean isChineseApp;
    //------------------
    // CATEGORY API
    CategoryNew categorySelected;
    public String categorySelectedId = "";
    public int categorySelectedIdInt;
    CategoryNew categoryNew;
    // Map<String, CategoryNew> categoryMap;
    public CategoryNew categoryAll = null;
    private MultiLevelListView mListView;
    private List<CategoryNew> categories, categoriesChild, categoriesTabhost;
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
    public final static String ACTION_SEARCH_ADVANCE = "actionSearchAdvance";
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

    boolean showTypeGrid = true;
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
    List<String> categoryIds;

    @Inject
    public Service service;
    BannerPresenter bannerPresenter;
    CategoryPresenter categoryPresenter;
    AdvertisePresenter advertisePresenter;
    PrefManager prefManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().injectHomeActivity(this);
        setContentView(R.layout.activity_home);
        initValue();
        initView();
        confViews();
        setUpMenu();
        registerBroadcast();
    }

    public void initValue() {
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

    }

    public void initView() {
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
        mToolbarHome = (Toolbar) findViewById(R.id.toolbar);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //  tabLayout.setupWithViewPager(viewPager);


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
            tvLikeUserMenu.setText(String.format(getString(R.string.class_field), user.getMyAdvertiseCount()));
        } else {
            tvNameUserMenu.setText("");
            tvLikeUserMenu.setText(String.format(getString(R.string.class_field), 0));
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

    public void showMessageError(String title, String message) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)getApplicationContext()).isDestroyed()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(HomeActivity.this);

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

    private void changeFragment(Fragment targetFragment) {
        if (resideMenu != null) {
            resideMenu.clearIgnoredViewList();
        }
       /* if (llCategory.getVisibility() == View.VISIBLE) {
            llCategory.setVisibility(View.GONE);
        }*/
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
                    .setTransitionStyle(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_fragment, targetFragment)
                    .setTransitionStyle(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commitAllowingStateLoss();
        }


    }

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
                if (categorySelectedId != null && !categorySelectedId.isEmpty() && mapCategory != null)
                    categorySelected = mapCategory.get(categorySelectedId);
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
                //TODO CHECK ADS
                //getListAdsWithParamAdvance();
                // queryAdvertiseWithDistance();
            }
        };
        registerReceiver(updateReceiver, new IntentFilter(ACTION_SEARCH_ADVANCE));

        updateReceiverCity = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                City city = (City) intent.getSerializableExtra("citySelected");
                if (city != null) {
                    Log.e(TAG, city.getCity());
                    //TODO CHECK ADS
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

  /*  private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "ONE");
        adapter.addFragment(new HomeFragment(), "TWO");
        adapter.addFragment(new HomeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }*/


    @Override
    public void onFailure(String appErrorMessage) {
        mapCategoryChildren = new HashMap<>();
        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        prefManager = new PrefManager(this);
        categories = prefManager.getListCategoriesApi();
        if (categories != null && categories.size() > 0) {
            categoriesTabhost = new ArrayList<>();
            categoriesChild = new ArrayList<>();
            processDataMenu();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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

    public ResideMenu getResideMenu() {
        return resideMenu;
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
        mToolbarHome.setVisibility(View.VISIBLE);
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
        List<CategoryNew> listCategoryChildren = new ArrayList<>();
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

        if (prefManager.getListCategoriesPopular() == null || prefManager.getListCategoriesPopular().size() == 0) {
            List<CategoryPopularCustomize> categoryPopularCustomizeArrayList = new ArrayList<>();
            CategoryPopularCustomize categoryPopularCustomize;
            if (categoriesTabhost != null && categoriesTabhost.size() > 0) {
                for (int i = 0; i < categoriesTabhost.size(); i++) {
                    if (categoriesTabhost.get(i).getPopular() > 0) {
                        categoryPopularCustomize = new CategoryPopularCustomize();
                        categoryPopularCustomize.setCategory(categoriesTabhost.get(i));
                        categoryPopularCustomize.setSelected(true);
                        categoryPopularCustomizeArrayList.add(categoryPopularCustomize);
                    }
                }
                Gson gson = new Gson();
                prefManager.setCategoriesPopular(gson.toJson(categoryPopularCustomizeArrayList));
            }
        }
        initTabshot(categoriesTabhost);

        //TODO GET ADS
        //initAndGetAdvertiseInCategory();
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
                idCategory = categorySelectedId;
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
    }

    private void initTabshot(final List<CategoryNew> categoriesTabhost) {
        categoryIds = new ArrayList<>();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        llTabHost = (LinearLayout) findViewById(R.id.llTabHost);

        Log.e(TAG, "initTabshot ");
        if (prefManager == null) prefManager = new PrefManager(this);
        if (prefManager.getListCategoriesPopular() != null) {
            Log.e(TAG, "prefManager.getListCategoriesPopular() ");
            List<CategoryPopularCustomize> popularCustomizeList = prefManager.getListCategoriesPopular();
            for (int i = 0; i < popularCustomizeList.size(); i++) {
                CategoryNew categoryNew = popularCustomizeList.get(i).getCategory();
                String nameCategory = isChineseApp ? categoryNew.getNameChinese() : categoryNew.getName();
                //  adapter.addFragment(new HomeFragment(), nameCategory);

                //======
               /* final TabLayout.Tab home = tabLayout.newTab();
                home.setText(nameCategory);
                home.setIcon(R.drawable.btn_tab_host_red);
                tabLayout.addTab(home);
                tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.orange));
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));*/
                //=====
                categoryIds.add(categoriesTabhost.get(i).getLegacy_id());
                TextView valueTV = new TextView(this);
                TextView viewLine = new TextView(this);
                viewLine.setBackgroundColor(getResources().getColor(R.color.white));


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
                //valueTV.setGravity(Gravity.CENTER);
                valueTV.setAllCaps(true);
                valueTV.setPadding(30, 15, 30, 15);

                valueTV.setOnClickListener(new OnTabHotsClick());

                valueTV.setGravity(Gravity.BOTTOM);
                llTabHost.addView(valueTV);
                tabLayout.addTab(tabLayout.newTab().setText(nameCategory));
            }
            //viewPager.setAdapter(adapter);
        }
        //llTabHost.setTa
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.e("onPageScrolled", "position " +position);
                String idCategory = categoryIds.get(position);
                CategoryNew cate = mapCategory.get(idCategory);
                if (cate != null) {
                    idCategory = cate.getLegacy_id();
                    categorySelected = cate;
                    Log.e("onPageScrolled", categorySelected.getLegacy_id());
                    //TODO CHECK HERE
                    categorySelectedId = categorySelected.getLegacy_id();

                }
                viewPager.setCurrentItem(categoryIds.indexOf(idCategory));
                resetTabhots();


                TextView tv = ((TextView) llTabHost.findViewById(position));
                tv.setBackgroundResource(R.drawable.btn_tab_host_red);
                //  tv.setTextColor(Color.parseColor("#ffffff"));
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setPadding(30, 15, 30, 15);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        /*llTabHost = (LinearLayout) findViewById(R.id.llTabHost);
        if (categoriesTabhost != null) {
            for (int i = 0; i < categoriesTabhost.size(); i++) {
                TextView valueTV = new TextView(this);
                TextView viewLine = new TextView(this);
                viewLine.setBackgroundColor(getResources().getColor(R.color.white));


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
                //valueTV.setGravity(Gravity.CENTER);
                valueTV.setAllCaps(true);
                valueTV.setPadding(30, 15, 30, 15);

                valueTV.setOnClickListener(new OnTabHotsClick());

                valueTV.setGravity(Gravity.BOTTOM);
                llTabHost.addView(valueTV);
            }
        }
        if (categorySelected != null) {
            Log.e("CategoryId initTabshot", categorySelected.getLegacy_id());
        }
        ApplicationUtils.closeMessage();*/
    }

    public class OnTabHotsClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //TODO CHECK HERE
            /*rlSlideImage.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.GONE);
            mPageEndOffset = 0;*/
            //   Log.e("OnTabHotsClick", Integer.toString(advertiseList.size()));


            CategoryNew cate = mapCategory.get(v.getTag().toString());
            if (cate != null) {
                idCategory = cate.getLegacy_id();
                categorySelected = cate;
                Log.e("OnTabHotsClick", categorySelected.getLegacy_id());
                //TODO CHECK HERE
               /* nextURL = null;
                initAndGetAdvertiseInCategory();*/

            }
            viewPager.setCurrentItem(categoryIds.indexOf(v.getTag().toString()));
            resetTabhots();
            TextView tv = ((TextView) v.findViewById(v.getId()));
            if (isChineseApp) {
                if (categoriesTabhost.get(v.getId()).getNameChinese().equals(tv.getText())) {
                    tv.setBackgroundResource(R.drawable.btn_tab_host_red);
                    //  tv.setTextColor(Color.parseColor("#ffffff"));
                    tv.setTextColor(getResources().getColor(R.color.white));
                    tv.setPadding(30, 15, 30, 15);

                }
            } else {
                if (categoriesTabhost.get(v.getId()).getName().equals(tv.getText())) {
                    //tv.setBackgroundResource(R.drawable.textlines);
                    tv.setBackgroundResource(R.drawable.btn_tab_host_red);
                    // tv.setBackgroundResource(R.color.orange);
                    //  tv.setTextColor(Color.parseColor("#ffffff"));
                    tv.setTextColor(getResources().getColor(R.color.white));
                    tv.setPadding(30, 15, 30, 15);
                }
            }

            animLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_activity_left_to_right);
            animDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_activity_right_to_left);
            //CHECK HERE
            //llContainMain.setAnimation(animLeft);

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
    public boolean dispatchTouchEvent(MotionEvent event){

        super.dispatchTouchEvent(event);
        //   return gestureDetector.onTouchEvent(ev);
        return viewPager.onTouchEvent(event);

    }

    @Override
    public void getCategoryListSuccess(List<CategoryNew> categoryListResponse) {


        categories = new ArrayList<>();
        mapCategory = new HashMap<>();
        categoriesChild = new ArrayList<>();
        categories = categoryListResponse;
        processDataMenu();
        Log.e(TAG, "getCategoryListSuccess ");

    }


    @Override
    protected void onDestroy() {

        categoryPresenter.onStop();
        unregisterReceiver(updateReceiver);
        unregisterReceiver(updateReceiverCity);
        unregisterReceiver(updateReceiverUnselectCity);
    }

    @Override
    public void onClick(View v) {
        android.app.FragmentManager fm = getFragmentManager();
        switch (v.getId()) {


            case R.id.ibSearch:
                /*DialogSearchAdvance dialogSearchAdvance = new DialogSearchAdvance();
                dialogSearchAdvance.showDialog(this, categorySelected);*/
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogSearchAdvance dialogSearchAdvance = DialogSearchAdvance.newInstance("",false);
                dialogSearchAdvance.show(fm, "fragment_edit_name");
                break;

            case R.id.ibViewHomeFragment:
                showTypeGrid = !showTypeGrid;

                //TODO CHECK HERE => PASS PARAM TO HOME FRAGMENT
                
                /*if (showTypeGrid) {

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
                }*/
                break;

            case R.id.ibCities:
               /* if (llCities.getVisibility() == View.VISIBLE) {
                    llCities.setVisibility(View.GONE);

                } else {
                    llCities.setVisibility(View.VISIBLE);

                }*/
                android.app.FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                android.app.Fragment prev1 = getFragmentManager().findFragmentByTag("dialog");
                if (prev1 != null) {
                    ft1.remove(prev1);
                }
                ft1.addToBackStack(null);

                // DialogCities dialogCities = new DialogCities();

                DialogCities dialogCities = DialogCities.newInstance("Some Title");
                dialogCities.show(fm, "fragment_edit_name");
                ibCities.setEnabled(false);
               /* DialogCities dialogCities = new DialogCities();
                dialogCities.showDialog(getActivity(), getActivity().getFragmentManager());*/
                break;
        }
    }
}