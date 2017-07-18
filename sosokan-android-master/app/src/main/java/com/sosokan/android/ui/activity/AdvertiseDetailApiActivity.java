package com.sosokan.android.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.buddy.sdk.Buddy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import com.sosokan.android.BaseApp;
import com.sosokan.android.R;
import com.sosokan.android.adapter.CommentAdapter;
import com.sosokan.android.adapter.CommentsApiAdapter;
import com.sosokan.android.adapter.FlagChoiceAdapter;

import com.sosokan.android.adapter.ListCategoryApiAdapter;
import com.sosokan.android.adapter.ViewPagerAdapter;
import com.sosokan.android.control.FullscreenVideoLayout;
import com.sosokan.android.control.multi.level.menu.DataProviderCategoryApi;
import com.sosokan.android.control.multi.level.menu.MultiLevelListAdapter;
import com.sosokan.android.control.multi.level.menu.MultiLevelListView;
import com.sosokan.android.events.Listener.EndCallListener;

import com.sosokan.android.models.AdvertiseApi;


import com.sosokan.android.models.CategoryNew;

import com.sosokan.android.models.CommentNew;
import com.sosokan.android.models.Conversation;
import com.sosokan.android.models.Flag;
import com.sosokan.android.models.FlagChoice;

import com.sosokan.android.models.Image;
import com.sosokan.android.models.ImageAdvertiseApi;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.mvp.advertise.AdvertisePresenter;
import com.sosokan.android.mvp.advertise.AdvertiseResponse;
import com.sosokan.android.mvp.advertise.AdvertiseView;
import com.sosokan.android.mvp.category.CategoryPresenter;
import com.sosokan.android.mvp.category.CategoryView;
import com.sosokan.android.mvp.flagchoice.FlagChoicePresenter;
import com.sosokan.android.mvp.flagchoice.FlagChoiceResponse;
import com.sosokan.android.mvp.flagchoice.FlagChoiceView;
import com.sosokan.android.mvp.userProfile.UserProfilePresenter;
import com.sosokan.android.mvp.userProfile.UserProfileResponse;
import com.sosokan.android.mvp.userProfile.UserProfileView;
import com.sosokan.android.networking.Service;
import com.sosokan.android.rest.UrlEndpoint;
import com.sosokan.android.utils.ApplicationUtils;

import com.sosokan.android.utils.comparator.CategoryApiSortComparator;
import com.sosokan.android.utils.Constant;

import com.sosokan.android.utils.DateUtils;
import com.sosokan.android.utils.DetectHtml;
import com.sosokan.android.utils.LocaleHelper;
import com.sosokan.android.utils.NetworkUtils;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

import static com.sosokan.android.utils.ColorHelper.getColorWithAlpha;

/**
 * Created by AnhZin on 8/24/2016.
 */
