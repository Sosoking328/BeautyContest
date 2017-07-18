package com.sosokan.android.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sosokan.android.R;
import com.sosokan.android.adapter.LanguageSpinnerAdapter;
import com.sosokan.android.control.menu.ResideMenu;
import com.sosokan.android.models.Image;
import com.sosokan.android.models.User;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.mvp.userProfile.UserProfilePresenter;
import com.sosokan.android.mvp.userProfile.UserProfileResponse;
import com.sosokan.android.mvp.userProfile.UserProfileView;
import com.sosokan.android.networking.Service;
import com.sosokan.android.ui.activity.EditProfileActivity;
import com.sosokan.android.ui.activity.FAQActivity;
import com.sosokan.android.ui.activity.InviteFriendActivity;
import com.sosokan.android.ui.activity.MenuActivity;
import com.sosokan.android.ui.activity.PrefManager;
import com.sosokan.android.ui.activity.SignUpActivity;
import com.sosokan.android.ui.activity.SplashActivity;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.CameraVideoHelper;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.FaceBookHelper;
import com.sosokan.android.utils.LocaleHelper;
import com.sosokan.android.utils.PermissionGrantedHelper;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.OnClick;
import io.doorbell.android.Doorbell;

import static com.sosokan.android.utils.Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE;
import static com.sosokan.android.utils.Constant.MEDIA_TYPE_IMAGE;
import static com.sosokan.android.utils.Constant.MEDIA_TYPE_VIDEO;
import static com.sosokan.android.utils.Constant.SELECT_PICTURE;
import static com.sosokan.android.utils.FireBaseUtils.generateId;

