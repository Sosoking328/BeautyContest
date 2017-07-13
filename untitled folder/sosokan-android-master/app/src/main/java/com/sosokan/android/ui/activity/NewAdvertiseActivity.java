package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
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
import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.AdvertiseMeta;
import com.sosokan.android.models.Category;
import com.sosokan.android.models.Image;
import com.sosokan.android.models.LocationSosokan;
import com.sosokan.android.models.User;
import com.sosokan.android.models.Video;
import com.sosokan.android.rest.UrlEndpoint;
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

import static com.sosokan.android.utils.Constant.*;
import static com.sosokan.android.utils.FireBaseUtils.generateId;

/**
 * Created by AnhZin on 8/26/2016.
 */
public class NewAdvertiseActivity extends BaseApp implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private static final String TAG = "NewPostActivity";


    private ImageView imgPreview;
    private VideoView videoPreview;
    private ImageButton ibCapturePictureNewPost, ibRecordVideoNewPost, ibBackNewPost, ibLocationNewPost;
    private RelativeLayout rlCoverAddNewPost;
    Spinner spCategory, spAction;
    private Button btnPostNewPost, btnEditPhotoNewPost;
    EditText edtTitleNewPost, edtDiscountNewPost, edtDescriptionNewPost, edtPriceNewPost;
    TextView tvLocationNewPost;
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

    // [END declare_ref]
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String title, description, stDiscount, address,
            nameImage, idUser, nameVideo, idPost;

    // String nameCategory;
    public List<Category> categoryList;
    double discount;
    double price;
    String saleOff;
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

    boolean isChineseApp;
    Activity activity;
    int imageHeight;
    int imageWidth;
    Map<String, Object> imagesMap;
    List<Image> imagesListUpload;
    //private SliderLayout mSlider;
    LinearLayout llDiscountNewAdvertise, llPriceNewAdvertise;
    TextView tvCategoryNewPost;

    String nameCategory;

    LinearLayout llSelectCategoryNewPost, llInformationAdvertiseNewPost;
    ImageButton ibBackSelectCategory;

    private MultiLevelListViewFirebase mListView;
    //private DatabaseReference mDatabaseCategory;
    private List<Category> categories, categoriesChild;
    public String categoryAllId = "All";
    public Category categoryAll = null;
    public static Map<String, Category> mapCategory;
    public static Map<String, List<String>> mapCategoryChildren;
    ListCategoryFireBaseAdapter listAdapter;
    public String categorySelectedId;
    Category categorySelected;
    LinearLayout llCategory;

    AlertDialog.Builder dlgAlert;
    private FirebaseAnalytics mFirebaseAnalytics;

    /*VIEWPAGER*/
    protected View view;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    RelativeLayout rlSlideImage;
    ArrayList<String> stImages;

    // ADD MORE FIELD
    EditText edtFaxNewPost, edtAddressNewPost, edtWebsiteNewPost, edtEmailNewPost, edtPhoneNewPost;
    ToggleButton tbAllowEmailNewPost, tbAllowCallNewPost;
    LinearLayout llEmailNewAdvertise, llPhoneNewAdvertise;

    boolean isCallAble, isEmailAble;
    //========
    private PrefManager prefManager;
    private RequestQueue mQueue;
    UrlEndpoint urlEndpoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_new_post);

        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        mapCategoryChildren = new HashMap<>();
        categorySelectedId = Constant.sosokanCategoryAll;
        stImages = new ArrayList<>();
        urlEndpoint = new UrlEndpoint();
        mQueue = Volley.newRequestQueue(this);
        InitView();

        initViewRecycleCategory();

        mapCategory = new HashMap<>();
        setUpFireBase();
        String languageToLoad = LocaleHelper.getLanguage(NewAdvertiseActivity.this);
        isChineseApp = languageToLoad.toString().equals("zh");
        if (savedInstanceState != null) {
            fileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            mDownloadImageUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
        }
        activity = this;
        // Checking camera availability
        permissionGrantedHelper = new PermissionGrantedHelper(this);
        if (!permissionGrantedHelper.isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

        permissionGrantedHelper.checkAndRequestPermissionForCamera();


        nameImage = "";
        nameVideo = "";

        categoryList = new ArrayList<>();
        uriList = new ArrayList<>();
        imagesMap = new HashMap<>();
        idPost = generateId();
        imagesListUpload = new ArrayList<>();
        handleInstanceState(savedInstanceState);
        //  mSlider = (SliderLayout) findViewById(R.id.sliderNewAdvertise);

    }

    public void setUpFireBase() {
        //  btnPostNewPost.setEnabled(false);
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL);
        // mDatabaseCategory = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.CATEGORIES);
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
                    btnPostNewPost.setEnabled(true);
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

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }

        mStorageRef = FirebaseStorage.getInstance().getReference();


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void InitView() {

        rlSlideImage = (RelativeLayout) findViewById(R.id.rlSlideImage);
        intro_images = (ViewPager) findViewById(R.id.pager_introduction);

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        ibBackNewPost = (ImageButton) findViewById(R.id.ibBackNewPost);
        ibBackNewPost.setOnClickListener(this);

        ibCapturePictureNewPost = (ImageButton) findViewById(R.id.ibCapturePictureNewPost);
        ibCapturePictureNewPost.setOnClickListener(this);

        ibRecordVideoNewPost = (ImageButton) findViewById(R.id.ibRecordVideoNewPost);
        ibRecordVideoNewPost.setOnClickListener(this);

        rlCoverAddNewPost = (RelativeLayout) findViewById(R.id.rlCoverAddNewPost);

        spCategory = (Spinner) findViewById(R.id.spCategory);

        btnPostNewPost = (Button) findViewById(R.id.btnPostNewPost);
        btnPostNewPost.setOnClickListener(this);

        rlCoverAddNewPost = (RelativeLayout) findViewById(R.id.rlCoverAddNewPost);

        btnEditPhotoNewPost = (Button) findViewById(R.id.btnEditPhotoNewPost);
        btnEditPhotoNewPost.setOnClickListener(this);

        edtDescriptionNewPost = (EditText) findViewById(R.id.edtDescriptionNewPost);

        edtDiscountNewPost = (EditText) findViewById(R.id.edtDiscountNewPost);

        edtTitleNewPost = (EditText) findViewById(R.id.edtTitleNewPost);

        edtPriceNewPost = (EditText) findViewById(R.id.edtPriceNewPost);

        tvLocationNewPost = (TextView) findViewById(R.id.tvLocationNewPost);

        imgPreview = (ImageView) findViewById(R.id.imgPreviewNewPost);

        ibLocationNewPost = (ImageButton) findViewById(R.id.ibLocationNewPost);
        ibLocationNewPost.setOnClickListener(this);

        videoPreview = (VideoView) findViewById(R.id.videoPreview);


        llDiscountNewAdvertise = (LinearLayout) findViewById(R.id.llDiscountNewAdvertise);

        llPriceNewAdvertise = (LinearLayout) findViewById(R.id.llPriceNewAdvertise);

        llDiscountNewAdvertise.setVisibility(View.GONE);


        tvCategoryNewPost = (TextView) findViewById(R.id.tvCategoryNewPost);
        tvCategoryNewPost.setOnClickListener(this);

        if (nameCategory != null && !nameCategory.isEmpty()) {
            tvCategoryNewPost.setText(nameCategory);
        }

        llSelectCategoryNewPost = (LinearLayout) findViewById(R.id.llSelectCategoryNewPost);
        llInformationAdvertiseNewPost = (LinearLayout) findViewById(R.id.llInformationAdvertiseNewPost);
        ibBackSelectCategory = (ImageButton) findViewById(R.id.ibBackSelectCategory);
        ibBackSelectCategory.setOnClickListener(this);


        edtAddressNewPost = (EditText) findViewById(R.id.edtAddressNewPost);

        edtFaxNewPost = (EditText) findViewById(R.id.edtFaxNewPost);

        edtWebsiteNewPost = (EditText) findViewById(R.id.edtWebsiteNewPost);

        edtEmailNewPost = (EditText) findViewById(R.id.edtEmailNewPost);

        edtPhoneNewPost = (EditText) findViewById(R.id.edtPhoneNewPost);

        tbAllowEmailNewPost = (ToggleButton) findViewById(R.id.tbAllowEmailNewPost);

        tbAllowCallNewPost = (ToggleButton) findViewById(R.id.tbAllowCallNewPost);

        tbAllowEmailNewPost.setOnClickListener(this);
        tbAllowCallNewPost.setOnClickListener(this);

        llEmailNewAdvertise = (LinearLayout) findViewById(R.id.llEmailNewAdvertise);

        llPhoneNewAdvertise = (LinearLayout) findViewById(R.id.llPhoneNewAdvertise);


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
                if (categoryList.get(i).getId() != null) {
                    mapCategory.put(categoryList.get(i).getId(), categoryList.get(i));
                }


            }
            String[] itemsNew = new String[categoryList.size()];
            itemsNew = items.toArray(itemsNew);
            ArrayAdapter<String> adapter = new ItemCategorySpinnerAdapter(this, R.layout.item_spinner_category, itemsNew);
            adapter.setDropDownViewResource(R.layout.item_spinner_category);
            spCategory.getLayoutParams();
            spCategory.setAdapter(adapter);
            spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    nameCategory = spCategory.getSelectedItem().toString();
                    categorySelected = findCategory(nameCategory);
                    if (categorySelected != null) {
                        if (categorySelected.getId().equals(Constant.sosokanCategoryCouponDiscount)) {
                            llDiscountNewAdvertise.setVisibility(View.VISIBLE);
                        } else {

                            llDiscountNewAdvertise.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
            adapter.notifyDataSetChanged();

        }

    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ibBackNewPost:
                intent = new Intent(this, MenuActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            case R.id.ibRecordVideoNewPost:
                recordVideo();
                break;
            case R.id.btnPostNewPost:
                //  nameCategory = spCategory.getSelectedItem().toString();
                //   categorySelected = findCategory(nameCategory);
                postDataToFirebase();
                break;
            case R.id.btnEditPhotoNewPost:
                if (imagesListUpload.size() >= 5) {
                    showMessageError(getResources().getString(R.string.alert), getResources().getString(R.string.error_reach_limit));
                } else {
                    selectImageFromGallery();
                }

                break;
            case R.id.ibCapturePictureNewPost:
                if (imagesListUpload.size() >= 5) {
                    showMessageError(getResources().getString(R.string.alert), getResources().getString(R.string.error_reach_limit));
                } else {
                    captureImage();
                }
                break;
            case R.id.ibLocationNewPost:
                getAddressFromLocation();
                break;

            case R.id.tvCategoryNewPost:
                llInformationAdvertiseNewPost.setVisibility(View.GONE);
                llSelectCategoryNewPost.setVisibility(View.VISIBLE);

                break;
            case R.id.ibBackSelectCategory:

                if (categorySelected != null) {
                    String nameCategory;
                    if (isChineseApp) {
                        nameCategory = categorySelected.getNameChinese() == null ? categorySelected.getName() : categorySelected.getNameChinese();
                    } else {
                        nameCategory = categorySelected.getName();
                    }
                    llInformationAdvertiseNewPost.setVisibility(View.VISIBLE);
                    llSelectCategoryNewPost.setVisibility(View.GONE);
                    if (nameCategory != null && !nameCategory.isEmpty()) {
                        tvCategoryNewPost.setTextColor(getResources().getColor(R.color.orange));
                        tvCategoryNewPost.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        tvCategoryNewPost.setText(nameCategory);
                    }
                }

                break;
            case R.id.tbAllowEmailNewPost:
                isEmailAble = !isEmailAble;
                if (isEmailAble) {
                    llEmailNewAdvertise.setVisibility(View.VISIBLE);
                } else {
                    llEmailNewAdvertise.setVisibility(View.GONE);
                }

                break;
            case R.id.tbAllowCallNewPost:
                isCallAble = !isCallAble;
                if (isCallAble) {
                    llPhoneNewAdvertise.setVisibility(View.VISIBLE);
                } else {
                    llPhoneNewAdvertise.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void processDataMenu() {
        for (Category category : categories) {
            mapCategory.put(category.getId(), category);
            if (category != null && category.getId() != null && category.getId().equals(Constant.sosokanCategoryAll)) {
                categoryAll = category;
                categoryAllId = category.getId();
                categorySelectedId = categoryAllId;
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
        listAdapter = new ListCategoryFireBaseAdapter(NewAdvertiseActivity.this);
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
                categorySelected = category;
                categorySelectedId = category.getId();
                Log.e("onItemClick cate", categorySelectedId);
                if (categorySelected != null) {
                    if (categorySelected.getId().equals(Constant.sosokanCategoryCouponDiscount)) {
                        llDiscountNewAdvertise.setVisibility(View.VISIBLE);
                    } else {

                        llDiscountNewAdvertise.setVisibility(View.GONE);
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
                        llDiscountNewAdvertise.setVisibility(View.VISIBLE);
                    } else {

                        llDiscountNewAdvertise.setVisibility(View.GONE);
                    }
                }
            }
        }
    };

    public void getAddressFromLocation() {
        permissionGrantedHelper.checkAndRequestPermissionForMap();
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(NewAdvertiseActivity.this);
        }

        GPSTracker gpsTracker = new GPSTracker(this);

        address = gpsTracker.getCompleteAddressString();
        locationAdvertise = gpsTracker.getLocation();
        tvLocationNewPost.setText(address);
        ApplicationUtils.closeMessage();
    }

    public void showMessageError(String title, String message) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)getApplicationContext()).isDestroyed()) {
            dlgAlert = new AlertDialog.Builder(NewAdvertiseActivity.this);
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

    @Override
    protected void onDestroy() {
        try {
            if (dlgAlert != null && dlgAlert.create().isShowing()) {
                dlgAlert.create().dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
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
        if (edtTitleNewPost.getText().toString().trim().isEmpty()) {
            alert = getResources().getString(R.string.error);
            message = getResources().getString(R.string.error_input_title_advertise);
            showMessageError(alert, message);
        } else if (edtDescriptionNewPost.getText().toString().trim().isEmpty()) {
            alert = getResources().getString(R.string.error);
            message = getResources().getString(R.string.error_input_description_advertise);
            showMessageError(alert, message);
        } else if (categorySelectedId == null || categorySelectedId.isEmpty()) {
            alert = getResources().getString(R.string.error);
            message = getResources().getString(R.string.error_select_category_advertise);
            showMessageError(alert, message);
        } else if (edtPriceNewPost.getText().toString().isEmpty()) {
            alert = getResources().getString(R.string.error);
            message = getResources().getString(R.string.error_input_price_advertise);
            showMessageError(alert, message);
        } else if (categorySelected != null && categorySelected.getId().equals(Constant.sosokanCategoryCouponDiscount) && edtDiscountNewPost.getText().toString().trim().isEmpty()) {
            alert = getResources().getString(R.string.error);
            message = getResources().getString(R.string.error_input_discount_advertise);
            showMessageError(alert, message);
        } else if (isEmailAble && edtEmailNewPost.getText().toString().isEmpty()) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_you_did_not_setup_email);
            showMessageError(alert, message);
            tbAllowEmailNewPost.setChecked(isCallAble);
        } else if (isEmailAble && !isValidEmail((edtEmailNewPost.getText().toString()))) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_email_is_not_valid);
            showMessageError(alert, message);
            tbAllowEmailNewPost.setChecked(isCallAble);
        } else if (isCallAble && edtPhoneNewPost.getText().toString().isEmpty()) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_you_did_not_setup_phone);
            showMessageError(alert, message);
            tbAllowCallNewPost.setChecked(isCallAble);
        } else if (!isCallAble && PhoneNumberUtils.isGlobalPhoneNumber(edtPhoneNewPost.getText().toString())) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_phone_number_is_not_valid);
            showMessageError(alert, message);
            tbAllowCallNewPost.setChecked(isCallAble);
        } else {
            //   ApplicationUtils.showProgress(NewAdvertiseActivity.this);
            if (requestCodeMedia == SELECT_PICTURE) {
                fileUri = fileUriSelected;
            }
            uploadFromUri();
        }

    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void postValueInToFireBase() {

        if (isNetworkAvailable(this)) {
            if (!ApplicationUtils.isShow) {
                ApplicationUtils.showProgress(NewAdvertiseActivity.this);
            }
            //Getting values to store
            getAllValueToPost();
            //Creating Person object
            Advertise advertise = new Advertise();
            advertise = addingValuesToPost(advertise);
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
                advertise.setCreatedAt(DateUtils.getDateInformation());
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
                    final Advertise finalAdvertise1 = advertise;
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
                                prefManager = new PrefManager(getApplicationContext());
                                prefManager.setUser(user);
                                syncAd(finalAdvertise1.getId());
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
        } else {
            ApplicationUtils.closeMessage();
            showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_internet));
        }
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
                            showMessageSuccess("", getResources().getString(R.string.post_successfully));


                        }
                    }
                });
            } catch (Exception ex) {
                Log.e("New Advertise ", ex.toString());
                callPageHome();
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

    public void setUpGeo(final Advertise advertise) {
        DatabaseReference ref = databaseReference.child(Constant.GEOFIRE);
        final GeoFire geoFire = new GeoFire(ref);
        if (advertise != null && advertise.getAddress() != null && !advertise.getAddress().isEmpty()) {
            /*Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });
            thread.start();*/
            LatLng ll = LocaleHelper.getLocationFromAddress(advertise.getAddress(), getApplicationContext());
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

    public void callPageHome() {
        ApplicationUtils.closeMessage();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.start_activity_right_to_left, 0);
        finish();
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

    private Advertise addingValuesToPost(Advertise advertise) {
        if (user != null) {

            advertise.setId(idPost);
            advertise.setAdvertiseId(idPost);
            advertise.setUserId(user.getId());
            advertise.setName(title);
            String coupon = getResources().getString(R.string.dollar) + discount;
            advertise.setCoupon(coupon);
            advertise.setPrice(price);
            advertise.setDescription(description);
            advertise.setDescriptionPlainText(description);
            advertise.setAddress(address);
            if (locationAdvertise != null && !tvLocationNewPost.getText().toString().isEmpty()) {
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
                    if (locationAdvertise != null && !tvLocationNewPost.getText().toString().isEmpty()) {
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

            if (nameVideo.trim().length() > 0 && video.getId() != null) {

                videos.add(video);
                advertise.setVideo(video);
            }
            advertise.setCreatedAt(DateUtils.getDateInformation());
            double time = 0 - advertise.getCreatedAt();
            advertise.setDescendingTime(time);
            advertise.setUpdatedAt(DateUtils.getDateInformation());
            advertise.setFavoriteCount(0);
            // advertise.setOwner(user);
            // advertise.setSaleOff(saleOff);

            advertise.setFax(edtFaxNewPost.getText().toString());
            advertise.setWebsite(edtWebsiteNewPost.getText().toString());
            advertise.setAddress(edtAddressNewPost.getText().toString());
            advertise.setEnableEmail(isEmailAble);
            advertise.setEnablePhone(isCallAble);
            advertise.setEmail(edtEmailNewPost.getText().toString());
            advertise.setPhone(edtPhoneNewPost.getText().toString());
            return advertise;
        }
        return null;
    }

    private void getAllValueToPost() {
        title = edtTitleNewPost.getText().toString().trim() + "-" + edtTitleNewPost.getText().toString().trim();
        description = edtDescriptionNewPost.getText().toString().trim();
        stDiscount = !edtDiscountNewPost.getText().toString().toString().isEmpty() ? edtDiscountNewPost.getText().toString().trim() : "0.00";
        address = tvLocationNewPost.getText().toString();
        nameCategory = spCategory.getSelectedItem().toString();
        String stPrice = !edtPriceNewPost.getText().toString().isEmpty() ? edtPriceNewPost.getText().toString() : "0.00";
        saleOff = "0";
        discount = 0.00;
        price = 0;
        String stSaleOff = !edtDiscountNewPost.getText().toString().toString().isEmpty() ? edtDiscountNewPost.getText().toString().trim() : "0";

        try {
            price = Double.parseDouble(stPrice);
            saleOff = stSaleOff;// Double.parseDouble(stSaleOff);
            discount = Double.parseDouble(stDiscount);
        } catch (NumberFormatException e) {
            Log.e("getAllValueToPost ", e.toString());
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
            // save file url in bundle as it will be null on scren orientation
            // changes
            if (fileUri != null) {
                savedInstanceState.putParcelable("file_uri", fileUri);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // save file url in bundle as it will be null on scren orientation
            // changes
            if (fileUri != null) {
                savedInstanceState.putParcelable("file_uri", fileUri);
            }
        }
        Log.e("new Detail Restore", "===================");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // get the file url
            fileUri = savedInstanceState.getParcelable("file_uri");
        }
    }

    /*
     * Recording video
     */
    private void recordVideo() {
        permissionGrantedHelper = new PermissionGrantedHelper(this);
        if (permissionGrantedHelper.isDeviceSupportCamera()) {
            if (permissionGrantedHelper.checkAnPermissionCamera() && permissionGrantedHelper.checkAnPermissionWriteStorage() && permissionGrantedHelper.checkAnPermissionReadStorage()) {
              /*  Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });

                thread.start();*/
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
                        ApplicationUtils.showProgress(NewAdvertiseActivity.this);
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
                            image.setAdvertiseId(idPost);
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
        if (fileUriVideo != null && user != null) {
            if (!ApplicationUtils.isShow) {
                ApplicationUtils.showProgress(NewAdvertiseActivity.this);
            }
            photoRef = mStorageRef.child(Constant.VIDEOS);
            photoRef.putFile(fileUriVideo)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ApplicationUtils.closeMessage();
                            switch (requestCodeMedia) {
                                case MEDIA_TYPE_VIDEO:
                                    btnPostNewPost.setEnabled(true);
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
        btnPostNewPost.setEnabled(false);
        rlSlideImage.setVisibility(View.VISIBLE);
        imgPreview.setVisibility(View.GONE);
        requestCodeMedia = MEDIA_TYPE_IMAGE;
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(NewAdvertiseActivity.this);
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
                                        image.setAdvertiseId(idPost);
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
                                                    /*TextSliderView textSliderView = new TextSliderView(NewAdvertiseActivity.this);
                                                    // initialize a SliderLayout
                                                    textSliderView
                                                            .description("")
                                                            .image(image.getImageUrl().toString())
                                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                                            .setOnSliderClickListener(NewAdvertiseActivity.this);
                                                    mSlider.addSlider(textSliderView);
                                                    mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                                    mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                                    mSlider.setCustomAnimation(new DescriptionAnimation());
                                                    //mSlider.setDuration(4000);
                                                    mSlider.addOnPageChangeListener(NewAdvertiseActivity.this);*/
                                                    stImages.add(image.getImageUrl().toString());
                                                    ShowSlideImage();
                                                    btnPostNewPost.setEnabled(true);
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

    // [START upload_from_uri]
    private void uploadFromUri() {
        ApplicationUtils.closeMessage();
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

    public void syncAd(final String adIdFirebase) {
        prefManager = new PrefManager(this);
        Log.d(TAG, " syncAd adIdFirebase " + adIdFirebase);
        String url = urlEndpoint.getUrlApi(Constant.SYNC_AD);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleRespondSyncAdvertise(response);
                    }
                },
                volleyErrListener
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ad_id", adIdFirebase);
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

    private void handleRespondSyncAdvertise(String response) {
        Log.e(TAG, "handleRespondSyncAdvertise response  " + response);
        /*try {
            JSONObject object = new JSONObject(response);
            JSONArray jsonArray = object.getJSONArray("results");

            if (jsonArray != null && jsonArray.length() > 0) {
                Gson gson = new Gson();
                try {
                    UserProfileApi userProfile = gson.fromJson(jsonArray.get(0).toString(), UserProfileApi.class);
                    prefManager = new PrefManager(getApplication());
                    // prefManager.setPrimaryKeyUser(pk);
                    if (userProfile != null) {
                        prefManager.setUserProfile(userProfile);
                    }
                    if (userProfile != null && userProfile.getUser() != null && !userProfile.getUser().isEmpty()) {

                        String stUser = userProfile.getUser();
                        String stUrl = urlEndpoint.getUrlApi(Constant.USERS);
                        stUser = stUser.replace(stUrl, "");
                        stUser = stUser.replace("/", "");
                        if (stUser != null && !stUser.isEmpty()) {
                            try {

                                int pkUser = Integer.valueOf(stUser);
                                prefManager.setPrimaryKeyUser(pkUser);
                            } catch (Exception ex) {
                                Log.e(TAG, "getUserInformation parse int ex " + ex);
                            }
                        }
                    }

                } catch (Exception ex) {
                    Log.e(TAG, "getUserInformation ex " + ex);
                }
            }


            Log.e(TAG, "getUserInformation response  " + response);
        } catch (JSONException e) {
            Log.e(TAG, "getUserInformation JSONException => " + e);
            e.printStackTrace();
        }*/
    }


    private Response.ErrorListener volleyErrListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: handle error
            Log.e(TAG, "Error: " + error.getMessage());
        }
    };

    public void showMessageSuccess(String title, String message) {

        try {
            dlgAlert = new AlertDialog.Builder(NewAdvertiseActivity.this);
            if (dlgAlert != null)
                dlgAlert.create().dismiss();


            dlgAlert.setMessage(message);
            dlgAlert.setTitle(title);
            dlgAlert.setPositiveButton(getApplicationContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    callPageHome();
                }


            });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

        } catch (Exception ex) {

        }

    }
}
