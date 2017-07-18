package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sosokan.android.R;
import com.sosokan.android.adapter.CountryAdapter;
import com.sosokan.android.events.Listener.OnPhoneChangedListener;
import com.sosokan.android.events.Listener.PhoneNumberTextWatcher;

import com.sosokan.android.models.Country;
import com.sosokan.android.models.Image;
import com.sosokan.android.models.User;
import com.sosokan.android.models.UserApi;
import com.sosokan.android.models.UserInformationTokenApi;

import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.models.WechatAuth;
import com.sosokan.android.models.WechatError;
import com.sosokan.android.models.WechatResponse;
import com.sosokan.android.models.WechatUserInfor;
import com.sosokan.android.rest.UrlEndpoint;
import com.sosokan.android.rest.PersistentCookieStore;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.CountryToPhonePrefix;
import com.sosokan.android.utils.FaceBookHelper;
import com.sosokan.android.utils.RingcaptchaUtils;
import com.sosokan.android.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.thrivecom.ringcaptcha.RingcaptchaService;
import com.thrivecom.ringcaptcha.lib.models.RingcaptchaResponse;
import com.thrivecom.ringcaptcha.lib.utils.ApiUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.Iterator;

import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

import io.fabric.sdk.android.Fabric;

public class SignUpActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG = "SignUpActivity";
    ImageView ivLoading;
    LinearLayout llLoginSplash, llCountry;
    TextView btnPhoneCode;
    private ProgressDialog mProgressDialog;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    User user;
    FirebaseUser mUser;
    private DatabaseReference mDatabase;
    ListView listCountryView;
    Country country;
    private PhoneNumberTextWatcher watcher;
    EditText edtPhone;
    protected SparseArray<ArrayList<Country>> mCountriesMap = new SparseArray<>();
    private ArrayList<Country> data;
    protected CountryAdapter mAdapter;
    protected String mLastEnteredPhone;
    protected PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();

    protected static final TreeSet<String> CANADA_CODES = new TreeSet<>();

    static {
        CANADA_CODES.add("204");
        CANADA_CODES.add("236");
        CANADA_CODES.add("249");
        CANADA_CODES.add("250");
        CANADA_CODES.add("289");
        CANADA_CODES.add("306");
        CANADA_CODES.add("343");
        CANADA_CODES.add("365");
        CANADA_CODES.add("387");
        CANADA_CODES.add("403");
        CANADA_CODES.add("416");
        CANADA_CODES.add("418");
        CANADA_CODES.add("431");
        CANADA_CODES.add("437");
        CANADA_CODES.add("438");
        CANADA_CODES.add("450");
        CANADA_CODES.add("506");
        CANADA_CODES.add("514");
        CANADA_CODES.add("519");
        CANADA_CODES.add("548");
        CANADA_CODES.add("579");
        CANADA_CODES.add("581");
        CANADA_CODES.add("587");
        CANADA_CODES.add("604");
        CANADA_CODES.add("613");
        CANADA_CODES.add("639");
        CANADA_CODES.add("647");
        CANADA_CODES.add("672");
        CANADA_CODES.add("705");
        CANADA_CODES.add("709");
        CANADA_CODES.add("742");
        CANADA_CODES.add("778");
        CANADA_CODES.add("780");
        CANADA_CODES.add("782");
        CANADA_CODES.add("807");
        CANADA_CODES.add("819");
        CANADA_CODES.add("825");
        CANADA_CODES.add("867");
        CANADA_CODES.add("873");
        CANADA_CODES.add("902");
        CANADA_CODES.add("905");
    }


    ImageButton btnBackSplash;
    Button btnFaceBook;
    Button btnVerify;
    Context context;
    ImageButton ibBackLanguage;
    private RequestQueue mQueue;
    UrlEndpoint urlEndpoint;
    protected OnPhoneChangedListener mOnPhoneChangedListener = new OnPhoneChangedListener() {
        @Override
        public void onPhoneChanged(String phone) {
            getCountryByPhone(phone);
        }
    };
    protected AdapterView.OnItemClickListener mOnItemSelectedListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            country = (Country) listCountryView.getItemAtPosition(position);
            btnPhoneCode.setText(country.getCountryCodeStr());
            watcher.setCountryCode(country.getCountryCodeStr());
            showEnterPhoneView();
            edtPhone.setFilters(new InputFilter[]{setFilter(country)});
        }
    };

    private PrefManager prefManager;

    private SharedPreferences _preferences;


    private IWXAPI api;
    Button btnWeChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }*/
        Fabric.with(this, new Crashlytics());
        urlEndpoint = new UrlEndpoint();
        api = WXAPIFactory.createWXAPI(this, WXEntryActivity.APP_ID, false);
        api.registerApp(WXEntryActivity.APP_ID);
        //FaceBook
        FacebookSdk.sdkInitialize(SignUpActivity.this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                saveFacebookLoginData("facebook", loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        setContentView(R.layout.activity_sign_up);
        context = this;
        initView();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _preferences = PreferenceManager.getDefaultSharedPreferences(context);
                // mDatabase=FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(mUser.getUid());
                mDatabase = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS);

                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();

                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    }
                };


                watcher = new PhoneNumberTextWatcher(mOnPhoneChangedListener);
                mAdapter = new CountryAdapter(context);
                listCountryView.setOnItemClickListener(mOnItemSelectedListener);
                listCountryView.setAdapter(mAdapter);
                mQueue = Volley.newRequestQueue(context);
                initCountryCode();
            }
        });
        // String token = "EAAYO95TZCEAsBAFj6is4M0ZAVXV2NsXBZAvfwMJzKqEuvZA9HuZAVHKHstesvQBZCTTo8egTNfJeiDQ6sMIsh1JFe3nEEJciMFCKTFSd3SSd8l1igjnVOoLD4FC4B9ZBEbd9lQjcq1kuyulJUcYNF5YAUdcyb85KegdsFjaZA3p15ZBvL03T3pFc32XMxY4YQKDV0XsRKJAj9RA0ZCRnQH5p9q";
        // getUserViaAPI(token);
        //   token = "EAAYO95TZCEAsBAOF6zmwjIJasZCRyf0ZBCFxA8dexLHPZBH0kSuqCQ8j0Q9ZCflxyMw8fZC0r4FW748X00qvqHA5olGX6pgROJ94QMarVWVYLydRGewKwJXsVrPv6ZBYlaBSyJeMPxmZBkBlUSJsUGtFulx3jDhzeC6FfMSYU1APnLPFrFNrFHktnNbUZCPjcqlQZD";
        //   requestAuthViaApi(token,token);

    }

    private void initCountryCode() {
        if (country != null) {
            btnPhoneCode.setText(country.getCountryCodeStr());
            watcher.setCountryCode(country.getCountryCodeStr());
        }
    }

    private void initView() {
        btnPhoneCode = (TextView) findViewById(R.id.btnPhoneCode);
        btnBackSplash = (ImageButton) findViewById(R.id.btnBackSplash);
        btnBackSplash.setOnClickListener(this);
        btnVerify = (Button) findViewById(R.id.btnVerify);
        btnFaceBook = (Button) findViewById(R.id.btnFaceBook);
        btnFaceBook.setOnClickListener(this);
        btnPhoneCode.setOnClickListener(this);
        btnVerify.setOnClickListener(this);


        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();
        CountryToPhonePrefix countryToPhonePrefix = new CountryToPhonePrefix();
        String codeNumber = "+1";
        try {
            codeNumber = countryToPhonePrefix.prefixFor(countryCode.toUpperCase());
        } catch (Exception e) {
            System.err.println("countryCode was thrown: " + countryCode);
        }
        btnPhoneCode.setText(codeNumber);


        llLoginSplash = (LinearLayout) findViewById(R.id.llLoginSplash);
        llCountry = (LinearLayout) findViewById(R.id.llCountry);
        ibBackLanguage = (ImageButton) findViewById(R.id.ibBackLanguage);
        ibBackLanguage.setOnClickListener(this);
        listCountryView = (ListView) findViewById(R.id.listCountryView);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtPhone.getText().toString().trim().length() >= 9) {
                    btnVerify.setEnabled(true);
                } else {
                    btnVerify.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });

        btnWeChat = (Button) findViewById(R.id.btnWeChat);
        btnWeChat.setOnClickListener(this);

    }

    private void getCountryByPhone(String phone) {
        try {
            mLastEnteredPhone = phone;
            Phonenumber.PhoneNumber p = mPhoneNumberUtil.parse(phone, null);
            ArrayList<Country> list = mCountriesMap.get(p.getCountryCode());
            Country country = null;
            if (list != null) {
                if (p.getCountryCode() == 1) {
                    String num = String.valueOf(p.getNationalNumber());
                    if (num.length() >= 3) {
                        String code = num.substring(0, 3);
                        if (CANADA_CODES.contains(code)) {
                            for (Country c : list) {
                                // Canada has priority 1, US has priority 0
                                if (c.getPriority() == 1) {
                                    country = c;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (country == null) {
                    for (Country c : list) {
                        if (c.getPriority() == 0) {
                            country = c;
                            break;
                        }
                    }
                }
            }
        } catch (NumberParseException ignore) {
        }
    }

    //FaceBook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
        FaceBookHelper helper = new FaceBookHelper();
        helper.disconnectFromFacebook();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy  ");
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        FaceBookHelper helper = new FaceBookHelper();
        helper.disconnectFromFacebook();
        if (mQueue != null)
            mQueue.cancelAll(this);
    }

    //FaceBook
    public void onFacebookLogInClicked(View view) {
        Log.e(TAG, "onFacebookLogInClicked  ");
        LoginManager
                .getInstance()
                .logInWithReadPermissions(
                        this,
                        Arrays.asList("public_profile", "user_friends", "email")
                );

    }

    //FaceBook
    private void saveFacebookLoginData(String provider, AccessToken accessToken) {
        final String token = accessToken.getToken();
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(SignUpActivity.this);
        }

        //setUpUser();
        if (token != null) {
            final AuthCredential credential = FacebookAuthProvider.getCredential(token);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mUser = mAuth.getCurrentUser();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                ApplicationUtils.closeMessage();
                            } else {
                                String uid = task.getResult().getUser().getUid();
                                String name = task.getResult().getUser().getDisplayName();
                                String email = task.getResult().getUser().getEmail();
                                String imageURL = task.getResult().getUser().getPhotoUrl().toString();


                                Log.e("TOKEN FB ", token);
                                //Create a new User and Save it in Firebase database
                                user = new User();
                                user.setId(uid);
                                user.setUserName(name);
                                user.setEmail(email);
                                user.setVerify(true);
                                //TODO
                                // create Image
                                Image imageHeader = new Image();
                                imageHeader.setImageUrl(imageURL);
                                Log.e(TAG, "requestAuthViaApiFacebook name  " + name);
                                Log.e(TAG, "requestAuthViaApiFacebook token  " + token);
                                if (user != null) {
                                    imageHeader.setUserId(user.getId());
                                    user.setAvatar(imageHeader);
                                    if (mUser != null) {
                                        user.setProviderId(mUser.getProviderId());
                                    }
                                    user.setLike(0);
                                }
                                requestAuthViaApiFacebook(token, name);
                                saveUserToFirebase();
                                // user.saveConversationForUser();
                                //mRef.child(uid).setValue(user);


                                ApplicationUtils.closeMessage();
                            }
                            // ...
                        }
                    });

        } else {
            // myFirebaseRef.unauth();
            ApplicationUtils.closeMessage();
        }
    }

    public void requestAuthViaApiFacebook(final String token, final String name) {
        prefManager = new PrefManager(this);

        String url = urlEndpoint.getUrlApi(Constant.REST_AUTH_FB);
        Log.e(TAG, "requestAuthViaApiFacebook token  " + token);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        handleResponseLoginFb(response, token);

                    }
                },
                volleyErrListener
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", token);
                //     params.put("username", token);
                params.put("code", "");
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
        boolean isClearCookies = true;
        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(this, isClearCookies), CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(cookieManager);
        mQueue.add(postRequest);
    }

    private void handleResponseLoginFb(String response, String token) {
        try {
            JSONObject object = new JSONObject(response);
            String csrfToken = object.getString(Constant.KEY);
            prefManager = new PrefManager(getApplication());
            if (token != null && !token.isEmpty()) {
                prefManager.setAccessToken(token);
            }

            if (csrfToken != null && !csrfToken.isEmpty()) {
                prefManager.setCsrfToken(csrfToken);
                requestUserViaApi(csrfToken);
            }
            //
            //prefManager.setUserApi(user);
            Log.e(TAG, "requestAuthViaApiFacebook token  " + token);
            Log.e(TAG, "requestAuthViaApiFacebook response  " + response);
            Log.e(TAG, "requestAuthViaApiFacebook csrfToken  " + csrfToken);

        } catch (JSONException e) {
            Log.e(TAG, "requestAuthViaApiFacebook onPostExecute > Try > JSONException => " + e);
            e.printStackTrace();
        }
    }

    public void requestUserViaApi(final String key) {
        prefManager = new PrefManager(this);

        String url = urlEndpoint.getUrlApi(Constant.REST_AUTH_USER);//+ "rest-auth/user/";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        handleResponseGetUser(response, key);

                    }
                },
                volleyErrListener
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            /*public Map<String, String> getHeaders() throws AuthFailureError {
                return urlEndpoint.getHeaderWithAccountDefault();
            }*/
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                3,
                1f));
        // boolean isClearCookies = true;
        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(this), CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(cookieManager);

        mQueue.add(postRequest);
    }

    private void handleResponseGetUser(String response, String key) {
        try {
            JSONObject object = new JSONObject(response);
            prefManager = new PrefManager(getApplication());
            UserApi userApi = new UserApi();
            int pk = object.getInt(Constant.PRIMARY_KEY);
            String username = object.getString(Constant.USERNAME);
            String email = object.getString(Constant.EMAIL);
            String first_name = object.getString(Constant.FIRST_NAME);
            String last_name = object.getString(Constant.LAST_NAME);
            prefManager = new PrefManager(getApplication());
            prefManager.setPrimaryKeyUser(pk);

            if (username != null && !username.isEmpty()) {
                prefManager.setUserName(username);
                userApi.setUsername(username);
            }
            if (email != null && !email.isEmpty()) {
                prefManager.setEmailUser(email);
                userApi.setEmail(email);
            }
            if (first_name != null && !first_name.isEmpty()) {
                prefManager.setFirstName(first_name);
                userApi.setFirst_name(first_name);
            }
            if (last_name != null && !last_name.isEmpty()) {
                prefManager.setLastName(last_name);
                userApi.setLast_name(last_name);
            }
            prefManager = new PrefManager(getApplication());
            UserInformationTokenApi userInformationTokenApi = new UserInformationTokenApi();
            userInformationTokenApi.setCsrftoken(key);
            userInformationTokenApi.setKey(key);
            userInformationTokenApi.setToken(key);
            userInformationTokenApi.setToken(key);
            prefManager.setUserInformationToken(userInformationTokenApi);
            prefManager.setUserApi(userApi);


            Log.e(TAG, "requestUserViaApi response  " + response);
        } catch (JSONException e) {
            Log.e(TAG, "requestUserViaApi JSONException => " + e);
            e.printStackTrace();
        }
    }

    public void getUserInformation(String userIdFirebase) {
        prefManager = new PrefManager(this);

        String url = urlEndpoint.getUrlApi(Constant.USER_PROFILE) + "?legacy_id=" + userIdFirebase;
        //Config.API_URL_STAGING + "api/userprofiles/?legacy_id=" + userIdFirebase;
        Log.e(TAG, "getUserInformation url  " + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        handleResponseGetUserProfile(response);

                    }
                },
                volleyErrListener
        ) {
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
                5000,
                3,
                1f));
        mQueue.add(postRequest);


    }

    private void handleResponseGetUserProfile(String response) {
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
                    } else {
                        String alert = "";
                        String message = getResources().getString(R.string.thank_you_for_report);
                        showMessageError(alert, message);
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
                    gotoMenuActivity();

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

    public void saveUserToFirebase() {
        if (user != null) {

            PrefManager prefManager = new PrefManager(getApplication());

            prefManager.setUser(user);
            mDatabase.child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                    } else {
                        getUserInformation(user.getId());

                    }
                }
            });


        } else {
            gotoMenuActivity();
        }
    }

    public void gotoVerifyCodeActivity(String token, String phoneNumber, String phoneCode) {
        if (phoneNumber.isEmpty() || phoneCode.isEmpty()) return;
        if (phoneNumber.startsWith("0") && phoneNumber.length() > 2) {
            phoneNumber = phoneNumber.substring(1, phoneNumber.length());
        }
        Intent intent = new Intent(this, VerifyCodeActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("phoneCode", phoneCode);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.start_activity_left_to_right, 0);
    }

    public void gotoMenuActivity() {
        ApplicationUtils.closeMessage();
        Intent intent = new Intent(SignUpActivity.this, MenuActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.start_activity_left_to_right, 0);
        finish();
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnBackSplash:
                intent = new Intent(this, SplashActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.slide_in_down, 0);
                finish();
                break;
            case R.id.btnFaceBook:
                onFacebookLogInClicked(v);
                break;
            case R.id.btnPhoneCode:
                showCountryView();
                break;
            case R.id.btnVerify:
                verifyPhone();
                break;
            case R.id.ibBackLanguage:
                hideChooseCountry(View.VISIBLE, View.GONE);
                break;

            case R.id.btnWeChat:
                onClickLoginWechatButton();
                break;
        }
    }

    private void hideChooseCountry(int visible, int gone) {
        llLoginSplash.setVisibility(visible);
        llCountry.setVisibility(gone);
    }

    private void verifyPhone() {
        cleanSession(context);
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(SignUpActivity.this);
        }
        if (!btnPhoneCode.getText().toString().isEmpty() && !edtPhone.getText().toString().isEmpty()) {
            final String phoneNumber = btnPhoneCode.getText().toString() + edtPhone.getText().toString();
            Log.i(TAG, phoneNumber);
            mQueue = Volley.newRequestQueue(this);
            callOnBoard(phoneNumber);
        } else if (btnPhoneCode.getText().toString().isEmpty()) {
            String alert = "";
            String message = getResources().getString(R.string.error_input_phone_code);
            showMessageError(alert, message);
        } else if (edtPhone.getText().toString().isEmpty()) {
            String alert = "";
            String message = getResources().getString(R.string.error_input_phone_number);
            showMessageError(alert, message);
        }

    }

    private void callOnBoard(final String phoneNumber) {
        String url = "https://api.ringcaptcha.com/" + Config.APP_KEY + "/" + "/onboard";
        final String locale = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("callOnBoard Response", response);
                        ApplicationUtils.closeMessage();
                        try {
                            RingcaptchaUtils ringcaptchaUtils = new RingcaptchaUtils();
                            RingcaptchaResponse ringcaptchaResponse = ringcaptchaUtils.processResponse(context, response);
                            if (ringcaptchaResponse != null) {
                                if (ringcaptchaResponse.status != null) {
                                    if (ringcaptchaResponse.status.equalsIgnoreCase("NEW")) {

                                        setToken(ringcaptchaResponse.token.toString(), context);
                                        callVerifySMS(phoneNumber);
                                    } else {
                                        //SHOW ERROR INVALID_RESPONSE
                                        showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_phone_number_is_not_valid));
                                    }
                                } else {
                                    showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_internet));
                                }
                            } else {
                                showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_internet));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                volleyErrListener
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("udid", ApiUtils.getTelephonyInfo(context, 3));
                params.put("locale", locale);
                if (Config.Secret_KEY != null) {
                    params.put("secret_key", Config.Secret_KEY);
                } else if (Config.API_KEY != null) {
                    params.put("api_key", Config.API_KEY);
                }

                String mccmnc = ApiUtils.getTelephonyInfo(context, 1);
                if (mccmnc != null) {
                    params.put("mccmnc", mccmnc);
                }

                String carriern = ApiUtils.getTelephonyInfo(context, 2);
                if (carriern != null) {
                    params.put("carriern", carriern);
                }

                String countryCode = ApiUtils.getTelephonyInfo(context, 4);
                if (countryCode != null) {
                    params.put("ic", countryCode);
                }

                String simNumber = ApiUtils.getTelephonyInfo(context, 5);
                if (simNumber != null) {
                    params.put("dn", simNumber);
                }
                params = checkParams(params);
                return params;
            }
        };
        mQueue.add(postRequest);
    }

    public void showMessageError(String title, String message) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) getApplicationContext()).isDestroyed()) {
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

    private void callVerifySMS(final String phoneNumber) {
        String url = "https://api.ringcaptcha.com/" + Config.APP_KEY + "/code" + "/" + RingcaptchaService.SMS.toString();
        final String locale = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponseCallVerify(response);

                    }
                },
                volleyErrListener
        ) {
            @Override
            protected Map<String, String> getParams() {
                return getParamForCallVerifyPhone(phoneNumber, locale);
            }
        };
        mQueue.add(postRequest);
    }

    private void handleResponseCallVerify(String response) {
        // response
        Log.d(TAG, "handleResponseCallVerify Response " + response);
        ApplicationUtils.closeMessage();
        try {
            RingcaptchaUtils ringcaptchaUtils = new RingcaptchaUtils();
            RingcaptchaResponse ringcaptchaResponse = ringcaptchaUtils.processResponse(context, response);
            if (ringcaptchaResponse != null) {
                if (ringcaptchaResponse.status != null) {
                    if (ringcaptchaResponse.status.equalsIgnoreCase("SUCCESS")) {
                        // cleanSession(context);
                        if (btnPhoneCode.getText().toString().isEmpty()) {

                        } else {
                            gotoVerifyCodeActivity(ringcaptchaResponse.token, edtPhone.getText().toString(), btnPhoneCode.getText().toString());
                        }

                    } else {
                        //SHOW ERROR INVALID_RESPONSE
                        showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_phone_number_is_not_valid));
                    }
                } else {
                    showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_internet));
                }
            } else {
                showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_internet));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getParamForCallVerifyPhone(String phoneNumber, String locale) {
        SharedPreferences prefsReader = context.getSharedPreferences("RC", 0);
        String token = prefsReader.getString("RCTK4" + Config.APP_KEY, null);
        Log.d("token", token.toString());

        Map<String, String> params = new HashMap<String, String>();

        params.put("phone", phoneNumber);
        params.put("udid", ApiUtils.getTelephonyInfo(context, 3));
        params.put("token", token);
        params.put("locale", locale);
        if (Config.Secret_KEY != null) {
            params.put("secret_key", Config.Secret_KEY);
        } else if (Config.API_KEY != null) {
            params.put("api_key", Config.API_KEY);
        }

        String mccmnc = ApiUtils.getTelephonyInfo(context, 1);
        if (mccmnc != null) {
            params.put("mccmnc", mccmnc);
        }

        String carriern = ApiUtils.getTelephonyInfo(context, 2);
        if (carriern != null) {
            params.put("carriern", carriern);
        }

        String countryCode = ApiUtils.getTelephonyInfo(context, 4);
        if (countryCode != null) {
            params.put("ic", countryCode);
        }

        String simNumber = ApiUtils.getTelephonyInfo(context, 5);
        if (simNumber != null) {
            params.put("dn", simNumber);
        }
        params = checkParams(params);
        return params;
    }

    private void cleanSession(Context context) {
        SharedPreferences.Editor prefsWriter = context.getSharedPreferences("RC", 0).edit();
        prefsWriter.remove("RCTK4" + Config.APP_KEY);
        prefsWriter.remove("RCTKTM4" + Config.APP_KEY);
        prefsWriter.remove("RCTKST4" + Config.APP_KEY);
        prefsWriter.commit();
    }

    private Map<String, String> checkParams(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = it.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }

    public void setToken(String token, Context context) {
        SharedPreferences.Editor prefsWriter = context.getSharedPreferences("RC", 0).edit();
        prefsWriter.putString("RCTK4" + Config.APP_KEY, token);
        prefsWriter.commit();
    }

    private void showCountryView() {
        if (mCountriesMap == null || mCountriesMap.size() <= 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new AsyncPhoneInitTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new AsyncPhoneInitTask(this).execute();
            }
        }

        hideChooseCountry(View.GONE, View.VISIBLE);
    }

    private InputFilter setFilter(final Country c) {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                try {
                    Log.e("pmqaz", "source:" + source + "start:" + start + "end:" + end + "dest:" + dest + "dstart:" + dstart + "dstart:" + dend);
                    if (c.getCountryISO().equalsIgnoreCase("US") && dest.toString().replace(" ", "").replace("-", "").trim().length() > 9 && end == 1) {
                        return "";
                    }
                    for (int i = start; i < end; i++) {
                        char c = source.charAt(i);
                        if (dstart > 0 && !Character.isDigit(c)) {
                            return "";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private void showEnterPhoneView() {
        llLoginSplash.setVisibility(View.VISIBLE);
        llCountry.setVisibility(View.GONE);
    }


    protected class AsyncPhoneInitTask extends AsyncTask<Void, Void, ArrayList<Country>> {

        private int mSpinnerPosition = -1;
        private Context mContext;
        private boolean isHaveCountryCode;

        public AsyncPhoneInitTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected ArrayList<Country> doInBackground(Void... params) {
            data = new ArrayList<>(233);
            String countryCode = "1";
            int code = 1;//mPhoneNumberUtil.getCountryCodeForRegion(countryCode);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("countries.dat"), "UTF-8"));
                // do reading, usually loop until end of file reading
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    //process line
                    Country c = new Country(mContext, line, i);
                    if (c.getCountryISO().equalsIgnoreCase(countryCode)) {
                        code = c.getCountryCode();
                    }
                    data.add(c);
                    ArrayList<Country> list = mCountriesMap.get(c.getCountryCode());
                    if (list == null) {
                        list = new ArrayList<>();
                        mCountriesMap.put(c.getCountryCode(), list);
                    }
                    list.add(c);
                    i++;
                }
            } catch (IOException e) {
                //log the exception
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //log the exception
                    }
                }
            }
            isHaveCountryCode = true;
            if (country == null) {
                isHaveCountryCode = false;
                country = new Country(SignUpActivity.this, "United States,us,1,0", 222);
                ArrayList<Country> list = mCountriesMap.get(code);
                if (list != null) {
                    for (Country c : list) {
                        if (c.getCountryISO().equalsIgnoreCase(countryCode)) {
                            country = c;
                            mSpinnerPosition = c.getNum();
                            break;
                        }
                    }
                }
            } else {
                mSpinnerPosition = country.getNum();
            }
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Country> data) {
            mAdapter.clear();
            mAdapter.addAll(data);
            listCountryView.setSelection(mSpinnerPosition);
            if (mSpinnerPosition == -1) {
                btnPhoneCode.setText("+1");
                watcher.setCountryCode(country.getCountryCodeStr());
            } else if (!isHaveCountryCode) {
                initCountryCode();
                edtPhone.setFilters(new InputFilter[]{setFilter(country)});
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "========= onPause");


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, " =========== onResume");
        ApplicationUtils.closeMessage();
        if (WXEntryActivity.token != null) {
            requestAccessTokenWechat(WXEntryActivity.token);
        }
    }

    private Response.ErrorListener volleyErrListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: handle error
            Log.e(TAG, "Error: " + error.getMessage());
        }
    };


    public void onClickLoginWechatButton() {
        ApplicationUtils.showProgress(this);
        api.registerApp(WXEntryActivity.APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo,post_timeline";
        req.state = "none";
        api.sendReq(req);


    }

    public void requestAccessTokenWechat(String token) {
        prefManager = new PrefManager(this);

        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(SignUpActivity.this);
        }

        String url = String.format(urlEndpoint.getUrlApi(Constant.WECHAT_ACCESS_TOKEN), WXEntryActivity.APP_ID, WXEntryActivity.APP_SEC, token);
        Log.e(TAG, "requestAccessTokenWechat url: " + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        handleResponseWechat(response);

                    }
                },
                volleyErrListener
        ) {

        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                3,
                1f));
        mQueue.add(postRequest);
    }


    private void handleResponseWechat(String response) {

        Log.e(TAG, "handleResponseWechat response  " + response);
        Gson gson = new Gson();
        WechatError wechatError = gson.fromJson(response, WechatError.class);
        if (wechatError != null) {

            Log.e(TAG, "handleResponseWechat wechatError.errcode  " + wechatError.errcode);
            Log.e(TAG, "handleResponseWechat wechatError.errmsg  " + wechatError.errmsg);
            if (wechatError.errcode == 40029 || wechatError.errcode == 40163 || wechatError.errcode == 40030) {
                ApplicationUtils.closeMessage();
                if (prefManager == null)
                    prefManager = new PrefManager(getApplicationContext());

                WechatAuth wechatAuth = prefManager.getWechatAuth();
                if (wechatAuth != null) {
                    requestRefreshAccessTokenWechat(wechatAuth.getCode());
                }

            } else if (wechatError.errcode == 0) {
                setWechatResponseIntoPrefManager(response, gson);
            }

        }
    }


    public void requestRefreshAccessTokenWechat(String token) {
        prefManager = new PrefManager(this);

        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(SignUpActivity.this);
        }

        String url = String.format(urlEndpoint.getUrlApi(Constant.WECHAT_REFRESH_ACCESS_TOKEN), WXEntryActivity.APP_ID, token);
        Log.e(TAG, "requestRefreshAccessTokenWechat url: " + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        handleResponseRefreshWechat(response);

                    }
                },
                volleyErrListener
        ) {
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                3,
                1f));
        mQueue.add(postRequest);
    }


    private void handleResponseRefreshWechat(String response) {

        Log.e(TAG, "handleResponseRefreshWechat response  " + response);
        Gson gson = new Gson();
        WechatError wechatError = gson.fromJson(response, WechatError.class);
        if (wechatError == null) {
            setWechatResponseIntoPrefManager(response, gson);

        } else {
            Log.e(TAG, "handleResponseRefreshWechat wechatError.errcode  " + wechatError.errcode);
            Log.e(TAG, "handleResponseRefreshWechat wechatError.errmsg  " + wechatError.errmsg);

            if (wechatError.errcode == 0) {
                setWechatResponseIntoPrefManager(response, gson);
            } else {
                ApplicationUtils.closeMessage();
            }
        }
    }

    private void setWechatResponseIntoPrefManager(String response, Gson gson) {
        WechatResponse wechatResponse = gson.fromJson(response, WechatResponse.class);
        if (wechatResponse != null) {
            if (prefManager == null) {
                prefManager = new PrefManager(getApplicationContext());
            }
            prefManager.setWechatResponse(wechatResponse);
            requestUserInforWechat(wechatResponse);
        }
    }

    public void requestUserInforWechat(WechatResponse wechatResponse) {
        prefManager = new PrefManager(this);

        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(SignUpActivity.this);
        }

        String url = String.format(urlEndpoint.getUrlApi(Constant.WECHAT_USER_INFOR), wechatResponse.getAccess_token(), wechatResponse.getOpenid());
        Log.e(TAG, "requestUserInforWechat url: " + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        handleResponseUserInforWechat(response);

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


    private void handleResponseUserInforWechat(String response) {

        Log.e(TAG, "handleResponseUserInforWechat response  " + response);
        Gson gson = new Gson();
        WechatError wechatError = gson.fromJson(response, WechatError.class);
        if (wechatError == null) {
            setWechatUserInforIntoPrefManager(response, gson);

        } else {
            Log.e(TAG, "handleResponseUserInforWechat wechatError.errcode  " + wechatError.errcode);
            Log.e(TAG, "handleResponseUserInforWechat wechatError.errmsg  " + wechatError.errmsg);
            if (wechatError.errcode == 0) {
                setWechatUserInforIntoPrefManager(response, gson);
            } else {
                ApplicationUtils.closeMessage();
            }
        }
    }

    private void setWechatUserInforIntoPrefManager(String response, Gson gson) {
        WechatUserInfor wechatUserInfor = gson.fromJson(response, WechatUserInfor.class);
        if (wechatUserInfor != null) {
            if (prefManager == null) {
                prefManager = new PrefManager(getApplicationContext());
            }
            prefManager.setWechatUserInfor(wechatUserInfor);
            requestCustomKeyOfWechat(wechatUserInfor.getUnionid());
            //  loginFirebaseWithWechatToken(wechatUserInfor.getUnionid());
        }
    }


    public void requestCustomKeyOfWechat(final String key) {
        prefManager = new PrefManager(this);

        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(SignUpActivity.this);
        }

        String url = urlEndpoint.getUrlApi(Constant.WECHAT_FIREBASE_CUSTOM_KEY);
        Log.e(TAG, "requestCustomKeyOfWechat url: " + url);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        handleCustomKeyOfWechat(response);

                    }
                },
                volleyErrListener
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", key);

                return params;
            }
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


    private void handleCustomKeyOfWechat(String response) {

        Log.e(TAG, "handleCustomKeyOfWechat response  " + response);

        try {
            JSONObject object = new JSONObject(response);
            if (object != null && object.get("key") != null && !object.get("key").toString().isEmpty()) {

                Log.e(TAG, "handleCustomKeyOfWechat object.get(\"key\")  " + object.get("key").toString());
                loginFirebaseWithWechatToken(object.get("key").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loginFirebaseWithWechatToken(final String token) {

        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(SignUpActivity.this);
        }

        if (token != null) {
            mAuth.signInWithCustomToken(token)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mUser = mAuth.getCurrentUser();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                ApplicationUtils.closeMessage();
                            } else {
                                String uid = task.getResult().getUser().getUid();
                                String name = task.getResult().getUser().getDisplayName();
                                String email = task.getResult().getUser().getEmail();
                                //  String imageURL = task.getResult().getUser().getPhotoUrl().toString();


                                Log.e("TOKEN custom ", token);
                                //Create a new User and Save it in Firebase database
                                //mUser.getUid();
                                if(mUser!=null)
                               // getUserInformation(mUser.getUid());
                                user = new User();
                                user.setId(uid);
                                user.setUserName(name);
                                user.setEmail(email);
                                user.setVerify(true);

                                WechatUserInfor wechatUserInfor = prefManager.getWechatUserInfor();
                                if (wechatUserInfor != null) {
                                    user.setUserName(wechatUserInfor.getNickname());
                                    user.setAddress(wechatUserInfor.getCountry());
                                    if (wechatUserInfor.getHeadimgurl() != null && !wechatUserInfor.getHeadimgurl().isEmpty()) {
                                        Image image = new Image();
                                        image.setImageUrl(wechatUserInfor.getHeadimgurl());
                                        if (user != null) {
                                            image.setUserId(user.getId());
                                            user.setAvatar(image);
                                            if (mUser != null) {
                                                user.setProviderId(mUser.getProviderId());
                                            }
                                            user.setLike(0);
                                        }
                                        user.setAvatar(image);
                                    }

                                    user.setCity(wechatUserInfor.getCity());

                                }
                                saveUserToFirebase();
                                // user.saveConversationForUser();
                                //mRef.child(uid).setValue(user);


                                ApplicationUtils.closeMessage();
                            }
                            // ...
                        }
                    });

        } else {
            // myFirebaseRef.unauth();
            ApplicationUtils.closeMessage();
        }
    }
}
