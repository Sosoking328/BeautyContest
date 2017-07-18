package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sosokan.android.BaseApp;
import com.sosokan.android.R;
import com.sosokan.android.adapter.CircleTransform;
import com.sosokan.android.adapter.FollowersAdapter;
import com.sosokan.android.control.RoundedImageView;
import com.sosokan.android.control.multi.level.menu.ItemInfo;
import com.sosokan.android.control.multi.level.menu.MultiLevelListAdapter;
import com.sosokan.android.control.multi.level.menu.MultiLevelListView;
import com.sosokan.android.control.multi.level.menu.OnItemCategoryClickListener;
import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.Category;
import com.sosokan.android.models.Conversation;
import com.sosokan.android.models.User;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.mvp.advertise.AdvertisePresenter;
import com.sosokan.android.mvp.category.CategoryPresenter;
import com.sosokan.android.mvp.flagchoice.FlagChoicePresenter;
import com.sosokan.android.mvp.userProfile.UserProfilePresenter;
import com.sosokan.android.mvp.userProfile.UserProfileResponse;
import com.sosokan.android.mvp.userProfile.UserProfileView;
import com.sosokan.android.networking.Service;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.DividerItemDecoration;
import com.sosokan.android.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

/**
 * Created by phuong.tran on 9/9/2016.
 */

public class EditProfileActivity extends BaseApp implements View.OnClickListener, UserProfileView {
    Button btnSaveEditProfile;
    ImageButton ibBackEditProfile;
    EditText edtCompanyNameEditProfile, edtUserNameEditProfile, edtAddressEditProfile, edtCityEditProfile, edtStateEditProfile, edtZipCodeEditProfile, edtPhoneNumberEditProfile,
            edtFaxEditProfile, edtEmailEditProfile, edtNoteEditProfile, edtWebsiteEditProfile;
    TextView tvNameEditProfile;

    FirebaseUser mUser;
    private FirebaseAuth mAuth;
    //User user;
    UserProfileApi user;
    private MultiLevelListView mListView;
    int positionCategory = 0;
    RoundedImageView ivUserProfile;
    ToggleButton tbAllowEmailProfile, tbAllowCallProfile;
    boolean isCallAble, isEmailAble;
    private List<Category> categories, categoriesChild;
    public String categoryAllId = "All";
    public Category categoryAll = null;
    public static Map<String, Category> mapCategory;
    public static Map<String, List<String>> mapCategoryChildren;

    public String categorySelectedId;
    Category categorySelected;
    LinearLayout llCategory, llFollowers;
    String emailUser, userName;
    RecyclerView rvFollowers;
    FollowersAdapter mAdapterFollower;
    List<User> mUserItems;
    Map<String, Object> mapFollowers;
    int count = 0;

    int position;
    String idUserOwnerAdvertise;
    boolean isChineseApp;
    boolean fromMess;
    String advertiseId, conversationId, categoryId;
    String tittlePost;
    Conversation conversation;
    long advertiseCreatedAt;
    Advertise advertise;

    private PrefManager prefManager;

