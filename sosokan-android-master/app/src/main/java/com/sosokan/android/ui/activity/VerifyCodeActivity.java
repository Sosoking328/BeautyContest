package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
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
import com.sosokan.android.BuildConfig;
import com.sosokan.android.R;
import com.sosokan.android.models.Image;
import com.sosokan.android.models.User;
import com.sosokan.android.models.UserInformationTokenApi;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.rest.UrlEndpoint;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.RingcaptchaUtils;
import com.sosokan.android.utils.TaskFailureLogger;
import com.thrivecom.ringcaptcha.lib.models.RingcaptchaResponse;
import com.thrivecom.ringcaptcha.lib.utils.ApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by AnhZin on 9/18/2016.
 */
public class VerifyCodeActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "VerifyCodeActivity";


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    User user;
    FirebaseUser mUser;
    private DatabaseReference mDatabase;

    Context context;
    private RequestQueue mQueue;
    String code, token, phoneNumber, phoneCode;
    EditText editCode1, editCode2, editCode3, editCode4;
    Button btnVerified;
    ImageButton ibBackVerify;
    private PrefManager prefManager;
    UrlEndpoint urlEndpoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        Fabric.with(this, new Crashlytics());
        token = getIntent().getStringExtra("token");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneCode = getIntent().getStringExtra("phoneCode");
        mQueue = Volley.newRequestQueue(this);
        urlEndpoint = new UrlEndpoint();
        context = this;
        initView();

        initFireBase();
    }

    private void initView() {
        btnVerified = (Button) findViewById(R.id.btnVerified);
        btnVerified.setOnClickListener(this);
        ibBackVerify = (ImageButton) findViewById(R.id.ibBackVerify);
        ibBackVerify.setOnClickListener(this);
        editCode1 = (EditText) findViewById(R.id.editCode1);
        editCode2 = (EditText) findViewById(R.id.editCode2);
        editCode3 = (EditText) findViewById(R.id.editCode3);
        editCode4 = (EditText) findViewById(R.id.editCode4);
        editCode1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editCode4.getText().toString().trim().length() >= 0) {
                    editCode2.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });
        editCode2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editCode4.getText().toString().trim().length() >= 0) {
                    editCode3.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });
        editCode3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editCode4.getText().toString().trim().length() >= 0) {
                    editCode4.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });
        editCode4.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editCode4.getText().toString().trim().length() >= 0) {
                    btnVerified.setEnabled(true);
                } else {
                    btnVerified.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initFireBase() {

        mDatabase = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Config.FIREBASE_URL);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnVerified:
                processVerify();
                break;
            case R.id.ibBackVerify:
                intent = new Intent(this, SignUpActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ApplicationUtils.closeMessage();
    }

    public void showMessageError(String title, String message) {
        try {
//          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) getApplicationContext()).isDestroyed()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

            dlgAlert.setMessage(message);
            dlgAlert.setTitle(title);
            dlgAlert.setPositiveButton(getResources().getString(R.string.ok), null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
//          }
        } catch (Exception ex) {

        }
    }

    public void processVerify() {
        String alert;
        alert = getResources().getString(R.string.alert);
        if (!checkNullOrEmpty(editCode1.getText().toString())) {
            if (!checkNullOrEmpty(editCode2.getText().toString())) {
                if (!checkNullOrEmpty(editCode3.getText().toString())) {
                    if (!checkNullOrEmpty(editCode4.getText().toString())) {
                        code = editCode1.getText().toString() + editCode2.getText().toString() + editCode3.getText().toString() + editCode4.getText().toString();
                        callVerifySMS();
                    } else {
                        showMessageVerify(alert);
                    }
                } else {
                    showMessageVerify(alert);
                }
            } else {
                showMessageVerify(alert);
            }
        } else {
            showMessageVerify(alert);
        }
    }

    private void showMessageVerify(String alert) {
        String message;
        message = getResources().getString(R.string.error_input_verify_1);
        showMessageError(alert, message);
    }

    public boolean checkNullOrEmpty(String stCheck) {
        return stCheck.trim().length() == 0;
    }

    private void callVerifySMS() {
        if (!ApplicationUtils.isShow) {
            ApplicationUtils.showProgress(VerifyCodeActivity.this);
        }
        String url = "https://api.ringcaptcha.com/" + Config.APP_KEY + "/verify";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("callVerifySMS  Response", response);

                        try {
                            RingcaptchaUtils ringcaptchaUtils = new RingcaptchaUtils();
                            RingcaptchaResponse ringcaptchaResponse = ringcaptchaUtils.processResponse(context, response);
                            if (ringcaptchaResponse != null) {
                                if (ringcaptchaResponse.status != null) {
                                    if (ringcaptchaResponse.status.equalsIgnoreCase("SUCCESS")) {
                                        getAndSaveUserToFirebase();
                                    } else {
                                        //SHOW ERROR INVALID_RESPONSE
                                        if (ringcaptchaResponse.message.equals("ERROR_INVALID_PIN_CODE")) {
                                            String alert = getResources().getString(R.string.we_are_sorry);
                                            String message = getResources().getString(R.string.error_invalid_pin_code);
                                            showMessageError(alert, message);
                                        }else if (ringcaptchaResponse.message.equals("ERROR_INVALID_NUMBER")) {
                                            String alert = getResources().getString(R.string.we_are_sorry);
                                            String message = getResources().getString(R.string.error_invalid_number);
                                            showMessageError(alert, message);
                                        }
                                        ApplicationUtils.closeMessage();
                                    }
                                } else {
                                    //SHOW ERROR INVALID_RESPONSE
                                    ApplicationUtils.closeMessage();
                                }
                            } else {
                                //SHOW ERROR INVALID_RESPONSE
                                ApplicationUtils.closeMessage();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ApplicationUtils.closeMessage();
                        }
                    }
                },
                volleyErrListener
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("code", code);
                params.put("udid", ApiUtils.getTelephonyInfo(context, 3));
                if (token != null) {
                    params.put("token", token);
                }

                if (phoneNumber != null && phoneCode != null) {
                    params.put("phone", phoneCode + phoneNumber.replaceFirst("0", ""));
                }

                if (Config.Secret_KEY != null) {
                    params.put("secret_key", Config.Secret_KEY);
                } else if (Config.API_KEY != null) {
                    params.put("api_key", Config.API_KEY);
                }

                params = checkParams(params);
                return params;
            }
        };
        mQueue.add(postRequest);
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

    public void getAndSaveUserToFirebase() {
        Log.d(TAG, "getAndSaveUserToFirebase " + "==================");
        final String fakeEmail = phoneCode + phoneNumber.replaceFirst("0", "-") + "@sosokan.com";
        final String fakePassword = fakeEmail;
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(fakeEmail, fakePassword)
                .addOnFailureListener(
                        new TaskFailureLogger(TAG, "Error signing in with email and password"))
                .addOnCompleteListener(VerifyCodeActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "getAndSaveUserToFirebase " + "not isSuccessful()");
                            createFakeUserBaseOnPhoneNumber(fakeEmail, fakePassword);
                        } else {

                            mUser = mAuth.getCurrentUser();

                            if (mUser != null) {
                                Log.d(TAG, "getAndSaveUserToFirebase mUser.getUid()" + mUser.getUid());
                                getUserProfile(mUser.getUid());
                                Log.d(TAG, "getAndSaveUserToFirebase " + "mUser != null");
                                loginWithPhoneViaApi(phoneCode + phoneNumber.replaceFirst("0", ""), code);
                                user = new User();
                                user.setId(mUser.getUid());
                                getInformationForUser();
                                saveUserToFirebase();
                            } else {
                                Log.d(TAG, "getAndSaveUserToFirebase " + "mUser ==== NULL");
                                createFakeUserBaseOnPhoneNumber(fakeEmail, fakePassword);
                            }
                        }
                    }
                });


    }

    public void createFakeUserBaseOnPhoneNumber(final String fakeEmail, final String fakePassword) {
        mAuth.createUserWithEmailAndPassword(fakeEmail, fakePassword).addOnCompleteListener(VerifyCodeActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(VerifyCodeActivity.this, getString(R.string.cannot_connect_firebase), Toast.LENGTH_LONG).show();
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
                    user.setEmail("");
                    Image imageHeader = new Image();
                    imageHeader.setImageUrl(imageURL);
                    if (user != null) {
                        imageHeader.setUserId(user.getId());
                        user.setAvatar(imageHeader);
                        user.setPhoneCode(phoneCode);
                        user.setPhoneNumber(phoneNumber);
                        user.setVerify(true);
                        saveUserToFirebase();
                    }

                    loginWithPhoneViaApi(phoneCode + phoneNumber.replaceFirst("0", ""), code);
                }
            }
        });
    }

    public void getInformationForUser() {



        if (user != null) {
            Image imageHeader = new Image();
            imageHeader.setImageUrl("");
            user = new User();
            if (mUser != null) {
                user.setId(mUser.getUid());
                user.setEmail(mUser.getEmail());
                user.setProviderId(mUser.getProviderId());
            }
            user.setUserName("");
            imageHeader.setUserId(user.getId());
            user.setAvatar(imageHeader);
            user.setPhoneCode(phoneCode);
            user.setPhoneNumber(phoneNumber);
            user.setVerify(true);
            user.setLike(0);
        }
    }

    public void saveUserToFirebase() {
        Log.d(TAG, "saveUserToFirebase " + "================== ");
        if (user != null) {

            PrefManager prefManager = new PrefManager(getApplication());

            prefManager.setUser(user);
            mDatabase = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS);
            mDatabase.child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d(TAG, "saveUserToFirebase " + "databaseError");
                        // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                    } else {
                        Log.d(TAG, "saveUserToFirebase " + " ============ SUCCESS ");
                        //loginWithPhoneViaApi(phoneCode + phoneNumber, code);

                        getUserInformation(user.getId());
                        gotoMainActivity();
                    }
                }
            });
        } else {
            Log.d(TAG, "saveUserToFirebase " + "user ==== NULL");
           // gotoMainActivity();
        }
    }

    public void gotoMainActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.start_activity_left_to_right, 0);
        finish();
    }

    public void loginWithPhoneViaApi(final String phone, final String code) {
        Gson gson = new Gson();

        String url = urlEndpoint.getUrlApi(Constant.PHONE_LOGIN);
        Log.e(TAG, "loginWithPhoneViaApi url " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("code", code);
        final String json = gson.toJson(params);
        Log.e(TAG, "loginWithPhoneViaApi json " + json);


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleRespondLoginPhoneViaApi(response);

                    }
                },
                volleyErrListener
        ) {


            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", phone);
                params.put("code", code);
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
        mQueue.getCache().clear();
        mQueue.add(postRequest);
    }

    private void handleRespondLoginPhoneViaApi(String response) {
        Log.e(TAG, "loginWithPhoneViaApi response  " + response);
        JSONObject object = null;
        try {
            object = new JSONObject(response);
            //JSONArray jsonArray = object.getJSONArray("results");

            if (object != null && object.get("status").equals("SUCCESS")) {
                Gson gson = new Gson();
                try {
                    Log.e(TAG, "loginWithPhoneViaApi object.toString() " + object.toString());
                    UserInformationTokenApi userInformationTokenApi = gson.fromJson(object.toString(), UserInformationTokenApi.class);
                    prefManager = new PrefManager(getApplication());
                    prefManager.setUserInformationToken(userInformationTokenApi);

                } catch (Exception ex) {
                    Log.e(TAG, "loginWithPhoneViaApi ex " + ex);
                }
            } else {

                /*String alert = getResources().getString(R.string.we_are_sorry);
                String message = getResources().getString(R.string.error_occur);
                showMessageError(alert, message);*/
            }
        } catch (JSONException e) {
            Log.e(TAG, "loginWithPhoneViaApi JSONException " + e.toString());
            e.printStackTrace();
        }
    }


    public void getUserProfile(String userIdFirebase) {
        prefManager = new PrefManager(this);

        String url = urlEndpoint.getUrlApi(Constant.USER_PROFILE) + "?legacy_id=" + userIdFirebase;
        //Config.API_URL_STAGING + "api/userprofiles/?legacy_id=" + userIdFirebase;
        Log.e(TAG, "getUserProfile url  " + url);
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
                                Log.e(TAG, "handleResponseGetUserProfile parse int ex " + ex);
                            }
                        }
                    }

                } catch (Exception ex) {
                    Log.e(TAG, "handleResponseGetUserProfile ex " + ex);
                }
            }


            Log.e(TAG, "handleResponseGetUserProfile response  " + response);
        } catch (JSONException e) {
            Log.e(TAG, "handleResponseGetUserProfile JSONException => " + e);
            e.printStackTrace();
        }
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
            ApplicationUtils.closeMessage();
        }
    };
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

           /* public Map<String, String> getHeaders() throws AuthFailureError {
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
}
