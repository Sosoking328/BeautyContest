package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.sosokan.android.BaseApp;
import com.sosokan.android.R;
import com.sosokan.android.adapter.ItemCategorySpinnerAdapter;
import com.sosokan.android.adapter.ViewPagerAdapter;
import com.sosokan.android.control.multi.level.menu.DataProviderCategory;
import com.sosokan.android.control.multi.level.menu.ItemInfo;
import com.sosokan.android.control.multi.level.menu.ListCategoryFireBaseAdapter;
import com.sosokan.android.control.multi.level.menu.MultiLevelListViewFirebase;
import com.sosokan.android.control.multi.level.menu.OnItemCategoryClickListenerFirebase;
import com.sosokan.android.events.Listener.EndCallListener;
import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.AdvertiseMeta;
import com.sosokan.android.models.Category;
import com.sosokan.android.models.Conversation;
import com.sosokan.android.models.Image;
import com.sosokan.android.models.LocationSosokan;
import com.sosokan.android.models.User;
import com.sosokan.android.models.Video;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.CameraVideoHelper;
import com.sosokan.android.utils.comparator.CategorySortComparator;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.DateUtils;
import com.sosokan.android.utils.GPSTracker;
import com.sosokan.android.utils.LocaleHelper;
import com.sosokan.android.utils.PermissionGrantedHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;

import static com.sosokan.android.utils.Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE;
import static com.sosokan.android.utils.Constant.CAMERA_CAPTURE_VIDEO_REQUEST_CODE;
import static com.sosokan.android.utils.Constant.MEDIA_TYPE_IMAGE;
import static com.sosokan.android.utils.Constant.MEDIA_TYPE_VIDEO;
import static com.sosokan.android.utils.Constant.SELECT_PICTURE;
import static com.sosokan.android.utils.FireBaseUtils.generateId;

/**
 * Created by AnhZin on 12/17/2016.
 */

