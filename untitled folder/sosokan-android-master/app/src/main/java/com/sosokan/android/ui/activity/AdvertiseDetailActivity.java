package com.sosokan.android.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
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
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sosokan.android.R;
import com.sosokan.android.adapter.CommentAdapter;
import com.sosokan.android.adapter.CommentsApiAdapter;
import com.sosokan.android.adapter.FlagChoiceAdapter;
import com.sosokan.android.adapter.ViewPagerAdapter;
import com.sosokan.android.control.multi.level.menu.DataProviderCategoryApi;
import com.sosokan.android.control.multi.level.menu.ItemInfo;
import com.sosokan.android.control.multi.level.menu.ListCategoryApiAdapter;
import com.sosokan.android.control.multi.level.menu.MultiLevelListAdapter;
import com.sosokan.android.control.multi.level.menu.MultiLevelListView;
import com.sosokan.android.control.multi.level.menu.OnItemCategoryClickListener;
import com.sosokan.android.events.Listener.EndCallListener;
import com.sosokan.android.models.AdvertiseApi;


import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.CommentNew;
import com.sosokan.android.models.Conversation;
import com.sosokan.android.models.Flag;
import com.sosokan.android.models.FlagChoice;
import com.sosokan.android.models.Image;
import com.sosokan.android.models.LocationSosokan;
import com.sosokan.android.models.User;
import com.sosokan.android.rest.UrlEndpoint;
import com.sosokan.android.utils.ApplicationUtils;

import com.sosokan.android.utils.comparator.CategoryApiSortComparator;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;

import com.sosokan.android.utils.DateUtils;
import com.sosokan.android.utils.DetectHtml;
import com.sosokan.android.utils.DividerItemDecoration;
import com.sosokan.android.utils.LocaleHelper;
import com.sosokan.android.utils.PermissionGrantedHelper;
import com.sosokan.android.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;


import static com.sosokan.android.utils.ColorHelper.getColorWithAlpha;

import com.sosokan.android.control.FullscreenVideoLayout;

/**
 * Created by AnhZin on 8/24/2016.
 */
public class AdvertiseDetailActivity extends Activity implements View.OnClickListener, OnMapReadyCallback,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, BottomNavigationBar.OnTabSelectedListener {

    private static final String TAG = "AdvertiseDetailActivity";
    private GoogleMap googleMap;
    ImageButton ibBackPostDetail, ibEditPostDetail, ibSharePostDetail, ibMessagePostDetail, ibFavoritePostDetail;
    TextView tvDiscountPostDetail, tvTitlePostDetail, tvCreatedDatePostDetail, tvDescriptionPostDetail,
            tvAvailabilityPostDetail, tvWebPostDetail, tvPhonePostDetail, tvEmailPostDetail, tvFaxPostDetail,
            tvUserNamePostDetail, tvAdvertiseCountPostDetail, tvCategoryPostDetail;
    RelativeLayout rlImageCoverPostDetail;
    ImageView imgPostDetail, imgProfilePostDetail;
    CommentAdapter commentAdapter;

    int position;

    Advertise advertise;
    FirebaseUser mUser;
    User user;
    User userOwner;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Query queryConversation;

    FullscreenVideoLayout videoLayout;
    MapFragment mapFragment;
    RatingBar ratingBarPostDetail;
    WebView webViewDescriptionPostDetail;
    RelativeLayout rlVideoWebPostDetail;
    String advertiseId;
    String conversationId;
    public String userId;
    List<Conversation> conversationList;
    private static DatabaseReference refConversation;
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
    String idCategory, idCategorySelected;
    LinearLayout llButtonsContact;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    CategoryNew category;
    int indexAdvertise;
    Map<String, Object> advertiseIds;
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
    private String legacyId;
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

    //============= COMMENTS
    RecyclerView rvComments;
    CommentsApiAdapter commentsAdapter;
    List<CommentNew> commentNews;
    PrefManager prefManager;
    EditText edtComment;
    ImageButton btnSendComment;
    int advertiseIdInt = -1;
    boolean isLoadAdvertise;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Fabric.with(this, new Crashlytics());
        if (getApplicationContext() != null) {
            Buddy.init(getApplicationContext(), "appId", "appKey");
        }
        initValue();
        initView();

        initViewRecycleCategory();

        setupRecycleViewFlagChoice();
        setupRecycleViewComments();

        //initDataFakeComment();
        setupMap();


        EndCallListener callListener = new EndCallListener();
        TelephonyManager mTM = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
        initFirebaseAndGetUser();
        initFirebaseAndGetInformation();
        handleInstanceState(savedInstanceState);

        if (prefManager.getFlagchoices() != null) {
            flagChoices = prefManager.getFlagchoices();
            setAdapterForFlagChoises();
        } else {
            getFlagChoices();
        }