public class AdvertiseDetailApiActivity extends BaseApp implements View.OnClickListener, OnMapReadyCallback,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, BottomNavigationBar.OnTabSelectedListener,
        CategoryView, UserProfileView, AdvertiseView, FlagChoiceView {
    private static final String TAG = "AdDetailApiActivity";
    private GoogleMap googleMap;
    ImageButton ibBackPostDetail, ibEditPostDetail, ibSharePostDetail, ibMessagePostDetail, ibFavoritePostDetail;
    TextView tvDiscountPostDetail, tvTitlePostDetail, tvCreatedDatePostDetail, tvDescriptionPostDetail,
            tvAvailabilityPostDetail, tvWebPostDetail, tvPhonePostDetail, tvEmailPostDetail, tvFaxPostDetail,
            tvUserNamePostDetail, tvAdvertiseCountPostDetail, tvCategoryPostDetail;
    RelativeLayout rlImageCoverPostDetail;
    ImageView imgPostDetail, imgProfilePostDetail;

    ListView lvComment;
    CommentAdapter adapter;
    int position;
    AdvertiseApi advertise;
    /*User user;
    User userOwner;*/

    UserProfileApi user;
    UserProfileApi userOwner;
    FullscreenVideoLayout videoLayout;
    MapFragment mapFragment;
    RatingBar ratingBarPostDetail;
    WebView webViewDescriptionPostDetail, webViewVideoPostDetail;
    RelativeLayout rlVideoWebPostDetail;
    //int advertiseIdApi;
    String conversationId;
    public String userId;
    List<Conversation> conversationList;

    ImageView ivEmailPostDetail, ivPhonePostDetail;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Map<String, Object> mapFavorite;
    boolean isFavorite;
    float ratingNumber = 0;
    TextView tvFollowPostDetail;
    boolean isChineseApp;
    ImageButton ibFacebookDetail, ibTwitter;

    int countImage = 0;
    String idCategory;
    LinearLayout llButtonsContact;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    CategoryNew category;
    int indexAdvertise;
    //    Map<String, Object> advertiseIds;
    List<String> advertiseIds;
    List<AdvertiseApi> advertiseApis;
    TextView tvNumberFavoritePostDetail;
    Spinner spPostDetail;

    /*VIEWPAGER*/
    protected View view;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    RelativeLayout rlSlideImage;
    ArrayList<String> stImages;
    int indexFragment;
    private final static String ACTION_WATCH_OF_SERVICE = "ACTION_WATCH_OF_SERVICE";
    boolean fromMess;
    private SliderLayout mDemoSlider;

    LinearLayout llContentPost;
    private RequestQueue mQueue;
    //private String legacyId;
    RelativeLayout llContentPostDetail;
    LinearLayout llSelectFlagChoicePostDetail;
    ImageButton ibBackFlagChoicePostDetail;

    //======================== FLAGCHOICE

    RecyclerView recyclerView;
    FlagChoiceAdapter flagChoiceAdapter;
    List<FlagChoice> flagChoices;
    RelativeLayout rlReport;
    Flag flagSelected;
    TextView btnSendFlagChoicePostDetail;
    int idUserApi = -1;
    int advertiseIdInt = -1;
    boolean isLoadAdvertise;
    //String advertiseIdLegacy = "";
    //============= COMMENTS
    RecyclerView rvComments;
    CommentsApiAdapter commentsAdapter;
    List<CommentNew> commentNews;
    PrefManager prefManager;
    EditText edtComment;
    ImageButton btnSendComment;

    //========== CLICKABLE

    LinearLayout llCategoryAdvertisePostDetail, llInformationAdvertisePostDetail;
    ImageButton ibBackSelectCategory;
    private List<CategoryNew> categories, categoriesChild;
    public String categoryAllId = "All";
    public CategoryNew categoryAll = null;

    public CategoryNew categoryNew = null;
    public static Map<String, CategoryNew> mapCategory;
    public static Map<String, List<String>> mapCategoryChildren;
    ListCategoryApiAdapter listAdapter;
    MultiLevelListAdapter multiLevelListAdapter;


    public String categorySelectedId;
    CategoryNew categorySelected;
    private MultiLevelListView mListView;
    private DatabaseReference databaseReference;
    ImageButton ibWechatDetail;
    int indexOfCategory = 0;

    // ADD BOTTOM BAR

    BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    BadgeItem numberBadgeItem;

    private boolean resumeHasRun = false;
    String token;
    CommentNew commentNew;

    UrlEndpoint urlEndpoint;
    ScrollView scrollViewPostDetail;


    //======= SHOW HIDE INFOR

    LinearLayout llPhonePostDetail, llFaxPostDetail, llEmailPostDetail, llWebsitePostDetail, llAddressPostDetail;
    TextView tvAddressPostDetail;


    // VIDEO IMGAE

    ImageView imgVideoPostDetail;

    @Inject
    public Service service;
    AdvertisePresenter advertisePresenter;
    CategoryPresenter categoryPresenter;
    FlagChoicePresenter flagChoicePresenter;
    UserProfilePresenter userProfilePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        getDeps().injectAdvertiseApiDetail(this);
        Fabric.with(this, new Crashlytics());
        if (getApplicationContext() != null) {
            Buddy.init(getApplicationContext(), getResources().getString(R.string.buddy_build_app_id), getResources().getString(R.string.buddy_build_access_token));
        }
        initView();

        initValue();
        initViewRecycleCategory();
        setupRecycleViewFlagChoice();
        setupRecycleViewComments();
        setupMap();


        EndCallListener callListener = new EndCallListener();
        TelephonyManager mTM = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);

        handleInstanceState(savedInstanceState);

        categoryPresenter = new CategoryPresenter(service, this, this);
        categoryPresenter.getCategoryList();
        flagChoicePresenter = new FlagChoicePresenter(service, this, this);
        flagChoicePresenter.getListFlagChoice();
        userProfilePresenter = new UserProfilePresenter(service, this, this);
        //  getAdvertiseViaApiWithIdOfFirebase();
        Log.e("Advertise API Detail ", "=========== Created");
    }

    private void initValue() {
        advertisePresenter = new AdvertisePresenter(service, this, this);
        categoryPresenter = new CategoryPresenter(service, this, this);
        flagChoicePresenter = new FlagChoicePresenter(service, this, this);

        mQueue = Volley.newRequestQueue(this);
        String languageToLoad = LocaleHelper.getLanguage(AdvertiseDetailApiActivity.this);
        isChineseApp = languageToLoad.toString().equals("zh");
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(this);
        }
        indexAdvertise = 0;
        conversationList = new ArrayList<>();
        advertiseIds = new ArrayList<>();
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        stImages = new ArrayList<>();
        flagChoices = new ArrayList<>();
        commentNews = new ArrayList<>();
        prefManager = new PrefManager(getApplication());
        user = prefManager.getUserProfile();
        idUserApi = prefManager.getPrimaryKeyUser();
        urlEndpoint = new UrlEndpoint();
        token = urlEndpoint.getUserToken(getApplicationContext());
        advertiseApis = new ArrayList<>();
        initAndGetInformation();
    }


    private void initAndGetInformation() {
        if (getIntent() != null) {
            indexAdvertise = getIntent().getIntExtra(Constant.INDEX_ADVERTISE, 0);
            advertiseIdInt = getIntent().getIntExtra(Constant.ADVERTISEID, -1);

            idCategory = getIntent().getStringExtra(Constant.CATEGORYID);
            indexFragment = getIntent().getIntExtra(Constant.INDEX_FRAGMENT, 0);
            fromMess = getIntent().getBooleanExtra(Constant.FROM_MESSAGE, false);
            conversationId = getIntent().getStringExtra(Constant.CONVERSATIONID);
            Log.e("getIntent ", " " + "initAndGetInformation");
            Log.e("indexAdvertise ", " " + indexAdvertise);
            Log.e("advertiseIdApi ", " " + advertiseIdInt);
            Log.e("idCategory ", " " + idCategory);
            Log.e("indexFragment ", " " + indexFragment);
            if (advertiseIdInt > 0)
                searchPost();


            if (idCategory != null && !idCategory.isEmpty()) {
                advertisePresenter.getListAdvertise(idCategory);
                Log.e(TAG, "getAdvertiseSuccess advertise.getCategoryId() " + idCategory);
            }
            //TODO SEARCH AD WHEN SWIFT LEFT RIGHT
            //processDirectFragment();

        } else {
            idCategory = Constant.sosokanCategoryAll;

        }
    }

    private void processDirectFragment() {

        String url = "";
        switch (indexFragment) {
            case 0:
                //   url = String.format(new UrlEndpoint().getUrlApi(Constant.ADS) + "?categoryId=%s", idCategory);
                advertisePresenter.getListAdvertise(idCategory);
                Log.e("directFragment idCate", idCategory);
                break;

            case 1:

                break;
            case 2: // MYPOST
                // url = String.format(new UrlEndpoint().getUrlApi(Constant.ADS) + "?userId=%s", userId);
                break;
            case 3://FAVORITE
                //url = String.format(new UrlEndpoint().getUrlApi(Constant.ADS) + "?userId=%s", userId);
                break;

            case 4://FOLLOWING
                //   url = String.format(new UrlEndpoint().getUrlApi(Constant.ADS) + "?userId=%s", userId);
                break;
        }


    }

    void getUserProfileOwner(AdvertiseApi advertiseApi) {
        if (advertiseApi.getUserId() != null && !advertiseApi.getUserId().isEmpty()) {

            userProfilePresenter.getUserProfileWithLegacyId(advertiseApi.getUserId());
        }
    }

    public void getConversation() {
        //TODO  Get CONVERSATION


    }


    private void initView() {
        initViewBottomBar();
        llContentPostDetail = (RelativeLayout) findViewById(R.id.llContentPostDetail);
        llSelectFlagChoicePostDetail = (LinearLayout) findViewById(R.id.llSelectFlagChoicePostDetail);
        ibBackFlagChoicePostDetail = (ImageButton) findViewById(R.id.ibBackFlagChoicePostDetail);
        ibBackFlagChoicePostDetail.setOnClickListener(this);

        btnSendFlagChoicePostDetail = (TextView) findViewById(R.id.btnSendFlagChoicePostDetail);
        btnSendFlagChoicePostDetail.setOnClickListener(this);
        rlReport = (RelativeLayout) findViewById(R.id.rlReport);
        rlReport.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        llContentPost = (LinearLayout) findViewById(R.id.llContentPost);
        llContentPost.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        llButtonsContact = (LinearLayout) findViewById(R.id.llButtonsContact);
        mDemoSlider = (SliderLayout) findViewById(R.id.sliderPostDetail);
        rlSlideImage = (RelativeLayout) findViewById(R.id.rlSlideImage);
        intro_images = (ViewPager) findViewById(R.id.pager_introduction);

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_post);
        mapFragment.getMapAsync(this);

        ibBackPostDetail = (ImageButton) findViewById(R.id.ibBackPostDetail);
        ibBackPostDetail.setOnClickListener(this);
        ibEditPostDetail = (ImageButton) findViewById(R.id.ibEditPostDetail);
        ibEditPostDetail.setOnClickListener(this);
        ibSharePostDetail = (ImageButton) findViewById(R.id.ibSharePostDetail);
        ibSharePostDetail.setOnClickListener(this);

        ibMessagePostDetail = (ImageButton) findViewById(R.id.ibMessagePostDetail);
        ibMessagePostDetail.setOnClickListener(this);

        ibFavoritePostDetail = (ImageButton) findViewById(R.id.ibFavoritePostDetail);
        ibFavoritePostDetail.setOnClickListener(this);

        ibFacebookDetail = (ImageButton) findViewById(R.id.ibFacebookDetail);
        ibFacebookDetail.setOnClickListener(this);
        ibTwitter = (ImageButton) findViewById(R.id.ibTwitterDetail);
        ibTwitter.setOnClickListener(this);

        tvDiscountPostDetail = (TextView) findViewById(R.id.tvDiscountPostDetail);
        tvDiscountPostDetail.setBackgroundColor(getColorWithAlpha(Color.DKGRAY, 0.6f));
        tvTitlePostDetail = (TextView) findViewById(R.id.tvTitlePostDetail);

        tvCreatedDatePostDetail = (TextView) findViewById(R.id.tvCreatedDatePostDetail);
        tvDescriptionPostDetail = (TextView) findViewById(R.id.tvDescriptionPostDetail);
        tvFollowPostDetail = (TextView) findViewById(R.id.tvFollowPostDetail);
        tvFollowPostDetail.setOnClickListener(this);
        tvAvailabilityPostDetail = (TextView) findViewById(R.id.tvAvailabilityPostDetail);
        tvWebPostDetail = (TextView) findViewById(R.id.tvWebPostDetail);
        tvPhonePostDetail = (TextView) findViewById(R.id.tvPhonePostDetail);
        tvEmailPostDetail = (TextView) findViewById(R.id.tvEmailPostDetail);
        tvFaxPostDetail = (TextView) findViewById(R.id.tvFaxPostDetail);
        tvUserNamePostDetail = (TextView) findViewById(R.id.tvUserNamePostDetail);
        rlImageCoverPostDetail = (RelativeLayout) findViewById(R.id.rlImageCoverPostDetail);
        imgPostDetail = (ImageView) findViewById(R.id.imgPostDetail);
        imgProfilePostDetail = (ImageView) findViewById(R.id.imgProfilePostDetail);
        tvAdvertiseCountPostDetail = (TextView) findViewById(R.id.tvAdvertiseCountPostDetail);

        tvCategoryPostDetail = (TextView) findViewById(R.id.tvCategoryPostDetail);
        tvCategoryPostDetail.setOnClickListener(this);

        tvNumberFavoritePostDetail = (TextView) findViewById(R.id.tvNumberFavoritePostDetail);
        ratingBarPostDetail = (RatingBar) findViewById(R.id.ratingBarPostDetail);

        spPostDetail = (Spinner) findViewById(R.id.spPostDetail);
        String arr[] = getResources().getStringArray(R.array.post_action_array);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner_action, arr) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View v = null;

                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                } else {
                    v = super.getDropDownView(position, null, parent);
                }

                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        spPostDetail.getLayoutParams();
        spPostDetail.setAdapter(adapter);
        spPostDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.e("position", String.valueOf(position));
                switch (position) {
                    case 1:

                        break;
                    case 2:
                        if (user != null) {
                            processEditAdvertise();
                        } else {
                            String alert = getResources().getString(R.string.we_are_sorry);
                            String message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                            showMessageError(alert, message);
                        }
                        break;
                    case 3:
                        processDeleteAdvertise();
                        break;
                    case 4:
                        break;
                }
                spPostDetail.setSelection(0);
            }

            private void processDeleteAdvertise() {
                try {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(AdvertiseDetailApiActivity.this);
                    if (dlgAlert != null)
                        dlgAlert.create().dismiss();
                    dlgAlert.setMessage(getResources().getString(R.string.confirm_delete_ad));
                    dlgAlert.setTitle(getResources().getString(R.string.app_name));
                    dlgAlert.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (advertise != null && advertise.getCategoryId() != null && !advertise.getCategoryId().isEmpty()) {
                                // updateCategoryWhenDeleteAdvertise(advertise.getCategoryId());
                            }
                        }


                    });
                    dlgAlert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                } catch (Exception ex) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        adapter.notifyDataSetChanged();

        if (user != null) {
            spPostDetail.setVisibility(View.VISIBLE);
        } else {
            spPostDetail.setVisibility(View.GONE);
        }
        ratingBarPostDetail.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, final float rating,
                                        boolean fromUser) {
                if (user != null && userOwner != null && rating != ratingNumber) {
                  /*  Map<String, Object> ratings;
                    ratings = user.getRatings();
                    if (ratings == null) {
                        ratings = new HashMap<>();
                    }
                    ratings.put(userOwner.getId(), rating);
                    user.setRatings(ratings);
                    //TODO RATING CHECK AGAIN
                    setRatingForOwnerUser(rating);*/


                }
            }
        });

        rlVideoWebPostDetail = (RelativeLayout) findViewById(R.id.rlVideoWebPostDetail);
        rlVideoWebPostDetail.setVisibility(View.GONE);
        // webViewVideoPostDetail = (WebView) findViewById(R.id.webViewVideoPostDetail);
        webViewDescriptionPostDetail = (WebView) findViewById(R.id.webViewDescriptionPostDetail);
        ivEmailPostDetail = (ImageView) findViewById(R.id.ivEmailPostDetail);
        ivEmailPostDetail.setOnClickListener(this);
        ivPhonePostDetail = (ImageView) findViewById(R.id.ivPhonePostDetail);
        ivPhonePostDetail.setOnClickListener(this);

        llButtonsContact = (LinearLayout) findViewById(R.id.llButtonsContact);
        rlReport = (RelativeLayout) findViewById(R.id.rlReport);
        rlReport.setOnClickListener(this);

        edtComment = (EditText) findViewById(R.id.edtComment);
        btnSendComment = (ImageButton) findViewById(R.id.btnSendComment);
        btnSendComment.setOnClickListener(this);


        //========


        llCategoryAdvertisePostDetail = (LinearLayout) findViewById(R.id.llCategoryAdvertisePostDetail);
        llInformationAdvertisePostDetail = (LinearLayout) findViewById(R.id.llInformationAdvertisePostDetail);
        ibBackSelectCategory = (ImageButton) findViewById(R.id.ibBackSelectCategory);
        ibBackSelectCategory.setOnClickListener(this);

        ibWechatDetail = (ImageButton) findViewById(R.id.ibWechatDetail);
        ibWechatDetail.setOnClickListener(this);


        bottomNavigationBar = new BottomNavigationBar(this);

        scrollViewPostDetail = (ScrollView) findViewById(R.id.scrollViewPostDetail);

        llPhonePostDetail = (LinearLayout) findViewById(R.id.llPhonePostDetail);
        llFaxPostDetail = (LinearLayout) findViewById(R.id.llFaxPostDetail);
        llEmailPostDetail = (LinearLayout) findViewById(R.id.llEmailPostDetail);
        llWebsitePostDetail = (LinearLayout) findViewById(R.id.llWebsitePostDetail);
        llAddressPostDetail = (LinearLayout) findViewById(R.id.llAddressPostDetail);
        tvAddressPostDetail = (TextView) findViewById(R.id.tvAddressPostDetail);

        imgVideoPostDetail = (ImageView) findViewById(R.id.imgVideoPostDetail);
        imgVideoPostDetail.setOnClickListener(this);
    }

    private void setRatingForOwnerUser(float rating) {
        //TODO SHOW RATING
        /*Map<String, Object> rateds;
        rateds = userOwner.getRateds();
        if (rateds == null) {
            rateds = new HashMap<>();

        }
        rateds.put(user.getId(), rating);
        userOwner.setRateds(rateds);
        int countRating = 0;
        if (userOwner.getRateds() != null) {
            countRating = userOwner.getRateds().size();
        }
        tvAdvertiseCountPostDetail.setText(Integer.toString(countRating));*/
        //TODO  SHOW MESSAGE WHEN RATING SUCCESS
        String alert;
        String message;
        alert = "";
        message = getResources().getString(R.string.thank_you_for_rating);
        showMessageError(alert, message);
    }

    private void CallEditAdvertiseActivity() {
        if (advertise != null && idCategory != null && !idCategory.isEmpty()) {
            Intent intent = new Intent(this, EditAdvertiseActivity.class);
            intent.putExtra(Constant.CATEGORYID, idCategory);
            intent.putExtra(Constant.INDEX_ADVERTISE, indexAdvertise);
            intent.putExtra(Constant.ADVERTISEID, advertiseIdInt);
            intent.putExtra(Constant.FROM_MESSAGE, true);
            intent.putExtra(Constant.CONVERSATIONID, conversationId);
            intent.putExtra(Constant.ADVERTISEID, advertise.getId());
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        } else if (advertise != null) {
            Intent intent = new Intent(this, EditAdvertiseActivity.class);
            intent.putExtra(Constant.CATEGORYID, advertise.getCategoryId());
            intent.putExtra(Constant.INDEX_ADVERTISE, indexAdvertise);
            intent.putExtra(Constant.ADVERTISEID, advertiseIdInt);
            intent.putExtra(Constant.FROM_MESSAGE, true);
            intent.putExtra(Constant.CONVERSATIONID, conversationId);
            intent.putExtra(Constant.ADVERTISEID, advertise.getId());
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

    }

    public void showMessageError(String title, String message) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) AdvertiseDetailApiActivity.this).isDestroyed()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(AdvertiseDetailApiActivity.this);
            if (dlgAlert != null)
                dlgAlert.create().dismiss();
            dlgAlert.setMessage(message);
            dlgAlert.setTitle(title);
            dlgAlert.setPositiveButton(getResources().getString(R.string.ok), null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
//            }
        } catch (Exception ex) {

        }
    }


    public void searchPost() {
        Log.e(TAG, "searchPost advertiseIdInt " + advertiseIdInt);
        if (mDemoSlider != null)
            mDemoSlider.removeAllSliders();
        stImages = new ArrayList<>();
        llContentPost = (LinearLayout) findViewById(R.id.llContentPost);
        llContentPost.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        advertisePresenter.getAdvertise(advertiseIdInt);
    }

    private void setValueForView() {
        if (advertise != null) {
            getConversation();
            if (idCategory == null || idCategory.isEmpty()) {
                idCategory = advertise.getCategoryId();
            }
            if (advertise != null && user != null) {
                if (advertise.getUserId().equals(user.getLegacy_id())) {
                    ibEditPostDetail.setVisibility(View.VISIBLE);
                }

            }
            if (advertise.getName() != null && !advertise.getName().isEmpty()) {
                Log.e("Name advertise", advertise.getName());
            }
            if (advertise.getUserId() != null) {
                ApplicationUtils.closeMessage();
                setUpMapReady(googleMap);
                //TODO FIND USER OWNER
                processUpdateViewWhenGetUserOwnerSuccess();
            }

            if (advertise != null && advertise.getCategoryId() != null && !advertise.getCategoryId().isEmpty()) {
                //TODO FIND CATEGORY
                prefManager = new PrefManager(getApplication());

                List<CategoryNew> categories = prefManager.getListCategoriesApi();
                if (categories != null && categories.size() > 0) {
                    for (CategoryNew categoryNew : categories) {
                        if (categoryNew.getLegacy_id() != null && !categoryNew.getLegacy_id().isEmpty() && categoryNew.getLegacy_id().equals(advertise.getCategoryId())) {
                            category = categoryNew;
                            break;
                        }
                    }
                }
                processSetTexForCategory(category);
            }

            tvNumberFavoritePostDetail.setText(Integer.toString(advertise.getFavoriteCount()));
            if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty() && advertise.getName().contains("-")) {
                String[] separated = advertise.getName().toString().split("-");
                String stChinese = separated != null && separated.length > 0 ? separated[0] : "";
                String stEnglish = separated != null && separated.length > 1 ? separated[1] : "";
                if (isChineseApp) {
                    tvTitlePostDetail.setText(stChinese);
                } else {
                    tvTitlePostDetail.setText(stEnglish);
                }
            } else if (advertise.getName() != null) {
                tvTitlePostDetail.setText(advertise.getName());
            }
            if (advertise.getPrice() != null && !advertise.getPrice().isEmpty()) {
                tvDiscountPostDetail.setText("$" + String.valueOf(advertise.getPrice()));
            } else {
                tvDiscountPostDetail.setVisibility(View.GONE);
            }

            String desciption = advertise.getShort_description() != null && !advertise.getShort_description().isEmpty() ? advertise.getShort_description()
                    : advertise.getDescription() != null && !advertise.getDescription().isEmpty() ? advertise.getDescription() : "";


            if (DetectHtml.isHtml(desciption)) {
                setValueForDescriptionIsHtml(desciption);
            } else {
                Log.e("DetectHtml.isHtml", "false");
                tvDescriptionPostDetail.setText(desciption);
                webViewDescriptionPostDetail.setVisibility(View.GONE);
                tvDescriptionPostDetail.setVisibility(View.VISIBLE);
            }

            if (user != null && user.getLegacy_id() == advertise.getUserId()) {
                ibFavoritePostDetail.setEnabled(false);
            }


            String createdAt = DateUtils.getTimeAgo(advertise.getCreated_on(), getApplicationContext());
            tvCreatedDatePostDetail.setText(createdAt);
            if(advertise.getAdimage()!=null && advertise.getAdimage().size()>0)
            {
                showImageSlider();
            }else{
                displayImageForDetail();
            }
            getInformationFavorite();

        }
    }

    private void showImageSlider() {
        for (ImageAdvertiseApi image1 : advertise.getAdimage()) {
            TextSliderView textSliderView = new TextSliderView(AdvertiseDetailApiActivity.this);
            // initialize a SliderLayout
            textSliderView
                    .description("")
                    .image(image1.getImage().toString())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(AdvertiseDetailApiActivity.this);

            if (mDemoSlider != null) {
                mDemoSlider.addSlider(textSliderView);
            } else {
                mDemoSlider = (SliderLayout) findViewById(R.id.sliderPostDetail);
                mDemoSlider.addSlider(textSliderView);
            }
            stImages.add(image1.getImage().toString());
        }
        imgPostDetail.setVisibility(View.GONE);
//                                                                        rlSlideImage.setVisibility(View.VISIBLE);
        //  ShowSlideImage();
        if (mDemoSlider == null) {
            mDemoSlider = (SliderLayout) findViewById(R.id.sliderPostDetail);
        }
        mDemoSlider.setVisibility(View.VISIBLE);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(AdvertiseDetailApiActivity.this);
    }

    private void setValueForDescriptionIsHtml(String description) {
        Log.e("DetectHtml.isHtml", "true");
        webViewDescriptionPostDetail.setWebViewClient(new WebViewClient());
        webViewDescriptionPostDetail.getSettings().setJavaScriptEnabled(true);
        webViewDescriptionPostDetail.getSettings().setDomStorageEnabled(true);
        //
        String page = "<html><body>"
                + description
                + "</body></html>";
        webViewDescriptionPostDetail.loadDataWithBaseURL(null, page, "text/html", "UTF-8", null);
        tvDescriptionPostDetail.setVisibility(View.GONE);
        webViewDescriptionPostDetail.setVisibility(View.VISIBLE);
        webViewDescriptionPostDetail.getSettings().setUseWideViewPort(true);
        webViewDescriptionPostDetail.getSettings().setLoadWithOverviewMode(true);
        final WebSettings webSettings = webViewDescriptionPostDetail.getSettings();
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        webViewDescriptionPostDetail.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webViewDescriptionPostDetail.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webViewDescriptionPostDetail.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
        webViewDescriptionPostDetail.getSettings().setDefaultFontSize(20);
        webSettings.setDefaultFontSize(20);
    }

    private void processUpdateViewWhenGetUserOwnerSuccess() {
        if (userOwner != null && user != null) {
            //
            int countRating = 0;
            //TODO: CHECK AND GET RATING
            /*
            if (userOwner.getRateds() != null) {
                countRating = userOwner.getRateds().size();
            }*/
            tvAdvertiseCountPostDetail.setText(Integer.toString(countRating));
            tvFaxPostDetail.setText(userOwner.getFaxNumber() == null || userOwner.getFaxNumber().isEmpty() ? getResources().getString(R.string.not_available) : userOwner.getFaxNumber());
            tvPhonePostDetail.setText(userOwner.getPhoneNumber() == null || userOwner.getPhoneNumber().isEmpty() ? getResources().getString(R.string.not_available) : userOwner.getPhoneNumber());

            //TODO: CHECK AND GET DATA CONTACT FOR ADVERTISE
            /*tvEmailPostDetail.setText(userOwner.getEmail() == null || userOwner.getEmail().isEmpty() ? getResources().getString(R.string.not_available) : userOwner.getEmail());
            tvWebPostDetail.setText(userOwner.getWebsite() == null || userOwner.getWebsite().isEmpty() ? getResources().getString(R.string.not_available) : userOwner.getWebsite());
            tvUserNamePostDetail.setText(userOwner.getUserName() == null || userOwner.getUserName().isEmpty() ? getResources().getString(R.string.not_available) : userOwner.getUserName());
            */


            if (userOwner.getImage_url() != null && !userOwner.getImage_url().isEmpty()) {
                UpdateAvatarUser(userOwner.getImage_url());
            }

            //TODO: GET USER OWNER INFORMATION VIA API
            if (!user.getLegacy_id().equals(userOwner.getLegacy_id())) {
                //TODO: GET RATING
                /*Map<String, Object> ratings;
                ratings = user.getRatings();
                if (ratings == null) {
                    ratingNumber = 0;
                } else {
                    for (Map.Entry<String, Object> entry : ratings.entrySet()) {
                        String userRatedId = entry.getKey();
                        if (userRatedId.equals(userOwner.getId())) {
                            String number = entry.getValue().toString();
                            ratingNumber = Float.parseFloat(number);
                            break;
                        }
                    }
                }
                ratingBarPostDetail.setRating(ratingNumber);*/

                //TODO: ENABLE EMAIL AND PHONE
                /*if (userOwner.getEmailAble() != null && userOwner.getEmailAble()) {
                    ivEmailPostDetail.setEnabled(true);
                    ivEmailPostDetail.setAlpha(1f);
                } else {
                    ivEmailPostDetail.setEnabled(false);
                    ivEmailPostDetail.setAlpha(.6f);
                }
                if (userOwner.getCallAble() != null && userOwner.getCallAble()) {
                    ivPhonePostDetail.setEnabled(true);
                    ivPhonePostDetail.setAlpha(1f);
                } else {
                    ivPhonePostDetail.setEnabled(false);
                    ivPhonePostDetail.setAlpha(.6f);
                }*/

                ivEmailPostDetail.setEnabled(false);
                ivEmailPostDetail.setAlpha(.6f);
                ivPhonePostDetail.setEnabled(false);
                ivPhonePostDetail.setAlpha(.6f);

                //TODO: GET FOLLOWING
                /*Map<String, Object> followingUsers;
                followingUsers = user.getFollowingUsers();
                if (followingUsers != null) {
                    if (followingUsers.get(userOwner.getId()) != null) {
                        tvFollowPostDetail.setText(getResources().getString(R.string.unfollow));
                    }
                }*/
            } else {
                tvFollowPostDetail.setVisibility(View.GONE);
                ratingBarPostDetail.setVisibility(View.GONE);
                ivEmailPostDetail.setEnabled(false);
                ivPhonePostDetail.setEnabled(false);

                ivEmailPostDetail.setAlpha(.6f);
                ivPhonePostDetail.setAlpha(.6f);
            }


        }


    }

    private void processSetTexForCategory(CategoryNew category) {
        if (category != null) {
            Log.e(TAG, "processSetTexForCategory " + category.getLegacy_id());
            if (!isChineseApp) {
                tvCategoryPostDetail.setText(category.getName());
            } else {
                if (category.getNameChinese() != null && !category.getNameChinese().isEmpty()) {
                    tvCategoryPostDetail.setText(category.getNameChinese());
                } else {
                    tvCategoryPostDetail.setText(category.getName());
                }
            }

        }
    }

    private void displayImageForDetail() {
        Log.e("displayImageForDetail", "displayImageForDetail");
     /*   custom_indicator.setVisibility(View.GONE);
        custom_indicator2.setVisibility(View.GONE);*/
        if (advertise != null && advertise.getImageHeader() != null && !advertise.getImageHeader().getUrl().isEmpty()) {
            Log.e("UpdateImageCover", "getImageHeader");
            UpdateImageCover(advertise.getImageHeader().getUrl());
        } else {
            if (advertise != null && advertise.getCategoryId() != null) {
                updateImageCoverWhenAdvertiseNotHaveImage(category);
            }
        }
    }

    private void updateImageCoverWhenAdvertiseNotHaveImage(CategoryNew category) {
        if (category != null) {
            if (isChineseApp && category.getIconChinese() != null) {
                String iconUrl = category.getIconChinese();
                if (iconUrl != null && !iconUrl.isEmpty()) {
                    UpdateImageCover(iconUrl);
                }

            } else {
                String iconUrl = category.getIconEnglish();
                if (iconUrl != null && !iconUrl.isEmpty()) {
                    UpdateImageCover(iconUrl);
                }
            }
        }
    }

    public void getInformationFavorite() {
//TODO GET FAVORITE OF USER
       /* if (user != null && advertise != null) {
            mapFavorite = user.getFavorites();
            if (mapFavorite != null) {
                Object obj = mapFavorite.get(advertise.getId());
                if (obj != null) {
                    isFavorite = true;
                } else {
                    isFavorite = false;
                }
            }
            setBackgroundFavorite();
        }*/
    }

    private void setBackgroundFavorite() {
        final int sdk = Build.VERSION.SDK_INT;

        int[] favorite = advertise.getFavorite();
        if (favorite != null && favorite.length > 0) {
            tvNumberFavoritePostDetail.setText(Integer.toString(advertise.getFavoriteCount()));
            //TODO
            /*
            * GET USER INFORMATION
            * CHECK ISFAVORITE
            * */
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                if (isFavorite) {
                    ibFavoritePostDetail.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
                } else {
                    ibFavoritePostDetail.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_white_30dp));
                }

            } else {
                if (isFavorite) {
                    ibFavoritePostDetail.setBackground(getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
                } else {
                    ibFavoritePostDetail.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_white_30dp));
                }

            }
        }


    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e("Advertise Detail ", "========= onPause");
        Intent i = new Intent(this, MenuActivity.WatchServiceReceiver.class);

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
        //TODO ONSTOP SUBSCIBE
        advertisePresenter.onStop();
        categoryPresenter.onStop();
        flagChoicePresenter.onStop();
        userProfilePresenter.onStop();
        Log.e("Advertise Detail ", "========= onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Advertise Detail ", " =========== onResum");
        //initFirebaseAndGetInformation();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // getIntent() should always return the most recent
        setIntent(intent);


    }

    public void UpdateImageCover(String url) {
        Log.e("UpdateImageCover", url);
        imgPostDetail.setVisibility(View.VISIBLE);
        rlSlideImage.setVisibility(View.GONE);
        mDemoSlider.setVisibility(View.GONE);
        Glide.with(imgPostDetail.getContext())
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        int imageWidth = bitmap.getWidth();
                        int imageHeight = bitmap.getHeight();
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        int newWidth = windowManager.getDefaultDisplay().getWidth(); //this method should return the width of device screen.
                        float scaleFactor = (float) newWidth / (float) imageWidth;
                        int newHeight = (int) ((imageHeight * scaleFactor));

                        bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

                        imgPostDetail.setImageBitmap(bitmap);

                    }
                });
    }

    public void UpdateAvatarUser(String url) {
        if (url.isEmpty() || imgProfilePostDetail == null) return;
        Glide.with(imgProfilePostDetail.getContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>(imgProfilePostDetail.getWidth(), imgProfilePostDetail.getHeight()) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imgProfilePostDetail.setBackground(drawable);

                }
            }
        });

    }

    private void handleInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putInt(Constant.INDEX_ADVERTISE, indexAdvertise);
            savedInstanceState.putInt(Constant.INDEX_FRAGMENT, indexFragment);
            savedInstanceState.putString(Constant.CATEGORYID, idCategory);
            savedInstanceState.putInt(Constant.ADVERTISEID, advertiseIdInt);
            savedInstanceState.putBoolean(Constant.FROM_MESSAGE, fromMess);
            savedInstanceState.putString(Constant.CONVERSATIONID, conversationId);

            savedInstanceState.putInt("advertiseIdInt", advertiseIdInt);
            Log.e("Advertise Detail ", "=========== handleInstanceState");

            Log.e("indexAdvertise ", " " + indexAdvertise);
            Log.e("advertiseId ", " " + advertiseIdInt);
            Log.e("idCategory ", " " + idCategory);
            Log.e("indexFragment ", " " + indexFragment);
            Log.e("advertiseIdInt ", " " + advertiseIdInt);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            handleInstanceState(savedInstanceState);
            Log.e("Ad Detail SaveIns", "===================");

        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            indexAdvertise = savedInstanceState.getInt(Constant.INDEX_ADVERTISE, 0);
            advertiseIdInt = savedInstanceState.getInt(Constant.ADVERTISEID);
            idCategory = savedInstanceState.getString(Constant.CATEGORYID);
            indexFragment = savedInstanceState.getInt(Constant.INDEX_FRAGMENT, 0);
            fromMess = savedInstanceState.getBoolean(Constant.FROM_MESSAGE, false);
            conversationId = savedInstanceState.getString(Constant.CONVERSATIONID);
            advertiseIdInt = savedInstanceState.getInt("advertiseIdInt", -1);
            Log.e("Ad Detail RestoreIns", "===================");
            Log.e("Advertise Detail ", "=========== handleInstanceState");

            Log.e("indexAdvertise ", " " + indexAdvertise);
            Log.e("advertiseIdApi ", " " + advertiseIdInt);
            Log.e("idCategory ", " " + idCategory);
            Log.e("indexFragment ", " " + indexFragment);
            Log.e("advertiseIdInt ", " " + advertiseIdInt);
        }
    }

    public void onClick(View v) {
        Intent intent;

        String alert;
        String message;
        switch (v.getId()) {
            case R.id.ibBackPostDetail:
                if (fromMess) {
                    gotoMessageActivity();
                } else {
                    gotoMenuActivity();
                }


                //  finish();

                break;

            case R.id.ibFavoritePostDetail:
                processClickFavorite();

                break;
            case R.id.ibEditPostDetail:

                if (user != null) {
                    processEditAdvertise();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;

            case R.id.ibTwitterDetail:
            case R.id.ibSharePostDetail:
                if (user != null) {
                    ShareOtherApp();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case R.id.ibMessagePostDetail:
                if (user != null) {
                    gotoMessageActivity();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case R.id.tvFollowPostDetail:
                if (user != null) {
                    processFollow();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case R.id.ibFacebookDetail:
                Log.i("processShareFacebook...", "");
                if (user != null) {
                    processShareFacebook();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
           /* case R.id.ibTwitterDetail:
                //TODO: TWITTER
                // The factory instance is re-useable and thread safe.
              *//*  Twitter twitter = TwitterFactory.getSingleton();
                Status status = twitter.updateStatus(latestStatus);
                System.out.println("Successfully updated the status to [" + status.getText() + "].");*//*
                break;*/

            case R.id.tvPhonePostDetail:
            case R.id.ivPhonePostDetail:
                if ((user != null) || (advertise != null && advertise.isEnablePhone())) {
                    processCallPhone();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;

            case R.id.tvEmailPostDetail:
            case R.id.ivEmailPostDetail:
                //TODO EMAIL NOT EXIST ON API
                /*if (user != null) {
                    if (userOwner != null && userOwner.getEmailAble() != null && userOwner.getEmailAble()) {
                        if (userOwner.getEmail() != null && !userOwner.getEmail().isEmpty()) {
                            sendEmail(userOwner.getEmail());
                        } else {

                        }

                    }
                } else if (advertise != null && advertise.isEnableEmail()) {
                    if (advertise.getEmail() != null && !advertise.getEmail().isEmpty()) {
                        sendEmail(advertise.getEmail());
                    } else {

                    }
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }*/
                break;

            case R.id.btnSendFlagChoicePostDetail:
                if ((user != null) || (prefManager != null && prefManager.getUserInformationToken() != null)) {
                    if (idUserApi < 0) {
                        alert = getResources().getString(R.string.we_are_sorry);
                        message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                        showMessageError(alert, message);

                    } else {
                        if (isLoadAdvertise && advertiseIdInt < 0) {
                            alert = getResources().getString(R.string.we_are_sorry);
                            message = getResources().getString(R.string.error_occur);
                            showMessageError(alert, message);
                        } else if (flagSelected != null) {
                            sendFlagChoice();
                        } else {
                            Log.e("...", "flagSelected isLoadAdvertise advertiseIdInt " + String.valueOf(flagSelected) + " " + isLoadAdvertise + " " + advertiseIdInt);
                            alert = getResources().getString(R.string.we_are_sorry);
                            message = getResources().getString(R.string.error_send_flag);
                            showMessageError(alert, message);
                        }
                    }
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);

                }
                break;
            case R.id.rlReport:
                llContentPostDetail.setVisibility(View.GONE);
                llSelectFlagChoicePostDetail.setVisibility(View.VISIBLE);
                break;
            case R.id.ibBackFlagChoicePostDetail:
                llContentPostDetail.setVisibility(View.VISIBLE);
                llSelectFlagChoicePostDetail.setVisibility(View.GONE);
                break;
            case R.id.btnSendComment:
                if ((user != null) || (prefManager != null && prefManager.getUserInformationToken() != null)) {
                    if (advertiseIdInt > 0) {
                        if (edtComment.getText().toString().isEmpty()) {

                            alert = "";
                            message = getResources().getString(R.string.error_input_comment);
                            showMessageError(alert, message);

                        } else {
                            sendCommentViaApi();
                        }
                    } else {
                        alert = getResources().getString(R.string.we_are_sorry);
                        message = getResources().getString(R.string.error_occur);
                        showMessageError(alert, message);

                    }
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);

                }
                break;

            case R.id.tvCategoryPostDetail:
                llInformationAdvertisePostDetail.setVisibility(View.GONE);
                llCategoryAdvertisePostDetail.setVisibility(View.VISIBLE);
                break;

            case R.id.tvWebPostDetail:
                if (advertise != null && advertise.getWebsite() != null && !advertise.getWebsite().isEmpty()) {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(advertise.getWebsite()));
                    startActivity(viewIntent);
                }

                break;
            case R.id.ibBackSelectCategory:
                llInformationAdvertisePostDetail.setVisibility(View.VISIBLE);
                llCategoryAdvertisePostDetail.setVisibility(View.GONE);
                break;


            case R.id.ibWechatDetail:
                /*WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = "http://mta.info";

                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = "New York City MTA";
                msg.description = "Timetables, maps, data, and rider info.";
                msg.thumbData = Util.bmpToByteArray(thumb, true);

                // Populate request
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                //req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession; // Or one of the other scene values

// Send
                api.sendReq(req);*/
                break;
            case R.id.imgVideoPostDetail:
                //TODO CHECK VIDEO
                /*if (advertise != null && advertise.getVideo() != null && advertise.getVideo() != null && !advertise.getVideo().getVideoURL().isEmpty()) {
                    intent = new Intent(this, VideoViewActivity.class);
                    intent.putExtra("videoUrl", advertise.getVideo().getVideoURL());
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }*/
                break;
        }
    }


    private void processEditAdvertise() {
        //  CallEditAdvertiseActivity();
        if (advertise != null && user != null) {
            if (advertise.getUserId() != null && advertise.getUserId().equals(user.getId())) {
                CallEditAdvertiseActivity();
            }

        }
    }

    private void processCallPhone() {
        //TODO CALL ABLE NOT EXIST ON API
        /*if (userOwner != null && userOwner.getCallAble() != null && userOwner.getCallAble()) {
            if (userOwner.getPhoneNumber() != null && !userOwner.getPhoneNumber().trim().isEmpty()) {
                String number = "tel:" + userOwner.getPhoneNumber().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                PermissionGrantedHelper permissionGrantedHelper = new PermissionGrantedHelper(this);
                if (permissionGrantedHelper.checkPermissionCall()) {
                    startActivity(callIntent);
                } else {
                    permissionGrantedHelper.requestPermissionCall();
                    if (permissionGrantedHelper.checkPermissionCall()) {
                        startActivity(callIntent);

                    }
                }
            } else {

            }

        }*/
    }

    private void gotoMenuActivity() {
        Intent intent;
        intent = new Intent(this, MenuActivity.class);
        switch (indexFragment) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                intent.putExtra(Constant.FRAGMENT, Constant.MyPost);
                break;

            case 3:
                intent.putExtra(Constant.FRAGMENT, Constant.Favorite);
                break;
            case 4:
                intent.putExtra(Constant.FRAGMENT, Constant.Following);
                break;
            default:

                break;
        }


        if (advertise != null) {
            if (idCategory == null) {

                intent.putExtra("categoryId", advertise.getCategoryId());
            } else {

                intent.putExtra("categoryId", idCategory);
            }
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            overridePendingTransition(R.anim.start_activity_left_to_right, 0);
        }
    }

    private void processClickFavorite() {
        String alert;
        String message;
        if (user != null) {
            processFavorite();
        } else {
            alert = getResources().getString(R.string.we_are_sorry);
            message = getResources().getString(R.string.you_need_to_login_your_sosokan);
            showMessageError(alert, message);
        }
    }

    private void processFollow() {
        //TODO PROCESS FOLLOW
        /*if (user != null && userOwner != null) {
            Map<String, Object> followingUsers;
            followingUsers = user.getFollowingUsers();
            if (followingUsers == null) {
                followingUsers = new HashMap<>();
                followingUsers.put(userOwner.getId(), 0 - userOwner.getCreatedAt());
                tvFollowPostDetail.setText(getResources().getString(R.string.unfollow));
                user.setFollowingUsers(followingUsers);
            } else {
                if (followingUsers.get(userOwner.getId()) != null) {
                    followingUsers.remove(userOwner.getId());
                    tvFollowPostDetail.setText(getResources().getString(R.string.follow));
                } else {
                    followingUsers.put(userOwner.getId(), 0 - userOwner.getCreatedAt());
                    tvFollowPostDetail.setText(getResources().getString(R.string.unfollow));
                }
            }
            //TODO UPDATE FOLLOW FOR USER


            Map<String, Object> usersSubscribingMe;
            usersSubscribingMe = userOwner.getUsersSubscribingMe();
            if (usersSubscribingMe == null) {
                usersSubscribingMe = new HashMap<>();
                usersSubscribingMe.put(user.getId(), 0 - user.getCreatedAt());
                userOwner.setFollowingUsers(followingUsers);
            } else {
                if (usersSubscribingMe.get(user.getId()) != null) {
                    usersSubscribingMe.remove(user.getId());
                } else {
                    usersSubscribingMe.put(user.getId(), 0 - user.getCreatedAt());
                }
            }
            //TODO  UPDATE FOLLOW FOR USER OWNER

        }*/
    }

    private void gotoMessageActivity() {
        Intent intent;
        if (user != null && advertise != null) {

            intent = new Intent(this, MessageActivity.class);
            intent.putExtra(Constant.POSSITION, position);

            if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty() && advertise.getName().contains("-")) {
                String[] separated = advertise.getName().toString().split("-");
                String stChinese = separated != null && separated.length > 0 ? separated[0] : "";
                String stEnglish = separated != null && separated.length > 1 ? separated[1] : "";
                if (isChineseApp) {
                    intent.putExtra(Constant.TITTLE_POST, stChinese);
                } else {
                    intent.putExtra(Constant.TITTLE_POST, stEnglish);
                }
            } else if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty()) {
                intent.putExtra(Constant.TITTLE_POST, advertise.getName());
            }

            intent.putExtra(Constant.ID_USER_OWNER, advertise.getUserId());
            if (conversationId != null && !conversationId.isEmpty()) {
                intent.putExtra(Constant.CONVERSATIONID, conversationId);
            }

            intent.putExtra(Constant.ID_USER_OWNER, advertise.getUserId());
            intent.putExtra(Constant.ADVERTISE_CREATED_AT, advertise.getCreated_on());
            intent.putExtra(Constant.FROM_DETAIL, !fromMess);
            intent.putExtra(Constant.ADVERTISEID, advertise.getId());
            // intent.putExtra(Constant.ADVERTISEID, advertiseIdApi);
            intent.putExtra(Constant.CATEGORYID, idCategory);
            intent.putExtra(Constant.CONVERSATIONID, conversationId);
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.start_activity_right_to_left, 0);
            finish();
        }
    }

    protected void sendEmail(String email) {
        Log.i("Send email", "");
        String[] TO = {email};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);

        if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty() && advertise.getName().contains("-")) {
            String[] separated = advertise.getName().toString().split("-");
            String stChinese = separated != null && separated.length > 0 ? separated[0] : "";
            String stEnglish = separated != null && separated.length > 1 ? separated[1] : "";
            if (isChineseApp) {
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, stChinese);
            } else {
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, stEnglish);
            }
        } else {
            if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty()) {
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, advertise.getName());
            }

        }

        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.send_email)));
            finish();
            Log.i("Finished send email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_no_email_client));
            //  Toast.makeText(AdvertiseDetailActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void processShareFacebook() {
        Log.i("processShareFacebook...", "");
        if (advertise != null) {
            FacebookSdk.sdkInitialize(AdvertiseDetailApiActivity.this);
            callbackManager = CallbackManager.Factory.create();
            shareDialog = new ShareDialog(this);
            String deeplink = "http://sosokan.herokuapp.com/link/" + advertise.getId();
            String desciption = advertise.getDescription() != null && !advertise.getDescription().isEmpty() ? advertise.getDescription()
                    : advertise.getDescription() != null && !advertise.getDescription().isEmpty() ? advertise.getDescription() : "";
            if (advertise != null && advertise.getVideo() != null) {
                //TODO CHECK VIDEO
                /* if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareVideo shareVideo1 = new ShareVideo.Builder()
                            .setLocalUrl(Uri.parse(advertise.getVideo().getVideoURL()))
                            .build();
                    ShareContent shareContent = new ShareMediaContent.Builder()
                            .addMedium(shareVideo1)
                            .build();
                    shareDialog.show(shareContent);
                }*/
            } else {
                String imageUrl = "";

                if (advertise != null && advertise.getImageHeader() != null) {

                    imageUrl = advertise.getImageHeader().getUrl();
                    if (imageUrl == null || imageUrl.isEmpty()) {
                        if (category != null) {
                            if (isChineseApp && category.getIconChinese() != null) {
                                String iconUrl = category.getIconChinese();
                                if (iconUrl != null && !iconUrl.isEmpty()) {
                                    imageUrl = iconUrl;
                                }

                            } else {
                                String iconUrl = category.getIconEnglish();
                                if (iconUrl != null && !iconUrl.isEmpty()) {
                                    imageUrl = iconUrl;
                                }
                            }
                        }
                    }
                }
                if (ShareDialog.canShow(ShareLinkContent.class)) {

                    String nameShare;
                    if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty() && advertise.getName().contains("-")) {
                        String[] separated = advertise.getName().toString().split("-");
                        String stChinese = separated != null && separated.length > 0 ? separated[0] : "";
                        String stEnglish = separated != null && separated.length > 1 ? separated[1] : "";
                        if (isChineseApp) {
                            nameShare = stChinese;
                        } else {
                            nameShare = stEnglish;
                        }
                    } else {
                        nameShare = advertise.getName();
                    }
                    if (nameShare != null && !nameShare.isEmpty() && imageUrl != null && !imageUrl.isEmpty()) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(nameShare)
                                .setContentDescription(desciption)
                                .setContentUrl(Uri.parse(deeplink))
                                .setImageUrl(Uri.parse(imageUrl))
                                .build();

                        shareDialog.show(linkContent);
                    } else {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(nameShare)
                                .setContentDescription(desciption)
                                .setContentUrl(Uri.parse(deeplink))
                                .build();
                        shareDialog.show(linkContent);
                    }

                }
            }

            // this part is optional
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

                @Override
                public void onSuccess(Sharer.Result result) {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

        }
    }

    public void processFavorite() {
        boolean isHasFavorite;


        //TODO process favorite
        setBackgroundFavorite();
    }

    public void ShareOtherApp() {
        if (advertise != null) {

            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String urlDeeplink = "http://sosokan.herokuapp.com/link/" + advertise.getId();
            String desciption = advertise.getDescription() != null && !advertise.getDescription().isEmpty() ? advertise.getDescription() : "";
            String nameAd = "";
            if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty() && advertise.getName().contains("-")) {

                String[] separated = advertise.getName().toString().split("-");
                String stChinese = separated != null && separated.length > 0 ? separated[0] : "";
                String stEnglish = separated != null && separated.length > 1 ? separated[1] : "";

                nameAd = isChineseApp ? stChinese : stEnglish;

            } else {
                if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty()) {
                    nameAd = advertise.getName();
                }

            }
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, nameAd);
            if (advertise.getDescription() != null && DetectHtml.isHtml(advertise.getDescription())) {
                Log.e(TAG, "EXTRA_HTML_TEXT advertise.getDescription()" + advertise.getDescription());
                shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, advertise.getDescription());
            } else if (!advertise.getDescription().isEmpty()) {
                Log.e(TAG, "EXTRA_HTML_TEXT advertise.getHtmlDescription() " + advertise.getDescription());
                shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, advertise.getDescription());
            } else {
                Log.e(TAG, "desciption " + desciption);
                shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, desciption);
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        //   setUpMapReady(map);


    }

    public void setupMap() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                // setUpMapReady(mMap);
            }
        });
    }

    public void setUpMapReady(GoogleMap map) {
        if (advertise != null && advertise.getLocation() != null && !advertise.getLocation().isEmpty()) {
            LatLng latLng = null;
            String[] separated = advertise.getLocation().split(";");
            String stLat, stLongitude;
            double lat = 0, log = 0;
            if (separated != null && separated.length > 1) {
                String location = separated[1];
                location = location.replace("POINT (", "");
                location = location.replace(")", "");
                if (location != null && !location.isEmpty())
                    Log.e("setUpMapReady location ", location);
                String[] separatedLocation = location.split(" ");
                stLat = separatedLocation[0];
                stLongitude = separatedLocation[1];
                if (stLat != null && !stLat.isEmpty()) {
                    lat = Double.parseDouble(stLat);
                    Log.e("setUpMapReady lat ", String.valueOf(lat));
                }
                if (stLongitude != null && !stLongitude.isEmpty()) {
                    log = Double.parseDouble(stLongitude);
                    Log.e("setUpMapReady log ", String.valueOf(log));
                }
            }

            latLng = new LatLng(lat, log);
            if (latLng != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map)));

            }

        } else {
            mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map_post);
            if (mapFragment != null && mapFragment.getView() != null) {
                mapFragment.getView().setVisibility(View.GONE);
            }


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llButtonsContact.getLayoutParams();

            params.setMargins(0, 0, 0, 0);
            llButtonsContact.setLayoutParams(params);
        }
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        //   return gestureDetector.onTouchEvent(ev);
        return llContentPost.onTouchEvent(ev);
    }

    @Override
    public void onFailure(String appErrorMessage) {
        mapCategoryChildren = new HashMap<>();
        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        prefManager = new PrefManager(this);
        categories = prefManager.getListCategoriesApi();
        if (categories != null && categories.size() > 0) {
            categoriesChild = new ArrayList<>();
            processDataMenu();
        }

    }

    @Override
    public void onFlagChoiceFailure(String appErrorMessage) {
        flagChoices = prefManager.getFlagchoices();
        setAdapterForFlagChoises();
    }

    @Override
    public void getFlagchoiceSuccess(FlagChoiceResponse flagChoiceResponse) {
        flagChoices = flagChoiceResponse.getResults();
        if (prefManager == null)
            prefManager = new PrefManager(getApplicationContext());
        prefManager.setFlagchoices(flagChoices);
        setAdapterForFlagChoises();
    }

    @Override
    public void onAdvertiseFailure(String appErrorMessage) {

    }

    @Override
    public void getListAdvertiseSuccess(AdvertiseResponse advertiseResponse) {
        advertiseIds = new ArrayList<>();
        Log.e(TAG, "getListAdvertiseSuccess advertiseResponse" + advertiseResponse);
        indexAdvertise = 0;
        advertiseApis = advertiseResponse.getResults();
        for (int i = 0; i < advertiseResponse.getResults().size(); i++) {
            AdvertiseApi advertiseApi = advertiseResponse.getResults().get(i);
            if (advertiseApi != null) {
                if (isChineseApp && advertiseApi.isChinese()) {
                    advertiseIds.add(String.valueOf(advertiseApi.getId()));
                } else if (!isChineseApp && !advertiseApi.isChinese()) {
                    advertiseIds.add(String.valueOf(advertiseApi.getId()));
                }
            }
        }
    }

    @Override
    public void getAdvertiseSuccess(AdvertiseApi advertiseApi) {
        ApplicationUtils.closeMessage();
        advertise = advertiseApi;
        if (advertise != null) {
            advertiseIdInt = advertise.getId();
            isLoadAdvertise = true;
            if (advertise.getComments() != null) {
                commentNews = advertise.getComments();
                updateUIForListComment();
            }
            //TODO UPDATE UI FOR ADVERTISE
            setValueForView();

            getUserProfileOwner(advertise);
        }

    }

    @Override
    public void onUserProfileFailure(String appErrorMessage) {
        tvUserNamePostDetail.setText("");
        Drawable icon = getResources().getDrawable(R.drawable.no_avatar);
        imgProfilePostDetail.setBackground(icon);
        if (prefManager == null) prefManager = new PrefManager(this);
        user = prefManager.getUserProfile();
    }

    @Override
    public void getUserProfileSuccess(UserProfileResponse userProfileResponse) {
        //TODO UPDATE USER PROFILE
        if (userProfileResponse != null) {
            if (userProfileResponse.getResults() != null && userProfileResponse.getResults().size() > 0) {
                UserProfileApi userProfile = userProfileResponse.getResults().get(0);
                if (userProfile != null && userProfile.getImage_url() != null && !userProfile.getImage_url().isEmpty()) {
                    UpdateAvatarUser(userProfile.getImage_url());
                }
                tvUserNamePostDetail.setText(userProfile.getDisplay_name());
            }

        }
    }

    @Override
    public void getCategoryListSuccess(List<CategoryNew> categoryListResponse) {
        categories = categoryListResponse;
        categories = new ArrayList<>();
        mapCategory = new HashMap<>();
        categoriesChild = new ArrayList<>();
        processDataMenu();
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                //TODO GET AD WHEN SLIDE LEFT RIGHT
                Log.e(TAG, "=========== onFling ==========  ");
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                    Log.e(TAG, "=========== onFling Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH  ");
                    return false;
                }

                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    /*if (advertiseIds != null && advertiseIds.size() > 0) {
                        int index = indexAdvertise - 1;

                        Log.e(TAG, "onFling index  " + index);

                        if (index >= 0) {
                            indexAdvertise = index;

                            if (advertiseIds.get(indexAdvertise) != null && !advertiseIds.get(indexAdvertise).isEmpty()) {

                                advertiseIdInt = Integer.parseInt(advertiseIds.get(indexAdvertise));
                                searchPost();
                            }
                        }

                    }*/
                    if (advertiseIds != null && advertiseIds.size() > 0) {
                        int index = indexAdvertise + 1;
                        Log.e(TAG, "onFling index  " + index);

                        if (index < advertiseApis.size()) {

                            indexAdvertise = index;

                            if (advertiseApis.get(indexAdvertise) != null) {
                                advertise = advertiseApis.get(indexAdvertise);
                                if (advertise != null) {
                                    advertiseIdInt = advertise.getId();
                                    isLoadAdvertise = true;
                                    if (advertise.getComments() != null) {
                                        commentNews = advertise.getComments();
                                        updateUIForListComment();
                                    }
                                    //TODO UPDATE UI FOR ADVERTISE
                                    setValueForView();

                                    getUserProfileOwner(advertise);
                                }
                            }
                        }
                    }
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    /*if (advertiseIds != null && advertiseIds.size() > 0) {
                        int index = indexAdvertise + 1;
                        if (index < advertiseIds.size()) {

                            indexAdvertise = index;

                            if (advertiseIds.get(indexAdvertise) != null && !advertiseIds.get(indexAdvertise).isEmpty()) {
                                advertiseIdInt = Integer.parseInt(advertiseIds.get(indexAdvertise));
                                searchPost();
                            }
                        }

                    }*/
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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

    private void setupRecycleViewFlagChoice() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        flagChoiceAdapter = new FlagChoiceAdapter(this, flagChoices);
        StaggeredGridLayoutManager mLayoutManagerAdvertiseList = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(flagChoiceAdapter);

        recyclerView.setLayoutManager(mLayoutManagerAdvertiseList);

        //   recyclerView.setNestedScrollingEnabled(false);
        flagChoiceAdapter.setListener(new FlagChoiceAdapter.Listener() {
            @Override
            public void onItemClick(int position) {
                FlagChoice flagChoice = flagChoices.get(position);
                if (flagChoice != null) {
                    if (user != null) {

                        flagSelected = new Flag();
                        String rtUrl = new UrlEndpoint().getUrlApi(""); //LIVE NOT WORK
                        String stAd = String.format(rtUrl + "api/ads/%s/", advertiseIdInt);
                        String stUser = String.format(rtUrl + "api/users/%s/", idUserApi);
                        String stReason = String.format(rtUrl + "api/flagchoices/%s/", flagChoice.getId());
                        flagSelected.setAd(stAd);
                        flagSelected.setReason(stReason);
                        flagSelected.setUser(stUser);
                    }
                }
            }
        });

    }


    private void setAdapterForFlagChoises() {
        flagChoiceAdapter = new FlagChoiceAdapter(getApplicationContext(), flagChoices);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(flagChoiceAdapter);
        flagChoiceAdapter.notifyDataSetChanged();
        flagChoiceAdapter.setListener(new FlagChoiceAdapter.Listener() {
            @Override
            public void onItemClick(int position) {
                Log.e(TAG, "onItemClick flag choice => " + position);
                FlagChoice flagChoice = flagChoices.get(position);
                if (flagChoice != null) {
                    if (user != null && advertiseIdInt > 0) {
                        flagSelected = new Flag();
                        String rtUrl = urlEndpoint.getUrlApi("");
                        String stAd = String.format(rtUrl + "api/ads/%s/", advertiseIdInt);
                        String stUser = String.format(rtUrl + "api/users/%s/", idUserApi);
                        String stReason = String.format(rtUrl + "api/flagchoices/%s/", flagChoice.getId());
                        flagSelected.setAd(stAd);
                        flagSelected.setReason(stReason);
                        flagSelected.setUser(stUser);
                    }
                }
            }
        });
    }

    public void sendFlagChoice() {
        String url = urlEndpoint.getUrlApi(Constant.FLAGS);
        Log.e(TAG, "===================== sendFlagChoice url " + url);
        Gson gson = new Gson();
        final String json = gson.toJson(flagSelected);
        Log.e(TAG, "sendFlagChoice flagSelected " + json);
        Log.e(TAG, "sendFlagChoice json  " + json);

        if (flagSelected != null && url != null && !url.isEmpty() && json != null && !json.isEmpty()) {
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            handleResponseSendFlagChoice(response);
                        }
                    },
                    volleyErrListener
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return json == null ? null : json.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

                @Override
                protected Map<String, String> getParams() {


                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ad", flagSelected.getAd());
                    params.put("user", flagSelected.getUser());
                    params.put("reason", flagSelected.getReason());
                    return params;
                }

                public Map<String, String> getHeaders() throws AuthFailureError {
                    return urlEndpoint.getHeaderWithTokenUser(getApplicationContext());
                }
            };

            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    3,
                    1f));
            mQueue.add(postRequest);
        }
    }

    private void handleResponseSendFlagChoice(String response) {
        Log.e(TAG, "sendFlagChoice response  " + response);
        if (String.valueOf(response).equals("201")) {
            String alert = "";
            String message = getResources().getString(R.string.thank_you_for_report);
            showMessageError(alert, message);
            flagSelected = null;

            llContentPostDetail.setVisibility(View.VISIBLE);
            llSelectFlagChoicePostDetail.setVisibility(View.GONE);

        }
    }


    private void setupRecycleViewComments() {
        rvComments = (RecyclerView) findViewById(R.id.rvComments);
        rvComments.setHasFixedSize(true);
        commentsAdapter = new CommentsApiAdapter(this, commentNews);
        StaggeredGridLayoutManager mLayoutManagerAdvertiseList = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvComments.setHasFixedSize(true);

        rvComments.setItemAnimator(new DefaultItemAnimator());

        rvComments.setAdapter(commentsAdapter);

        rvComments.setLayoutManager(mLayoutManagerAdvertiseList);


    }


    public void sendCommentViaApi() {

        String url = urlEndpoint.getUrlApi(Constant.COMMENTS);
        if (urlEndpoint.getUserToken(this).isEmpty()) {
            Log.e(TAG, "  send comment getUserToken NULL ");
            String alert = getResources().getString(R.string.we_are_sorry);
            String message = getResources().getString(R.string.error_occur);
            showMessageError(alert, message);
        } else {
            if (url != null && !url.isEmpty()) {
                Log.e(TAG, "  send comment url " + url);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        volleyResponseListenerComment(),
                        volleyErrListener
                ) {


                    @Override
                    protected Map<String, String> getParams() {

                        return getParamForSendComment();
                    }

                    public Map<String, String> getHeaders() throws AuthFailureError {

                        return urlEndpoint.getHeaderWithTokenUser(getApplicationContext());
                    }
                };

                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        3,
                        1f));
                mQueue.add(postRequest);
            }
        }
    }

    @NonNull
    private Response.Listener<String> volleyResponseListenerComment() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                handleResponseSendComment(response);
            }
        };
    }

    @NonNull
    private Map<String, String> getParamForSendComment() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("object_pk", String.valueOf(advertiseIdInt));
        params.put("comment", edtComment.getText().toString());
        params.put("ip_address", new NetworkUtils().getLocalIpAddress());
        params.put("is_public", "true");
        params.put("is_removed", "false");
        params.put("submit_date", DateUtils.getCurrentTime());

        return params;
    }

    private void handleResponseSendComment(String response) {
        Log.e(TAG, "  send comment response " + response);
        if (response != null) {
            Gson gson = new Gson();
            CommentNew commentNew = gson.fromJson(response, CommentNew.class);
            if (commentNew != null) {
                commentNews.add(commentNew);
                updateUIForListComment();
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                edtComment.setText("");
                scrollViewPostDetail.fullScroll(View.FOCUS_DOWN);
            }
            //commentNews.add(commentNew);

        }
    }

    private void updateUIForListComment() {
        commentsAdapter = new CommentsApiAdapter(getApplicationContext(), commentNews);
        StaggeredGridLayoutManager mLayoutManagerAdvertiseList = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvComments.setHasFixedSize(true);

        rvComments.setItemAnimator(new DefaultItemAnimator());

        rvComments.setAdapter(commentsAdapter);

        rvComments.setLayoutManager(mLayoutManagerAdvertiseList);
        rvComments.setNestedScrollingEnabled(false);
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    commentsAdapter.notifyDataSetChanged();// Notify the commentAdapter
                } catch (Exception e) {
                    Log.e(TAG, "rvComments ex => " + e);
                }
            }
        });
    }

    private Response.ErrorListener volleyErrListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: handle error
            Log.e(TAG, "Error: " + error.getMessage());
        }
    };

    private void initViewRecycleCategory() {
        mListView = (MultiLevelListView) findViewById(R.id.listViewCategorySelect);

        listAdapter = new ListCategoryApiAdapter(AdvertiseDetailApiActivity.this);
        multiLevelListAdapter = mListView.getmAdapter();
        if (multiLevelListAdapter == null) {
            mListView.setAdapter(listAdapter);
        } else {

            mListView.setAdapter(multiLevelListAdapter);
        }


        // mListView.setOnItemClickListener(mOnItemClickListener);
        mapCategoryChildren = new HashMap<>();
        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        prefManager = new PrefManager(this);


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
                    mapCategory.put(String.valueOf(categories.get(i).getId()), categories.get(i));
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
                        //   Log.e(TAG, "categories.get(i).getLegacy_id() " + categories.get(i).getLegacy_id());
                    }

                }
            }
        }

        Collections.sort(categoriesChild, new CategoryApiSortComparator());
        categoriesChild.add(0, categoryAll);
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
                    listCategoryChildren.add(category);
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
        if (categoriesChild != null && categoriesChild.size() > 0) {
            categorySelected = categoriesChild.get(0);
        }
    }

    private void initViewBottomBar() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
        refreshBottomBar();


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
                intent = new Intent(this, MenuActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case 1:
                intent = new Intent(this, MenuActivity.class);
                intent.putExtra("ShowSearch", true);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                //   parentActivity.showViewSearch();
                //  broadcastIntentSearchHome();
                break;
            case 2:


                String alert;
                String message;
                if (user != null) {
                    intent = new Intent(this, NewAdvertiseActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case 3:
                if (user != null) {
                    intent = new Intent(this, MenuActivity.class);
                    intent.putExtra("Fragment", "Favorite");
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case 4:
                intent = new Intent(this, MenuActivity.class);
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
                intent = new Intent(this, MenuActivity.class);
                //  Category category = idCategory;
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case 1:
                //showViewSearch();
                break;
            case 2:
                String alert;
                String message;
                if (user != null) {
                    intent = new Intent(this, NewAdvertiseActivity.class);
                    startActivity(intent);
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case 3:
                if (user != null) {
                    intent = new Intent(this, MenuActivity.class);
                    intent.putExtra("Fragment", "Favorite");
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case 4:
                intent = new Intent(this, MenuActivity.class);
                intent.putExtra("Fragment", "Setting");
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }

    }

}
