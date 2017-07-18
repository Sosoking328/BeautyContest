package com.sosokan.android.ui.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.sosokan.android.BuildConfig;
import com.sosokan.android.R;
import com.sosokan.android.utils.ApplicationUtils;

import io.fabric.sdk.android.Fabric;

/**
 * Created by AnhZin on 9/18/2016.
 */
public class InviteFriendActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "InviteFriendActivity";
    private static final int REQUEST_INVITE = 0;
    CallbackManager callbackManager;
    LinearLayout llInviteFacebook;
    ImageButton ibBackInviteFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intive_friend);
        Fabric.with(this, new Crashlytics());
        initView();


    }

    private void initView() {
        llInviteFacebook = (LinearLayout) findViewById(R.id.llInviteFacebook);
        llInviteFacebook.setOnClickListener(this);
        ibBackInviteFriend = (ImageButton) findViewById(R.id.ibBackInviteFriend);
        ibBackInviteFriend.setOnClickListener(this);
    }


    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {

            case R.id.ibBackInviteFriend:
                gotoProfile();
                break;
            case R.id.llInviteFacebook:
                onInviteClicked();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ApplicationUtils.closeMessage();
    }

    private void onInviteClicked() {
        callbackManager = CallbackManager.Factory.create();

        String AppURl = "https://fb.me/567307806810881";  //Generated from //fb developers

        String previewImageUrl = "http://www.sosokanapp.com/assets/images/hand.png";


        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(AppURl).setPreviewImageUrl(previewImageUrl)
                    .build();

            AppInviteDialog appInviteDialog = new AppInviteDialog(InviteFriendActivity.this);
            appInviteDialog.registerCallback(callbackManager,
                    new FacebookCallback<AppInviteDialog.Result>() {
                        @Override
                        public void onSuccess(AppInviteDialog.Result result) {
                            Log.d("Invitation", "Invitation Sent Successfully");
                            finish();
                            showMessageInvitationSuccess();
                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException e) {
                            Log.d("Invitation", "Error Occured");
                        }
                    });

            appInviteDialog.show(content);
        }
    }

    public void gotoProfile() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("Fragment", "Profile");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, 0);
        finish();
    }

    public void showMessageInvitationSuccess() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) getApplicationContext()).isDestroyed()) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("").setMessage(
                            getResources().getString(R.string.the_invitation_was_sent));

            final AlertDialog alert = dialog.create();
            alert.show();

            new CountDownTimer(5000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub

                    alert.dismiss();
                }
            }.start();
        }
//    }
}

