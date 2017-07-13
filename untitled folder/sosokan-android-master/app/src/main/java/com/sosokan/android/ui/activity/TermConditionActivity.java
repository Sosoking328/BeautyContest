package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.sosokan.android.BuildConfig;
import com.sosokan.android.R;
import io.fabric.sdk.android.Fabric;

/**
 * Created by AnhZin on 1/7/2017.
 */

public class TermConditionActivity extends Activity implements View.OnClickListener {

    ImageButton ibBackTermPolicy;
    WebView webViewTermPolicy;
    TextView tvTermPolicy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);
        Fabric.with(this, new Crashlytics());
        ibBackTermPolicy=   (ImageButton) findViewById(R.id.ibBackTermPolicy);
        ibBackTermPolicy.setOnClickListener(this);
        webViewTermPolicy = (WebView) findViewById(R.id.webViewTermPolicy);
        webViewTermPolicy.setWebViewClient(new WebViewClient());
        webViewTermPolicy.getSettings().setJavaScriptEnabled(true);
        webViewTermPolicy.getSettings().setDomStorageEnabled(true);

        webViewTermPolicy.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webViewTermPolicy.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    /*    webViewTermPolicy.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
        webViewTermPolicy.getSettings().setDefaultFontSize(20);*/
        tvTermPolicy =   (TextView) findViewById(R.id.tvTermPolicy);
        Bundle extras = getIntent().getExtras();
        boolean isTerm = extras.getBoolean("isTerm",true);
        if (isTerm) {
            webViewTermPolicy.loadUrl("file:///android_asset/terms_conditions.html");
            tvTermPolicy.setText(getResources().getString(R.string.term));
        }else {
            webViewTermPolicy.loadUrl("file:///android_asset/privacy_policy.html");
            tvTermPolicy.setText(getResources().getString(R.string.privacy_policy));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBackTermPolicy:
                Intent intent = new Intent(this, SplashActivity.class);
                finish();
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

        }
    }
}