/**
 * Created by phuong.tran on 8/29/2016.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener, UserProfileView {
    private static final String TAG = "ProfileFragment";
    private static final String CHOOSE_LANGUAGE = "Choose.Language";
    private View parentView;
    Context appContext;
    LinearLayout llFAQ, llLogOut, llEditProfile, llInvite, llFeedback, llSubscript;
    Profile profile;
    private ResideMenu resideMenu;
    TextView tvNameUserProfile, tvUserClassifiedsProfile;
    FirebaseUser mUser;
    private FirebaseAuth mAuth;
    Spinner spnLanguage;
    ImageView ivCameraProfile, ivAvatarSetting;
    private Uri mDownloadImageUrl = null;
    private Uri mDownloadVideoUrl = null;
    private Uri fileUri; // file url to store image/video
    private Uri fileUriSelected; // file url to store image/video
    public int requestCodeMedia = -1;
    CameraVideoHelper helper;
    PermissionGrantedHelper permissionGrantedHelper;
    String nameImage;
    private StorageReference mStorageRef;
    //private DatabaseReference mDatabase, mDatabaseUser;
    // User user;
    UserProfileApi user;
    ImageView icLogoutLogInSetting;
    TextView tvLogoutLogInSetting;
    PrefManager prefManager;

    @Inject
    public Service service;
    UserProfilePresenter userProfilePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initValue(inflater, container);
        initView();
        initFacebook();
        initSpinnerLanguage();

        initFirebaseAndUser();
        return parentView;
    }

    private void initSpinnerLanguage() {
        String[] countryNames = getResources().getStringArray(R.array.languages_array);
        int flags[] = {R.drawable.f222, R.drawable.f043};
        spnLanguage = (Spinner) parentView.findViewById(R.id.spnLanguage);
        final MenuActivity menuActivity = (MenuActivity) getActivity();
        LanguageSpinnerAdapter customAdapter = new LanguageSpinnerAdapter(getActivity(), R.layout.item_spinner_language, flags, countryNames);

        spnLanguage.setAdapter(customAdapter);
        if (appContext != null && LocaleHelper.getLanguage(appContext).equals("en")) {
            spnLanguage.setSelection(0);
        } else {
            spnLanguage.setSelection(1);
        }
        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  setLanguageByPosition(position);
                if (getLanguage(appContext) != position) {
                    setSaveLanguage(position);
                    menuActivity.setLanguageByPosition(position);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initValue(LayoutInflater inflater, ViewGroup container) {
        parentView = inflater.inflate(R.layout.activity_profile, container, false);
        appContext = container.getContext();
        userProfilePresenter = new UserProfilePresenter(service, this, getActivity());

        nameImage = "";
        prefManager = new PrefManager(getContext());
        if (prefManager != null)
            user = prefManager.getUserProfile();
    }

    private static int getLanguage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(CHOOSE_LANGUAGE, 0);
    }

    public void setSaveLanguage(int position) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(CHOOSE_LANGUAGE, position);
        editor.commit();
        editor.apply();
    }

    private void initFirebaseAndUser() {
//        mDatabase = FirebaseDatabase.getInstance()
//                .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        if (mUser != null) {
            userProfilePresenter.getUserProfileWithLegacyId(mUser.getUid());
//            mDatabaseUser = FirebaseDatabase.getInstance()
//                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(mUser.getUid());
           /* FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(mUser.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            user = dataSnapshot.getValue(User.class);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });*/
        }


    }

    private void updateUIWhenGetUserProfileSuccess() {
        if (user != null) {
            //  Log.d(TAG, user.getId());
            String alert;
            String message;

            tvNameUserProfile.setText(user.getDisplay_name());
            tvUserClassifiedsProfile.setText(String.format(appContext.getString(R.string.class_field), user.getMyAdvertiseCount()));
            if (user.getImage_url() != null && !user.getImage_url().isEmpty()) {
                ivCameraProfile.setVisibility(View.GONE);
                ivAvatarSetting.setVisibility(View.VISIBLE);
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)appContext).isDestroyed())
                try {
                    Glide.with(appContext).load(user.getImage_url()).asBitmap().into(new SimpleTarget<Bitmap>(200, 200) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                ivAvatarSetting.setBackground(drawable);

                            }
                        }
                    });
                } catch (Exception ex) {

                }

            }


            if (user != null) {
                icLogoutLogInSetting.setImageResource(R.mipmap.logout_profile);
                tvLogoutLogInSetting.setText(appContext.getResources().getString(R.string.logout_menu));
            } else {
                icLogoutLogInSetting.setImageResource(R.mipmap.login_profile);
                tvLogoutLogInSetting.setText(appContext.getResources().getString(R.string.login_menu));

            }
        } else {
            tvNameUserProfile.setText("");
            tvUserClassifiedsProfile.setText(String.format(appContext.getString(R.string.class_field), 0));
        }
    }

    private void initFacebook() {
        FacebookSdk.sdkInitialize(appContext);
        profile = Profile.getCurrentProfile();
    }

    private void initView() {
        llFAQ = (LinearLayout) parentView.findViewById(R.id.llFAQ);
        llFAQ.setOnClickListener(this);

        llLogOut = (LinearLayout) parentView.findViewById(R.id.llLogOut);
        llLogOut.setOnClickListener(this);

        llEditProfile = (LinearLayout) parentView.findViewById(R.id.llEditProfile);
        llEditProfile.setOnClickListener(this);

        llInvite = (LinearLayout) parentView.findViewById(R.id.llInvite);
        llInvite.setOnClickListener(this);

        llFeedback = (LinearLayout) parentView.findViewById(R.id.llFeedback);
        llFeedback.setOnClickListener(this);

        llSubscript = (LinearLayout) parentView.findViewById(R.id.llSubscript);
        llSubscript.setOnClickListener(this);

        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        parentView.findViewById(R.id.ibMenuLeftProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        parentView.findViewById(R.id.ibEditProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoEditProfile();
            }


        });

        tvNameUserProfile = (TextView) parentView.findViewById(R.id.tvNameUserProfile);
        tvUserClassifiedsProfile = (TextView) parentView.findViewById(R.id.tvUserClassifiedsProfile);

        ivCameraProfile = (ImageView) parentView.findViewById(R.id.ivCameraProfile);
        ivCameraProfile.setOnClickListener(this);
        ivAvatarSetting = (ImageView) parentView.findViewById(R.id.ivAvatarSetting);

        icLogoutLogInSetting = (ImageView) parentView.findViewById(R.id.icLogoutLogInSetting);
        tvLogoutLogInSetting = (TextView) parentView.findViewById(R.id.tvLogoutLogInSetting);

    }


    private void gotoEditProfile() {
        Intent intent = new Intent(appContext, EditProfileActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @OnClick(R.id.llFAQ)
    void onFAQClick() {
        Intent intent = new Intent(appContext, FAQActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void showMessageError(String title, String message) {

        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)getContext()).isDestroyed()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
            if (dlgAlert != null)
                dlgAlert.create().dismiss();


            dlgAlert.setMessage(message);
            dlgAlert.setTitle(title);
            dlgAlert.setPositiveButton(getResources().getString(R.string.SignUp), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(appContext, SignUpActivity.class);
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

    @Override
    public void onClick(View v) {
        String alert;
        String message;
        Intent intent;
        switch (v.getId()) {
            case R.id.llFAQ:
                intent = new Intent(appContext, FAQActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case R.id.llLogOut:
                FaceBookHelper helper = new FaceBookHelper();
                helper.disconnectFromFacebook();
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(appContext, SplashActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case R.id.llEditProfile:
                if (user != null) {
                    gotoEditProfile();
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }

                break;
            case R.id.llInvite:
                intent = new Intent(appContext, InviteFriendActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case R.id.llSubscript:
                if (user != null) {
                  /*  intent = new Intent(appContext, SubscriptionsActivity.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));*/
                    intent = new Intent(appContext, MenuActivity.class);
                    intent.putExtra("Fragment", "Following");

                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    alert = getResources().getString(R.string.we_are_sorry);
                    message = getResources().getString(R.string.you_need_to_login_your_sosokan);
                    showMessageError(alert, message);
                }
                /*alert = getResources().getString(R.string.we_are_sorry);
                message = getResources().getString(R.string.feature_is_not_available);
                showMessageError(alert, message);*/
                break;
            case R.id.llFeedback:
                sendFeedback();
                break;
            case R.id.ivCameraProfile:

                if (user != null) {
                    if (user != null) {
                        captureImage();
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

                break;
        }
    }

    public void sendFeedback() {
        // In your activity
        int appId = Config.APP_ID_DOORBLE; // Replace with your application's ID
        String apiKey = Config.API_KEY_DOORBLE; // Replace with your application's API key
        Doorbell doorbellDialog = new Doorbell(getActivity(), appId, apiKey); // Create the Doorbell object

        doorbellDialog.setEmailHint(getResources().getString(R.string.your_email_address));
        doorbellDialog.setMessageHint(getResources().getString(R.string.what_on_your_mind));
        doorbellDialog.setPositiveButtonText(getResources().getString(R.string.send));
        doorbellDialog.setNegativeButtonText(android.R.string.cancel);
        // Callback for when a message is successfully sent
        doorbellDialog.setOnFeedbackSentCallback(new io.doorbell.android.callbacks.OnFeedbackSentCallback() {
            @Override
            public void handle(String message) {
                // Show the message in a different way, or use your own message!
                Toast.makeText(appContext, message, Toast.LENGTH_LONG).show();
            }
        });

        // Callback for when the dialog is shown
        doorbellDialog.setOnShowCallback(new io.doorbell.android.callbacks.OnShowCallback() {
            @Override
            public void handle() {
//                Toast.makeText(appContext, "Dialog shown", Toast.LENGTH_LONG).show();
            }
        });

        doorbellDialog.show();
    }

    private void captureImage() {

        permissionGrantedHelper = new PermissionGrantedHelper(getActivity());
        if (permissionGrantedHelper.isDeviceSupportCamera()) {
            if (permissionGrantedHelper.checkAnPermissionCamera() && permissionGrantedHelper.checkAnPermissionWriteStorage() && permissionGrantedHelper.checkAnPermissionReadStorage()) {
                callIntentCamera();
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

    public void callIntentCamera() {

       /* Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


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
            // Create and launch the intent
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        // if the result is capturing Image
        if (requestCode == Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                if (fileUri != null) {
                    requestCodeMedia = MEDIA_TYPE_IMAGE;
                    nameImage = fileUri.toString().substring(fileUri.toString().lastIndexOf("/") + 1, fileUri.toString().length());
                    uploadFromUri();

                } else {
                    Log.w(TAG, "File URI is null");
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(appContext,
                        "USER cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(appContext,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                requestCodeMedia = SELECT_PICTURE;
                setImageSelected(data);
            }

        }
    }

    private void setImageSelected(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            fileUriSelected = data.getData();
            if (fileUriSelected != null) {
                helper = new CameraVideoHelper(getActivity());
                try {
                    bm = helper.decodeUri(data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nameImage = fileUriSelected.toString().substring(fileUriSelected.toString().lastIndexOf("/") + 1, fileUriSelected.toString().length());

                if (bm != null) {
                    Drawable drawable = new BitmapDrawable(getResources(), bm);
                    ivAvatarSetting.setBackgroundDrawable(drawable);
                }
            }
            //  imgPreview.setImageBitmap(bm);
        }

    }

    // [END upload_from_uri]

    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            ivCameraProfile.setVisibility(View.GONE);

            ivAvatarSetting.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !(getActivity()).isDestroyed()) {
                Glide.with(ivAvatarSetting.getContext()).load(user.getImage_url()).asBitmap().into(new SimpleTarget<Bitmap>(ivAvatarSetting.getWidth(), ivAvatarSetting.getHeight()) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ivAvatarSetting.setBackground(drawable);

                        }
                    }
                });
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void uploadFromUri() {
        if (fileUri != null) {
            Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

            // [START get_child_ref]
            // Get a reference to store file at photos/<FILENAME>.jpg
            String folder = Constant.IMAGES;
            if (requestCodeMedia == MEDIA_TYPE_VIDEO) // VIDEO
            {
                folder = Constant.VIDEOS;
            }
            final StorageReference photoRef = mStorageRef.child(folder)
                    .child(fileUri.getLastPathSegment());
            // [END get_child_ref]

            // Upload file to Firebase Storage
            // [START_EXCLUDE]
            if (!ApplicationUtils.isShow) {
                ApplicationUtils.showProgress(getActivity());
            }
            // [END_EXCLUDE]
            Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());

            photoRef.putFile(fileUri)
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Upload succeeded
                            Log.d(TAG, "uploadFromUri:onSuccess");
                            //System.out.println("taskSnapshot " + taskSnapshot);
                            // Get the public download URL
                            switch (requestCodeMedia) {
                                case SELECT_PICTURE:
                                case MEDIA_TYPE_IMAGE:
                                    mDownloadImageUrl = taskSnapshot.getDownloadUrl();
                                    updateAvatarForUser(mDownloadImageUrl.toString());
                                    break;
                            }

                            ApplicationUtils.closeMessage();

                        }
                    })
                    .addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Upload failed
                            Log.w(TAG, "uploadFromUri:onFailure", exception);

                            switch (requestCodeMedia) {
                                case SELECT_PICTURE:
                                case MEDIA_TYPE_IMAGE:
                                    mDownloadImageUrl = null;
                                    break;
                                case MEDIA_TYPE_VIDEO:
                                    mDownloadVideoUrl = null;
                                    break;
                            }

                            // [START_EXCLUDE]
                            ApplicationUtils.closeMessage();
                            Toast.makeText(getActivity(), "Error: upload failed",
                                    Toast.LENGTH_SHORT).show();
                            // [END_EXCLUDE]

                            ApplicationUtils.closeMessage();
                        }
                    });
        }
    }

    public void updateAvatarForUser(String avatarUrl) {
        if (user != null) {
            // Log.d(TAG, user.getId());
            //TODO UPDATE USER WITH AVATAR
           /* Image imageHeader = new Image();
            imageHeader.setImageUrl(avatarUrl);
            imageHeader.setUserId(user.getId());
            user.setAvatar(imageHeader);
            FirebaseDatabase.getInstance()
                    .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                    } else {
                        previewCapturedImage();
                    }
                }
            });*/

        }
    }

    @Override
    public void onUserProfileFailure(String appErrorMessage) {

    }

    @Override
    public void getUserProfileSuccess(UserProfileResponse userProfileResponse) {
        if (userProfileResponse.getResults() != null && userProfileResponse.getResults().size() > 0) {
            user = userProfileResponse.getResults().get(0);
            updateUIWhenGetUserProfileSuccess();
        }

    }
}