    @Inject
    public Service service;
    UserProfilePresenter userProfilePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getDeps().injectEditProfile(this);
        Fabric.with(this, new Crashlytics());
        initValue();
        initView();
        initFirebaseAndUser();
        handleInstanceState(savedInstanceState);

    }

    private void initValue() {
        categories = new ArrayList<>();
        categoriesChild = new ArrayList<>();
        mapCategory = new HashMap<>();
        mapCategoryChildren = new HashMap<>();
        mUserItems = new ArrayList<>();
        mapFollowers = new HashMap<>();
        categorySelectedId = Constant.sosokanCategoryAll;
        userProfilePresenter = new UserProfilePresenter(service, this, this);


        if (getIntent() != null) {
            idUserOwnerAdvertise = getIntent().getStringExtra(Constant.ID_USER_OWNER);
            fromMess = getIntent().getBooleanExtra(Constant.FROM_MESSAGE, false);

            position = getIntent().getIntExtra(Constant.POSSITION, -1);
            conversationId = getIntent().getStringExtra(Constant.CONVERSATIONID);
            tittlePost = getIntent().getStringExtra(Constant.TITTLE_POST);
            advertiseId = getIntent().getStringExtra(Constant.ADVERTISEID);
            advertiseCreatedAt = getIntent().getLongExtra(Constant.ADVERTISE_CREATED_AT, 0);
            advertiseId = getIntent().getStringExtra(Constant.ADVERTISEID);
            categoryId = getIntent().getStringExtra(Constant.CATEGORYID);
            if (idUserOwnerAdvertise != null) {
                Log.e("idUserOwnerAdvertise ", idUserOwnerAdvertise);
            }
            Log.e("fromDetail ", String.valueOf(fromMess));
            Log.e("position ", String.valueOf(position));

            Log.e("conversationId ", String.valueOf(conversationId));
            Log.e("tittlePost ", String.valueOf(tittlePost));

            Log.e("advertiseId ", String.valueOf(advertiseId));
            Log.e("advertiseCreatedAt ", String.valueOf(advertiseCreatedAt));

        }
        prefManager = new PrefManager(this);
        if (prefManager != null)
            user = prefManager.getUserProfile();
    }

    private void handleInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {


            savedInstanceState.putString(Constant.ID_USER_OWNER, idUserOwnerAdvertise);
            savedInstanceState.putBoolean(Constant.FROM_MESSAGE, fromMess);
            savedInstanceState.putInt(Constant.POSSITION, position);
            savedInstanceState.putString(Constant.CONVERSATIONID, conversationId);
            savedInstanceState.putString(Constant.TITTLE_POST, tittlePost);
            savedInstanceState.putString(Constant.ADVERTISEID, advertiseId);
            savedInstanceState.putLong(Constant.ADVERTISE_CREATED_AT, advertiseCreatedAt);
            savedInstanceState.putString(Constant.CATEGORYID, categoryId);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            handleInstanceState(savedInstanceState);
            Log.e("Edit Profile SaveIns", "===================");
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            idUserOwnerAdvertise = savedInstanceState.getString(Constant.ID_USER_OWNER);
            fromMess = savedInstanceState.getBoolean(Constant.FROM_MESSAGE);
            position = savedInstanceState.getInt(Constant.POSSITION);
            conversationId = savedInstanceState.getString(Constant.CONVERSATIONID);
            tittlePost = savedInstanceState.getString(Constant.TITTLE_POST);
            advertiseId = savedInstanceState.getString(Constant.ADVERTISEID);
            advertiseCreatedAt = savedInstanceState.getLong(Constant.ADVERTISE_CREATED_AT);
            categoryId = savedInstanceState.getString(Constant.CATEGORYID);
            Log.e("Edit Profile RestoreIns", "===================");

        }

    }

    private void initFirebaseAndUser() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            userProfilePresenter.getUserProfileWithLegacyId(mUser.getUid());
            /*FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(mUser.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            user = dataSnapshot.getValue(User.class);
                            if (user != null) {
                                Log.e("edit profile userId ", user.getId());
                                getListUserFollowMe();
                                setInformationForUser();
                                if (user.getAvatar() != null && user.getAvatar().getImageUrl().toString().trim().length() > 0) {
                                    Glide.with(ivUserProfile.getContext()).load(user.getAvatar().getImageUrl()).centerCrop()
                                            .transform(new CircleTransform(ivUserProfile.getContext())).override(40, 40).into(ivUserProfile);
                                } else {

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });*/
        }

    }