public class EditAdvertiseActivity extends BaseApp implements View.OnClickListener,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private static final String TAG = "EditAdvertiseActivity";

    boolean isChineseApp;
    String advertiseId;
    String conversationId;
    public String userId;
    List<Conversation> conversationList;
    private static DatabaseReference refConversation;

    //private SliderLayout mDemoSlider;
    int countImage = 0;


    Activity activity;
    int imageHeight;
    int imageWidth;
    Map<String, Object> imagesMap;
    List<Image> imagesListUpload;
    String idCategory;
    // com.daimajia.slider.library.Indicators.PagerIndicator  custom_indicator;


    User userOwner;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //private DatabaseReference mDatabase, mDatabaseFavorite, mDatabaseUser, mDatabaseAdvertise;
    Query queryConversation;

    Map<String, Object> mapFavorite;
    Advertise advertise;
    Category category;
    ImageButton ibLocationEditPost, ibBackSelectCategoryEditPost, ibRecordVideoEditPost, ibCapturePictureEditPost;
    //Spinner spCategoryEditPost;
    TextView tvCategoryEditPost, tvLocationEditPost, tvTitleSubscriptionEditPost;
    EditText edtTitleEditPost, edtDescriptionEditPost, edtPriceEditPost, edtDiscountEditPost;

    Button btnPostEditPost, btnEditPhotoEditPost;
    LinearLayout llInformationAdvertiseEditPost, llSelectCategoryEditPost, llDiscountEditPost;

    String title, description, stDiscount, address,
            nameImage, idUser, nameVideo;

    private List<Category> categories, categoriesChild;
    public String categoryAllId = "All";
    public Category categoryAll = null;
    public static Map<String, Category> mapCategory;
    public static Map<String, List<String>> mapCategoryChildren;
    ListCategoryFireBaseAdapter listAdapter;
    Category categorySelected;
    public String categorySelectedId;
    public List<Category> categoryList;
    double discount;
    double price;
    String saleOff;
    CameraVideoHelper helper;
    PermissionGrantedHelper permissionGrantedHelper;

    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_DOWNLOAD_URL = "key_download_url";

    private Uri mDownloadImageUrl = null;
    private Uri mDownloadVideoUrl = null;
    private Uri fileUri; // file url to store image/video
    private List<Uri> uriList;
    private Uri fileUriSelected; // file url to store image/video
    public int requestCodeMedia = -1;

    // [START declare_ref]
    private StorageReference mStorageRef;
    StorageReference photoRef;

    FirebaseUser mUser;
    User user;
    private DatabaseReference databaseReference, mDatabaseUser;

    /**
     * The {@code FirebaseAnalytics} used to record screen views.
     */
    Image imageHeader;
    List<Video> videos;
    Video video;
    Query queryCategory;
    Location locationAdvertise;
    private FirebaseAnalytics mFirebaseAnalytics;
    String nameCategory;
    AlertDialog.Builder dlgAlert;
    private ImageView imgPreview;
    private VideoView videoPreview;


    //ImageView imgPreviewEditPost;
    List<Image> images;
    private MultiLevelListViewFirebase mListView;


    /*VIEWPAGER*/
    protected View view;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    RelativeLayout rlSlideImage;
    ArrayList<String> stImages;

    int indexAdvertise, indexFragment;
    boolean fromMess;
    ImageButton ibBackEditPost;
    private PrefManager prefManager;

    // ADD MORE FIELD
    EditText edtFaxEditPost, edtAddressEditPost, edtWebsiteEditPost, edtEmailEditPost, edtPhoneEditPost;
    ToggleButton tbAllowEmailEditPost, tbAllowCallEditPost;
    LinearLayout llEmailEditAdvertise, llPhoneEditAdvertise;

    boolean isCallAble, isEmailAble;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }*/

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_new_post);

        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        mapCategoryChildren = new HashMap<>();
        categorySelectedId = Constant.sosokanCategoryAll;

        nameImage = "";
        nameVideo = "";
        permissionGrantedHelper = new PermissionGrantedHelper(this);
        if (!permissionGrantedHelper.isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

        permissionGrantedHelper.checkAndRequestPermissionForCamera();
        categoryList = new ArrayList<>();
        uriList = new ArrayList<>();
        imagesMap = new HashMap<>();

        imagesListUpload = new ArrayList<>();
        stImages = new ArrayList<>();
        // mDemoSlider = (SliderLayout) findViewById(R.id.sliderEditPost);

        String languageToLoad = LocaleHelper.getLanguage(EditAdvertiseActivity.this);
        isChineseApp = languageToLoad.toString().equals("zh");
        setContentView(R.layout.activity_post_edit);
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(this);
        }
        conversationList = new ArrayList<>();
        if (getIntent() != null) {

            advertiseId = getIntent().getStringExtra(Constant.ADVERTISEID);
            idCategory = getIntent().getStringExtra(Constant.CATEGORYID);
            indexAdvertise = getIntent().getIntExtra(Constant.INDEX_ADVERTISE, 0);
            idCategory = getIntent().getStringExtra(Constant.CATEGORYID);
            indexFragment = getIntent().getIntExtra(Constant.INDEX_FRAGMENT, 0);
            fromMess = getIntent().getBooleanExtra(Constant.FROM_MESSAGE, false);
            conversationId = getIntent().getStringExtra(Constant.CONVERSATIONID);

            if (advertiseId != null && idCategory != null) {
                Log.e(Constant.ADVERTISEID, advertiseId);
                categorySelectedId = idCategory;
                Log.e(Constant.CATEGORYID, idCategory);
            } else {
                categorySelectedId = Constant.sosokanCategoryAll;
            }

        } else {
            categorySelectedId = Constant.sosokanCategoryAll;
        }


        initView();
        initViewRecycleCategory();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        setUpFireBase();

        // mDemoSlider = (SliderLayout) findViewById(R.id.sliderEditPost);
        //   custom_indicator = (PagerIndicator) findViewById(R.id.custom_indicator_edit_post);
        EndCallListener callListener = new EndCallListener();
        TelephonyManager mTM = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
        handleInstanceState(savedInstanceState);
    }

    public void setUpFireBase() {
        databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Config.FIREBASE_URL);
        refConversation = databaseReference.child(Constant.CONVERSATIONS);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();
            }
        };

        if (mUser != null) {
            mDatabaseUser = databaseReference.child(Constant.USERS).child(mUser.getUid());

//            queryCategory = mDatabaseCategory;
            databaseReference.child(Constant.CATEGORIES).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    categoryAll = null;
                    mapCategory = new HashMap<>();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        try {
                            Category category = postSnapshot.getValue(Category.class);
                            if (category != null) {
                                categoryList.add(category);
                                if (category.getId() != null) {
                                    mapCategory.put(category.getId(), category);
                                }
                                if (category != null && category.getId().equals(Constant.sosokanCategoryAll)) {
                                    categoryAll = category;
                                }

                            }
                        } catch (Exception ex) {

                        }
                    }
                    btnPostEditPost.setEnabled(true);
                    List<Category> categories = new ArrayList<>();
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (categoryList.get(i) != null && categoryList.get(i).getName() != null && !categoryList.get(i).getName().equals("All")) {
                            categories.add(categoryList.get(i));
                        }
                    }
                    if (categoryAll != null) categories.add(0, categoryAll);
                    categoryList = categories;

                    setAdapterCategory();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });


            mDatabaseUser.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                userId = user.getId();
                                mapFavorite = user.getFavorites();

                                searchPost();
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

        mStorageRef = FirebaseStorage.getInstance().getReference();
    }


    private void initView() {
        rlSlideImage = (RelativeLayout) findViewById(R.id.rlSlideImage);
        intro_images = (ViewPager) findViewById(R.id.pager_introduction);

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        ibBackEditPost = (ImageButton) findViewById(R.id.ibBackEditPost);
        ibBackEditPost.setOnClickListener(this);
        /* ImageButton ibLocationEditPost,ibBackSelectCategoryEditPost;
        Spinner spCategoryEditPost;
        TextView tvCategoryEditPost,tvLocationEditPost,tvTitleSubscriptionEditPost;
        EditAdvertiseActivity edtTitleEditPost,edtDescriptionEditPost,edtPriceEditPost,edtDiscountEditPost;*/
        ibLocationEditPost = (ImageButton) findViewById(R.id.ibLocationEditPost);
        ibLocationEditPost.setOnClickListener(this);
        ibBackSelectCategoryEditPost = (ImageButton) findViewById(R.id.ibBackSelectCategoryEditPost);
        ibBackSelectCategoryEditPost.setOnClickListener(this);
        ibRecordVideoEditPost = (ImageButton) findViewById(R.id.ibRecordVideoEditPost);
        ibRecordVideoEditPost.setOnClickListener(this);

        ibCapturePictureEditPost = (ImageButton) findViewById(R.id.ibCapturePictureEditPost);
        ibCapturePictureEditPost.setOnClickListener(this);

      //  spCategoryEditPost = (Spinner) findViewById(R.id.spCategoryEditPost);
        tvCategoryEditPost = (TextView) findViewById(R.id.tvCategoryEditPost);
        tvCategoryEditPost.setOnClickListener(this);
        tvLocationEditPost = (TextView) findViewById(R.id.tvLocationEditPost);
        tvTitleSubscriptionEditPost = (TextView) findViewById(R.id.tvTitleSubscriptionEditPost);
        edtTitleEditPost = (EditText) findViewById(R.id.edtTitleEditPost);
        edtDescriptionEditPost = (EditText) findViewById(R.id.edtDescriptionEditPost);
        edtDescriptionEditPost = (EditText) findViewById(R.id.edtDescriptionEditPost);
        edtPriceEditPost = (EditText) findViewById(R.id.edtPriceEditPost);
        edtDiscountEditPost = (EditText) findViewById(R.id.edtDiscountEditPost);
        btnPostEditPost = (Button) findViewById(R.id.btnPostEditPost);
        btnPostEditPost.setOnClickListener(this);
        btnEditPhotoEditPost = (Button) findViewById(R.id.btnEditPhotoEditPost);
        btnEditPhotoEditPost.setOnClickListener(this);

        llInformationAdvertiseEditPost = (LinearLayout) findViewById(R.id.llInformationAdvertiseEditPost);
        llSelectCategoryEditPost = (LinearLayout) findViewById(R.id.llSelectCategoryEditPost);
        llDiscountEditPost = (LinearLayout) findViewById(R.id.llDiscountEditPost);

        imgPreview = (ImageView) findViewById(R.id.imgPreviewEditPost);

        videoPreview = (VideoView) findViewById(R.id.videoPreview);

        if (categorySelectedId.equals(Constant.sosokanCategoryCouponDiscount)) {
            llDiscountEditPost.setVisibility(View.VISIBLE);
        } else {

            llDiscountEditPost.setVisibility(View.GONE);
        }

        edtAddressEditPost = (EditText) findViewById(R.id.edtAddressEditPost);

        edtFaxEditPost = (EditText) findViewById(R.id.edtFaxEditPost);

        edtWebsiteEditPost = (EditText) findViewById(R.id.edtWebsiteEditPost);

        edtEmailEditPost = (EditText) findViewById(R.id.edtEmailEditPost);

        edtPhoneEditPost = (EditText) findViewById(R.id.edtPhoneEditPost);

        tbAllowEmailEditPost = (ToggleButton) findViewById(R.id.tbAllowEmailEditPost);

        tbAllowCallEditPost = (ToggleButton) findViewById(R.id.tbAllowCallEditPost);

        tbAllowEmailEditPost.setOnClickListener(this);
        tbAllowCallEditPost.setOnClickListener(this);

        llEmailEditAdvertise = (LinearLayout) findViewById(R.id.llEmailEditAdvertise);

        llPhoneEditAdvertise = (LinearLayout) findViewById(R.id.llPhoneEditAdvertise);

    }


    public void searchPost() {
        databaseReference.child(Constant.ADVERTISES).child(advertiseId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        advertise = dataSnapshot.getValue(Advertise.class);
                        if (advertise != null) {

                            idCategory = advertise.getCategoryId();
                            if (advertise.getName() != null && !advertise.getName().isEmpty()) {
                                Log.e("Name advertise", advertise.getName());
                            }

                            /*advertise.setFax(edtFaxEditPost.getText().toString());
                            advertise.setWebsite(edtWebsiteEditPost.getText().toString());
                            advertise.setAddress(edtAddressEditPost.getText().toString());
                            advertise.setEnableEmail(isEmailAble);
                            advertise.setEnablePhone(isCallAble);
                            advertise.setEmail(edtEmailEditPost.getText().toString());
                            advertise.setPhone(edtPhoneEditPost.getText().toString());
                            */

                            edtFaxEditPost.setText(advertise.getFax());
                            edtWebsiteEditPost.setText(advertise.getWebsite());
                            edtAddressEditPost.setText(advertise.getAddress());
                            isEmailAble = advertise.isEnableEmail();
                            tbAllowCallEditPost.setChecked(isEmailAble);
                            isCallAble = advertise.isEnableEmail();
                            tbAllowCallEditPost.setChecked(isCallAble);
                            edtEmailEditPost.setText(advertise.getEmail());
                            edtPhoneEditPost.setText(advertise.getPhone());

                            if (advertise.getUserId() != null) {
                                ApplicationUtils.closeMessage();
                                //setUpMapReady(googleMap);
                                databaseReference
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
                                                            String CategoryId = "";
                                                            if (!idCategory.isEmpty()) {
                                                                CategoryId = idCategory;
                                                            } else if (advertise != null && advertise.getCategoryId() != null && !advertise.getCategoryId().isEmpty()) {
                                                                CategoryId = advertise.getCategoryId();
                                                            }
                                                            if (!CategoryId.isEmpty()) {
                                                                databaseReference
                                                                        .child(Constant.CATEGORIES).child(advertise.getCategoryId())
                                                                        .addListenerForSingleValueEvent(
                                                                                new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        try {
                                                                                            category = dataSnapshot.getValue(Category.class);
                                                                                            if (category != null) {
                                                                                                String nameCategory;
                                                                                                if (isChineseApp) {
                                                                                                    nameCategory = category.getNameChinese() == null ? category.getName() : category.getNameChinese();
                                                                                                } else {
                                                                                                    nameCategory = category.getName();
                                                                                                }

                                                                                                if (nameCategory != null && !nameCategory.isEmpty()) {
                                                                                                    tvCategoryEditPost.setTextColor(getResources().getColor(R.color.orange));
                                                                                                    tvCategoryEditPost.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                                                                                    tvCategoryEditPost.setText(nameCategory);
                                                                                                }
                                                                                                setAdapterCategory();
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
 /* ImageButton ibLocationEditPost,ibBackSelectCategoryEditPost;
        Spinner spCategoryEditPost;
        TextView tvCategoryEditPost,tvLocationEditPost,tvTitleSubscriptionEditPost;
        EditAdvertiseActivity edtTitleEditPost,edtDescriptionEditPost,edtPriceEditPost,edtDiscountEditPost;*/
                            if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty() && advertise.getName().contains("-")) {
                                String[] separated = advertise.getName().toString().split("-");
                                String stChinese = separated != null && separated.length > 0 ? separated[0] : "";
                                String stEnglish = separated != null && separated.length > 1 ? separated[1] : "";
                                if (isChineseApp) {
                                    edtTitleEditPost.setText(stChinese);
                                } else {
                                    edtTitleEditPost.setText(stEnglish);
                                }
                            } else if (advertise != null && advertise.getName() != null) {
                                edtTitleEditPost.setText(advertise.getName());
                            }
                            edtDescriptionEditPost.setText(advertise.getDescription());
                            edtPriceEditPost.setText(String.valueOf(advertise.getPrice()));
                            // edtDiscountEditPost.setText(String.valueOf(advertise.getSaleOff()));
                            tvLocationEditPost.setText(advertise.getAddress());


                            if (advertise != null && advertise.getImages() != null && advertise.getImages().size() > 0) {
                                images = new ArrayList<Image>();
                                final Map<String, Object> url_maps = advertise.getImages();
                                countImage = 0;
                                for (Map.Entry<String, Object> entry : url_maps.entrySet()) {
                                    databaseReference
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
                                                                if (images.size() > 0) {
                                                                    for (Image image1 : images) {
                                                                       /* TextSliderView textSliderView = new TextSliderView(EditAdvertiseActivity.this);
                                                                        // initialize a SliderLayout
                                                                        textSliderView
                                                                                .description("")
                                                                                .image(image1.getImageUrl().toString())
                                                                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                                                                .setOnSliderClickListener(EditAdvertiseActivity.this);

                                                                       // mDemoSlider.addSlider(textSliderView);*/
                                                                        stImages.add(image1.getImageUrl().toString());
                                                                    }
                                                                    ShowSlideImage();
                                                                   /* mDemoSlider.setVisibility(View.VISIBLE);
                                                                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                                                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                                                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                                                                    mDemoSlider.setDuration(4000);
                                                                    mDemoSlider.addOnPageChangeListener(EditAdvertiseActivity.this);*/
                                                                } else {

                                                                    displayImageForEdit();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                }
                            } else {
                                displayImageForEdit();
                            }

                            //   getInformationFavorite();

                           /* setUrlForVideo();*/

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void ShowSlideImage() {
        if (stImages != null && stImages.size() > 0) {
            rlSlideImage.setVisibility(View.VISIBLE);
            imgPreview.setVisibility(View.GONE);
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
            imgPreview.setVisibility(View.VISIBLE);
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


    private void setAdapterCategory() {
        // mapCategory = new HashMap<>();
        if (mapCategory == null) {
            mapCategory = new HashMap<>();
        }
        if (categoryList != null && categoryList.size() > 0) {

            List<String> items = new ArrayList<>();
            for (int i = 0; i < categoryList.size(); i++) {
                items.add(categoryList.get(i).getName());
                if (categoryList.get(i) != null && categoryList.get(i).getId() != null) {
                    mapCategory.put(categoryList.get(i).getId(), categoryList.get(i));
                }


            }
            String[] itemsNew = new String[categoryList.size()];
            itemsNew = items.toArray(itemsNew);
            ArrayAdapter<String> adapter = new ItemCategorySpinnerAdapter(this, R.layout.item_spinner_category, itemsNew);
            adapter.setDropDownViewResource(R.layout.item_spinner_category);
           /* spCategoryEditPost.getLayoutParams();
            spCategoryEditPost.setAdapter(adapter);
            spCategoryEditPost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    nameCategory = spCategoryEditPost.getSelectedItem().toString();
                    categorySelected = findCategory(nameCategory);
                    if (categorySelected != null) {
                        if (categorySelected.getId().equals(Constant.sosokanCategoryCouponDiscount)) {
                            llDiscountEditPost.setVisibility(View.VISIBLE);
                        } else {

                            llDiscountEditPost.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });*/
            adapter.notifyDataSetChanged();

        }

    }

    public void UpdateImageCover(String url) {
        imgPreview.setVisibility(View.VISIBLE);
        Log.e("UpdateImageCover", url);
        /*Glide.with(this).load(url).asBitmap().into(new SimpleTarget<Bitmap>(imgPreview.getWidth(), imgPreview.getHeight()) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imgPreview.setBackground(drawable);

                }
                Log.e("onResourceReady", "onResourceReady");
            }
        });*/
        Glide.with(imgPreview.getContext())
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
                        int newHeight = (int) (imageHeight * scaleFactor);

                        bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

                        imgPreview.setImageBitmap(bitmap);

                    }
                });
    }

    private void displayImageForEdit() {

        rlSlideImage.setVisibility(View.GONE);
        imgPreview.setVisibility(View.VISIBLE);
        // custom_indicator.setVisibility(View.GONE);
        if (advertise != null && advertise.getImageHeader() != null && !advertise.getImageHeader().getImageUrl().isEmpty()) {

            UpdateImageCover(advertise.getImageHeader().getImageUrl());
        } else if (advertise != null && advertise.getVideo() != null && advertise.getVideo().getVideoImage() != null && !advertise.getVideo().getVideoImage().getImageUrl().isEmpty()) {
            UpdateImageCover(advertise.getVideo().getVideoImage().getImageUrl());
        } else {
            if (advertise != null && advertise.getCategoryId() != null) {
                databaseReference
                        .child(Constant.CATEGORIES).child(advertise.getCategoryId())
                        .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            Category category = dataSnapshot.getValue(Category.class);
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
                                });
            }
        }
    }

    /*public void getInformationFavorite() {

        if (user != null && advertise != null) {
            mapFavorite = user.getFavorites();
            if (mapFavorite != null) {
                Object obj = mapFavorite.get(advertise.getId());
                if (obj != null) {
                    //isFavorite = (boolean) obj;
                }
            }

        }
    }*/


    @Override
    protected void onDestroy() {
        if (getApplicationContext() != null) {
            Glide.with(getApplicationContext()).pauseRequests();
            Glide.get(getApplicationContext()).clearMemory();
        }
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.ibBackEditPost:

                if (advertiseId != null && !advertiseId.isEmpty()) {
                    intent = new Intent(this, AdvertiseDetailActivity.class);
                    intent.putExtra(Constant.INDEX_ADVERTISE, indexAdvertise);
                    intent.putExtra(Constant.ADVERTISEID, advertiseId);
                    intent.putExtra(Constant.CATEGORYID, idCategory);
                    intent.putExtra(Constant.FROM_MESSAGE, true);
                    intent.putExtra(Constant.CONVERSATIONID, conversationId);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    overridePendingTransition(R.anim.start_activity_left_to_right, 0);
                }

                //  finish();
                break;
            case R.id.ibLocationEditPost:
                getAddressFromLocation();
                break;
            case R.id.ibBackSelectCategoryEditPost:
                if (categorySelected != null) {
                    String nameCategory;
                    if (isChineseApp) {
                        nameCategory = categorySelected.getNameChinese() == null ? categorySelected.getName() : categorySelected.getNameChinese();
                    } else {
                        nameCategory = categorySelected.getName();
                    }
                    llInformationAdvertiseEditPost.setVisibility(View.VISIBLE);
                    llSelectCategoryEditPost.setVisibility(View.GONE);
                    if (nameCategory != null && !nameCategory.isEmpty()) {
                        tvCategoryEditPost.setTextColor(getResources().getColor(R.color.orange));
                        tvCategoryEditPost.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        tvCategoryEditPost.setText(nameCategory);
                    }
                }

                break;
            case R.id.ibRecordVideoEditPost:
                recordVideo();
                break;
            case R.id.ibCapturePictureEditPost:

                if (images != null && images.size() >= 5) {
                    showMessageError(getResources().getString(R.string.alert), getResources().getString(R.string.error_reach_limit));
                } else if (imagesListUpload != null && imagesListUpload.size() >= 5) {
                    showMessageError(getResources().getString(R.string.alert), getResources().getString(R.string.error_reach_limit));
                } else {
                    captureImage();
                }
                break;
            case R.id.tvCategoryEditPost:
                llInformationAdvertiseEditPost.setVisibility(View.GONE);
                llSelectCategoryEditPost.setVisibility(View.VISIBLE);
                break;
            case R.id.btnPostEditPost:
                postDataToFirebase();
                break;
            case R.id.btnEditPhotoEditPost:
                if (images != null && images.size() >= 5) {
                    showMessageError(getResources().getString(R.string.alert), getResources().getString(R.string.error_reach_limit));
                } else if (imagesListUpload.size() >= 5) {
                    showMessageError(getResources().getString(R.string.alert), getResources().getString(R.string.error_reach_limit));
                } else {
                    selectImageFromGallery();
                }
                break;
            case R.id.tbAllowEmailNewPost:
                isEmailAble = !isEmailAble;
                if (isEmailAble) {
                    llEmailEditAdvertise.setVisibility(View.VISIBLE);
                } else {
                    llEmailEditAdvertise.setVisibility(View.GONE);
                }

                break;
            case R.id.tbAllowCallNewPost:
                isCallAble = !isCallAble;
                if (isCallAble) {
                    llPhoneEditAdvertise.setVisibility(View.VISIBLE);
                } else {
                    llPhoneEditAdvertise.setVisibility(View.GONE);
                }
                break;

        }
    }

    private Category findCategory(String nameCategory) {
        for (Category category : categoryList) {
            if (category.getName().equals(nameCategory)) {
                //System.out.print(nameCategory);
                return category;
            }
        }
        return null;

    }

    public void getAddressFromLocation() {
        if (permissionGrantedHelper == null)
            permissionGrantedHelper = new PermissionGrantedHelper(this);
        if (permissionGrantedHelper != null) {
            permissionGrantedHelper.checkAndRequestPermissionForMap();
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(EditAdvertiseActivity.this);
        }

        GPSTracker gpsTracker = new GPSTracker(this);

        address = gpsTracker.getCompleteAddressString();
        locationAdvertise = gpsTracker.getLocation();
        tvLocationEditPost.setText(address);
        ApplicationUtils.closeMessage();
    }

    public void showMessageError(String title, String message) {
        try {
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&  !((Activity)getApplicationContext()).isDestroyed()) {
            dlgAlert = new AlertDialog.Builder(EditAdvertiseActivity.this);
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

    private Advertise addingValuesToPost() {
        if (user != null) {


            advertise.setUserId(user.getId());
            advertise.setName(title);
            String coupon = getResources().getString(R.string.dollar) + discount;
            advertise.setCoupon(coupon);
            advertise.setPrice(price);
            advertise.setDescription(description);
            advertise.setDescriptionPlainText(description);
            advertise.setAddress(address);
            if (locationAdvertise != null && !tvLocationEditPost.getText().toString().isEmpty()) {
                LocationSosokan locationSosokan = new LocationSosokan();
                locationSosokan.setLatitude(locationAdvertise.getLatitude());
                locationSosokan.setLongitude(locationAdvertise.getLongitude());
                advertise.setLatitude(locationAdvertise.getLatitude());
                advertise.setLongitude(locationAdvertise.getLongitude());
                advertise.setLocation(locationSosokan);
                advertise.setAddress(address);
            } else {
                if (user.getAddress() != null && !user.getAddress().isEmpty()) {
                    advertise.setAddress(user.getAddress());
                } else {
                    getAddressFromLocation();
                    if (locationAdvertise != null && !tvLocationEditPost.getText().toString().isEmpty()) {
                        LocationSosokan locationSosokan = new LocationSosokan();
                        locationSosokan.setLatitude(locationAdvertise.getLatitude());
                        locationSosokan.setLongitude(locationAdvertise.getLongitude());
                        advertise.setLatitude(locationAdvertise.getLatitude());
                        advertise.setLongitude(locationAdvertise.getLongitude());
                        advertise.setLocation(locationSosokan);
                        advertise.setAddress(address);
                    }
                }

            }
          /*  categorySelected = findCategory(nameCategory);
            if (categorySelected != null) {
                advertise.setCategoryId(categorySelected.getId());
            }*/
            advertise.setCategoryId(categorySelectedId);
            if (imageHeader == null) {
                if (imagesListUpload != null && imagesListUpload.size() > 0) {
                    imageHeader = imagesListUpload.get(0);

                } else if (nameImage.trim().length() > 0) {
                    if (mDownloadImageUrl != null) {
                        if (imageWidth == 0) imageWidth = 200;
                        if (imageHeight == 0) imageHeight = 200;
                        String imageId = generateId();
                        imageHeader.setId(imageId);
                        imageHeader.setImageUrl(mDownloadImageUrl.toString());
                        imageHeader.setWidth(imageWidth);
                        imageHeader.setHeight(imageHeight);
                        advertise.setImageHeader(imageHeader);
                    }

                }
            }

            if (nameVideo.trim().length() > 0 && video != null && video.getId() != null) {

                videos.add(video);
                advertise.setVideo(video);
            }
            //   advertise.setCreatedAt(DateUtils.getDateInformation());
            double time = 0 - advertise.getCreatedAt();
            advertise.setDescendingTime(time);
            advertise.setUpdatedAt(DateUtils.getDateInformation());
            advertise.setFavoriteCount(0);
            // advertise.setOwner(user);
            //  advertise.setSaleOff(saleOff);
            advertise.setFax(edtFaxEditPost.getText().toString());
            advertise.setWebsite(edtWebsiteEditPost.getText().toString());
            advertise.setAddress(edtAddressEditPost.getText().toString());
            advertise.setEnableEmail(isEmailAble);
            advertise.setEnablePhone(isCallAble);
            advertise.setEmail(edtEmailEditPost.getText().toString());
            advertise.setPhone(edtPhoneEditPost.getText().toString());
            return advertise;
        }
        return null;
    }

    private void getAllValueToPost() {
        title = edtTitleEditPost.getText().toString().trim() + "-" + edtTitleEditPost.getText().toString().trim();
        description = edtDescriptionEditPost.getText().toString().trim();
        stDiscount = !edtDiscountEditPost.getText().toString().isEmpty() ? edtDiscountEditPost.getText().toString().trim() : "0.00";
        address = tvLocationEditPost.getText().toString();

        //TODO: Category
       // nameCategory = spCategoryEditPost.getSelectedItem().toString();
        String stPrice = !edtPriceEditPost.getText().toString().isEmpty() ? edtPriceEditPost.getText().toString() : "0.00";
        saleOff = "0";
        discount = 0.00;
        price = 0;
        try {
            saleOff = stDiscount;//Double.parseDouble(stDiscount);
            discount = Double.parseDouble(stDiscount); // Make use of autoboxing.  It's also easier to read.
            price = Double.parseDouble(stPrice);
        } catch (NumberFormatException e) {
            // p did not contain a valid double
        }


    }

    private void selectImageFromGallery() {
        permissionGrantedHelper = new PermissionGrantedHelper(this);
        if (permissionGrantedHelper.checkAnPermissionReadStorage()) {
            callIntentGallery();
        } else {
            permissionGrantedHelper.checkAndRequestPermissionForCamera();
        }
    }

    private void callIntentGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    /*
     * Capturing Camera Image will lauch camera app requrest image capture
     */

    private void captureImage() {

        permissionGrantedHelper = new PermissionGrantedHelper(this);
        if (permissionGrantedHelper.isDeviceSupportCamera()) {
            if (permissionGrantedHelper.checkAnPermissionCamera() && permissionGrantedHelper.checkAnPermissionWriteStorage() && permissionGrantedHelper.checkAnPermissionReadStorage()) {
                callIntentCamera();
            } else {
                permissionGrantedHelper.checkAndRequestPermissionForCamera();
            }
        } else {
            // TODO
            // Show message not support camera
        }
    }

    public void callIntentCamera() {

        // Choose file storage location, must be listed in res/xml/file_paths.xml


        /*Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                // Create and launch the intent

            }
        });
        thread.start();*/
        File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
        File file = new File(dir, generateId() + ".jpg");
        try {
            // Create directory if it does not exist.
            if (!dir.exists()) {
                dir.mkdir();
            }
            boolean created = file.createNewFile();
            Log.d(TAG, "file.createNewFile:" + file.getAbsolutePath() + ":" + created);
        } catch (IOException e) {
            Log.e(TAG, "file.createNewFile" + file.getAbsolutePath() + ":FAILED", e);
        }
        // Create content:// URI for file, required since Android N
        // See: https://developer.android.com/reference/android/support/v4/content/FileProvider.html
        helper = new CameraVideoHelper();
        try {
            fileUri = helper.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        } catch (Exception ex) {
            Log.e("error multimedia", ex.toString());
        }
        if (fileUri != null) {
            uriList.add(fileUri);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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
            Log.e("Advertise EDIT ", "=========== handleInstanceState");

            Log.e("indexAdvertise ", " " + indexAdvertise);
            Log.e("advertiseId ", " " + advertiseId);
            Log.e("idCategory ", " " + idCategory);
            Log.e("indexFragment ", " " + indexFragment);

            if (fileUri != null) {
                savedInstanceState.putParcelable("file_uri", fileUri);
            }

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
            advertiseId = savedInstanceState.getString(Constant.ADVERTISEID);
            idCategory = savedInstanceState.getString(Constant.CATEGORYID);
            indexFragment = savedInstanceState.getInt(Constant.INDEX_FRAGMENT, 0);
            fromMess = savedInstanceState.getBoolean(Constant.FROM_MESSAGE, false);
            conversationId = savedInstanceState.getString(Constant.CONVERSATIONID);
            fileUri = savedInstanceState.getParcelable("file_uri");
            Log.e("Ad Detail RestoreIns", "===================");
            Log.e("Advertise Detail ", "=========== handleInstanceState");

            Log.e("indexAdvertise ", " " + indexAdvertise);
            Log.e("advertiseId ", " " + advertiseId);
            Log.e("idCategory ", " " + idCategory);
            Log.e("indexFragment ", " " + indexFragment);
        }
    }

    /*
     * Recording video
     */
    private void recordVideo() {
        permissionGrantedHelper = new PermissionGrantedHelper(this);
        if (permissionGrantedHelper.isDeviceSupportCamera()) {
            if (permissionGrantedHelper.checkAnPermissionCamera() && permissionGrantedHelper.checkAnPermissionWriteStorage() && permissionGrantedHelper.checkAnPermissionReadStorage()) {

                File dir = new File(Environment.getExternalStorageDirectory() + "/videos");
                File file = new File(dir, generateId() + ".mp4");
                try {
                    // Create directory if it does not exist.
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    boolean created = file.createNewFile();
                    Log.d(TAG, "file.createNewFile:" + file.getAbsolutePath() + ":" + created);
                } catch (IOException e) {
                    Log.e(TAG, "file.createNewFile" + file.getAbsolutePath() + ":FAILED", e);
                }
                // Create content:// URI for file, required since Android N
                // See: https://developer.android.com/reference/android/support/v4/content/FileProvider.html
                helper = new CameraVideoHelper();

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                try {
                    fileUri = helper.getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                } catch (Exception ex) {
                    Log.e("error video", ex.toString());
                }
                if (fileUri != null) {
                    // set video quality
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
                    // name

                    // start the video capture Intent

                    startActivityForResult(intent, Constant.CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
                }
            } else {
                permissionGrantedHelper.checkAndRequestPermissionForCamera();
                // TODO
                // callback to take camera
            }
        } else {
            // TODO
            // Show message not support camera
        }

    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        // if the result is capturing Image
        if (requestCode == Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                if (fileUri != null) {
                    uploadImageToFirebase();
                    previewCapturedImage();
                } else {
                    Log.e(TAG, "File URI is null");
                }
            } else if (resultCode == RESULT_CANCELED) {

            } else {
                showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_failed_capture_image));
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                if (fileUri != null && user != null) {
                    if (!ApplicationUtils.isShow) {
                        ApplicationUtils.showProgress(EditAdvertiseActivity.this);
                    }
                    requestCodeMedia = MEDIA_TYPE_VIDEO;
                    nameVideo = fileUri.toString().substring(fileUri.toString().lastIndexOf("/") + 1, fileUri.toString().length());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(new File(fileUri.getPath()).getAbsolutePath(), options);
                    try {
                        Bitmap image1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri), null, null);
                        if (image1 != null) {
                            imageHeight = image1.getHeight();
                            imageWidth = image1.getWidth();

                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        imageHeight = options.outHeight;
                        imageWidth = options.outWidth;
                    }
                    nameImage = nameVideo;
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(new File(fileUri.getPath()).getAbsolutePath(),
                            MediaStore.Images.Thumbnails.MINI_KIND);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data1 = baos.toByteArray();

                    photoRef = mStorageRef.child(Constant.IMAGES);
                    UploadTask uploadTask = photoRef.putBytes(data1);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(new File(fileUri.getPath()).getAbsolutePath(), options);
                            try {
                                Bitmap image1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri), null, null);
                                if (image1 != null) {
                                    imageHeight = image1.getHeight();
                                    imageWidth = image1.getWidth();
                                }

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                imageHeight = options.outHeight;
                                imageWidth = options.outWidth;
                            }
                            if (imageWidth == 0) imageWidth = 200;
                            if (imageHeight == 0) imageHeight = 200;
                            final Image image = new Image();
                            image.setId(generateId());
                            image.setCreatedAt(DateUtils.getDateInformation());
                            image.setDescendingTime(0 - image.getCreatedAt());
                            image.setAdvertiseId(advertiseId);
                            image.setStatus(true);
                            image.setVideoThumb(true);
                            image.setStoredInStorage(true);
                            image.setWidth(imageWidth);
                            image.setHeight(imageHeight);
                            image.setImageUrl(downloadUrl.toString());
                            image.setUserId(user.getId());
                            imagesListUpload.add(image);
                            imageHeader = image;
                            Log.e("Video image.getId()", image.getId());
                            uploadVideoTofirebase(fileUri);
                        }
                    });

                    previewVideo();
                } else {
                    Log.e(TAG, "File URI is null");
                }

            } else if (resultCode == RESULT_CANCELED) {
            } else {
                showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_failed_record_video));
            }
        } else if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {

                requestCodeMedia = SELECT_PICTURE;
                setImageSelected(data);
            }

        }
    }

    public void uploadVideoTofirebase(Uri fileUriVideo) {
        if (fileUriVideo != null) {
            if (!ApplicationUtils.isShow) {
                ApplicationUtils.showProgress(EditAdvertiseActivity.this);
            }
            photoRef = mStorageRef.child(Constant.VIDEOS);
            photoRef.putFile(fileUriVideo)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ApplicationUtils.closeMessage();
                            switch (requestCodeMedia) {
                                case MEDIA_TYPE_VIDEO:
                                    btnPostEditPost.setEnabled(true);
                                    mDownloadVideoUrl = taskSnapshot.getDownloadUrl();
                                    video = new Video();
                                    video.setId(generateId());
                                    video.setVideoURL(mDownloadVideoUrl.toString());
                                    video.setCreatedAt(DateUtils.getDateInformation());
                                    video.setDescendingTime(0 - video.getCreatedAt());
                                    video.setUserId(user.getId());
                                    video.setStatus(true);
                                    video.setStatus(true);
                                    if (user != null) {
                                        video.setUserId(user.getId());
                                        video.setOwnerId(user.getId());
                                        Map<String, Object> videos = user.getVideos();
                                        if (videos == null) {
                                            videos = new HashMap<>();
                                        }
                                        videos.put(video.getId(), 0 - video.getCreatedAt());
                                        user.setVideos(videos);
                                    }
                                    video.setVideoImage(imageHeader);
                                    video.setName(nameVideo);
                                    Log.e("Video image.getId()", video.getId());


                                    databaseReference.child(Constant.VIDEOS).child(video.getId()).setValue(video, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                ApplicationUtils.closeMessage();
                                                showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                                            }
                                        }
                                    });
                                    break;
                            }

                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            ApplicationUtils.closeMessage();
                            showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                        }
                    });
        }
    }


    private void setImageSelected(final Intent data) {
      /*  Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        thread.start();*/
        Bitmap bm = null;
        if (data != null) {
            fileUriSelected = data.getData();
            if (fileUriSelected != null) {
                fileUri = fileUriSelected;
                uploadImageToFirebase();


                try {
                    helper = new CameraVideoHelper(this);
                    bm = helper.decodeUri(data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (fileUriSelected != null) {
                    nameImage = fileUriSelected.toString().substring(fileUriSelected.toString().lastIndexOf("/") + 1, fileUriSelected.toString().length());

                }

                if (bm != null) {
                    Drawable drawable = new BitmapDrawable(getResources(), bm);
                    imgPreview.setBackgroundDrawable(drawable);

                }
            }

            //  imgPreview.setImageBitmap(bm);
        }
    }

    private void uploadImageToFirebase() {
        btnPostEditPost.setEnabled(false);
        rlSlideImage.setVisibility(View.VISIBLE);
        imgPreview.setVisibility(View.GONE);
        requestCodeMedia = MEDIA_TYPE_IMAGE;
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(EditAdvertiseActivity.this);
        }
        if (fileUri != null && user != null) {
            photoRef = mStorageRef.child(Constant.IMAGES).child(fileUri.getLastPathSegment());

            photoRef.putFile(fileUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            switch (requestCodeMedia) {
                                case SELECT_PICTURE:
                                case MEDIA_TYPE_IMAGE:
                                    mDownloadImageUrl = taskSnapshot.getDownloadUrl();
                                    if (mDownloadImageUrl != null) {
                                        nameImage = fileUri.toString().substring(fileUri.toString().lastIndexOf("/") + 1, fileUri.toString().length());

                                        /*Thread thread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {

                                        });
                                        //start thread
                                        thread.start();*/
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inJustDecodeBounds = true;
                                        BitmapFactory.decodeFile(new File(fileUri.getPath()).getAbsolutePath(), options);
                                        try {
                                            Bitmap image1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri), null, null);
                                            if (image1 != null) {
                                                imageHeight = image1.getHeight();
                                                imageWidth = image1.getWidth();
                                            }

                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                            imageHeight = options.outHeight;
                                            imageWidth = options.outWidth;
                                        }

                                        if (imageWidth == 0) imageWidth = 200;
                                        if (imageHeight == 0) imageHeight = 200;
                                        final Image image = new Image();
                                        image.setId(generateId());
                                        image.setCreatedAt(DateUtils.getDateInformation());
                                        image.setDescendingTime(0 - image.getCreatedAt());
                                        image.setAdvertiseId(advertiseId);
                                        image.setStatus(true);
                                        image.setUserId(user.getId());
                                        image.setStoredInStorage(true);
                                        image.setWidth(imageWidth);
                                        image.setHeight(imageHeight);
                                        image.setImageUrl(mDownloadImageUrl.toString());
                                        imagesListUpload.add(image);
                                        if (imageHeader == null) {
                                            imageHeader = image;
                                        }


                                        databaseReference.child(Constant.IMAGES).child(image.getId()).setValue(image, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    ApplicationUtils.closeMessage();
                                                    showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                                                } else {
                                                    Log.e("image", "Success");
                                                    TextSliderView textSliderView = new TextSliderView(EditAdvertiseActivity.this);
                                                    // initialize a SliderLayout

                                                    if (images != null && images.size() > 0 && images.size() < 4) {
                                                        for (Image image1 : images) {
                                                            textSliderView = new TextSliderView(EditAdvertiseActivity.this);
                                                            // initialize a SliderLayout
                                                            textSliderView
                                                                    .description("")
                                                                    .image(image1.getImageUrl().toString())
                                                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                                                    .setOnSliderClickListener(EditAdvertiseActivity.this);

                                                            stImages.add(image1.getImageUrl().toString());
                                                        }
                                                        textSliderView
                                                                .description("")
                                                                .image(image.getImageUrl().toString())
                                                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                                                .setOnSliderClickListener(EditAdvertiseActivity.this);
                                                        stImages.add(image.getImageUrl().toString());

                                                    } else {
                                                        /*textSliderView
                                                                .description("")
                                                                .image(image.getImageUrl().toString())
                                                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                                                .setOnSliderClickListener(EditAdvertiseActivity.this);
                                                        mDemoSlider.addSlider(textSliderView);*/
                                                        stImages.add(image.getImageUrl().toString());
                                                        // displayImageForEdit();
                                                    }
                                                    ShowSlideImage();
                                                    /*mDemoSlider.setVisibility(View.VISIBLE);
                                                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                                                    mDemoSlider.setDuration(4000);
                                                    mDemoSlider.addOnPageChangeListener(EditAdvertiseActivity.this);*/
                                                    btnPostEditPost.setEnabled(true);
                                                    ApplicationUtils.closeMessage();

                                                }
                                            }
                                        });
                                    }

                                    /*}
                                    break;*/

                            }

                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            ApplicationUtils.closeMessage();
                            Toast.makeText(activity, "Error: upload failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public final static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
        }
    }

    public void postDataToFirebase() {

        String alert;
        String message;
        if (edtTitleEditPost.getText().toString().trim().isEmpty()) {
            alert = getResources().getString(R.string.error);
            message = getResources().getString(R.string.error_input_title_advertise);
            showMessageError(alert, message);
        } else if (edtDescriptionEditPost.getText().toString().trim().isEmpty()) {
            alert = getResources().getString(R.string.error);
            message = getResources().getString(R.string.error_input_description_advertise);
            showMessageError(alert, message);
        } else if (categorySelectedId == null || categorySelectedId.isEmpty()) {
            alert = getResources().getString(R.string.error);
            message = getResources().getString(R.string.error_select_category_advertise);
            showMessageError(alert, message);
        } else if (edtPriceEditPost.getText().toString().isEmpty()) {
            alert = getResources().getString(R.string.error);
            message = getResources().getString(R.string.error_input_price_advertise);
            showMessageError(alert, message);
        } else if (categorySelected != null && categorySelected.getId().equals(Constant.sosokanCategoryCouponDiscount) && edtDiscountEditPost.getText().toString().trim().isEmpty()) {
            alert = getResources().getString(R.string.error);
            message = getResources().getString(R.string.error_input_discount_advertise);
            showMessageError(alert, message);
        } else if (isEmailAble && edtEmailEditPost.getText().toString().isEmpty()) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_you_did_not_setup_email);
            showMessageError(alert, message);
            tbAllowEmailEditPost.setChecked(isCallAble);
        } else if (isEmailAble && !isValidEmail((edtEmailEditPost.getText().toString()))) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_email_is_not_valid);
            showMessageError(alert, message);
            tbAllowEmailEditPost.setChecked(isCallAble);
        } else if (isCallAble && edtPhoneEditPost.getText().toString().isEmpty()) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_you_did_not_setup_phone);
            showMessageError(alert, message);
            tbAllowEmailEditPost.setChecked(isCallAble);
        } else if (!isCallAble && PhoneNumberUtils.isGlobalPhoneNumber(edtPhoneEditPost.getText().toString())) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_phone_number_is_not_valid);
            showMessageError(alert, message);
            tbAllowEmailEditPost.setChecked(isCallAble);
        } else {
            //   ApplicationUtils.showProgress(EditPostActivity.this);
            if (requestCodeMedia == SELECT_PICTURE) {
                fileUri = fileUriSelected;
            }
            uploadFromUri();
        }

    }

    // [START upload_from_uri]
    private void uploadFromUri() {
        //  Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());
        if (imagesListUpload.size() > 0) {
            postValueInToFireBase();

            ApplicationUtils.closeMessage();
        } else {
            if (fileUri != null) {
                switch (requestCodeMedia) {
                    case SELECT_PICTURE:
                    case MEDIA_TYPE_IMAGE:
                        uploadImageToFirebase();

                        break;
                    case MEDIA_TYPE_VIDEO:
                        if (fileUri != null) {
                            uploadVideoTofirebase(fileUri);
                        }

                        break;
                }
            } else {
                postValueInToFireBase();
            }

        }
    }
    // [END upload_from_uri]

    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            videoPreview.setVisibility(View.GONE);

            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
            if (fileUri != null) {
               /* Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                //start thread
                thread.start();*/
                // BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                        options);

                imgPreview.setImageBitmap(bitmap);

            }


           /* Drawable drawableCover = new BitmapDrawable(getResources(), bitmap);
            imgPreview.setBackground(drawableCover);*/

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void processDataMenu() {

        mapCategory = new HashMap<>();
        for (Category category : categories) {
            mapCategory.put(category.getId(), category);
            if (category != null && category.getId() != null && category.getId().equals(Constant.sosokanCategoryAll)) {
                categoryAll = category;
                categoryAllId = category.getId();
                // categorySelectedId = categoryAllId;
            }

            if (category.getSort() > 0) {
                if (category != null && !category.getId().equals(Constant.sosokanCategoryAll)
                        && category.getParentId() == null) {
                    categoriesChild.add(category);
                }

            }
            List<String> categoriesId;
            if (category.getParentId() != null) {
                categoriesId = mapCategoryChildren.get(category.getParentId());
                if (categoriesId == null) {
                    categoriesId = new ArrayList<>();
                }
                categoriesId.add(category.getId());
                mapCategoryChildren.put(category.getParentId(), categoriesId);
            }
        }
        Collections.sort(categoriesChild, new CategorySortComparator());
        categoriesChild.add(0, categoryAll);

        DataProviderCategory.initData(mapCategory, mapCategoryChildren, categories);
        categorySelected = categoriesChild.get(0);
        listAdapter.setDataItems(categoriesChild);

    }

    private void initViewRecycleCategory() {
        mListView = (MultiLevelListViewFirebase) findViewById(R.id.listViewCategorySelect);
        listAdapter = new ListCategoryFireBaseAdapter(EditAdvertiseActivity.this);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mapCategoryChildren = new HashMap<>();
        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        prefManager = new PrefManager(this);
        categories = prefManager.getListCategoriesFirebase();
        if (categories != null && categories.size() > 0) {
            processDataMenu();
        } else {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL);
            databaseReference.child(Constant.CATEGORIES).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    categories = new ArrayList<>();
                    categoriesChild = new ArrayList<>();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        try {
                            Category category = postSnapshot.getValue(Category.class);
                            if (category != null && category.getId() != null && !category.getId().isEmpty()) {

                                categories.add(category);
                                mapCategory.put(category.getId(), category);
                            }


                        } catch (Exception ex) {
                            Log.e(TAG, "GET CATEGORY ERROR: " + ex.toString());
                            Log.e(TAG, "GET CATEGORY postSnapshot: " + postSnapshot);
                        }
                    }

                    Gson gson = new Gson();
                    prefManager.setCategoriesFirebase(gson.toJson(categories));

                    processDataMenu();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getMessage());
                }
            });
        }


    }

    private OnItemCategoryClickListenerFirebase mOnItemClickListener = new OnItemCategoryClickListenerFirebase() {

        @Override
        public void onItemClicked(MultiLevelListViewFirebase parent, View view, Object item, ItemInfo itemInfo) {
            Category category = ((Category) item);
            if (category != null) {
                categorySelectedId = category.getId();
                categorySelected = category;
                Log.e("onGroupItemClicked cate", categorySelectedId);
                if (categorySelected != null) {
                    if (categorySelected.getId().equals(Constant.sosokanCategoryCouponDiscount)) {
                        llDiscountEditPost.setVisibility(View.VISIBLE);
                    } else {

                        llDiscountEditPost.setVisibility(View.GONE);
                    }
                }
            }
        }

        @Override
        public void onGroupItemClicked(MultiLevelListViewFirebase parent, View view, Object item, ItemInfo itemInfo) {
            Category category = ((Category) item);
            if (category != null) {
                categorySelectedId = category.getId();
                categorySelected = category;
                Log.e("onGroupItemClicked cate", categorySelectedId);
                if (categorySelected != null) {
                    if (categorySelected.getId().equals(Constant.sosokanCategoryCouponDiscount)) {
                        llDiscountEditPost.setVisibility(View.VISIBLE);
                    } else {

                        llDiscountEditPost.setVisibility(View.GONE);
                    }
                }
            }
        }
    };

    /*
     * Previewing recorded video
     */
    private void previewVideo() {
        try {
            // hide image preview
            imgPreview.setVisibility(View.GONE);

            videoPreview.setVisibility(View.VISIBLE);
            if (fileUri != null) {
                videoPreview.setVideoPath(fileUri.getPath());
                // start playing
                videoPreview.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpGeo(final Advertise advertise) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Constant.GEOFIRE);
        final GeoFire geoFire = new GeoFire(ref);
        if (advertise != null && advertise.getAddress() != null && !advertise.getAddress().isEmpty() && getApplicationContext() != null) {
            /*Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });
            thread.start();*/
            LatLng ll = getApplicationContext() != null ? LocaleHelper.getLocationFromAddress(advertise.getAddress(), getApplicationContext()) :
                    LocaleHelper.getLocationFromAddress(advertise.getAddress(), getBaseContext());
            geoFire.setLocation(advertise.getId(), new GeoLocation(ll.latitude, ll.longitude), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully!");
                    }
                }
            });
        }
    }

    public void findAdvertiseCategory(Category category) {
        if (category == null) return;
        //  Category category = mapCategory.get(id);
        if (category == null || category.getParentId() == null) return;
        Category categoryParent = mapCategory.get(category.getParentId());
        if (categoryParent == null) {
            categoryParent = category;
            Map<String, Object> mapAdParentEnglish = category.getAdvertisesEnglish();
            Map<String, Object> mapAdParentChinese = category.getAdvertisesChinese();
            if (mapAdParentEnglish == null) {
                mapAdParentEnglish = new HashMap<>();
            }
            if (mapAdParentChinese == null) {
                mapAdParentChinese = new HashMap<>();
            }
            if (categoryList != null) {
                for (Category categoryChild : categoryList) {
                    categoryChild = mapCategory.get(categoryChild.getId());
                    if (category.getId().equals(categoryChild.getParentId())) {
                        if (categoryParent.getAdvertisesEnglish() != null && categoryChild.getAdvertisesEnglish() != null) {
                            mapAdParentEnglish.putAll(categoryChild.getAdvertisesEnglish());
                            mapAdParentEnglish.putAll(categoryParent.getAdvertisesEnglish());
                            categoryParent.setAdvertisesEnglish(mapAdParentEnglish);
                        } else if (categoryChild.getAdvertisesEnglish() != null) {
                            mapAdParentEnglish.putAll(categoryChild.getAdvertisesEnglish());
                            categoryParent.setAdvertisesEnglish(mapAdParentEnglish);
                        } else if (categoryParent.getAdvertisesEnglish() != null) {
                            mapAdParentEnglish.putAll(categoryParent.getAdvertisesEnglish());
                            categoryParent.setAdvertisesEnglish(mapAdParentEnglish);
                        }

                        if (categoryParent.getAdvertisesChinese() != null && categoryChild.getAdvertisesChinese() != null) {
                            mapAdParentChinese.putAll(categoryChild.getAdvertisesChinese());
                            mapAdParentChinese.putAll(categoryParent.getAdvertisesChinese());
                            categoryParent.setAdvertisesChinese(mapAdParentChinese);
                        } else if (categoryChild.getAdvertisesChinese() != null) {
                            mapAdParentChinese.putAll(categoryChild.getAdvertisesChinese());
                            categoryParent.setAdvertisesChinese(mapAdParentChinese);
                        } else if (categoryParent.getAdvertisesChinese() != null) {
                            mapAdParentChinese.putAll(categoryParent.getAdvertisesChinese());
                            categoryParent.setAdvertisesChinese(mapAdParentChinese);
                        }

                        int count = 0;
                        if (categoryParent.getAdvertisesEnglish() != null && categoryParent.getAdvertisesChinese() != null) {
                            count = categoryParent.getAdvertisesEnglish().size() + categoryParent.getAdvertisesChinese().size();
                        } else if (categoryParent.getAdvertisesEnglish() != null) {
                            count = categoryParent.getAdvertisesEnglish().size();
                        } else if (categoryParent.getAdvertisesChinese() != null) {
                            count = categoryParent.getAdvertisesChinese().size();
                        }
                        categoryParent.setAdvertiseCount(count);
                        if (categoryParent.getId() != null) {
                            mapCategory.put(categoryParent.getId(), categoryParent);
                        }

                    }
                }
            }
        } else {
            Map<String, Object> mapAdParentEnglish = categoryParent.getAdvertisesEnglish();
            Map<String, Object> mapAdParentChinese = categoryParent.getAdvertisesChinese();
            if (mapAdParentEnglish == null) {
                mapAdParentEnglish = new HashMap<>();
            }
            if (mapAdParentChinese == null) {
                mapAdParentChinese = new HashMap<>();
            }
            /*================ENGLISH============================*/
            if (categoryParent.getAdvertisesEnglish() != null && category.getAdvertisesEnglish() != null) {
                mapAdParentEnglish.putAll(category.getAdvertisesEnglish());
                mapAdParentEnglish.putAll(categoryParent.getAdvertisesEnglish());
                categoryParent.setAdvertisesEnglish(mapAdParentEnglish);
            } else if (category.getAdvertisesEnglish() != null) {
                mapAdParentEnglish.putAll(category.getAdvertisesEnglish());
                categoryParent.setAdvertisesEnglish(mapAdParentEnglish);
            } else if (categoryParent.getAdvertisesEnglish() != null) {
                mapAdParentEnglish.putAll(categoryParent.getAdvertisesEnglish());
                categoryParent.setAdvertisesEnglish(mapAdParentEnglish);
            }
            /*================CHINESE============================*/
            if (categoryParent.getAdvertisesChinese() != null && category.getAdvertisesChinese() != null) {
                mapAdParentChinese.putAll(category.getAdvertisesChinese());
                mapAdParentChinese.putAll(categoryParent.getAdvertisesChinese());
                categoryParent.setAdvertisesChinese(mapAdParentChinese);
            } else if (category.getAdvertisesChinese() != null) {
                mapAdParentChinese.putAll(category.getAdvertisesChinese());
                categoryParent.setAdvertisesChinese(mapAdParentChinese);
            } else if (categoryParent.getAdvertisesChinese() != null) {
                mapAdParentChinese.putAll(categoryParent.getAdvertisesChinese());
                categoryParent.setAdvertisesChinese(mapAdParentChinese);
            }
             /*================END============================*/
            int count = 0;
            if (categoryParent.getAdvertisesEnglish() != null && categoryParent.getAdvertisesChinese() != null) {
                count = categoryParent.getAdvertisesEnglish().size() + categoryParent.getAdvertisesChinese().size();
            } else if (categoryParent.getAdvertisesEnglish() != null) {
                count = categoryParent.getAdvertisesEnglish().size();
            } else if (categoryParent.getAdvertisesChinese() != null) {
                count = categoryParent.getAdvertisesChinese().size();
            }
            categoryParent.setAdvertiseCount(count);
            if (categoryParent.getId() != null) {

                mapCategory.put(categoryParent.getId(), categoryParent);
            }
            findAdvertiseCategory(categoryParent);
        }

    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void postValueInToFireBase() {

        if (isNetworkAvailable(this)) {
            if (advertise != null) {

                if (!ApplicationUtils.isShow) {
                    ApplicationUtils.showProgress(EditAdvertiseActivity.this);
                }
                //Getting values to store
                getAllValueToPost();
                //Creating Person object

                advertise = addingValuesToPost();
                if (advertise != null) {
                    advertise.setChinese(isChineseApp);

                    Map<String, Object> map = new HashMap<>();
                    if (imagesListUpload != null && imagesListUpload.size() > 0) {
                        imageHeader = imagesListUpload.get(0);
                        Map<String, Object> images = advertise.getImages();
                        if (images == null) {
                            images = new HashMap<>();
                        }
                        for (Image image : imagesListUpload) {
                            images.put(image.getId(), 0 - image.getCreatedAt());
                        }
                        advertise.setImages(images);
                        //  user.setImages(images);
                    }
                    if (imageHeader != null) {
                        advertise.setImageHeader(imageHeader);
                    }

                    Map<String, Object> mapMyPostUser = user.getAdvertises();
                    if (mapMyPostUser == null) {
                        mapMyPostUser = new HashMap<>();
                    }
                    mapMyPostUser.put(advertise.getId(), 0 - advertise.getCreatedAt());

                    user.setAdvertises(mapMyPostUser);
                    Integer countMyPost = user.getMyAdvertiseCount();
                    user.setMyAdvertiseCount(countMyPost + 1);

                    Map<String, Object> mapUser = new HashMap<>();
                    mapUser.put(user.getId(), user);
                    //  advertise.setCreatedAt(DateUtils.getDateInformation());
                    double time = 0 - advertise.getCreatedAt();
                    advertise.setDescendingTime(time);
                    advertise.setUpdatedAt(DateUtils.getDateInformation());
                    Map<String, Object> mapAdvertise = new HashMap<>();
                    mapAdvertise.put(advertise.getId(), advertise);

                    if (categorySelected != null) {
                        int countCategory = categorySelected.getAdvertiseCount();
                        categorySelected.setAdvertiseCount(countCategory + 1);

                        if (!isChineseApp) {
                            Map<String, Object> mapAdvertisesInCate = categorySelected.getAdvertisesEnglish();
                            if (mapAdvertisesInCate == null) {
                                mapAdvertisesInCate = new HashMap<>();
                            }
                            mapAdvertisesInCate.put(advertise.getId(), 0 - advertise.getCreatedAt());
                            categorySelected.setAdvertisesEnglish(mapAdvertisesInCate);


                            if (!categorySelected.getId().equals(categoryAll.getId())) {
                                Map<String, Object> mapAdvertisesInCateAll = categoryAll.getAdvertisesEnglish();
                                if (mapAdvertisesInCateAll == null) {
                                    mapAdvertisesInCateAll = new HashMap<>();
                                }
                                mapAdvertisesInCateAll.put(advertise.getId(), 0 - advertise.getCreatedAt());
                                categoryAll.setAdvertisesEnglish(mapAdvertisesInCateAll);
                            }
                        } else {
                            Map<String, Object> mapAdvertisesInCate = categorySelected.getAdvertisesChinese();
                            if (mapAdvertisesInCate == null) {
                                mapAdvertisesInCate = new HashMap<>();
                            }
                            mapAdvertisesInCate.put(advertise.getId(), 0 - advertise.getCreatedAt());
                            categorySelected.setAdvertisesChinese(mapAdvertisesInCate);

                            if (!categorySelected.getId().equals(categoryAll.getId())) {
                                Map<String, Object> mapAdvertisesInCateAll = categoryAll.getAdvertisesChinese();
                                if (mapAdvertisesInCateAll == null) {
                                    mapAdvertisesInCateAll = new HashMap<>();
                                }
                                mapAdvertisesInCateAll.put(advertise.getId(), 0 - advertise.getCreatedAt());
                                categoryAll.setAdvertisesEnglish(mapAdvertisesInCateAll);
                            }
                        }
                        if (!categorySelected.getId().equals(categoryAll.getId())) {
                            int count = 0;
                            if (categoryAll.getAdvertisesEnglish() != null && categoryAll.getAdvertisesChinese() != null) {
                                count = categoryAll.getAdvertisesEnglish().size() + categoryAll.getAdvertisesChinese().size();
                            } else if (categoryAll.getAdvertisesEnglish() != null) {
                                count = categoryAll.getAdvertisesEnglish().size();
                            } else if (categoryAll.getAdvertisesChinese() != null) {
                                count = categoryAll.getAdvertisesChinese().size();
                            }
                            categoryAll.setAdvertiseCount(count);
                            if (categoryAll.getId() != null) {

                                mapCategory.put(categoryAll.getId(), categoryAll);
                            }
                        }
                        if (categorySelected.getId() != null) {

                            mapCategory.put(categorySelected.getId(), categorySelected);
                        }
                        findAdvertiseCategory(categorySelected);
                        map.put(Constant.CATEGORIES, mapCategory);
                        final Advertise finalAdvertise = advertise;
                        System.out.print(advertise);
                        System.out.print(advertise.getAdvertiseId());
                        databaseReference.child(Constant.ADVERTISES).child(advertise.getId()).setValue(finalAdvertise, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    ApplicationUtils.closeMessage();
                                    showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                                } else {
                                    Log.e("postValueInToFireBase", "success");
                                    Log.e("finalAdvertise.getId()", finalAdvertise.getId());
                                    setUpGeo(finalAdvertise);
                                    setUpAdvetiseMetas(finalAdvertise, user);
                                    ApplicationUtils.closeMessage();
                                    PrefManager prefManager = new PrefManager(getApplicationContext());
                                    prefManager.setUser(user);
                                    databaseReference.child(Constant.USERS).child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                ApplicationUtils.closeMessage();
                                                showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                                            } else {
                                                updateCategory();

                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }


                }
            }
        } else {
            ApplicationUtils.closeMessage();
            showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_internet));
        }
    }

    public void callAdvertiseDetail() {
        ApplicationUtils.closeMessage();
        Intent intent = new Intent(this, AdvertiseDetailActivity.class);
        if (advertise != null) {

            if (idCategory == null) {

                intent.putExtra(Constant.CATEGORYID, advertise.getCategoryId());
            } else {

                intent.putExtra(Constant.CATEGORYID, idCategory);
            }
            intent.putExtra(Constant.ADVERTISEID, advertise.getId());
            startActivity(intent);

            overridePendingTransition(R.anim.start_activity_left_to_right, 0);
        }
       /* Intent intent = new Intent(this, AdvertiseDetailActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.start_activity_right_to_left, 0);
        finish();*/
    }

    private void updateCategory() {


        if (mapCategory != null && mapCategory.size() > 0) {
            try {

                databaseReference.child(Constant.CATEGORIES).setValue(mapCategory, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            ApplicationUtils.closeMessage();
                            showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                        } else {
                            ApplicationUtils.closeMessage();
                            //  showMessageError("", getResources().getString(R.string.post_successfully));
                            callAdvertiseDetail();
                        }
                    }
                });
            } catch (Exception ex) {
                Log.e("New Advertise ", ex.toString());
                callAdvertiseDetail();
            }
        }
    }

    public void setUpAdvetiseMetas(Advertise advertise, User userOwner) {
        if (advertise != null && userOwner != null) {
            DatabaseReference ref = databaseReference.child(Constant.ADVERTISEMETAS);
            AdvertiseMeta advertiseMeta = new AdvertiseMeta();
            advertiseMeta.setId(generateId());
            advertiseMeta.setAdvertiseId(advertise.getId());
            advertiseMeta.setDescendingTime(0 - advertise.getCreatedAt());
            String stData = userOwner.getEmail() + userOwner.getPhoneNumber() + userOwner.getAddress() + userOwner.getCompanyName() + advertise.getName() + advertise.getDescription();
            advertiseMeta.setStringData(stData);
            ref.child(advertiseMeta.getId()).setValue(advertiseMeta, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        ApplicationUtils.closeMessage();
                        showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                    } else {
                        ApplicationUtils.closeMessage();
                    }
                }
            });
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

}
