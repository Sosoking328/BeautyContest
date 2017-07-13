package com.sosokan.android.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sosokan.android.BaseApp;
import com.sosokan.android.R;
import com.sosokan.android.control.FullscreenVideoLayout;
import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.AdvertiseApi;
import com.sosokan.android.models.Category;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.CategoryPopularCustomize;
import com.sosokan.android.models.City;
import com.sosokan.android.models.FlagChoice;
import com.sosokan.android.models.Image;
import com.sosokan.android.models.User;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.models.UserSelection;
import com.sosokan.android.models.VideoSplash;
import com.sosokan.android.mvp.advertise.AdvertisePresenter;
import com.sosokan.android.mvp.advertise.AdvertiseResponse;
import com.sosokan.android.mvp.advertise.AdvertiseView;
import com.sosokan.android.mvp.category.CategoryPresenter;
import com.sosokan.android.mvp.category.CategoryView;
import com.sosokan.android.mvp.flagchoice.FlagChoicePresenter;
import com.sosokan.android.mvp.flagchoice.FlagChoiceResponse;
import com.sosokan.android.mvp.flagchoice.FlagChoiceView;
import com.sosokan.android.mvp.splash.SplashPresenter;
import com.sosokan.android.mvp.splash.SplashView;
import com.sosokan.android.networking.Service;
import com.sosokan.android.rest.UrlEndpoint;
import com.sosokan.android.ui.view.DialogCities;
import com.sosokan.android.ui.view.DialogSearchAdvance;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.LocaleHelper;
import com.sosokan.android.utils.TaskFailureLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

import static com.google.gson.internal.bind.TypeAdapters.URL;

/**
 * Created by AnhZin on 8/19/2016.
 */
public class SplashActivity extends BaseApp implements View.OnClickListener, CategoryView, FlagChoiceView, SplashView, AdvertiseView {
    private static final String TAG = "SplashActivity";
    private static final String SHOW_MESSAGE = "Show.Message";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    User user;
    FirebaseUser mUser;
    private DatabaseReference mDatabase, mDatabaseUser;
    Context context;
    AlertDialog.Builder dlgAlert;
    TextView tvTermCondition, tvPolicy;
    private PrefManager prefManager;
    private RequestQueue mQueue;
    private DatabaseReference mDataRef;
    List<Category> categories;
    List<CategoryNew> categoryNewList;

    UrlEndpoint urlEndpoint;
    //===== CITIES
    List<String> states;
    List<City> cities;
    Map<String, List<City>> mapCities;


    boolean isChineseApp;

    boolean isShow;

    FrameLayout frameSplash;

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
    BroadcastReceiver updateReceiver;
    public CategoryNew categorySelected;
    public String categorySelectedId = "";
    public int categorySelectedIdInt;

    @Inject
    public Service service;
    CategoryPresenter categoryPresenter;
    FlagChoicePresenter flagChoicePresenter;
    SplashPresenter splashPresenter;
    FullscreenVideoLayout videoLayout;
    AdvertisePresenter advertisePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getDeps().injectSplash(this);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            ActionBar actionBar = getActionBar();
            if (actionBar != null)
                actionBar.hide();
        }
        setContentView(R.layout.activity_splash);
        Fabric.with(this, new Crashlytics());
        registerBroadcast();
        isShow = initValue();
        initView();

        if (!isShow) {
            showMessageLanguage();
        } else {
            setSaveShowMessage(true);
            initFireBase();
        }

    }

    private boolean initValue() {
        try {
            if (getApplicationContext() != null) {
                String languageToLoad = LocaleHelper.getLanguage(getApplicationContext());
                isChineseApp = languageToLoad.toString().equals("zh");

            }
        } catch (Exception ex) {
            System.out.println("languageToLoad failed: " + ex.getMessage());
        }
        urlEndpoint = new UrlEndpoint();
        context = this;
        categories = new ArrayList<>();
        boolean isShow = getShowMessage(context);
        mQueue = Volley.newRequestQueue(context);
        categoryNewList = new ArrayList<>();

        categoryPresenter = new CategoryPresenter(service, this, this);
        categoryPresenter.getCategoryList();
        flagChoicePresenter = new FlagChoicePresenter(service, this, this);
        flagChoicePresenter.getListFlagChoice();
        splashPresenter = new SplashPresenter(service, this, this);
        splashPresenter.getVideoSplash();
        advertisePresenter = new AdvertisePresenter(service, this, this);
        advertisePresenter.getListAdvertise();
        getAllCategoriesFirebase();

        if (prefManager == null) prefManager = new PrefManager(this);
        if (prefManager.getListCities() == null || prefManager.getListCities().size() <= 0) {
            initValueCities();
        }
        return isShow;
    }

    private void initView() {
        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        TextView tvSkip = (TextView) findViewById(R.id.tvSkip);
        tvSkip.setOnClickListener(this);
        tvTermCondition = (TextView) findViewById(R.id.tvTermCondition);
        tvTermCondition.setOnClickListener(this);
        tvPolicy = (TextView) findViewById(R.id.tvPolicy);
        tvPolicy.setOnClickListener(this);

        frameSplash = (FrameLayout) findViewById(R.id.frameSplash);
        videoLayout = new FullscreenVideoLayout(this);
        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);
        showViewWithContantsUrl();
    }

    private static boolean getShowMessage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(SHOW_MESSAGE, false);
    }

    public void setSaveShowMessage(boolean isShow) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(SHOW_MESSAGE, isShow);
        editor.commit();
        editor.apply();
    }

    public void setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
        setSaveShowMessage(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Intent refresh = new Intent(context, SplashActivity.class);
        startActivity(refresh);
        finish();
    }

    private void refreshActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        SplashActivity.this.overridePendingTransition(R.anim.nothing, R.anim.nothing);
    }

    public void showMessageLanguage() {
        setSaveShowMessage(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !(SplashActivity.this).isDestroyed())
//        {
        try {
            dlgAlert = new AlertDialog.Builder(this);
            if (dlgAlert != null)
                dlgAlert.create().dismiss();
            dlgAlert.setMessage(getResources().getString(R.string.please_select_your_language));
            dlgAlert.setTitle(getResources().getString(R.string.app_name));
            dlgAlert.setPositiveButton(getResources().getString(R.string.chinese), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    setLocale(context, "zh");
                    if (context != null) {
                        LocaleHelper.onCreate(context, "zh");
                    }
                    refreshActivity();
                }


            });
            dlgAlert.setNegativeButton(getResources().getString(R.string.english), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    setLocale(context, "en");
                    if (context != null) {
                        LocaleHelper.onCreate(context, "en");
                    }
                    refreshActivity();
                }
            });

            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        } catch (Exception ex) {

        }