//TODO DISPLAY SUBSCRIBING ME
   /* private void getListUserFollowMe() {
        mapFollowers = user.getUsersSubscribingMe();

        if (mapFollowers != null && mapFollowers.size() > 0) {
            if (llFollowers.getVisibility() == View.GONE) {
                llFollowers.setVisibility(View.VISIBLE);

            }
            for (Map.Entry<String, Object> entry : mapFollowers.entrySet()) {
                {
                    String key = entry.getKey();
                    FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(key).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value
                                    user = dataSnapshot.getValue(User.class);
                                    if (user != null && user.getUserName() != null && !user.getUserName().isEmpty()) {
                                        mUserItems.add(user);
                                    }
                                    count++;
                                    if (count > mapFollowers.size()) {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                try {
                                                    mAdapterFollower.notifyDataSetChanged();// Notify the commentAdapter
                                                } catch (Exception e) {

                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
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
        userProfilePresenter.onStop();
    }

    private void initView() {
        ibBackEditProfile = (ImageButton) findViewById(R.id.ibBackEditProfile);
        ibBackEditProfile.setOnClickListener(this);

        btnSaveEditProfile = (Button) findViewById(R.id.btnSaveEditProfile);
        btnSaveEditProfile.setOnClickListener(this);

        edtCompanyNameEditProfile = (EditText) findViewById(R.id.edtCompanyNameEditProfile);
        edtUserNameEditProfile = (EditText) findViewById(R.id.edtUserNameEditProfile);
        edtAddressEditProfile = (EditText) findViewById(R.id.edtAddressEditProfile);
        edtCityEditProfile = (EditText) findViewById(R.id.edtCityEditProfile);
        edtStateEditProfile = (EditText) findViewById(R.id.edtStateEditProfile);
        edtZipCodeEditProfile = (EditText) findViewById(R.id.edtZipCodeEditProfile);
        edtPhoneNumberEditProfile = (EditText) findViewById(R.id.edtPhoneNumberEditProfile);
        edtFaxEditProfile = (EditText) findViewById(R.id.edtFaxEditProfile);
        edtEmailEditProfile = (EditText) findViewById(R.id.edtEmailEditProfile);
        edtNoteEditProfile = (EditText) findViewById(R.id.edtNoteEditProfile);
        edtWebsiteEditProfile = (EditText) findViewById(R.id.edtWebsiteEditProfile);
        tvNameEditProfile = (TextView) findViewById(R.id.tvNameEditProfile);

        ivUserProfile = (RoundedImageView) findViewById(R.id.ivUserProfile);

        tbAllowEmailProfile = (ToggleButton) findViewById(R.id.tbAllowEmailProfile);

        tbAllowCallProfile = (ToggleButton) findViewById(R.id.tbAllowCallProfile);

        tbAllowEmailProfile.setOnClickListener(this);
        tbAllowCallProfile.setOnClickListener(this);
        llCategory = (LinearLayout) findViewById(R.id.llCategoryProfile);

        llFollowers = (LinearLayout) findViewById(R.id.llFollowers);
        setupRecycleView();
    }


    public static void setListViewHeightBasedOnChildren(MultiLevelListView listView) {
        MultiLevelListAdapter listAdapter = listView.getmAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getmProxyAdapter().getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + ((listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void setupRecycleView() {
        rvFollowers = (RecyclerView) findViewById(R.id.rvFollowers);
        mAdapterFollower = new FollowersAdapter(this, mUserItems);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        rvFollowers.setLayoutManager(mLayoutManager);
        rvFollowers.setItemAnimator(new DefaultItemAnimator());
        rvFollowers.setAdapter(mAdapterFollower);
        rvFollowers.setHasFixedSize(false);
        rvFollowers.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAdapterFollower.setListener(new FollowersAdapter.Listener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvFollowers.setLayoutManager(gridLayoutManager);
        rvFollowers.setNestedScrollingEnabled(false);

    }


    private OnItemCategoryClickListener mOnItemClickListener = new OnItemCategoryClickListener() {

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            Category category = ((Category) item);
            if (category != null) {
                categorySelected = category;
                categorySelectedId = category.getId();
                setListViewHeightBasedOnChildren(mListView);
            }
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            Category category = ((Category) item);
            if (category != null) {
                categorySelected = category;
                categorySelectedId = category.getId();
                setListViewHeightBasedOnChildren(mListView);
            }
        }
    };

    @Override
    public void onClick(View v) {
        String alert;
        String message;
        switch (v.getId()) {

            case R.id.ibBackEditProfile:
                if (fromMess) {
                    Intent intent = new Intent(this, MessageActivity.class);
                    intent.putExtra(Constant.POSSITION, position);
                    intent.putExtra(Constant.TITTLE_POST, tittlePost);
                    intent.putExtra(Constant.CATEGORYID, categoryId);
                    intent.putExtra(Constant.ID_USER_OWNER, idUserOwnerAdvertise);
                    intent.putExtra(Constant.ADVERTISEID, advertiseId);
                    intent.putExtra(Constant.CONVERSATIONID, conversationId);
                    intent.putExtra(Constant.ADVERTISE_CREATED_AT, advertiseCreatedAt);
                    intent.putExtra(Constant.FROM_DETAIL, false);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    gotoProfile();
                }

                break;

            case R.id.btnSaveEditProfile:
                processClickSave();

            case R.id.tbAllowEmailProfile:

                if (edtEmailEditProfile.getText().toString().trim().length() == 0) {
                    alert = getResources().getString(R.string.alert);
                    message = getResources().getString(R.string.error_you_did_not_setup_email);
                    showMessageError(alert, message);
                    tbAllowEmailProfile.setChecked(isEmailAble);
                } else if (!isValidEmail(edtEmailEditProfile.getText().toString())) {
                    alert = getResources().getString(R.string.alert);
                    message = getResources().getString(R.string.error_email_is_not_valid);
                    showMessageError(alert, message);
                    tbAllowEmailProfile.setChecked(isEmailAble);
                } else {
                    isEmailAble = !isEmailAble;
                }
                break;
            case R.id.tbAllowCallProfile:

                if (edtPhoneNumberEditProfile.getText().toString().trim().length() == 0) {
                    alert = getResources().getString(R.string.alert);
                    message = getResources().getString(R.string.error_you_did_not_setup_phone);
                    showMessageError(alert, message);
                    tbAllowCallProfile.setChecked(isCallAble);
                } else if (!PhoneNumberUtils.isGlobalPhoneNumber(edtPhoneNumberEditProfile.getText().toString())) {
                    alert = getResources().getString(R.string.alert);
                    message = getResources().getString(R.string.error_phone_number_is_not_valid);
                    showMessageError(alert, message);
                    tbAllowCallProfile.setChecked(isCallAble);
                } else {
                    isCallAble = !isCallAble;
                }
                break;


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

    private void processClickSave() {
        StringUtil utils = new StringUtil();
        String alert;
        String message;
        if (edtEmailEditProfile.getText().toString().trim().length() == 0) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_input_email);
            showMessageError(alert, message);
        } else if (!isValidEmail(edtEmailEditProfile.getText().toString())) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_email_is_not_valid);
            showMessageError(alert, message);
        } else if (edtPhoneNumberEditProfile.getText().toString().trim().length() == 0) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_input_phone_number);
            showMessageError(alert, message);
        } else if (!PhoneNumberUtils.isGlobalPhoneNumber(edtPhoneNumberEditProfile.getText().toString())) {
            alert = getResources().getString(R.string.alert);
            message = getResources().getString(R.string.error_phone_number_is_not_valid);
            showMessageError(alert, message);
        } else if (edtUserNameEditProfile.getText().toString().trim().length() == 0) {
            alert = getResources().getString(R.string.error);
            message = getResources().getString(R.string.error_input_user_name);
            showMessageError(alert, message);
        } else {
            boolean isValid = utils.isEmailValid(edtEmailEditProfile.getText());
            if (!isValid) {
                alert = getResources().getString(R.string.error);
                message = getResources().getString(R.string.invalidate_email);
                showMessageError(alert, message);
            } else {
                isValid = utils.isPhoneNumberValid(edtPhoneNumberEditProfile.getText());
                if (!isValid) {
                    alert = getResources().getString(R.string.error);
                    message = getResources().getString(R.string.invalidate_phone);
                    showMessageError(alert, message);
                } else {
                    gotoMenuAndSetFragmentProfile();
                }
            }
        }
    }

    public void setInformationForUser() {


        if (user != null) {
            edtCompanyNameEditProfile.setText(user.getCompanyName());
            edtUserNameEditProfile.setText(user.getDisplay_name());
            edtAddressEditProfile.setText(user.getAddress());
            edtCityEditProfile.setText(user.getCity());
            edtStateEditProfile.setText(user.getState());
            edtZipCodeEditProfile.setText(user.getZip());
            edtPhoneNumberEditProfile.setText(user.getPhoneNumber());
            edtFaxEditProfile.setText(user.getFaxNumber());
            tvNameEditProfile.setText(user.getDisplay_name());
            //TODO EMAIL AND WEBSITE
            /*edtEmailEditProfile.setText(user.get());
            edtWebsiteEditProfile.setText(user.get());

            if (user.getCallAble() != null) {
                isCallAble = user.getCallAble();
                tbAllowCallProfile.setChecked(isCallAble);
            }
            if (user.getEmailAble() != null) {
                isEmailAble = user.getEmailAble();
                tbAllowEmailProfile.setChecked(isEmailAble);
            }
            edtNoteEditProfile.setText(user.getNote());
            categorySelectedId = user.getSubscriptionId() == null ? Constant.sosokanCategoryAll : user.getSubscriptionId();
            if (user.getEmail() != null && !user.getEmail().isEmpty() && user.getEmail().toLowerCase().contains("sosokan.com")) {
                emailUser = user.getEmail();
                edtEmailEditProfile.setText("");
            }
            if (user.getUserName() != null && !user.getUserName().isEmpty() && user.getUserName().toLowerCase().contains(getResources().getString(R.string.guest).toLowerCase())) {
                userName = user.getEmail();
                edtUserNameEditProfile.setText("");
            }
            */
        }

        edtEmailEditProfile.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void showMessageError(String title, String message) {
        try {
            //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)getApplicationContext()).isDestroyed()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            if (dlgAlert != null)
                dlgAlert.create().dismiss();
            dlgAlert.setMessage(message);
            dlgAlert.setTitle(title);
            dlgAlert.setPositiveButton(getResources().getString(R.string.ok), null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            //}
        } catch (Exception ex) {

        }
    }

    public void setInformationForUserFromView() {
        if (user != null) {
            user.setCompanyName(edtCompanyNameEditProfile.getText().toString());
            user.setDisplay_name(edtUserNameEditProfile.getText().toString());
            user.setAddress(edtAddressEditProfile.getText().toString());
            user.setCity(edtCityEditProfile.getText().toString());
            user.setState(edtStateEditProfile.getText().toString());
            user.setZip(edtZipCodeEditProfile.getText().toString());
            user.setPhoneNumber(edtPhoneNumberEditProfile.getText().toString());
            user.setFaxNumber(edtFaxEditProfile.getText().toString());
            user.setNote(edtNoteEditProfile.getText().toString());
            user.setEmailAble(isEmailAble);
            user.setCallAble(isCallAble);
            //TODO CHECK SUB
            /*if (edtEmailEditProfile.getText().toString().isEmpty()) {
                user.setEmail(emailUser);
            }
            user.setEmail(edtEmailEditProfile.getText().toString());

            user.setSubscriptionId(Integer.toString(positionCategory));

            user.setSubscriptionId(categorySelectedId);
            user.setWebsite(edtWebsiteEditProfile.getText().toString());*/
        }
    }


    public void gotoMenuAndSetFragmentProfile() {
        setInformationForUserFromView();
        if (user != null) {
            PrefManager prefManager = new PrefManager(getApplicationContext());
            prefManager.setUserProfile(user);
            //TODO SAVE USER
           /* FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                    } else {
                        gotoProfile();
                    }
                }
            });*/


        } else {
            gotoProfile();
        }
    }


    public void gotoProfile() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("Fragment", "Profile");
        startActivity(intent);
        overridePendingTransition(0, R.anim.slide_in_up);
        finish();
    }

    @Override
    public void onUserProfileFailure(String appErrorMessage) {

    }

    @Override
    public void getUserProfileSuccess(UserProfileResponse userProfileResponse) {
        if (userProfileResponse != null) {
            if (userProfileResponse.getResults() != null && userProfileResponse.getResults().size() > 0) {
                user = userProfileResponse.getResults().get(0);
                if (user != null) {
                    Log.e("edit profile userId "," " + user);
                    //getListUserFollowMe();
                    setInformationForUser();
                    if (user.getImage_url() != null && user.getImage_url().toString().trim().length() > 0) {
                        Glide.with(ivUserProfile.getContext()).load(user.getImage_url()).centerCrop()
                                .transform(new CircleTransform(ivUserProfile.getContext())).override(40, 40).into(ivUserProfile);
                    } else {

                    }
                }
            }

        }
    }
}
