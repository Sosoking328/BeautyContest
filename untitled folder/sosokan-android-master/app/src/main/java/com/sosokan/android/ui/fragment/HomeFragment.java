package com.sosokan.android.ui.fragment;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.roughike.bottombar.BottomBar;
import com.sosokan.android.R;
import com.sosokan.android.adapter.AdvertiseNewAdapter;
import com.sosokan.android.adapter.CitiesAdapter;
import com.sosokan.android.adapter.DistanceAdapter;
import com.sosokan.android.adapter.ListCategoryApiAdapter;
import com.sosokan.android.adapter.ViewPagerAdapter;
import com.sosokan.android.control.menu.ResideMenu;
import com.sosokan.android.control.multi.level.menu.DataProviderCategoryApi;
import com.sosokan.android.control.multi.level.menu.ItemInfo;
import com.sosokan.android.control.multi.level.menu.MultiLevelListView;
import com.sosokan.android.control.multi.level.menu.OnItemCategoryClickListener;
import com.sosokan.android.events.Listener.HideShowScrollListener;
import com.sosokan.android.models.AdListApi;
import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.AdvertiseApi;
import com.sosokan.android.models.Banner;
//import com.sosokan.android.models.Category;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.City;
import com.sosokan.android.models.Favorite;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.models.UserSelection;
import com.sosokan.android.mvp.advertise.AdvertisePresenter;
import com.sosokan.android.mvp.advertise.AdvertiseResponse;
import com.sosokan.android.mvp.advertise.AdvertiseView;
import com.sosokan.android.mvp.banner.BannerPresenter;
import com.sosokan.android.mvp.banner.BannerView;
import com.sosokan.android.mvp.category.CategoryPresenter;
import com.sosokan.android.mvp.category.CategoryView;
import com.sosokan.android.mvp.home.HomeView;
import com.sosokan.android.networking.Service;
import com.sosokan.android.rest.UrlEndpoint;
import com.sosokan.android.ui.activity.AdvertiseDetailApiActivity;
import com.sosokan.android.ui.activity.HomeActivity;
import com.sosokan.android.ui.activity.MenuActivity;
import com.sosokan.android.ui.activity.NewAdvertiseActivity;
import com.sosokan.android.ui.activity.PrefManager;
import com.sosokan.android.ui.activity.SignUpActivity;
import com.sosokan.android.ui.view.DialogCities;
import com.sosokan.android.ui.view.DialogSearchAdvance;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.comparator.CategoryApiPopularComparator;
import com.sosokan.android.utils.comparator.CategoryApiSortComparator;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.DateUtils;
import com.sosokan.android.utils.GPSTracker;
import com.sosokan.android.utils.LocaleHelper;
import com.sosokan.android.utils.PermissionGrantedHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class HomeFragment extends Fragment implements View.OnClickListener,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, BottomNavigationBar.OnTabSelectedListener,
        SwipeRefreshLayout.OnRefreshListener, BannerView, MenuActivity.OnCategoryChangeListener {


    private View parentView;


    private static final String TAG = "HomeFragment";

    private static HomeFragment homeFragment;
    //private DatabaseReference mDataRef;
    RelativeLayout llContainMain;
    private ArrayList<Advertise> mAdvertiseItems;
    private ArrayList<Favorite> favorites;
    RecyclerView recyclerView;
    BottomNavigationBar bottomNavigationBar;
    BottomBar bottomBar;
    ImageButton imbAdd;
    int lastSelectedPosition = 0;
    BadgeItem numberBadgeItem;

    StaggeredGridLayoutManager mLayoutManagerAdvertiseList, mLayoutManagerAdvertiseGrid;


    Context appContext;
    String idCategory;
    UserProfileApi user;
    ArrayList<String> listAdvertiseId;
    //Category category;

    // List<Category> categories;
    int sortBy;
    double distance;

    List<String> advertiseIds;

    static MenuActivity parentActivity;

    TextView tvNoResultFound;

    int index = 0;
    int mPageEndOffset, mPageLimit;

    Map<String, Object> advertises;

    private SwipeRefreshLayout swipeRefreshLayout;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbar;
    List<Banner> banners;

    Location location;

    ArrayList<String> stImages;
    /*VIEWPAGER*/
    protected View view;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    RelativeLayout rlSlideImage;


    String searchValue;

    //======= SCROLL
    int mScrollPosition = 0;
    boolean mScrollQueryInProgress = false;
    private final static int INIT_DOWNLOADS = 50;
    private final static int MAX_AD_LIST = 100;
    private final static int DOWNLOAD_SIZE = 25;
    private final static int MIN_AD_LIST = MAX_AD_LIST - DOWNLOAD_SIZE;


    //Spinner spnDistance;
    //Spinner spnSort;

    //=====
    private PrefManager prefManager;


    //======= NEW LIST WITH API
    List<AdvertiseApi> advertiseListApi;
    private AdvertiseNewAdapter mAdapterAdApi;
    private AdListApi mAdListApi = null;
    private List<AdvertiseApi> mAdvertiseItemsApi;
    private RequestQueue mQueue;
    private RequestQueue mQueueApi = null;
    private long mRequestTime = 0;
    private final static String API_URL = new UrlEndpoint().getUrlApi(Constant.ADS) +
            "?platform=json&limit=%d&offset=%d";

    String nextURL = null;
    String url;
    String urlAdsApi = new UrlEndpoint().getUrlApi(Constant.ADS);
    String languageApi;


    //SLIDE VIEW
    private GestureDetector gestureDetector;
    FrameLayout container;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    boolean isChineseApp;
    boolean showTypeGrid = true;
    /*CheckBox chbGridList;*/
    UrlEndpoint urlEndpoint;
    private List<CategoryNew> categories, categoriesChild, categoriesTabhost;

    @Inject
    public Service service;
    BannerPresenter bannerPresenter;
    CategoryPresenter categoryPresenter;
    // AdvertisePresenter advertisePresenter;
    CategoryNew categorySelected;

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

    boolean isAdvance;
    PermissionGrantedHelper permissionGrantedHelper;
    public String categorySelectedId = "";

    GPSTracker tracker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mQueue = Volley.newRequestQueue(getContext());
        parentView = inflater.inflate(R.layout.fragment_home, container, false);
        parentView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //do something
                    Log.d(TAG, "gestureDetector ");
                    return gestureDetector.onTouchEvent(event);
                }
                return true;
            }
        });

        if (getContext() != null) {
            String languageToLoad = LocaleHelper.getLanguage(getContext());
            isChineseApp = languageToLoad.toString().equals("zh");
            Log.e("isChineseApp", String.valueOf(isChineseApp));
        }
        initValue(container);

        Log.d(TAG, "INIT");
        initView();

        setupRecycleViewAdvertiseApi();


        initAndGetAdvertiseInCategory();
        handleInstanceState(savedInstanceState);
        return parentView;

    }


    private void initValue(ViewGroup container) {
        appContext = container.getContext();
        homeFragment = this;
        banners = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            idCategory = bundle.getString(Constant.CATEGORYID);
            sortBy = bundle.getInt("SortBy", -1);
            distance = bundle.getDouble("Distance", -1);
            showTypeGrid = bundle.getBoolean("showTypeGrid", false);
            searchValue = bundle.getString("searchValue");
        } else {
            idCategory = Constant.sosokanCategoryAll;
            sortBy = -1;
            distance = -1;
            showTypeGrid = false;
        }
        advertiseIds = new ArrayList<>();
        mAdvertiseItems = new ArrayList<>();
        favorites = new ArrayList<>();
        mPageEndOffset = 0;
        mPageLimit = 20;
        parentActivity = (MenuActivity) getActivity();
        //TODO: GET CATEGORY

        listAdvertiseId = new ArrayList<>();
        stImages = new ArrayList<>();


        urlEndpoint = new UrlEndpoint();
        languageApi = urlEndpoint.getLanguage(isChineseApp);
        mQueueApi = Volley.newRequestQueue(getActivity());
        prefManager = new PrefManager(getActivity());

        parentActivity.getDeps().injectHome(this);

        bannerPresenter = new BannerPresenter(service, this, getActivity());
       /* advertisePresenter = new AdvertisePresenter(service, this, getActivity());
        advertisePresenter.getListAdvertise();*/
        user = prefManager.getUserProfile();
        bottomNavigationBar = new BottomNavigationBar(getActivity());
        //SLIDE VIEW
        gestureDetector = new GestureDetector(getContext(), new MyGestureDetector());


        idCategory = parentActivity.categorySelectedId;
        categorySelected = parentActivity.categorySelected;
        categories = parentActivity.categories;
        Log.d(TAG, "idCategory " + idCategory);
    }

    @Override
    public void onPause() {
        super.onPause();
//        parentActivity.unregisterReceiver(updateReceiver);
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

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
    public void onRefresh() {

        swipeRefreshLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                //  handler.sendEmptyMessage(0);
            }
        }, 1000);
    }


   /* @Override
    public void getListAdvertiseSuccess(AdvertiseResponse advertiseResponse) {
        final long currentTime = DateUtils.getDateInformation();
        Log.e("currentTime ", String.valueOf(currentTime));

        if (advertiseResponse.getResults() != null) {
            mAdvertiseItemsApi = new ArrayList<>();
            List<AdvertiseApi> advertiseApis = new ArrayList<>();
            for (int i = 0; i < advertiseResponse.getResults().size(); i++) {
                AdvertiseApi advertise = advertiseResponse.getResults().get(i);
                if (advertise != null) {

                    double timeCreateOrUpdate;
                    if (Math.abs(advertise.getCreatedAt()) >= Math.abs(advertise.getCreatedAt())) {
                        timeCreateOrUpdate = Math.abs(advertise.getCreatedAt());
                    } else {
                        timeCreateOrUpdate = Math.abs(advertise.getUpdatedAt());
                    }
                    if (advertise != null && timeCreateOrUpdate <= currentTime) {

                        if (isChineseApp && advertise.isChinese()) {
                            advertiseApis.add(advertise);
                        } else if (!isChineseApp && !advertise.isChinese()) {
                            advertiseApis.add(advertise);
                        }

                    }


                }
            }
            updateMainList(advertiseApis);

            updateAdvertiseAdapterApi(mAdvertiseItemsApi);
        }
    }

    @Override
    public void getAdvertiseSuccess(AdvertiseApi advertiseApi) {

    }*/

    @Override
    public void onBannerFailure(String appErrorMessage) {
        Log.d(TAG, "onBannerFailure");
        stImages = new ArrayList<>();
        this.banners = new ArrayList<>();
        updateBanner();
    }

    @Override
    public void getBannerListSuccess(List<Banner> banners) {

        stImages = new ArrayList<>();
        this.banners = banners;
        Log.d(TAG, "banners" + banners);
        Log.d(TAG, "getBannerListSuccess");
        updateBanner();
    }


    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];
        pager_indicator.removeAllViews();


        if (dots != null && dots.length > 1) {
            for (int i = 0; i < dotsCount; i++) {
                if (dots[i] != null && getContext() != null) {
                    dots[i] = new ImageView(getContext());
                    dots[i].setImageDrawable(appContext.getResources().getDrawable(R.drawable.nonselecteditem_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    params.setMargins(4, 0, 4, 0);

                    pager_indicator.addView(dots[i], params);
                }

            }
            if (dots[0] != null) {
                dots[0].setImageDrawable(appContext.getResources().getDrawable(R.drawable.selecteditem_dot));
            }

        } else {

        }

    }

    private void initView() {

        rlSlideImage = (RelativeLayout) parentView.findViewById(R.id.rlSlideImage);
        intro_images = (ViewPager) parentView.findViewById(R.id.pager_introduction);

        pager_indicator = (LinearLayout) parentView.findViewById(R.id.viewPagerCountDots);


       /* swipeRefreshLayout = (SwipeRefreshLayout) parentView.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.orange, R.color.orange);
        swipeRefreshLayout.setDistanceToTriggerSync(20);// in dips
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used*/


        appBarLayout = (AppBarLayout) parentView.findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) parentView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");
        tvNoResultFound = (TextView) parentView.findViewById(R.id.tvNoResultFound);
        llContainMain = (RelativeLayout) parentView.findViewById(R.id.llContainMain);
        initViewBottomBar();
        imbAdd = (ImageButton) parentView.findViewById(R.id.imbAdd);
        imbAdd.setOnClickListener(this);


        //====== SLIDE VIEW
        container = (FrameLayout) parentView.findViewById(R.id.container);
        container.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "sortBy " + sortBy);
                return gestureDetector.onTouchEvent(event);
            }
        });
    }


    // Restoring the item list and the keys of the items: they will be passed to the adapter
    private void handleInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            savedInstanceState.putString(Constant.CATEGORYID, idCategory);
            savedInstanceState.putInt("SortBy", sortBy);
            savedInstanceState.putDouble("Distance", distance);
            savedInstanceState.putBoolean("showTypeGrid", showTypeGrid);
            savedInstanceState.putString("searchValue", searchValue);
            Log.d(TAG, "sortBy " + sortBy);
            Log.d(TAG, "distance " + distance);
            Log.d(TAG, "showTypeGrid " + showTypeGrid);
            Log.d(TAG, "searchValue " + searchValue);
        } else {
            mAdvertiseItems = new ArrayList<Advertise>();
        }
    }

    public void initAndGetAdvertiseInCategory() {
        mAdvertiseItemsApi = new ArrayList<>();
        mScrollPosition = 0;
        bottomNavigationBar.setVisibility(View.VISIBLE);

        Log.e("languageApi", languageApi);
        if (nextURL != null && !nextURL.isEmpty()) {
            url = nextURL;
        } else {
            if ((idCategory == null || idCategory.isEmpty()) || idCategory.equals(Constant.sosokanCategoryAll))
            //  idCategory = Constant.sosokanCategoryAll;
            {
                url = String.format(urlAdsApi + "?language=%s&platform=json&limit=%d&offset=%d", languageApi, INIT_DOWNLOADS, 0);
            } else {
                url = String.format(urlAdsApi + "?categoryId=%s&language=%s&platform=json&limit=%d&offset=%d", idCategory, languageApi, INIT_DOWNLOADS, 0);
            }
        }

        if (url != null && !url.isEmpty()) {
            initApiRequestAdvertiseWithUrl(url);
        }
        queryImages();
    }


    private void queryAllAdvertiseBySearch(final String searchText) {

        //TODO SEACH AD VIA API
        List<AdvertiseApi> foundList = new ArrayList<>();

        if (searchText == null) {
            return;
        }

        if (mAdvertiseItemsApi != null) {
            for (AdvertiseApi ad : mAdvertiseItemsApi) {
                if (ad != null) {

                    if ((ad.getName() != null && !isChineseApp) &&
                            ad.getName().toLowerCase().contains(searchText.toLowerCase())) {
                        foundList.add(ad);
                        break;
                    }
                    if ((ad.getCategoryId() != null) &&
                            ad.getCategoryId().toLowerCase().contains(searchText.toLowerCase())) {
                        foundList.add(ad);
                        break;
                    }
                    if ((ad.getDescription() != null) &&
                            ad.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                        foundList.add(ad);
                        break;
                    }
                    if ((ad.getShort_description() != null) &&
                            ad.getShort_description().toLowerCase().contains(searchText.toLowerCase())) {
                        foundList.add(ad);
                        break;
                    }
                }
            }
        }
        updateAdvertiseAdapterApi(foundList);
    }

    private void queryImages() {
        // String language = isChineseApp ? "?format=json&language=zh-hans" : "?format=json&language=en";
        Map<String, String> params = new HashMap<>();
        params.put("language", urlEndpoint.getLanguage(isChineseApp));

        /*if (categorySelected != null) {
            if (categorySelected.getLegacy_id() != null && !categorySelected.getLegacy_id().isEmpty() && !categorySelected.getLegacy_id().equals(Constant.sosokanCategoryAll)) {
                params.put("categoryId", categorySelected.getLegacy_id());
            }
        }*/
        if (idCategory != null && !idCategory.isEmpty()) {
            params.put("categoryId", idCategory);
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (location != null) {
                params.put("latitude", String.valueOf(location.getLatitude()));
                params.put("longitude", String.valueOf(location.getLongitude()));
            }
        }

        bannerPresenter.getBannerList(params);
    }

    private void updateBanner() {
        if (banners != null && banners.size() > 0) {
            for (int i = 0; i < banners.size(); i++) {
                stImages.add(banners.get(i).getImage());
            }
            rlSlideImage.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.VISIBLE);
            appBarLayout.setExpanded(true);
            collapsingToolbar.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "appBarLayout GONE");
            rlSlideImage.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.GONE);
            appBarLayout.setExpanded(false);
            // collapsingToolbar.setVisibility(View.GONE);
            stImages = new ArrayList<>();

            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            final AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
            if (behavior != null) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt();
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        behavior.setTopAndBottomOffset((Integer) animation.getAnimatedValue());
                        appBarLayout.requestLayout();
                    }
                });
                valueAnimator.setIntValues(0, -appBarLayout.getHeight());
                valueAnimator.setDuration(0);
                valueAnimator.start();
            }
        }

        if (stImages != null && stImages.size() > 0) {
            rlSlideImage.setVisibility(View.VISIBLE);
            mAdapter = new ViewPagerAdapter(getContext(), stImages);
            if (intro_images != null) {
                intro_images.setAdapter(mAdapter);
                intro_images.setCurrentItem(0);
                intro_images.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (dots != null && dots.length > 1) {
                            for (int i = 0; i < dotsCount; i++) {
                                if (dots[i] != null && getContext() != null) {
                                    dots[i] = new ImageView(getContext());
                                    dots[i].setImageDrawable(appContext.getResources().getDrawable(R.drawable.nonselecteditem_dot));

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );

                                    params.setMargins(4, 0, 4, 0);

                                    pager_indicator.addView(dots[i], params);
                                }

                            }
                            if (dots[position] != null) {
                                dots[position].setImageDrawable(appContext.getResources().getDrawable(R.drawable.selecteditem_dot));
                            }

                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                setUiPageViewController();
            }
        } else {
            rlSlideImage.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            idCategory = savedInstanceState.getString(Constant.CATEGORYID);
            sortBy = savedInstanceState.getInt("SortBy", -1);
            distance = savedInstanceState.getDouble("Distance", -1);
            showTypeGrid = savedInstanceState.getBoolean("showTypeGrid", false);
            searchValue = savedInstanceState.getString("searchValue");
        }

        Log.e("Home activity create", "===================");
        Log.d(TAG, "onActivityCreated");
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.putString(Constant.CATEGORYID, idCategory);
            savedInstanceState.putInt("SortBy", sortBy);
            savedInstanceState.putDouble("Distance", distance);
            savedInstanceState.putBoolean("showTypeGrid", showTypeGrid);
            savedInstanceState.putString("searchValue", searchValue);
        }
    }


    private void setupRecycleViewAdvertiseApi() {
        Log.d(TAG, "setupRecycleViewAdvertiseApi()");
        tvNoResultFound.setVisibility(View.GONE);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        mAdapterAdApi = new AdvertiseNewAdapter(parentActivity, advertiseListApi, categories, user, favorites, showTypeGrid);
        mLayoutManagerAdvertiseList = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManagerAdvertiseGrid = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(false);

        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemAnimator(null);

        recyclerView.setAdapter(mAdapterAdApi);

        showTypeOfAdapter();
        /*animUp = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_up);
        animDown = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_down);
*/
        recyclerView.addOnScrollListener(
                new HideShowScrollListener() {
                    @Override
                    public void onHide() {
                        bottomNavigationBar.setVisibility(View.VISIBLE);
                       /* bottomBar.setVisibility(View.VISIBLE);
                        imbAdd.setVisibility(View.GONE);
                        imbAdd.setAnimation(animDown);*/
                        //imbAdd.setVisibility(View.GONE);
                        imbAdd.setVisibility(View.GONE);
                        //imbAdd.setAnimation(animDown);
                    }

                    @Override
                    public void onShow() {
                        bottomNavigationBar.setVisibility(View.GONE);
                        /*bottomBar.setVisibility(View.GONE);
                        imbAdd.setVisibility(View.VISIBLE);
                        imbAdd.setAnimation(animUp);*/
                        //imbAdd.setVisibility(View.VISIBLE);
                        imbAdd.setVisibility(View.VISIBLE);
                        //imbAdd.setAnimation(animUp);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        //  Log.e("...", "onScrolled!");
                        visibleItemCount = mLayoutManagerAdvertiseGrid.getChildCount();
                        totalItemCount = mLayoutManagerAdvertiseGrid.getItemCount();

                        //   Log.e("mPageEndOffset", Integer.toString(mPageEndOffset));
                        int scrollCount = mPageEndOffset == 0 ? mPageLimit : mPageEndOffset;
                        if (dy > 0 && dy % 10 == 0) //check for scroll down
                        {
                            visibleItemCount = mLayoutManagerAdvertiseGrid.getChildCount();
                            totalItemCount = mLayoutManagerAdvertiseGrid.getItemCount();
                            int[] viewsIds = mLayoutManagerAdvertiseGrid.findFirstCompletelyVisibleItemPositions(null);
                            int[] lastViewId = mLayoutManagerAdvertiseGrid.findLastVisibleItemPositions(null);
                            pastVisiblesItems = viewsIds == null ? scrollCount : viewsIds[0] <= 0 ? scrollCount : viewsIds[0];

                            //Log.d(TAG, "visible = " + visibleItemCount);
                            Log.d(TAG, "total = " + totalItemCount);
                            for (int id : lastViewId) {
                                Log.d(TAG, "anylastViewId = " + id);
                            }

                            int last = Math.max(lastViewId[0], lastViewId[1]);
                            float percent = (float) last / (float) totalItemCount;
                            Log.d(TAG, "scroll percent = " + (percent * 100));
                            if ((percent > 0.8) && (!mScrollQueryInProgress)) {
                                AdListApi adListApi = getAdListApi();
                                if (adListApi != null) {
                                    String next = adListApi.getNext();
                                    initApiRequestAdvertiseWithUrl(getNextUrl(next));
                                    int position = last - DOWNLOAD_SIZE;
                                    if (position < 0) {
                                        position = 0;
                                    }
                                    mScrollPosition = position;
                                    mScrollQueryInProgress = true;
                                }
                            } else {
                                mScrollQueryInProgress = false;
                            }


                        } else if (dy < 0 && dy % 10 == 0) {        //check for scroll down
                            totalItemCount = mLayoutManagerAdvertiseGrid.getItemCount();
                            int[] lastViewId = mLayoutManagerAdvertiseGrid.findLastVisibleItemPositions(null);
                            Log.d(TAG, "total = " + totalItemCount);
                            for (int id : lastViewId) {
                                Log.d(TAG, "anylastViewId = " + id);
                            }

                            int last = Math.min(lastViewId[0], lastViewId[1]);
                            float percent = (float) last / (float) totalItemCount;
                            Log.d(TAG, "scroll percent = " + (percent * 100));
                            if ((percent < 0.2) && (!mScrollQueryInProgress)) {
                                AdListApi adListApi = getAdListApi();
                                if (adListApi != null) {
                                    String previous = adListApi.getPrevious();
                                    initApiRequestAdvertiseWithUrl(getNextUrl(previous));
                                    int position = last + DOWNLOAD_SIZE;
                                    if (position > totalItemCount) {
                                        position = totalItemCount;
                                    }
                                    mScrollPosition = position;
                                    mScrollQueryInProgress = true;
                                }
                            } else {
                                mScrollQueryInProgress = false;
                            }
                        }


                    }
                }
        );
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mQueue != null)
            mQueue.cancelAll(this);


        bannerPresenter.onStop();
        Log.e("Home ", "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        advertises = new HashMap<>();

        if (mQueue != null) {
            mQueue.cancelAll(this);
        }

        Log.e("Home ", "onDestroyView");
    }

    private void initViewBottomBar() {
        bottomNavigationBar = (BottomNavigationBar) parentView.findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
        refreshBottomBar();


    }

    public static HomeFragment getInstance() {
        return homeFragment;
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
                    alert = appContext.getResources().getString(R.string.we_are_sorry);
                    message = appContext.getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;

        }
    }

    public void showMessageError(String title, String message) {

        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) getContext()).isDestroyed()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(parentActivity);
            if (dlgAlert != null)
                dlgAlert.create().dismiss();


            dlgAlert.setMessage(message);
            dlgAlert.setTitle(title);
            dlgAlert.setPositiveButton(appContext.getResources().getString(R.string.SignUp), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(getContext(), SignUpActivity.class);
                    startActivity(intent);
                }


            });
            dlgAlert.setNegativeButton(appContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
//            }
        } catch (Exception ex) {

        }

    }


    public void callDetailPage(int advertiseId, String legacyId, String categoryId) {
        Log.d(TAG, "callDetailPage advertiseId = " + advertiseId);
        Log.d(TAG, "callDetailPage legacyId = " + legacyId);
        Log.d(TAG, "callDetailPage categoryId = " + categoryId);
        if (categoryId != null) {
            Intent intent = new Intent(appContext, AdvertiseDetailApiActivity.class);
            //intent.putExtra(Constant.POSSITION, position);
            intent.putExtra(Constant.ADVERTISEID, advertiseId);
            intent.putExtra(Constant.CATEGORYID, categoryId);
            intent.putExtra(Constant.LEGACY_ID, legacyId);
            if (idCategory != null && !idCategory.isEmpty()) {
                Log.d(Constant.ADVERTISEID, String.valueOf(advertiseId));
            }
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }
    }

    private void refreshBottomBar() {
        bottomNavigationBar.clearAll();
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED);
        numberBadgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.blue)
                .setText("" + lastSelectedPosition)
                .setHideOnSelect(true);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, "").setActiveColorResource(R.color.black).setInActiveColorResource(R.color.black))
                .addItem(new BottomNavigationItem(R.drawable.ic_search_grey_36dp, "").setActiveColorResource(R.color.grey))
                .addItem(new BottomNavigationItem(R.drawable.ic_add_circle_orange_36dp, "").setActiveColorResource(R.color.orange).setInActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.ic_favorite_border_grey_24dp, "").setActiveColorResource(R.color.grey))
                .addItem(new BottomNavigationItem(R.drawable.ic_settings_applications_grey_24dp, "").setActiveColorResource(R.color.grey))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();

       /* bottomBar = (BottomBar) parentView.findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                processWhenTabSelected(tabId);
            }
        });*/
    }

    private void processWhenTabSelected(@IdRes int tabId) {
        Intent intent;
        switch (tabId) {
            case R.id.tab_home:
                intent = new Intent(appContext, MenuActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case R.id.tab_search:
                //TODO SEARCH AD
                /*intent = new Intent(appContext, MenuActivity.class);
                intent.putExtra("ShowSearch", true);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                showViewSearch();*/
                break;
            case R.id.tab_add_post:
                String alert;
                String message;
                if (user != null) {
                    intent = new Intent(appContext, NewAdvertiseActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    alert = appContext.getResources().getString(R.string.we_are_sorry);
                    message = appContext.getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case R.id.tab_favorite:
                if (user != null) {
                    intent = new Intent(appContext, MenuActivity.class);
                    intent.putExtra("Fragment", "Favorite");
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    alert = appContext.getResources().getString(R.string.we_are_sorry);
                    message = appContext.getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case R.id.tab_setting:
                intent = new Intent(appContext, MenuActivity.class);
                intent.putExtra("Fragment", "Setting");
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                break;
        }
    }

    @Override
    public void onTabSelected(int position) {
        lastSelectedPosition = position;
        // setMessageText(position + " Tab Selected");
        if (numberBadgeItem != null) {
            numberBadgeItem.setText(Integer.toString(position));
        }
        Log.d("onTabSelected position", Integer.toString(position));
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(appContext, MenuActivity.class);
                //  Category category = idCategory;
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case 1:
                //TODO: SHOW VIEW SEARCH

                //showViewSearch();
                break;
            case 2:


                String alert;
                String message;
                if (user != null) {
                    intent = new Intent(appContext, NewAdvertiseActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    alert = appContext.getResources().getString(R.string.we_are_sorry);
                    message = appContext.getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case 3:

                if (user != null) {
                    intent = new Intent(appContext, MenuActivity.class);
                    intent.putExtra("Fragment", "Favorite");
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    alert = appContext.getResources().getString(R.string.we_are_sorry);
                    message = appContext.getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case 4:
                intent = new Intent(appContext, MenuActivity.class);
                intent.putExtra("Fragment", "Setting");
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }

    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
        lastSelectedPosition = position;
        // setMessageText(position + " Tab Selected");
        if (numberBadgeItem != null) {
            numberBadgeItem.setText(Integer.toString(position));
        }
        Log.d("onTabSelected position", Integer.toString(position));
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(appContext, MenuActivity.class);
                //  Category category = idCategory;
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case 1:
                //TODO: SHOW VIEW SEARCH
                //showViewSearch();

                break;
            case 2:
                String alert;
                String message;
                if (user != null) {
                    intent = new Intent(appContext, NewAdvertiseActivity.class);
                    startActivity(intent);
                } else {
                    alert = appContext.getResources().getString(R.string.we_are_sorry);
                    message = appContext.getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case 3:
                if (user != null) {
                    intent = new Intent(appContext, MenuActivity.class);
                    intent.putExtra("Fragment", "Favorite");
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    alert = appContext.getResources().getString(R.string.we_are_sorry);
                    message = appContext.getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case 4:
                intent = new Intent(appContext, MenuActivity.class);
                intent.putExtra("Fragment", "Setting");
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }

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







    /*BEGIN CODE API*/

    private Response.Listener<String> volleyListenerWithUrl = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // Display the first 100 characters of the response string.
            long delay = System.currentTimeMillis() - mRequestTime;
            Log.d(TAG, "Response: " + response);
            Log.d(TAG, "delay = " + delay);
            handleApiResponseGetAdvertiseWithUrl(response);
        }
    };

    private Response.ErrorListener volleyErrListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: handle error
            Log.d(TAG, "Error: " + error.getMessage());
        }
    };

    private void initApiRequestAdvertiseWithUrl(String url) {
        Log.d(TAG, "API url = " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                volleyListenerWithUrl, volleyErrListener) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String utf8String = null;
                try {
                    utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return urlEndpoint.getHeaderWithAccountDefault(getContext());
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                3,
                1f));
        mRequestTime = System.currentTimeMillis();
        mQueueApi.add(stringRequest);
    }

    private void handleApiResponseGetAdvertiseWithUrl(String response) {
        Collection<AdvertiseApi> advertisesApi = new ArrayList<>();
        String url = null;

        int count = 0;
        String next = "";
        String previous = "";
        final long currentTime = DateUtils.getDateInformation();
        Log.e("currentTime ", String.valueOf(currentTime));

        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(response);
            JsonArray jsonArr = jo.getAsJsonArray("results");
            Gson googleJson = new Gson();
            AdListApi adListApi = googleJson.fromJson(jo, AdListApi.class);
            setAdListApi(adListApi);
          /*  Type collectionType = new TypeToken<Collection<AdvertiseApi>>() {
            }.getType();
            advertisesApi = googleJson.fromJson(jsonArr, collectionType);*/
            if (jsonArr != null && jsonArr.size() > 0) {
                for (int i = 0; i < jsonArr.size(); i++) {
                    Gson gson = new Gson();
                    try {
                        AdvertiseApi advertise = gson.fromJson(jsonArr.get(i).toString(), AdvertiseApi.class);
                        if (advertise != null) {
                            double timeCreateOrUpdate;
                            if (Math.abs(advertise.getCreatedAt()) >= Math.abs(advertise.getCreatedAt())) {
                                timeCreateOrUpdate = Math.abs(advertise.getCreatedAt());
                            } else {
                                timeCreateOrUpdate = Math.abs(advertise.getUpdatedAt());
                            }
                            if (timeCreateOrUpdate <= currentTime) {

                                if (advertise.getLanguage() != null && advertise.getLanguage().equals(languageApi)) {
                                    advertisesApi.add(advertise);
                                } else {

                                    if (isChineseApp && advertise.isChinese()) {
                                        advertisesApi.add(advertise);
                                    } else if (!isChineseApp && !advertise.isChinese()) {
                                        advertisesApi.add(advertise);
                                    }
                                }

                            }
                        }

                    } catch (Exception ex) {
                        Log.e(TAG, "handleApiResponseGetAdvertiseWithUrl " + ex);
                    }

                    // banners.add(banner);

                }
            }
            count = adListApi.getCount();
            next = adListApi.getNext();
            previous = adListApi.getPrevious();
            Log.d(TAG, "ad collection filled");
            //Log.d(TAG, "count = " + count + ", next = " + next + ", prev = " + previous);
        } catch (JsonParseException e) {
            logException("JsonParseException", e);
        } catch (Exception e) {
            logException("Exception (unknown type)", e);
        }

        updateMainList(advertisesApi);
        if ((mAdvertiseItemsApi.size() < MIN_AD_LIST) && (mAdvertiseItemsApi.size() < count)) {
            Log.d(TAG, "got mAdvertiseItemsApi.size() = " + mAdvertiseItemsApi.size());
            url = getNextUrl((next));
            if (url != null) {
                initApiRequestAdvertiseWithUrl(url);
            }
        }
        updateAdvertiseAdapterApi(mAdvertiseItemsApi);
        Log.d(TAG, "got count = " + mAdapterAdApi.getItemCount());
        Log.d(TAG, "thread = " + Thread.currentThread().getName());
    }

    // make sure no R/W conflict since we're setting this from a callback
    private synchronized void setAdListApi(AdListApi adListApi) {
        mAdListApi = new AdListApi();
        mAdListApi.setCount(adListApi.getCount());
        mAdListApi.setNext(adListApi.getNext());
        mAdListApi.setPrevious(adListApi.getPrevious());
        // we don't need the "results" array
    }

    // make sure no R/W conflict since we're getting this from a callback
    private synchronized AdListApi getAdListApi() {
        return mAdListApi;
    }

    private String getNextUrl(String next) {
        String url = null;

        if (next != null) {
            //int index = next.indexOf("limit=");
            //Log.d(TAG, "index = " + index);
            String str1[] = next.split("limit=");
            if (!str1[0].equals(next)) {
                String str2[] = str1[1].split("&");
                if (str2.length > 1) {
                    Log.d(TAG, "str1 = " + str1[0] + ", " + str1[1]);
                    Log.d(TAG, "str2 = " + str2[0] + ", " + str2[1]);

                    if (Integer.valueOf(str2[0]) >= DOWNLOAD_SIZE) {
                        url = next;
                    } else {
                        url = str1[0] +
                                "limit=" +
                                String.valueOf(DOWNLOAD_SIZE) +
                                "&" +
                                str2[1];
                    }
                } else {
                    url = next;
                }
            }
        }
        return url;
    }

    // add new items to master list then remove earlier entries if size > MAX
    //      mAdvertiseItemsApi will be up to date on return
    private void updateMainList(Collection<AdvertiseApi> advertiseApiCollection) {
//        List<AdvertiseApi> adList = new ArrayList<>(advertiseApiCollection);

        if (mAdvertiseItemsApi == null) {
            mAdvertiseItemsApi = new ArrayList<>();
        }
        if (advertiseApiCollection != null)
            mAdvertiseItemsApi.addAll(advertiseApiCollection);
        while (mAdvertiseItemsApi.size() > MAX_AD_LIST) {
            mAdvertiseItemsApi.remove(0);
        }
    }

    private void updateAdvertiseAdapterApi(Collection<AdvertiseApi> advertiseApis) {

        advertiseListApi = new ArrayList<>(advertiseApis);

        // remove any ads date stamped for future posting
        int size = advertiseListApi.size();
        /*for (int i = 0; i < size; i++) {
            Date date = DateUtils.parseDate(advertiseListApi.get(i).getCreated_on());
            if (date.compareTo(new Date()) > 0) {
                // TODO: handle item removal more cleanly
                Log.d(TAG, "advertiseListApi.get(i).getCreated_on() " + advertiseListApi.get(i).getCreated_on());
                Log.d(TAG, "date " + date);
                Log.d(TAG, "remove any ads date stamped for future posting " + advertiseListApi.get(i).getId());
                advertiseListApi.remove(i);
                i--;
                size--;
            }
        }*/

        parentActivity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    mAdapterAdApi.notifyDataSetChanged();// Notify the adapter
                    Log.d(TAG, "notified on entry of update");
                } catch (Exception e) {

                }
            }
        });

        if (advertiseListApi != null && advertiseListApi.size() > 0) {
            Log.d(TAG, "ad list size = " + advertiseListApi.size());
            tvNoResultFound.setVisibility(View.GONE);
            llContainMain.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(false);
            showTypeOfAdapter();
            mAdapterAdApi = new AdvertiseNewAdapter(parentActivity, advertiseListApi, categories, user, favorites, showTypeGrid);
            //recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(mAdapterAdApi);

            parentActivity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        mAdapterAdApi.notifyDataSetChanged();// Notify the adapter
                        Log.d(TAG, "notified new adapter");
                    } catch (Exception e) {

                    }
                }
            });


            mAdapterAdApi.setListener(new AdvertiseNewAdapter.Listener() {
                @Override
                public void onItemClick(int position) {
                    if (advertiseListApi != null && advertiseListApi.size() > 0 && advertiseListApi.size() > position) {
                        AdvertiseApi advertiseNew = advertiseListApi.get(position);
                        Log.d(TAG, "mAdapterAdApi onItemClick position = " + position);
                        if (advertiseNew != null)
                            callDetailPage(advertiseNew.getId(), advertiseNew.getLegacy_id(), advertiseNew.getCategoryId());
                    }

                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            showTypeOfAdapter();
            recyclerView.setPreserveFocusAfterLayout(true);
            recyclerView.scrollToPosition(mScrollPosition);

        } else {
            Log.d(TAG, "updateAdvertiseAdapter = NULL");
            tvNoResultFound.setVisibility(View.VISIBLE);
            llContainMain.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.GONE);
            advertiseListApi.clear();
            mAdapterAdApi.notifyDataSetChanged();// Notify the adapter
            ApplicationUtils.closeMessage();
        }
    }

    public void showTypeOfAdapter() {
        showTypeGrid = parentActivity.showTypeGrid;
        if (!showTypeGrid) {
            recyclerView.setLayoutManager(mLayoutManagerAdvertiseList);
        } else {
            recyclerView.setLayoutManager(mLayoutManagerAdvertiseGrid);
        }
    }

    private void logException(String name, Exception e) {
        Log.e(TAG, name);
        Log.e(TAG, "msg =  " + e.getMessage());
        Log.e(TAG, "cause  = " + e.getCause());
    }

    @Override
    public void OnCategoryChange(String categoryId) {
        idCategory = categoryId;
        Log.e(TAG, "onCategoryChange  idCategory = " + idCategory);
        initAndGetAdvertiseInCategory();
    }


    private class FavoriteCountComparator implements Comparator<AdvertiseApi> {
        @Override
        public int compare(AdvertiseApi lhs, AdvertiseApi rhs) {
            return (rhs.getFavoriteCount() - lhs.getFavoriteCount());
        }
    }

    private class CreatedOnComparator implements Comparator<AdvertiseApi> {
        @Override
        public int compare(AdvertiseApi lhs, AdvertiseApi rhs) {
            try {
                return (DateUtils.parseDate(rhs.getCreated_on())
                        .compareTo(DateUtils.parseDate(lhs.getCreated_on())));
            } catch (NullPointerException e) {
                logException("CreatedOnComparator", e);
                return 0;
            }
        }
    }


    private void checkAdvertiseWithConditionNormal(Collection<AdvertiseApi> advertisesApi, AdvertiseApi advertise) {
        if (advertise != null) {
            if (advertise != null && advertise.getLanguage() != null) {
                if (isChineseApp && advertise.getLanguage().equals(languageApi)) {
                    advertisesApi.add(advertise);
                } else if (!isChineseApp && advertise.getLanguage().equals(languageApi)) {
                    advertisesApi.add(advertise);
                }

            } else {
                if (isChineseApp && advertise.isChinese()) {
                    advertisesApi.add(advertise);
                } else if (!isChineseApp && !advertise.isChinese()) {
                    advertisesApi.add(advertise);
                }
            }
        }
    }


    //== QUERY SEARCH
    /*public void queryAdsByCity(final City city) {
        //TODO SEARCH AD ADVANCE
        advertises = new HashMap<>();
        mAdvertiseItems = new ArrayList<>();
        mPageEndOffset = 0;
        if (city != null)
            getAdvertiseWithDistance(500, city.getLatitude(), city.getLongitude());
    }*/

    public void getListAdsWithParamAdvance() {
        initValueForSearchAdvance();
        mAdvertiseItemsApi = new ArrayList<>();
        bottomNavigationBar.setVisibility(View.VISIBLE);
        isAdvance = true;
        if (city != null && !isAllowDistance) {
            advertiseIds = new ArrayList<>();
            mAdvertiseItems = new ArrayList<>();
            getAdvertiseWithDistanceAdvanceParam(distanceMax, city.getLatitude(), city.getLongitude());
        } else if (city == null && !isAllowDistance) {
            tracker = new GPSTracker(appContext);
            if (tracker != null && !tracker.canGetLocation()) {
                tracker.showSettingsAlert();
            } else if (tracker != null) {

                permissionGrantedHelper = new PermissionGrantedHelper(getActivity());
                permissionGrantedHelper.checkAndRequestPermissionForMap();
                advertiseIds = new ArrayList<>();
                mAdvertiseItems = new ArrayList<>();
                // getAdvertiseWithDistance(distanceMax, tracker.getLatitude(), tracker.getLongitude());
                getAdvertiseWithDistanceAdvanceParam(distanceMax, tracker.getLatitude(), tracker.getLongitude());
            }
        } else {
            queryAdvertiseByCategoryInAdvance();
        }
    }

    void initValueForSearchAdvance() {
        UserSelection userSelection = parentActivity.userSelection;
        if (userSelection != null) {
            isAllDay = userSelection.isAllDay();
            isAdvance = true;
            isAllowDistance = userSelection.isAllowDistance();
            isAllPrice = userSelection.isAllPrice();
            // isSearchAdvance = userSelection.isS
            dayMax = userSelection.getDayMax();
            dayMin = userSelection.getDayMin();
            priceMax = userSelection.getPriceMax();
            priceMin = userSelection.getPriceMin();
            distanceMax = userSelection.getDistanceMax();
            distanceMin = userSelection.getDistanceMin();
            categorySelectedId = userSelection.getCategorySelectedId();
            textSearch = userSelection.getTextSearch();
            if (userSelection.getCategory() != null)
                categorySelected = userSelection.getCategory();
            city = userSelection.getCity();
        }
    }

    private void queryAdvertiseByCategoryInAdvance() {
        queryImages();
        ApplicationUtils.closeMessage();
        Log.e(TAG, "========== queryAdvertiseByCategory");
        //TODO SEARCH AD ADVANCE

    }

    public void getAdvertiseWithDistanceAdvanceParam(double distance, double latitude, double longitude) {
        queryImages();
        Log.e(TAG, "getAdvertiseWithDistanceAdvanceParam languageApi " + languageApi);
        url = String.format(urlAdsApi + "?dist=%s&point=%s,%s&language=%s&limit=%d&offset=%d", distance, latitude, longitude, languageApi, INIT_DOWNLOADS, 0);
        if (url != null && !url.isEmpty()) {

            Log.e(TAG, "getAdvertiseWithDistanceAdvanceParam url " + url);
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    volleyListenerGetAdvertiseWithAdvanceParam,
                    volleyErrListener
            ) {

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                    String utf8String = null;
                    try {
                        utf8String = new String(response.data, "UTF-8");
                        return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));

                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                public Map<String, String> getHeaders() throws AuthFailureError {
                    return urlEndpoint.getHeaderWithAccountDefault(getContext());
                }
            };

            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    100000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mQueue.add(postRequest);
        }
    }


    private Response.Listener<String> volleyListenerGetAdvertiseWithAdvanceParam = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // Display the first 100 characters of the response string.
            long delay = System.currentTimeMillis() - mRequestTime;
            Log.d(TAG, "volleyListenerGetAdvertiseWithAdvanceParam Response: " + response);
            Log.d(TAG, "volleyListenerGetAdvertiseWithAdvanceParam delay = " + delay);
            handleApiResponseGetAdvertiseWithAdvanceParam(response);
        }
    };


    private void handleApiResponseGetAdvertiseWithAdvanceParam(String response) {
        Collection<AdvertiseApi> advertiseApis = new ArrayList<>();
        String url = null;

        int count = 0;
        String next = "";
        // String previous = "";

        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject) jsonParser.parse(response);
            JsonArray jsonArr = jo.getAsJsonArray("results");
            Gson googleJson = new Gson();
            AdListApi adListApi = googleJson.fromJson(jo, AdListApi.class);
            setAdListApi(adListApi);
            double timeCreateOrUpdate = 0;
            final double currentTime = DateUtils.getDateInformation();
            if (jsonArr != null && jsonArr.size() > 0) {
                for (int i = 0; i < jsonArr.size(); i++) {
                    Gson gson = new Gson();
                    try {

                        //======
                        AdvertiseApi advertise = gson.fromJson(jsonArr.get(i).toString(), AdvertiseApi.class);

                        if (isAdvance) {
                            if (categorySelectedId.equals(Constant.sosokanCategoryAll) && advertise != null) {
                                checkAdvertiseWithConditionAdvance(advertiseApis, currentTime, advertise);
                            } else if (advertise != null && advertise.getCategoryId().equals(categorySelectedId)) {
                                checkAdvertiseWithConditionAdvance(advertiseApis, currentTime, advertise);
                            }

                        } else {
                            checkAdvertiseWithConditionNormal(advertiseApis, advertise);
                        }
                    } catch (Exception ex) {
                        Log.d(TAG, "handleApiResponseGetAdvertiseWithAdvanceParam ex " + ex);
                    }
                }
            }
            updateMainList(advertiseApis);
            if ((mAdvertiseItemsApi.size() < MIN_AD_LIST) && (mAdvertiseItemsApi.size() < count)) {
                Log.d(TAG, "got mAdvertiseItemsApi.size() = " + mAdvertiseItemsApi.size());
                url = getNextUrl((next));
                if (url != null) {
                    initApiRequestAdvertiseWithUrl(url);
                }
            }
            updateAdvertiseAdapterApi(mAdvertiseItemsApi);
            if (textSearch != null && !textSearch.isEmpty()) {
                queryAllAdvertiseBySearch(textSearch);
            }
            Log.d(TAG, "got count = " + mAdapterAdApi.getItemCount());
            Log.d(TAG, "thread = " + Thread.currentThread().getName());
        } catch (Exception ex) {
            Log.d(TAG, "ex = " + ex);
        }
    }

    private void checkAdvertiseWithConditionAdvance(Collection<AdvertiseApi> advertisesApi, double currentTime, AdvertiseApi advertise) {
        double timeCreateOrUpdate;

        if (advertise != null) {
            if (Math.abs(advertise.getCreatedAt()) >= Math.abs(advertise.getCreatedAt())) {
                timeCreateOrUpdate = Math.abs(advertise.getCreatedAt());
            } else {
                timeCreateOrUpdate = Math.abs(advertise.getUpdatedAt());
            }

            if (advertise != null && timeCreateOrUpdate <= currentTime) {

                if ((advertise.isChinese() && isChineseApp)) {
                    if (!isAllPrice && !isAllDay) {
                        double miliMax = (currentTime - timeCreateOrUpdate) / 86400000;
                        Log.e("miliMax", "isChinese !isAllPrice && !isAllDay miliMax " + miliMax);
                        if (advertise.getPrice() != null && !advertise.getPrice().isEmpty()) {
                            if (miliMax > 0 && miliMax <= dayMax && miliMax >= dayMin) {
                                double price = parseToInt(advertise.getPrice(), 0);
                                if (price <= priceMax && price >= priceMin) {
                                    advertisesApi.add(advertise);
                                }
                            }

                        }

                    } else if (!isAllDay) {
                        double miliMax = (currentTime - timeCreateOrUpdate) / 86400000;
                        Log.e("miliMax", "isChinese !isAllDay miliMax " + miliMax);
                        if (miliMax > 0 && miliMax <= dayMax && miliMax >= dayMin) {
                            advertisesApi.add(advertise);
                        }
                    } else if (!isAllPrice) {
                        double price = parseToInt(advertise.getPrice(), 0);

                        if (price <= priceMax && price >= priceMin) {
                            advertisesApi.add(advertise);
                        }
                    }
                } else if (!advertise.isChinese() && !isChineseApp) {
                    if (!isAllPrice && !isAllDay) {
                        double miliMax = (currentTime - timeCreateOrUpdate) / 86400000;
                        Log.e("miliMax", "!isAllPrice && !isAllDay miliMax " + miliMax);
                        if (advertise.getPrice() != null && !advertise.getPrice().isEmpty()) {
                            if (miliMax > 0 && miliMax <= dayMax && miliMax >= dayMin) {
                                double price = parseToInt(advertise.getPrice(), 0);
                                Log.e("miliMax", "!isAllPrice && !isAllDay advertise.getPrice() " + advertise.getPrice());
                                if (price <= priceMax && price >= priceMin) {
                                    advertisesApi.add(advertise);
                                }
                            }

                        }

                    } else if (!isAllDay) {
                        double miliMax = (currentTime - timeCreateOrUpdate) / 86400000;
                        Log.e("miliMax", "!isAllDay miliMax " + miliMax);
                        if (miliMax > 0 && miliMax <= dayMax && miliMax >= dayMin) {
                            advertisesApi.add(advertise);
                        }
                    } else if (!isAllPrice) {
                        double price = parseToInt(advertise.getPrice(), 0);

                        if (price <= priceMax && price >= priceMin) {
                            advertisesApi.add(advertise);
                        }
                    }
                }
            }
        }
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                //TODO GET AD WHEN SLIDE LEFT RIGHT
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (advertiseIds != null && advertiseIds.size() > 0) {
                        Log.e("onFling", "e1.getX() - e2.getX()   > SWIPE_MIN_DISTANCE");

                    }

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (advertiseIds != null && advertiseIds.size() > 0) {

                        Log.e("onFling", "e1.getX() - e2.getX()  ");
                    }
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    double parseToInt(String maybeInt, double defaultValue) {
        if (maybeInt == null) return defaultValue;
        maybeInt = maybeInt.trim();
        if (maybeInt.isEmpty()) return defaultValue;
        return Double.parseDouble(maybeInt);
    }
}

