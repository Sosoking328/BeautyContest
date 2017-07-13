package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.sosokan.android.BuildConfig;
import com.sosokan.android.R;
import com.sosokan.android.utils.ApplicationUtils;

import io.fabric.sdk.android.Fabric;

/**
 * Created by AnhZin on 9/19/2016.
 */
public class SubscriptionsActivity  extends Activity implements View.OnClickListener {
    private static final String TAG = "InviteFriendActivity";

    ImageButton ibBackSubscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);
        Fabric.with(this, new Crashlytics());
        initView();


    }

    private void initView() {
        ibBackSubscription =  (ImageButton) findViewById(R.id.ibBackSubscription) ;
        ibBackSubscription.setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {

            case R.id.ibBackSubscription:
                gotoProfile();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ApplicationUtils.closeMessage();
    }

    public void gotoProfile()
    {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("Fragment", "Profile");
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        overridePendingTransition(R.anim.start_activity_left_to_right, 0);
        finish();
    }

}