        getAdvertiseViaApiWithIdOfFirebase();
        Log.e("Advertise Detail ", "=========== Created");

    }

    private void initValue() {

        String languageToLoad = LocaleHelper.getLanguage(AdvertiseDetailActivity.this);
        isChineseApp = languageToLoad.toString().equals("zh");

        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(this);
        }
        indexAdvertise = 0;
        conversationList = new ArrayList<>();

        gestureDetector = new GestureDetector(this, new MyGestureDetector());

        stImages = new ArrayList<>();
        mQueue = Volley.newRequestQueue(this);
        flagChoices = new ArrayList<>();
        commentNews = new ArrayList<>();
        prefManager = new PrefManager(getApplication());
        user = prefManager.getUser();
        idUserApi = prefManager.getPrimaryKeyUser();

        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        mapCategoryChildren = new HashMap<>();
        categorySelectedId = Constant.sosokanCategoryAll;
        if (getIntent() != null) {
            indexAdvertise = getIntent().getIntExtra(Constant.INDEX_ADVERTISE, 0);
            advertiseId = getIntent().getStringExtra(Constant.ADVERTISEID);

            idCategory = getIntent().getStringExtra(Constant.CATEGORYID);
            idCategorySelected = getIntent().getStringExtra(Constant.CATEGORYID);
            indexFragment = getIntent().getIntExtra(Constant.INDEX_FRAGMENT, 0);
            fromMess = getIntent().getBooleanExtra(Constant.FROM_MESSAGE, false);
            conversationId = getIntent().getStringExtra(Constant.CONVERSATIONID);
            Log.e("getIntent ", " " + "initFirebaseAndGetInformation");
            Log.e("indexAdvertise ", " " + indexAdvertise);
            Log.e("advertiseId ", " " + advertiseId);
            Log.e("idCategory ", " " + idCategory);
            Log.e("indexFragment ", " " + indexFragment);
            //  searchPost();
        }
        urlEndpoint = new UrlEndpoint();
        token = urlEndpoint.getUserToken(getApplicationContext());
    }

    private void initFirebaseAndGetUser() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();
            }
        };
        refConversation = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.CONVERSATIONS);
        if (mUser != null) {
            FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(mUser.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                Log.e(TAG, "mUser.getUid() " + mUser.getUid());
                                ApplicationUtils.closeMessage();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } else {
            ApplicationUtils.closeMessage();
        }

    }

    private void initFirebaseAndGetInformation() {
        if (getIntent() != null) {

            switch (indexFragment) {
                case 0:
                    queryAdvertiseIDForHomeFragment();
                    break;

                case 1:

                    break;
                case 2:
                    if (user != null && user.getAdvertises() != null && user.getAdvertises().size() > 0) {
                        advertiseIds = user.getAdvertises();
                        findeIdOfAdvertiseInRange();
                    }
                    break;
                case 3:
                    if (user != null && user.getFavorites() != null && user.getFavorites().size() > 0) {
                        advertiseIds = user.getFavorites();
                        findeIdOfAdvertiseInRange();
                    }
                    break;

                case 4:
                    if (user != null && user.getFollowingUsers() != null && user.getFollowingUsers().size() > 0) {

                        advertiseIds = new HashMap<>();
                        final Map<String, Object> subscriptions = user.getFollowingUsers();

                        if (subscriptions != null) {
                            for (Map.Entry<String, Object> entry : subscriptions.entrySet()) {
                                String keySubscriptions = entry.getKey();
                                Log.e("keySubscriptions ", String.valueOf(keySubscriptions));
                                String value = entry.getValue().toString();

                                FirebaseDatabase.getInstance()
                                        .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(keySubscriptions).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        try {
                                            User userOwner = dataSnapshot.getValue(User.class);
                                            if (userOwner != null) {
                                                Map<String, Object> advertisesUser = userOwner.getAdvertises();
                                                advertiseIds.putAll(advertisesUser);
                                                findeIdOfAdvertiseInRange();

                                            }
                                        } catch (Exception ex) {
                                            System.out.print("ERRORR " + ex);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                        } else {
                            ApplicationUtils.closeMessage();
                        }
                    }

                    break;

            }

        } else {
            idCategory = Constant.sosokanCategoryAll;
            setUpFireBase();
        }
    }

    private void findeIdOfAdvertiseInRange() {
        if (advertiseId == null || advertiseId.isEmpty()) {
            int index = 0;
            for (Map.Entry<String, Object> entry : advertiseIds.entrySet()) {
                if (index == indexAdvertise) {
                    advertiseId = entry.getKey();
                    getAdvertiseViaApiWithIdOfFirebase();
                    setUpFireBase();
                    break;
                }
                index++;
                //System.out.println(entry.getKey() + "/" + entry.getValue());
            }
        } else {
            setUpFireBase();
        }
    }

    private void queryAdvertiseIDForHomeFragment() {
        if (advertiseId != null && idCategory != null) {
            Log.e(Constant.ADVERTISEID, advertiseId);

            Log.e(Constant.CATEGORYID, idCategory);
            /*FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.CATEGORIES).child(idCategory).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        category = snapshot.getValue(CategoryNew.class);
                        if (isChineseApp) {
                            advertiseIds = category.getAdvertisesChinese();
                        } else {
                            advertiseIds = category.getAdvertisesEnglish();
                        }

                    } catch (Exception ex) {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });*/
            setUpFireBase();
        } else if (idCategory != null) {
            /*FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.CATEGORIES).child(idCategory).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        category = snapshot.getValue(CategoryNew.class);
                        if (isChineseApp) {
                            advertiseIds = category.getAdvertisesChinese();
                        } else {
                            advertiseIds = category.getAdvertisesEnglish();
                        }
                        if (advertiseIds != null) {
                            findeIdOfAdvertiseInRange();
                        }
                    } catch (Exception ex) {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });*/
        }
    }

    public void setUpFireBase() {
        refConversation = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.CONVERSATIONS);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();
            }
        };
        searchPost();
        ApplicationUtils.closeMessage();
        if (user != null) {


        } else if (mUser != null) {
            FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(mUser.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                userId = user.getId();
                                mapFavorite = user.getFavorites();

                              /*  searchPost();
                                ApplicationUtils.closeMessage();*/
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        } else {
            ApplicationUtils.closeMessage();
        }
    }

    public void getConversation() {
        // refConversation.orderByChild(advertiseId).equals(advertiseId).
        if (advertiseId != null && !advertiseId.isEmpty()) {
            queryConversation = refConversation.orderByChild(Constant.ADVERTISEID).equalTo(advertiseId);
            queryConversation.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Conversation conversation = snapshot.getValue(Conversation.class);
                        conversationList.add(conversation);
                    }
                    findConversationId();
                    ApplicationUtils.closeMessage();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public void findConversationId() {
        if (conversationList != null && conversationList.size() > 0) {
            for (Conversation conversation : conversationList) {
                Map<String, Object> mapUser = conversation.getUsers();
                if (mapUser != null) {
                    for (Map.Entry<String, Object> entry : mapUser.entrySet()) {
                        String userId = entry.getKey();
                        if (userId.equals(this.userId)) {
                            conversationId = conversation.getConversationId();
                            break;
                        }
                    }
                }
            }
        }


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
        llContentPost = (LinearLayout) findViewById(R.id.llContentPost);
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
        tvWebPostDetail.setOnClickListener(this);
        tvPhonePostDetail = (TextView) findViewById(R.id.tvPhonePostDetail);
        tvPhonePostDetail.setOnClickListener(this);
        tvEmailPostDetail = (TextView) findViewById(R.id.tvEmailPostDetail);
        tvEmailPostDetail.setOnClickListener(this);

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
                        if (user != null && user.isVerify()) {
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
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(AdvertiseDetailActivity.this);
                    if (dlgAlert != null)
                        dlgAlert.create().dismiss();
                    dlgAlert.setMessage(getResources().getString(R.string.confirm_delete_ad));
                    dlgAlert.setTitle(getResources().getString(R.string.app_name));
                    dlgAlert.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (advertise != null && advertise.getCategoryId() != null && !advertise.getCategoryId().isEmpty()) {
                                updateCategoryWhenDeleteAdvertise(advertise.getCategoryId());
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

        if (user != null && user.isVerify() && advertiseId != null && !advertiseId.isEmpty()) {
            spPostDetail.setVisibility(View.VISIBLE);
        } else {
            spPostDetail.setVisibility(View.GONE);
        }
        ratingBarPostDetail.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, final float rating,
                                        boolean fromUser) {
                if (user != null && userOwner != null && rating != ratingNumber) {
                    Map<String, Object> ratings;
                    ratings = user.getRatings();
                    if (ratings == null) {
                        ratings = new HashMap<>();
                    }
                    ratings.put(userOwner.getId(), rating);
                    user.setRatings(ratings);
                    PrefManager prefManager = new PrefManager(getApplicationContext());
                    prefManager.setUser(user);
                    FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                            } else {
                                Map<String, Object> rateds;
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
                                tvAdvertiseCountPostDetail.setText(Integer.toString(countRating));
                                FirebaseDatabase.getInstance()
                                        .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(userOwner.getId()).setValue(userOwner, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                        } else {
                                            String alert;
                                            String message;
                                            alert = "";
                                            message = getResources().getString(R.string.thank_you_for_rating);
                                            showMessageError(alert, message);
                                        }
                                    }
                                });
                            }
                        }
                    });


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

    private void updateCategoryWhenDeleteAdvertise(final String cateId) {
        /*FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.CATEGORIES).child(cateId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    CategoryNew category = snapshot.getValue(CategoryNew.class);
                    if (category != null) {
                        category.setUpdatedAt(DateUtils.getDateInformation());
                        if (advertise.isChinese()) {
                            Map<String, Object> mapAds = category.getAdvertisesChinese();
                            if (mapAds != null) {
                                mapAds.remove(advertise.getId());
                                category.setAdvertisesChinese(mapAds);
                            }
                        } else {
                            Map<String, Object> mapAds = category.getAdvertisesEnglish();
                            if (mapAds != null) {
                                mapAds.remove(advertise.getId());
                                category.setAdvertisesEnglish(mapAds);
                            }
                        }
                        FirebaseDatabase.getInstance()
                                .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.CATEGORIES).child(cateId).setValue(category);
                        String parentId = category.getParentId();
                        if (parentId == null || parentId.isEmpty()) {
                            FirebaseDatabase.getInstance()
                                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.ADVERTISES).child(advertise.getId()).removeValue();
                            indexFragment = 2; //GOTO MYPOST
                            gotoMenuActivity();

                        } else {
                            updateCategoryWhenDeleteAdvertise(parentId);
                        }

                    }
                } catch (Exception ex) {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });*/
    }

    private void ShowSlideImage() {
        if (stImages != null && stImages.size() > 0) {
            rlSlideImage.setVisibility(View.VISIBLE);
            imgPostDetail.setVisibility(View.GONE);
            mAdapter = new ViewPagerAdapter(this, stImages);
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
                            if (dots[i] != null && getApplicationContext() != null) {
                                dots[i] = new ImageView(getApplicationContext());
                                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                                params.setMargins(4, 0, 4, 0);

                                pager_indicator.addView(dots[i], params);
                            }

                        }
                        if (dots[position] != null) {
                            dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                        }

                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            setUiPageViewController();
        } else {
            rlSlideImage.setVisibility(View.GONE);
            imgPostDetail.setVisibility(View.VISIBLE);
        }
    }

    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];
        pager_indicator.removeAllViews();


        if (dots != null && dots.length > 1) {
            for (int i = 0; i < dotsCount; i++) {
                if (dots[i] != null && getApplicationContext() != null) {
                    dots[i] = new ImageView(getApplicationContext());
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    params.setMargins(4, 0, 4, 0);

                    pager_indicator.addView(dots[i], params);
                }

            }
            if (dots[0] != null) {
                dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            }

        }

    }

    private void CallEditAdvertiseActivity() {
        if (advertise != null && idCategory != null && !idCategory.isEmpty()) {
            Intent intent = new Intent(this, EditAdvertiseActivity.class);
            intent.putExtra(Constant.CATEGORYID, idCategory);
            intent.putExtra(Constant.INDEX_ADVERTISE, indexAdvertise);
            intent.putExtra(Constant.ADVERTISEID, advertiseId);
            intent.putExtra(Constant.FROM_MESSAGE, true);
            intent.putExtra(Constant.CONVERSATIONID, conversationId);
            intent.putExtra(Constant.ADVERTISEID, advertise.getId());
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        } else if (advertise != null) {
            Intent intent = new Intent(this, EditAdvertiseActivity.class);
            intent.putExtra(Constant.CATEGORYID, advertise.getCategoryId());
            intent.putExtra(Constant.INDEX_ADVERTISE, indexAdvertise);
            intent.putExtra(Constant.ADVERTISEID, advertiseId);
            intent.putExtra(Constant.FROM_MESSAGE, true);
            intent.putExtra(Constant.CONVERSATIONID, conversationId);
            intent.putExtra(Constant.ADVERTISEID, advertise.getId());
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

    }

    public void showMessageError(String title, String message) {
        try {
            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) getApplicationContext()).isDestroyed()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
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

    public synchronized void setUrlForVideo() {
        /*if (advertise != null && advertise.getVideo() != null && advertise.getVideo().getVideoURL() != null
                && !advertise.getVideo().getVideoURL().isEmpty()) {
            Intent intent;
            intent = new Intent(this, VideoViewActivity.class);
            intent.putExtra("videoUrl", advertise.getVideo().getVideoURL());
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }*/

        if (advertise != null && advertise.getVideo() != null && advertise.getVideo().getVideoImage() != null
                && !advertise.getVideo().getVideoImage().getImageUrl().isEmpty()) {
            Log.e(TAG, "setUrlForVideo");
            /*videoLayout = new com.sosokan.android.control.FullscreenVideoLayout(this);
            videoLayout = (com.sosokan.android.control.FullscreenVideoLayout) findViewById(R.id.videoview);
            videoLayout.setActivity(this);
          //  videoLayout.stop();

            Uri videoUri = Uri.parse(advertise.getVideo().getVideoURL());
            try {
                videoLayout.setVideoURI(videoUri);

            } catch (IOException e) {
                e.printStackTrace();
            }

*/
            imgVideoPostDetail.setVisibility(View.VISIBLE);
            Glide.with(imgVideoPostDetail.getContext()).load(advertise.getVideo().getVideoImage().getImageUrl()).asBitmap().into(new SimpleTarget<Bitmap>(250, 250) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    //  Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        imgVideoPostDetail.setImageBitmap(resource);

                    }
                }
            });

            rlVideoWebPostDetail.setVisibility(View.VISIBLE);
        } else {
            rlVideoWebPostDetail.setVisibility(View.GONE);
        }
    }

    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public void searchPost() {

        if (advertiseId != null && !advertiseId.isEmpty()) {
            mDemoSlider.removeAllSliders();
            stImages = new ArrayList<>();
            llContentPost = (LinearLayout) findViewById(R.id.llContentPost);
            llContentPost.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

            Log.e(TAG, "searchPost");
            FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.ADVERTISES).child(advertiseId).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            advertise = dataSnapshot.getValue(Advertise.class);

                            if (advertise != null) {
                                getConversation();
                                if (idCategory == null || idCategory.isEmpty()) {
                                    idCategory = advertise.getCategoryId();
                                }
                                if (advertise != null && user != null) {
                                    if (advertise.getUserId().equals(user.getId())) {
                                        ibEditPostDetail.setVisibility(View.VISIBLE);
                                    }

                                }
                                if (advertise.getName() != null && !advertise.getName().isEmpty()) {
                                    Log.e("Name advertise", advertise.getName());
                                }
                                if (advertise.getUserId() != null) {
                                    ApplicationUtils.closeMessage();
                                    setUpMapReady(googleMap);
                                    FirebaseDatabase.getInstance()
                                            .getReferenceFromUrl(Config.FIREBASE_URL)
                                            .child(Constant.USERS).child(advertise.getUserId())
                                            .addListenerForSingleValueEvent(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            userOwner = dataSnapshot.getValue(User.class);
                                                            if (userOwner != null && user != null) {
                                                                //
                                                                int countRating = 0;
                                                                if (userOwner.getRateds() != null) {
                                                                    countRating = userOwner.getRateds().size();
                                                                }
                                                                tvAdvertiseCountPostDetail.setText(Integer.toString(countRating));

                                                                if (advertise != null && advertise.getFax() != null && !advertise.getFax().isEmpty()) {
                                                                    tvFaxPostDetail.setText(userOwner.getFaxNumber());
                                                                    llFaxPostDetail.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    if (userOwner.getFaxNumber() == null || userOwner.getFaxNumber().isEmpty()) {
                                                                        llFaxPostDetail.setVisibility(View.GONE);
                                                                    } else {
                                                                        tvFaxPostDetail.setText(userOwner.getFaxNumber());
                                                                        llFaxPostDetail.setVisibility(View.VISIBLE);
                                                                    }

                                                                }

                                                                if (advertise != null && advertise.getEmail() != null && !advertise.getEmail().isEmpty()) {
                                                                    tvEmailPostDetail.setText(advertise.getEmail());
                                                                    llEmailPostDetail.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    if (userOwner.getEmail() == null || userOwner.getEmail().isEmpty()) {

                                                                        llEmailPostDetail.setVisibility(View.GONE);

                                                                    } else {
                                                                        llEmailPostDetail.setVisibility(View.VISIBLE);
                                                                        tvEmailPostDetail.setText(userOwner.getEmail());
                                                                    }
                                                                }

                                                                if (advertise != null && advertise.getPhone() != null && !advertise.getPhone().isEmpty()) {
                                                                    tvPhonePostDetail.setText(advertise.getPhone());
                                                                    llPhonePostDetail.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    if (userOwner.getPhoneNumber() == null || userOwner.getPhoneNumber().isEmpty()) {
                                                                        llPhonePostDetail.setVisibility(View.GONE);
                                                                    } else {
                                                                        llPhonePostDetail.setVisibility(View.VISIBLE);
                                                                        tvPhonePostDetail.setText(userOwner.getPhoneNumber());
                                                                    }

                                                                }

                                                                if (advertise != null && advertise.getWebsite() != null && !advertise.getWebsite().isEmpty()) {
                                                                    tvWebPostDetail.setText(advertise.getWebsite());
                                                                    llWebsitePostDetail.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    if (userOwner.getWebsite() == null || userOwner.getWebsite().isEmpty()) {
                                                                        llWebsitePostDetail.setVisibility(View.GONE);
                                                                    } else {
                                                                        tvWebPostDetail.setText(userOwner.getWebsite());
                                                                        llWebsitePostDetail.setVisibility(View.VISIBLE);
                                                                    }

                                                                }
                                                                if (advertise != null && advertise.getAddress() != null && !advertise.getAddress().isEmpty()) {
                                                                    tvAddressPostDetail.setText(advertise.getAddress());
                                                                    llAddressPostDetail.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    if (userOwner.getAddress() == null || userOwner.getAddress().isEmpty()) {
                                                                        llAddressPostDetail.setVisibility(View.GONE);
                                                                    } else {
                                                                        tvAddressPostDetail.setText(userOwner.getAddress());
                                                                        llAddressPostDetail.setVisibility(View.VISIBLE);
                                                                    }

                                                                }
                                                                tvUserNamePostDetail.setText(userOwner.getUserName() == null || userOwner.getUserName().isEmpty() ? getResources().getString(R.string.not_available) : userOwner.getUserName());
                                                                if (advertise != null)
                                                                    tvNumberFavoritePostDetail.setText(Integer.toString(advertise.getFavoriteCount()));
                                                                if (userOwner.getAvatar() != null && !userOwner.getAvatar().getImageUrl().isEmpty()) {
                                                                    UpdateAvatarUser(userOwner.getAvatar().getImageUrl());
                                                                }

                                                                if (!user.getId().equals(userOwner.getId())) {
                                                                    Map<String, Object> ratings;
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
                                                                    ratingBarPostDetail.setRating(ratingNumber);
                                                                    if (userOwner.getEmailAble() != null && userOwner.getEmailAble()) {
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
                                                                    }

                                                                    Map<String, Object> followingUsers;
                                                                    followingUsers = user.getFollowingUsers();
                                                                    if (followingUsers != null) {
                                                                        if (followingUsers.get(userOwner.getId()) != null) {
                                                                            tvFollowPostDetail.setText(getResources().getString(R.string.unfollow));
                                                                        }
                                                                    }
                                                                } else {
                                                                    tvFollowPostDetail.setVisibility(View.GONE);
                                                                    ratingBarPostDetail.setVisibility(View.GONE);
                                                                    ivEmailPostDetail.setEnabled(false);
                                                                    ivPhonePostDetail.setEnabled(false);

                                                                    ivEmailPostDetail.setAlpha(.6f);
                                                                    ivPhonePostDetail.setAlpha(.6f);
                                                                }
                                                                if (advertise != null && advertise.getCategoryId() != null && !advertise.getCategoryId().isEmpty()) {
                                                                    FirebaseDatabase.getInstance()
                                                                            .getReferenceFromUrl(Config.FIREBASE_URL)
                                                                            .child(Constant.CATEGORIES).child(advertise.getCategoryId())
                                                                            .addListenerForSingleValueEvent(
                                                                                    new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            try {
                                                                                                CategoryNew category = dataSnapshot.getValue(CategoryNew.class);
                                                                                                if (category != null) {
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
                                                                                            } catch (Exception ex) {

                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                                        }
                                                                                    });
                                                                }

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                }

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
                                if (advertise.getPrice() > 0) {
                                    tvDiscountPostDetail.setText("$" + String.valueOf(advertise.getPrice()));
                                } else {
                                    tvDiscountPostDetail.setVisibility(View.GONE);
                                }


                                if (advertise.getHtmlDescription() == null || advertise.getHtmlDescription().isEmpty()) {

                                    if (advertise.getDescription() != null && DetectHtml.isHtml(advertise.getDescription())) {
                                        setValueForDescriptionIsHtml(advertise.getDescription());
                                    } else {
                                        Log.e("DetectHtml.isHtml", "false");
                                        tvDescriptionPostDetail.setText(advertise.getDescription());
                                        webViewDescriptionPostDetail.setVisibility(View.GONE);
                                        tvDescriptionPostDetail.setVisibility(View.VISIBLE);
                                    }
                                } else {

                                    setValueForDescriptionIsHtml(advertise.getHtmlDescription());
                                }


                                if (user != null && user.getId() == advertise.getUserId()) {
                                    ibFavoritePostDetail.setEnabled(false);
                                }
                                String createdAt;
                                if (Math.abs(advertise.createdAt) > Math.abs(advertise.getUpdatedAt())) {
                                    createdAt = DateUtils.toDuration(advertise.getCreatedAt(), getApplicationContext());
                                } else {
                                    createdAt = DateUtils.toDuration(advertise.getUpdatedAt(), getApplicationContext());
                                }
                                // String createdAt = DateUtils.toDuration(Math.abs(advertise.getUpdatedAt()), isChineseApp,getApplicationContext());
                                tvCreatedDatePostDetail.setText(createdAt);

                                if (advertise != null && advertise.getImages() != null && advertise.getImages().size() > 1) {
                                    final List<Image> images = new ArrayList<>();
                                    final Map<String, Object> url_maps = advertise.getImages();
                                    countImage = 0;
                                    for (Map.Entry<String, Object> entry : url_maps.entrySet()) {
                                        FirebaseDatabase.getInstance()
                                                .getReferenceFromUrl(Config.FIREBASE_URL)
                                                .child(Constant.IMAGES).child(entry.getKey())
                                                .addListenerForSingleValueEvent(
                                                        new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                Image image = dataSnapshot.getValue(Image.class);
                                                                if (image != null && image.getImageUrl() != null && !image.getImageUrl().isEmpty()) {
                                                                    images.add(image);
                                                                }
                                                                countImage++;
                                                                if (countImage >= url_maps.size()) {
                                                                    if (images.size() > 1) {
                                                                        for (Image image1 : images) {
                                                                            TextSliderView textSliderView = new TextSliderView(AdvertiseDetailActivity.this);
                                                                            // initialize a SliderLayout
                                                                            textSliderView
                                                                                    .description("")
                                                                                    .image(image1.getImageUrl().toString())
                                                                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                                                                    .setOnSliderClickListener(AdvertiseDetailActivity.this);

                                                                            if (mDemoSlider != null) {
                                                                                mDemoSlider.addSlider(textSliderView);
                                                                            } else {
                                                                                mDemoSlider = (SliderLayout) findViewById(R.id.sliderPostDetail);
                                                                                mDemoSlider.addSlider(textSliderView);
                                                                            }
                                                                            stImages.add(image1.getImageUrl().toString());
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
                                                                        mDemoSlider.addOnPageChangeListener(AdvertiseDetailActivity.this);
                                                                    } else {

                                                                        displayImageForDetail();
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                    }
                                } else {
                                    displayImageForDetail();
                                }

                                getInformationFavorite();
                                setUrlForVideo();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }
    }

    private void displayImageForDetail() {
        Log.e("displayImageForDetail", "displayImageForDetail");
     /*   custom_indicator.setVisibility(View.GONE);
        custom_indicator2.setVisibility(View.GONE);*/
        if (advertise != null && advertise.getImageHeader() != null && !advertise.getImageHeader().getImageUrl().isEmpty()) {
            Log.e("UpdateImageCover", "getImageHeader");
            UpdateImageCover(advertise.getImageHeader().getImageUrl());
        } else if (advertise != null && advertise.getVideo() != null && advertise.getVideo().getVideoImage() != null && !advertise.getVideo().getVideoImage().getImageUrl().isEmpty()) {
            UpdateImageCover(advertise.getVideo().getVideoImage().getImageUrl());
            Log.e("UpdateImageCover", "getVideo");
        } else {
            if (advertise != null && advertise.getCategoryId() != null) {

                //TODO CATE
                /*FirebaseDatabase.getInstance()
                        .getReferenceFromUrl(Config.FIREBASE_URL)
                        .child(Constant.CATEGORIES).child(advertise.getCategoryId())
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            CategoryNew category = dataSnapshot.getValue(CategoryNew.class);
                                            if (category != null && category.getIcons() != null) {
                                                if (isChineseApp && category.getIcons().getIconChinese() != null) {
                                                    Image icon = category.getIcons().getIconChinese();
                                                    if (icon.getImageUrl() != null && !icon.getImageUrl().isEmpty()) {
                                                        UpdateImageCover(icon.getImageUrl().toString());
                                                    }

                                                } else {
                                                    Image icon = category.getIcons().getIconEnglish();
                                                    if (icon.getImageUrl() != null && !icon.getImageUrl().isEmpty()) {
                                                        UpdateImageCover(icon.getImageUrl().toString());
                                                    }
                                                }
                                            }
                                        } catch (Exception ex) {

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });*/
            }
        }
    }

    public void getInformationFavorite() {

        if (user != null && advertise != null) {
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
        }
    }

    private void setBackgroundFavorite() {
        final int sdk = Build.VERSION.SDK_INT;
        Map<String, Object> favoriteUsers = advertise.getFavoritedUsers();

        if (favoriteUsers == null) {
            favoriteUsers = new HashMap<>();
        }
        tvNumberFavoritePostDetail.setText(Integer.toString(favoriteUsers.size()));
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            if (isFavorite) {
                ibFavoritePostDetail.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
            } else {
                ibFavoritePostDetail.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_grey_24dp));
            }

        } else {
            if (isFavorite) {
                ibFavoritePostDetail.setBackground(getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
            } else {
                ibFavoritePostDetail.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_grey_24dp));
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

        if (mQueue != null)
            mQueue.cancelAll(this);
        freeMemory();
        Log.e("Advertise Detail ", "========= onDestroy");
    }

    @Override
    protected void onResume() {

        if (isNetworkAvailable(this)) {
            if (!ApplicationUtils.isShow) {
                ApplicationUtils.showProgress(AdvertiseDetailActivity.this);
            }
            Log.e("Advertise Detail ", " =========== CONNECT");
        } else {
            Log.e("Advertise Detail ", " =========== LOST CONNECT");
        }
        ApplicationUtils.closeMessage();
        super.onResume();

        Log.e("Advertise Detail ", " =========== onResume");

        //initFirebaseAndGetInformation();
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
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

    public void UpdateImageCover(String url) {
        Log.e("UpdateImageCover", url);
        imgPostDetail.setVisibility(View.VISIBLE);
        rlSlideImage.setVisibility(View.GONE);
        mDemoSlider.setVisibility(View.GONE);
        /*Glide.with(imgPostDetail.getContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>(imgPostDetail.getWidth(), imgPostDetail.getHeight()) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                //  Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imgPostDetail.setImageBitmap(resource);

                }
            }
        });*/
        Glide.with(imgPostDetail.getContext())
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (bitmap != null) {
                            int imageWidth = bitmap.getWidth();
                            int imageHeight = bitmap.getHeight();
                            WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                            if (windowManager != null && windowManager.getDefaultDisplay() != null) {
                                int newWidth = windowManager.getDefaultDisplay().getWidth(); //this method should return the width of device screen.
                                float scaleFactor = (float) newWidth / (float) imageWidth;
                                int newHeight = (int) ((imageHeight * scaleFactor));

                                bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

                                imgPostDetail.setImageBitmap(bitmap);
                            }
                        }

                    }
                });
    }

    public void UpdateAvatarUser(String url) {
        if (url.isEmpty() || imgProfilePostDetail == null) return;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !this.isDestroyed() && imgProfilePostDetail.getContext() != null) {
//
//        }
        try {
            if (imgProfilePostDetail != null) {
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

        } catch (Exception ex) {
            Log.e(TAG, "UpdateAvatarUser ex " + ex.toString());
        }

    }

    private void handleInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putInt(Constant.INDEX_ADVERTISE, indexAdvertise);
            savedInstanceState.putInt(Constant.INDEX_FRAGMENT, indexFragment);
            savedInstanceState.putString(Constant.CATEGORYID, idCategory);
            savedInstanceState.putString(Constant.ADVERTISEID, advertiseId);
            savedInstanceState.putBoolean(Constant.FROM_MESSAGE, fromMess);
            savedInstanceState.putString(Constant.CONVERSATIONID, conversationId);
            savedInstanceState.putInt("advertiseIdInt", advertiseIdInt);
            Log.e(TAG, "=========== handleInstanceState");

            Log.e("indexAdvertise ", " " + indexAdvertise);
            Log.e("indexFragment ", " " + indexFragment);
            Log.e("idCategory ", " " + idCategory);
            Log.e("advertiseId ", " " + advertiseId);
            Log.e("fromMess ", " " + fromMess);
            Log.e("conversationId ", " " + conversationId);
            Log.e("advertiseIdInt ", " " + advertiseIdInt);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            handleInstanceState(savedInstanceState);
            Log.e(TAG, "=================== onSaveInstanceState");

        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            indexAdvertise = savedInstanceState.getInt(Constant.INDEX_ADVERTISE, 0);
            advertiseId = savedInstanceState.getString(Constant.ADVERTISEID);

            idCategory = savedInstanceState.getString(Constant.CATEGORYID);
            idCategorySelected = savedInstanceState.getString(Constant.CATEGORYID);
            indexFragment = savedInstanceState.getInt(Constant.INDEX_FRAGMENT, 0);
            fromMess = savedInstanceState.getBoolean(Constant.FROM_MESSAGE, false);
            conversationId = savedInstanceState.getString(Constant.CONVERSATIONID);
            advertiseIdInt = savedInstanceState.getInt("advertiseIdInt", 0);
            getAdvertiseViaApiWithIdOfFirebase();
            Log.e("Ad Detail RestoreIns", "===================");
            Log.e("Advertise Detail ", "=========== handleInstanceState");

            Log.e("indexAdvertise ", " " + indexAdvertise);
            Log.e("advertiseId ", " " + advertiseId);
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
                if (user != null && user.isVerify()) {
                    processEditAdvertise();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;

            case R.id.ibTwitterDetail:
            case R.id.ibSharePostDetail:
                if (user != null && user.isVerify()) {
                    ShareOtherApp();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case R.id.ibMessagePostDetail:
                if (user != null && user.isVerify()) {
                    gotoMessageActivity();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case R.id.tvFollowPostDetail:
                if (user != null && user.isVerify()) {
                    processFollow();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case R.id.ibFacebookDetail:
                if (user != null && user.isVerify()) {
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
                if ((user != null && user.isVerify()) || (advertise != null && advertise.isEnablePhone())) {
                    processCallPhone();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;

            case R.id.tvEmailPostDetail:
            case R.id.ivEmailPostDetail:
                if (user != null && user.isVerify()) {
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
                }
                break;

            case R.id.btnSendFlagChoicePostDetail:
                if ((user != null && user.isVerify()) || (prefManager != null && prefManager.getUserInformationToken() != null)) {
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
                if ((user != null && user.isVerify()) || (prefManager != null && prefManager.getUserInformationToken() != null)) {
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

                if (advertise != null && advertise.getVideo() != null && advertise.getVideo() != null && !advertise.getVideo().getVideoURL().isEmpty()) {
                    intent = new Intent(this, VideoViewActivity.class);
                    intent.putExtra("videoUrl", advertise.getVideo().getVideoURL());
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                break;
        }
    }

    private void processEditAdvertise() {
        // CallEditAdvertiseActivity();
        if (advertise != null && user != null && user.isVerify()) {
            if (advertise.getUserId() != null && advertise.getUserId().equals(user.getId())) {
                CallEditAdvertiseActivity();
            }

        }
    }

    private void processCallPhone() {
        if (userOwner != null && userOwner.getCallAble() != null && userOwner.getCallAble()) {
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

        } else if (advertise != null && advertise.isEnablePhone()) {
            if (advertise.getPhone() != null && !advertise.getPhone().trim().isEmpty()) {
                String number = "tel:" + advertise.getPhone().trim();
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

        }
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
            if (idCategorySelected != null && !idCategorySelected.isEmpty()) {

                intent.putExtra(Constant.CATEGORYID, idCategorySelected);
            } else if (idCategory != null && !idCategory.isEmpty()) {

                intent.putExtra(Constant.CATEGORYID, idCategory);
            } else {

                intent.putExtra(Constant.CATEGORYID, advertise.getCategoryId());
            }
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            overridePendingTransition(R.anim.start_activity_left_to_right, 0);
        }
    }

    private void processClickFavorite() {
        String alert;
        String message;
        if (user != null) {
            if (user.isVerify()) {
                processFavorite();
            } else {
                alert = getResources().getString(R.string.we_are_sorry);
                message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                showMessageError(alert, message);
            }
        } else {
            alert = getResources().getString(R.string.we_are_sorry);
            message = getResources().getString(R.string.you_need_to_login_your_sosokan);
            showMessageError(alert, message);
        }
    }

    private void processFollow() {
        if (user != null && userOwner != null) {
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
            PrefManager prefManager = new PrefManager(getApplicationContext());
            prefManager.setUser(user);
            FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                    } else {

                    }
                }
            });

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
            FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(userOwner.getId()).setValue(userOwner, new DatabaseReference.CompletionListener() {
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
            intent.putExtra(Constant.ADVERTISE_CREATED_AT, advertise.getCreatedAt());
            intent.putExtra(Constant.FROM_DETAIL, !fromMess);
            intent.putExtra(Constant.ADVERTISEID, advertise.getId());
            intent.putExtra(Constant.ADVERTISEID, advertiseId);
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
        if (getApplicationContext() != null && advertise != null) {
            FacebookSdk.sdkInitialize(this);
            callbackManager = CallbackManager.Factory.create();
            shareDialog = new ShareDialog(this);
            String deeplink = "http://sosokan.herokuapp.com/link/" + advertise.getId();
            String desciption = advertise.getHtmlDescription() != null && !advertise.getHtmlDescription().isEmpty() ? advertise.getHtmlDescription()
                    : advertise.getDescription() != null && !advertise.getDescription().isEmpty() ? advertise.getDescription() : "";
            if (advertise != null && advertise.getVideo() != null) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareVideo shareVideo1 = new ShareVideo.Builder()
                            .setLocalUrl(Uri.parse(advertise.getVideo().getVideoURL()))
                            .build();
                    ShareContent shareContent = new ShareMediaContent.Builder()
                            .addMedium(shareVideo1)
                            .build();
                    shareDialog.show(shareContent);
                }
            } else {
                String imageUrl = "";

                if (advertise != null && advertise.getImageHeader() != null) {

                    imageUrl = advertise.getImageHeader().getImageUrl();
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
                    if (nameShare != null && !nameShare.isEmpty()) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(nameShare)
                                .setContentDescription(desciption)
                                .setContentUrl(Uri.parse(deeplink))
                                .setImageUrl(Uri.parse(imageUrl))
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


        if (user != null && advertise != null && user.getId() != advertise.getUserId()) {
            isHasFavorite = advertise.getFavoritedUsers() == null || advertise.getFavoritedUsers().get(user.getId()) == null ? false : true;

            Map<String, Object> favoriteUsers = advertise.getFavoritedUsers();
            if (favoriteUsers == null) {
                favoriteUsers = new HashMap<>();
            }
            mapFavorite = user.getFavorites();
            if (mapFavorite == null) {
                mapFavorite = new HashMap<>();
            }
            if (!isHasFavorite) {
                favoriteUsers.put(user.getId(), 0 - DateUtils.getDateInformation());
                mapFavorite.put(advertise.getId(), true);
            } else {
                favoriteUsers.remove(user.getId());
                mapFavorite.remove(advertise.getId());
            }
            advertise.setFavoriteCount(favoriteUsers.size());
            advertise.setFavoritedUsers(favoriteUsers);
            FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.ADVERTISES).child(advertise.getId()).setValue(advertise);
            user.setFavorites(mapFavorite);
            FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.ADVERTISES).child(advertise.getId()).setValue(advertise);
            isFavorite = !isHasFavorite;
            if (user != null) {
                PrefManager prefManager = new PrefManager(getApplicationContext());
                prefManager.setUser(user);
                FirebaseDatabase.getInstance()
                        .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    }
                });
            }
        }
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
            } else if (!advertise.getHtmlDescription().isEmpty()) {
                Log.e(TAG, "EXTRA_HTML_TEXT advertise.getHtmlDescription() " + advertise.getHtmlDescription());
                shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, advertise.getHtmlDescription());
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
        if (advertise != null && advertise.getAddress() != null && !advertise.getAddress().isEmpty()) {
            LatLng latLng = null;
            try {
                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                if ((permissionCheck == PackageManager.PERMISSION_GRANTED)) {

                    latLng = LocaleHelper.getLocationFromAddress(advertise.getAddress(), this);

                } else {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 42);

                }

            } catch (Exception ex) {
                latLng = new LatLng(0, 0);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                PermissionGrantedHelper permissionGrantedHelper = new PermissionGrantedHelper(this);
                permissionGrantedHelper.checkAndRequestPermissionForMap();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

            }
            if (latLng != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(advertise.getAddress())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map)));

            }

        } else if (advertise != null && advertise.getLocation() != null) {
            LatLng latLng;
            try {
                LocationSosokan location = advertise.getLocation();
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            } catch (Exception ex) {
                latLng = new LatLng(0, 0);
            }
            if (latLng != null) {
                map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(advertise.getAddress())
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

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (advertiseIds != null) {
                        int index = indexAdvertise - 1;
                        if (index >= 0) {
                            //callAdvertiseDetailWithoutId(index,true);
                            indexAdvertise = index;
                            int indexNew = 0;
                            for (Map.Entry<String, Object> entry : advertiseIds.entrySet()) {
                                if (indexNew == indexAdvertise) {
                                    advertiseId = entry.getKey();

                                    callAdvertiseDetailWithoutId(index, false);
                                    getAdvertiseViaApiWithIdOfFirebase();
                                    break;
                                }
                                indexNew++;
                            }
                        }

                    }

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (advertiseIds != null) {
                        int index = indexAdvertise + 1;
                        if (index < advertiseIds.size()) {

                            indexAdvertise = index;
                            int indexNew = 0;
                            for (Map.Entry<String, Object> entry : advertiseIds.entrySet()) {
                                if (indexNew == indexAdvertise) {
                                    advertiseId = entry.getKey();

                                    callAdvertiseDetailWithoutId(index, false);
                                    getAdvertiseViaApiWithIdOfFirebase();
                                    break;
                                }
                                indexNew++;
                            }

                            //System.out.println(entry.getKey() + "/" + entry.getValue());
                        }

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

    public User getUser() {
        return user;
    }

    private void callAdvertiseDetailWithoutId(int index, boolean isRight) {
        if (getApplicationContext() != null) {
            Glide.with(getApplicationContext()).pauseRequests();
            Glide.get(getApplicationContext()).clearMemory();
        }
       /* Glide.get(this).clearMemory();
        Glide.get(this).clearDiskCache();*/
        if (getApplicationContext() != null) {
            Glide.with(getApplicationContext()).pauseRequests();
            Glide.get(getApplicationContext()).clearMemory();
        }
        if (mQueue != null)
            mQueue.cancelAll(this);
        freeMemory();
        searchPost();
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
                    if (user != null && advertiseIdInt > 0) {

                        flagSelected = new Flag();
                        String rtUrl = urlEndpoint.getUrlApi(""); //LIVE NOT WORK
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

    public void getFlagChoices() {
        String url = urlEndpoint.getUrlApi(Constant.FLAG_CHOICES);
        if (url != null && !url.isEmpty()) {
            Log.e(TAG, "===================== getFlagChoices url " + url);
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            handleResponseGetFlagChoices(response);
                        }
                    },
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
                    return urlEndpoint.getHeaderWithAccountDefault(getApplicationContext());
                }
            };

            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    3,
                    1f));
            mQueue.add(postRequest);
        }

    }

    private void handleResponseGetFlagChoices(String response) {
        Log.e(TAG, "getFlagChoices response " + response);

        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject) jsonParser.parse(response);
        JsonArray jsonArr = jo.getAsJsonArray("results");
        Gson googleJson = new Gson();
        Type collectionType = new TypeToken<Collection<FlagChoice>>() {
        }.getType();
        flagChoices = googleJson.fromJson(jsonArr, collectionType);
        if (prefManager == null)
            prefManager = new PrefManager(getApplicationContext());
        prefManager.setFlagchoices(flagChoices);
        setAdapterForFlagChoises();
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
        // recyclerView.setNestedScrollingEnabled(false);
        commentsAdapter.setListener(new CommentsApiAdapter.Listener() {
            @Override
            public void onItemClick(int position) {
                Log.e("... ", "setupRecycleViewComments" + "onItemClick");
            }
        });
        rvComments.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        StaggeredGridLayoutManager mLayoutManagerAdvertiseList = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvComments.setHasFixedSize(true);

        rvComments.setItemAnimator(new DefaultItemAnimator());

        rvComments.setAdapter(commentsAdapter);

        rvComments.setLayoutManager(mLayoutManagerAdvertiseList);

        rvComments.setNestedScrollingEnabled(false);
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
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                handleResponseSendComment(response);
                            }
                        },
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


    public void getAdvertiseViaApiWithIdOfFirebase() {
        if (advertiseId != null && !advertiseId.isEmpty()) {
            String url = urlEndpoint.getUrlApi(Constant.ADS) + "?legacy_id=" + advertiseId;
            if (url != null && !url.isEmpty()) {
                Log.e(TAG, "  getAdvertiseViaApiWithIdOfFirebase url " + url);
                StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                handleResponseGetAdvertiseWithIdOfFirebase(response);

                           /* */
                            }
                        },
                        volleyErrListener
                ) {


                    @Override
                    protected Map<String, String> getParams() {


                        Map<String, String> params = new HashMap<String, String>();

                        params.put("legacy_id", advertiseId);

                        return params;
                    }

                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return urlEndpoint.getHeaderWithAccountDefault(getApplicationContext());
                    }
                };

                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        3,
                        1f));
                mQueue.add(postRequest);
            }
        } else {
            Log.e(TAG, "  getAdvertiseViaApiWithIdOfFirebase advertiseId " + advertiseId);
        }
    }

    private void handleResponseGetAdvertiseWithIdOfFirebase(String response) {
        Log.e(TAG, "getAdvertiseViaApiWithIdOfFirebase response " + response);
        JSONObject object = null;
        try {
            object = new JSONObject(response);
            JSONArray jsonArray = object.getJSONArray("results");
            Gson gson = new Gson();
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Log.e(TAG, "getAdvertiseViaApiWithIdOfFirebase jsonArray.get(i).toString() " + jsonArray.get(i).toString());
                    try {

                        AdvertiseApi advertiseApi = gson.fromJson(jsonArray.get(0).toString(), AdvertiseApi.class);

                        if (advertiseApi != null) {
                            advertiseIdInt = advertiseApi.getId();
                            isLoadAdvertise = true;
                            if (advertiseIdInt > 0) {
                                getAdvertiseViaApiWithApiId();
                            }
                            Log.e(TAG, "getAdvertiseViaApiWithIdOfFirebase advertiseIdInt " + advertiseIdInt);
                        }
                        break;
                    } catch (Exception ex) {
                        Log.e(TAG, "getAdvertiseViaApiWithIdOfFirebase " + ex);
                    }

                    // banners.add(banner);

                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "getAdvertiseViaApiWithIdOfFirebase JSONException " + e);
        }
    }

    public void getAdvertiseViaApiWithApiId() {
        if (advertiseId != null && !advertiseId.isEmpty()) {
            String url = urlEndpoint.getUrlApi(Constant.ADS) + advertiseIdInt;
            if (url != null && !url.isEmpty()) {
                Log.e(TAG, "  getAdvertiseViaApiWithIdOfFirebase url " + url);
                StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                handleResponseGetAdvertise(response);

                            }
                        },
                        volleyErrListener
                ) {


                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("legacy_id", advertiseId);
                        return params;
                    }

                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return urlEndpoint.getHeaderWithAccountDefault(getApplicationContext());
                    }
                };

                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        3,
                        1f));
                mQueue.add(postRequest);
            }
        } else {
            Log.e(TAG, "  getAdvertiseViaApiWithIdOfFirebase advertiseId " + advertiseId);
        }
    }

    private void handleResponseGetAdvertise(String response) {
        Log.e(TAG, "getAdvertiseViaApiWithApiId response " + response);
        JSONObject object = null;
        Gson gson = new Gson();
        try {

            AdvertiseApi advertiseApi = gson.fromJson(response, AdvertiseApi.class);

            if (advertiseApi != null) {
                advertiseIdInt = advertiseApi.getId();
                isLoadAdvertise = true;
                if (advertiseApi.getComments() != null) {
                    commentNews = advertiseApi.getComments();
                    updateUIForListComment();
                } else {

                }
                //TODO UPDATE UI FOR ADVERTISE
                // getComments();

                Log.e(TAG, "getAdvertiseViaApiWithApiId advertiseIdInt " + advertiseIdInt);
            }
            if (advertise == null) {
                setValueForView(advertiseApi);
            }
        } catch (Exception ex) {
            Log.e(TAG, "getAdvertiseViaApiWithApiId " + ex);
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

                if (categories.get(i) != null && categories.get(i).getName() != null && categories.get(i).getName().equals(Constant.News)) {
                    categoryNew = categories.get(i);
                }
                /*if (categories.get(i).getSort() > 0) {


                }*/
                if (categories.get(i) != null && categories.get(i).getLegacy_id() != null
                        && !categories.get(i).getLegacy_id().equals(Constant.sosokanCategoryAll)) {
                    if (categories.get(i).getParentId() == null || categories.get(i).getParentId().isEmpty() || categories.get(i).getParent() == null || categories.get(i).getLevel() == 0) {
                        categoriesChild.add(categories.get(i));
                        Log.e(TAG, "categories.get(i).getLegacy_id() " + categories.get(i).getLegacy_id());
                    }

                }
            }
        }

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
    }

    private OnItemCategoryClickListener mOnItemClickListener = new OnItemCategoryClickListener() {

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            CategoryNew category = ((CategoryNew) item);
            if (category != null) {
                categorySelectedId = category.getLegacy_id();
                idCategory = categorySelectedId;
                categorySelected = category;

            }

        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            CategoryNew category = ((CategoryNew) item);
            if (category != null) {
                categorySelectedId = category.getLegacy_id();
                idCategory = categorySelectedId;
                categorySelected = category;

            }

        }
    };



    private void initViewRecycleCategory() {
        mListView = (MultiLevelListView) findViewById(R.id.listViewCategorySelect);

        listAdapter = new ListCategoryApiAdapter(AdvertiseDetailActivity.this);
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
        categories = prefManager.getListCategoriesApi();
        if (categories != null && categories.size() > 0) {

            categoriesChild = new ArrayList<>();
            processDataMenu();
        } else {
            getAllCategory();
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
                //  Category category = idCategory;
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
                if (user != null && user.isVerify()) {
                    intent = new Intent(this, NewAdvertiseActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case 3:
                if (user != null && user.isVerify()) {
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
                if (user != null && user.isVerify()) {
                    intent = new Intent(this, NewAdvertiseActivity.class);
                    startActivity(intent);
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                break;
            case 3:
                if (user != null && user.isVerify()) {
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

    public void initDataFakeComment() {
        commentNews = new ArrayList<>();
        String idUserFirebase = user == null ? "WZgSCvtuwmSImVAjhHv4KQrmpgf2" : user.getId();
        for (int i = 0; i <= 30; i++) {
            if (i % 2 == 0) {
                CommentNew commentNew = new CommentNew();
                commentNew.setId(i);
                commentNew.setComment("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.");
                commentNew.setFirebase_user_id(idUserFirebase);
                commentNew.setUser_email("zinto@gmail.com");
                commentNew.setSubmit_date("2017-02-03T01:58:20.316092Z");
                commentNew.setUser_name("zin to");
                commentNews.add(commentNew);
            } else {
                CommentNew commentNew = new CommentNew();
                commentNew.setId(i);
                commentNew.setComment("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.");
                commentNew.setFirebase_user_id(idUserFirebase);
                commentNew.setUser_email("zinto@gmail.com");
                commentNew.setUser_name("Sosokan");
                commentNew.setSubmit_date("2017-02-03T01:58:20.316092Z");
                commentNews.add(commentNew);
            }

        }

        updateUIForListComment();

    }

    private void setValueForView(AdvertiseApi advertiseApi) {
        if (advertiseApi != null) {
            getConversation();
            if (idCategory == null || idCategory.isEmpty()) {
                idCategory = advertise.getCategoryId();
            }
            if (advertiseApi != null && advertiseApi != null && user != null) {
                if (advertiseApi.getUserId().equals(user.getId())) {
                    ibEditPostDetail.setVisibility(View.VISIBLE);
                }

            }
            if (advertiseApi.getName() != null && !advertiseApi.getName().isEmpty()) {
                Log.e("Name advertise", advertiseApi.getName());
            }
            if (advertiseApi.getUserId() != null) {
                ApplicationUtils.closeMessage();
                setUpMapReady(googleMap);
                //TODO FIND USER OWNER
                processUpdateViewWhenGetUserOwnerSuccess(advertiseApi);
            }
            tvNumberFavoritePostDetail.setText(Integer.toString(advertiseApi.getFavoriteCount()));
            if (advertiseApi != null && advertiseApi.getName() != null && !advertiseApi.getName().isEmpty() && advertiseApi.getName().contains("-")) {
                String[] separated = advertiseApi.getName().toString().split("-");
                String stChinese = separated != null && separated.length > 0 ? separated[0] : "";
                String stEnglish = separated != null && separated.length > 1 ? separated[1] : "";
                if (isChineseApp) {
                    tvTitlePostDetail.setText(stChinese);
                } else {
                    tvTitlePostDetail.setText(stEnglish);
                }
            } else if (advertiseApi.getName() != null) {
                tvTitlePostDetail.setText(advertiseApi.getName());
            }
            if (advertiseApi.getPrice() != null && !advertiseApi.getPrice().isEmpty()) {
                tvDiscountPostDetail.setText("$" + String.valueOf(advertiseApi.getPrice()));
            } else {
                tvDiscountPostDetail.setVisibility(View.GONE);
            }

            String desciption = advertiseApi.getShort_description() != null && !advertiseApi.getShort_description().isEmpty() ? advertiseApi.getShort_description()
                    : advertiseApi.getDescription() != null && !advertiseApi.getDescription().isEmpty() ? advertiseApi.getDescription() : "";


            if (DetectHtml.isHtml(desciption)) {
                setValueForDescriptionIsHtml(desciption);
            } else {
                Log.e("DetectHtml.isHtml", "false");
                tvDescriptionPostDetail.setText(desciption);
                webViewDescriptionPostDetail.setVisibility(View.GONE);
                tvDescriptionPostDetail.setVisibility(View.VISIBLE);
            }

            if (user != null && user.getId() == advertise.getUserId()) {
                ibFavoritePostDetail.setEnabled(false);
            }


            String createdAt = DateUtils.getTimeAgo(advertiseApi.getCreated_on(), getApplicationContext());
            tvCreatedDatePostDetail.setText(createdAt);
            displayImageForDetailWithAdvertiseNew(advertiseApi);

            getInformationFavorite();


        }
    }

    private void setValueForDescriptionIsHtml(String description) {

        Log.d("DetectHtml.isHtml", "true");
        webViewDescriptionPostDetail.setWebViewClient(new WebViewClient());
        webViewDescriptionPostDetail.getSettings().setJavaScriptEnabled(true);
        webViewDescriptionPostDetail.getSettings().setDomStorageEnabled(true);
        //
        String page = "<html><body>"
                + description
                + "</body></html>";
        webViewDescriptionPostDetail.loadDataWithBaseURL(null, page, "text/html", "UTF-8", "");
        tvDescriptionPostDetail.setVisibility(View.GONE);
        webViewDescriptionPostDetail.setVisibility(View.VISIBLE);
        webViewDescriptionPostDetail.getSettings().setUseWideViewPort(true);
        webViewDescriptionPostDetail.getSettings().setLoadWithOverviewMode(true);
        final WebSettings webSettings = webViewDescriptionPostDetail.getSettings();
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        webViewDescriptionPostDetail.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webViewDescriptionPostDetail.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webViewDescriptionPostDetail.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
        webViewDescriptionPostDetail.getSettings().setDefaultFontSize(25);
        webSettings.setDefaultFontSize(25);
    }

    private void processUpdateViewWhenGetUserOwnerSuccess(AdvertiseApi advertiseApi) {
        if (userOwner != null && user != null) {
            //
            int countRating = 0;
            if (userOwner.getRateds() != null) {
                countRating = userOwner.getRateds().size();
            }
            tvAdvertiseCountPostDetail.setText(Integer.toString(countRating));
            if (userOwner.getFaxNumber() == null || userOwner.getFaxNumber().isEmpty()) {
                llFaxPostDetail.setVisibility(View.GONE);

            } else {
                tvFaxPostDetail.setText(userOwner.getFaxNumber());
            }

            if (userOwner.getPhoneNumber() == null || userOwner.getPhoneNumber().isEmpty()) {
                llPhonePostDetail.setVisibility(View.GONE);
            } else {
                tvPhonePostDetail.setText(userOwner.getPhoneNumber());
            }

            if (userOwner.getEmail() == null || userOwner.getEmail().isEmpty()) {
                llEmailPostDetail.setVisibility(View.GONE);
            } else {
                tvEmailPostDetail.setText(userOwner.getEmail());
            }

            if (userOwner.getWebsite() == null || userOwner.getWebsite().isEmpty()) {
                llWebsitePostDetail.setVisibility(View.GONE);
            } else {
                tvWebPostDetail.setText(userOwner.getWebsite());
            }


            tvUserNamePostDetail.setText(userOwner.getUserName() == null || userOwner.getUserName().isEmpty() ? getResources().getString(R.string.not_available) : userOwner.getUserName());


            if (userOwner.getAvatar() != null && !userOwner.getAvatar().getImageUrl().isEmpty()) {
                UpdateAvatarUser(userOwner.getAvatar().getImageUrl());
            }

            if (!user.getId().equals(userOwner.getId())) {
                Map<String, Object> ratings;
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
                ratingBarPostDetail.setRating(ratingNumber);
                if (userOwner.getEmailAble() != null && userOwner.getEmailAble()) {
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
                }

                Map<String, Object> followingUsers;
                followingUsers = user.getFollowingUsers();
                if (followingUsers != null) {
                    if (followingUsers.get(userOwner.getId()) != null) {
                        tvFollowPostDetail.setText(getResources().getString(R.string.unfollow));
                    }
                }
            } else {
                tvFollowPostDetail.setVisibility(View.GONE);
                ratingBarPostDetail.setVisibility(View.GONE);
                ivEmailPostDetail.setEnabled(false);
                ivPhonePostDetail.setEnabled(false);

                ivEmailPostDetail.setAlpha(.6f);
                ivPhonePostDetail.setAlpha(.6f);
            }
            if (advertiseApi != null && advertiseApi.getCategoryId() != null && !advertiseApi.getCategoryId().isEmpty()) {
                //TODO FIND CATEGORY
                PrefManager prefManager = new PrefManager(getApplication());

                /*List<CategoryNew> categories = prefManager.getListCategoriesFirebase();
                if (categories != null && categories.size() > 0) {
                    for (CategoryNew categoryNew : categories) {
                        if (categoryNew.getId() != null && !categoryNew.getId().isEmpty() && categoryNew.getId().equals(advertise.getCategoryId())) {
                            category = categoryNew;
                            break;
                        }
                    }
                }*/
                processSetTexForCategory(category);
            }

        }
    }

    private void processSetTexForCategory(CategoryNew category) {
        if (category != null) {
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

    private void displayImageForDetailWithAdvertiseNew(AdvertiseApi advertiseApi) {
        Log.e("displayImageForDetail", "displayImageForDetail");
     /*   custom_indicator.setVisibility(View.GONE);
        custom_indicator2.setVisibility(View.GONE);*/
        if (advertiseApi != null && advertiseApi.getImageHeader() != null && !advertiseApi.getImageHeader().getUrl().isEmpty()) {
            Log.e("UpdateImageCover", "getImageHeader");
            UpdateImageCover(advertiseApi.getImageHeader().getUrl());
        } else {
            if (advertise != null && advertise.getCategoryId() != null) {
                //updateImageCoverWhenAdvertiseNotHaveImage(category);
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

    private Response.ErrorListener volleyErrListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: handle error
            Log.e(TAG, "Error: " + error.getMessage());
        }
    };

    public void getAllCategory() {
        Log.e(TAG, "===================== getAllCategory ");

        String url = new UrlEndpoint().getUrlApi(Constant.CATEGORIES);
        Log.e(TAG, "===================== url " + url);

        if (url != null && !url.isEmpty()) {
            categories = new ArrayList<>();
            mapCategory = new HashMap<>();
            categoriesChild = new ArrayList<>();

            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                ApplicationUtils.closeMessage();
                                //  JSONObject object = new JSONObject(response);
                                if (response != null) {
                                    JSONArray jsonArray = new JSONArray(response);
                                    Gson gson = new Gson();
                                    if (jsonArray != null && jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            try {
                                                CategoryNew category = gson.fromJson(jsonArray.get(i).toString(), CategoryNew.class);
                                                if (category != null) {

                                                    categories.add(category);
                                                }
                                            } catch (Exception ex) {
                                                Log.e(TAG, "getAllCategory " + ex);
                                            }
                                        }
                                        prefManager.setCategoriesApi(gson.toJson(categories));
                                        processDataMenu();
                                    }
                                }
                                Log.e(TAG, "getAllCategory response  " + response);
                            } catch (JSONException e) {
                                Log.e(TAG, "getAllCategory JSONException => " + e);
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                            ApplicationUtils.closeMessage();
                        }
                    }
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

                @Override
                protected Map<String, String> getParams() {


                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

                public Map<String, String> getHeaders() throws AuthFailureError {
                    return urlEndpoint.getHeaderWithAccountDefault(getApplicationContext());
                }
            };

            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    100000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mQueue.add(postRequest);
        }
    }
}