//        }


    }

    public void showMessageError(String title, String message) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)getApplicationContext()).isDestroyed()) {
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

    private void initFireBase() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Config.FIREBASE_URL);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                Log.i("Authentication Splash ", userFirebase.toString());
            }
        };
        if (prefManager == null)
            prefManager = new PrefManager(this);
        if (prefManager.getUserProfile() == null) {
            if (mUser != null) {
                getUserInformation(mUser.getUid());
                mDatabaseUser = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(mUser.getUid());
                mDatabaseUser.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                user = dataSnapshot.getValue(User.class);
                                if (user != null) {
                                    Log.d(TAG, "mUser != null");
                                    Log.d(TAG, String.valueOf(user));
                                    if (prefManager == null)
                                        prefManager = new PrefManager(getApplicationContext());
                                    prefManager.setUser(user);
                                    gotoMainActivity();
                                } else {
                                        /*getInformationForUser();
                                        saveUserToFireBase();*/
                                    Log.d(TAG, "mUser ==== NULL");
                                    ApplicationUtils.closeMessage();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                ApplicationUtils.closeMessage();
                                // gotoMainActivity();
                            }
                        });

            } else {

                ApplicationUtils.closeMessage();
            }
        } else {
            ApplicationUtils.closeMessage();
            gotoMainActivity();
        }

    }

    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.tvSkip:

                processClickSkip();
                break;
            case R.id.btnSignUp:
                intent = new Intent(this, SignUpActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
                break;
            case R.id.tvTermCondition:
                intent = new Intent(this, TermConditionActivity.class);
                intent.putExtra("isTerm", true);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            case R.id.tvPolicy:
                intent = new Intent(this, TermConditionActivity.class);
                intent.putExtra("isTerm", false);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ApplicationUtils.closeMessage();
    }


    public void processClickSkip() {
        if (prefManager == null)
            prefManager = new PrefManager(this);
        prefManager.deleteUser();
        prefManager.deleteUserFirebase();
        prefManager.deleteUserInformationTokenApi();
        prefManager.deleteUserProfile();
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        final String fakeEmail = android_id + "@sosokan.com";
        final String fakePassword = fakeEmail;
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(fakeEmail, fakePassword)
                .addOnFailureListener(
                        new TaskFailureLogger(TAG, "Error signing in with email and password"))
                .addOnCompleteListener(SplashActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            createFakeUserBasedOnDeviceId(fakeEmail, fakePassword);

                        } else {
                            mUser = mAuth.getCurrentUser();
                            if (mUser != null) {

                                Log.d("processClickSkip", "mUser != null");

                                user = new User();
                                user.setId(mUser.getUid());
                                getInformationForUser();
                                saveUserToFireBase();
                            } else {
                                Log.d("processClickSkip", "mUser  ==== null");
                                createFakeUserBasedOnDeviceId(fakeEmail, fakePassword);
                            }

                        }
                    }
                });
    }

    public void createFakeUserBasedOnDeviceId(final String fakeEmail, final String fakePassword) {
        mAuth.createUserWithEmailAndPassword(fakeEmail, fakePassword).addOnCompleteListener(SplashActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SplashActivity.this, getString(R.string.cannot_connect_firebase), Toast.LENGTH_LONG).show();
                    ApplicationUtils.closeMessage();

                } else {
                    String uid = task.getResult().getUser().getUid();
                    String name = task.getResult().getUser().getDisplayName();
                    String email = task.getResult().getUser().getEmail();
                    String imageURL = "";

                    //Create a new User and Save it in Firebase database
                    user = new User();
                    user.setId(uid);
                    user.setUserName(name);
                    user.setEmail(email);
                    //TODO
                    // create Image
                    Image imageHeader = new Image();
                    imageHeader.setImageUrl(imageURL);
                    if (user != null) {
                        imageHeader.setUserId(user.getId());
                        user.setAvatar(imageHeader);
                    }
                    saveUserToFireBase();

                }
            }
        });
    }

    public void getInformationForUser() {
        if (user != null) {
            user = new User();

            user.setUserName(getResources().getString(R.string.guest));
            if (mUser != null) {
                user.setId(mUser.getUid());
                //user.setEmail(mUser.getEmail());
                user.setProviderId(mUser.getProviderId());
            }
            Image imageHeader = new Image();
            imageHeader.setImageUrl("");
            imageHeader.setUserId(user.getId());
            user.setAvatar(imageHeader);
            user.setLike(0);
        }
    }

    public void saveUserToFireBase() {
        if (user != null) {
            mDatabase = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS);
            mDatabase.child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                    } else {
                        gotoMainActivity();
                    }
                }
            });

        } else {
            gotoMainActivity();
        }
    }

    public void gotoMainActivity() {
       /* DialogSearchAdvance dialogSearchAdvance = new DialogSearchAdvance();
        dialogSearchAdvance.showDialog(this, null);*/
        if (prefManager == null) prefManager = new PrefManager(this);
        if (prefManager.getUserSelection() == null) {
            android.app.FragmentManager fm = getFragmentManager();
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            DialogSearchAdvance dialogSearchAdvance = DialogSearchAdvance.newInstance("", true);
            dialogSearchAdvance.show(fm, "fragment_search_advance");

        } else {
            callMenuActivity();
        }


    }

    private void callMenuActivity() {
        ApplicationUtils.closeMessage();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.start_activity_left_to_right, R.anim.start_activity_right_to_left);
        finish();
    }

    public void getUserInformation(String userIdFirebase) {
        prefManager = new PrefManager(this);
        Log.d(TAG, " getUserInformation userIdFirebase " + userIdFirebase);
        String url = urlEndpoint.getUrlApi(Constant.USER_PROFILE) + "?legacy_id=" + userIdFirebase;
        //Config.API_URL_STAGING + "api/userprofiles/?legacy_id=" + userIdFirebase;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleRespondUserProfile(response);
                    }
                },
                volleyErrListener
        ) {

            /*public Map<String, String> getHeaders() throws AuthFailureError {
                return urlEndpoint.getHeaderWithAccountDefault();
            }*/
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                3,
                1f));
        mQueue.add(postRequest);


    }

    private void handleRespondUserProfile(String response) {
        try {
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
        }
    }


    public void getAllCategoriesFirebase() {
        Log.e(TAG, "================== getAllCategoriesFirebase");
        mDataRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Config.FIREBASE_URL);
        mDataRef.child(Constant.CATEGORIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    try {
                        Category category = postSnapshot.getValue(Category.class);
                        if (category != null) {
                            categories.add(category);
                        }

                    } catch (Exception ex) {
                        Log.e(TAG, "getAllCategoriesFirebase ERROR: " + ex.toString());
                        Log.e(TAG, "getAllCategoriesFirebase postSnapshot: " + postSnapshot);

                    }
                }
                mDataRef.removeEventListener(this);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        Gson gson = new Gson();
        prefManager = new PrefManager(this);
        prefManager.setCategoriesFirebase(gson.toJson(categories));
        //loginWithPhoneViaApi("+841225561435", "3649");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "========= onPause");


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

        categoryPresenter.onStop();
        flagChoicePresenter.onStop();
        splashPresenter.onStop();
        advertisePresenter.onStop();
        unregisterReceiver(updateReceiver);
        if (videoLayout != null)
            videoLayout.stop();
        Log.e(TAG, "========= onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, " =========== onResume");

        //initFirebaseAndGetInformation();
    }

    private Response.ErrorListener volleyErrListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: handle error
            Log.e(TAG, "Error: " + error.getMessage());
        }
    };

    public String loadJSONFromAsset(Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getResources().openRawResource(R.raw.cities);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void initValueCities() {
        try {

            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(this));
            Gson gson = new Gson();
            cities = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                City city = gson.fromJson(jsonArray.get(i).toString(), City.class);
                if (city != null) {
                    cities.add(city);

                }
            }
            prefManager = new PrefManager(this);
            prefManager.setCities(gson.toJson(cities));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String appErrorMessage) {

    }

    @Override
    public void onAdvertiseFailure(String appErrorMessage) {

    }

    @Override
    public void getListAdvertiseSuccess(AdvertiseResponse advertiseResponse) {
        prefManager = new PrefManager(this);
        List<AdvertiseApi> advertiseApis = advertiseResponse.getResults();
        Gson gson = new Gson();
        prefManager.setListAdvertiseApiWithCategoryId(Constant.sosokanCategoryAll, gson.toJson(advertiseApis));
    }

    @Override
    public void getAdvertiseSuccess(AdvertiseApi advertiseApi) {

    }

    @Override
    public void getListVideoSplashSuccess(List<VideoSplash> videoSplashes) {
        urlEndpoint = new UrlEndpoint();
        String videoUrl = null;
        String imageUrl = null;
        if (videoSplashes != null && videoSplashes.size() > 0) {
            for (int i = 0; i < videoSplashes.size(); i++) {
                VideoSplash videoSplash = videoSplashes.get(i);
                if (videoSplash != null && videoSplash.getLanguage().equals(urlEndpoint.getLanguage(isChineseApp))) {
                    String mime = getMimeType(Uri.parse(videoSplash.getVideo()));
                    Log.e(TAG, mime);
                    if (mime.contains("video")) {
                        videoUrl = videoSplash.getVideo();
                    } else {
                        imageUrl = videoSplash.getVideo();
                    }
                }
            }
        }

        if (videoUrl != null && !videoUrl.isEmpty()) {

            showViewWithContantsUrl();
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            updateImageCover(imageUrl);
        }
    }

    private void showViewWithContantsUrl() {
        String url = "https://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        showVideo(url);
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public void showVideo(String videoUrl) {
        Log.e("showVideo URL ", videoUrl);

        if (!videoLayout.isPlaying()) {
            videoLayout.setShouldAutoplay(true);
            videoLayout.hideControls();
            //  videoLayout.stop();

            Uri videoUri = Uri.parse(videoUrl);
            try {
                videoLayout.setVideoURI(videoUri);
                if (!videoLayout.isPlaying()) {
                    videoLayout.start();
                }

            } catch (IOException e) {
                videoLayout.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }

    }


    public void updateImageCover(String url) {
        Log.e("UpdateImageCover", url);
        videoLayout.setVisibility(View.GONE);
        Glide.with(frameSplash.getContext())
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {

                        Drawable drawable = new BitmapDrawable(bitmap);
                        frameSplash.setBackground(drawable);
                    }
                });
    }

    @Override
    public void getCategoryListSuccess(List<CategoryNew> categoryListResponse) {
        Log.e(TAG, "getCategoryListSuccess ");
        if (prefManager == null) prefManager = new PrefManager(this);

        if (prefManager.getListCategoriesPopular() == null || prefManager.getListCategoriesPopular().size() == 0) {
            List<CategoryPopularCustomize> categoryPopularCustomizeArrayList = new ArrayList<>();
            CategoryPopularCustomize categoryPopularCustomize;
            if (categoryListResponse != null && categoryListResponse.size() > 0) {
                for (int i = 0; i < categoryListResponse.size(); i++) {
                    if (categoryListResponse.get(i).getPopular() > 0) {
                        categoryPopularCustomize = new CategoryPopularCustomize();
                        categoryPopularCustomize.setCategory(categoryListResponse.get(i));
                        categoryPopularCustomize.setSelected(true);
                        categoryPopularCustomizeArrayList.add(categoryPopularCustomize);
                    }
                }
                Gson gson = new Gson();
                prefManager.setCategoriesPopular(gson.toJson(categoryPopularCustomizeArrayList));
            }
        }
    }

    @Override
    public void onFlagChoiceFailure(String appErrorMessage) {

    }

    @Override
    public void getFlagchoiceSuccess(FlagChoiceResponse flagChoiceResponse) {


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

                UserSelection userSelection = new UserSelection();
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
                if (prefManager == null) prefManager = new PrefManager(getApplicationContext());

                prefManager.setUserSelection(userSelection);
                callMenuActivity();
            }
        };
        registerReceiver(updateReceiver, new IntentFilter(MenuActivity.ACTION_SEARCH_ADVANCE));

    }
}
