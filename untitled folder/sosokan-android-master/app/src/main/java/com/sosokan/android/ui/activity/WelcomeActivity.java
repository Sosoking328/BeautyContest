package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.sosokan.android.BuildConfig;
import com.sosokan.android.R;
import com.sosokan.android.adapter.MyViewPagerAdapter;
import com.sosokan.android.utils.LocaleHelper;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * Created by AnhZin on 12/16/2016.
 */

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;
    private static final String SHOW_MESSAGE = "Show.Message";

    boolean isChineseApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }*/
        Fabric.with(this, new Crashlytics());
        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        // layouts of all welcome sliders
        // add few more layouts if you want
        if (getApplicationContext() != null) {
            String languageToLoad = LocaleHelper.getLanguage(getApplicationContext());
            isChineseApp = languageToLoad.toString().equals("zh");
            boolean isShow = getShowMessage(getApplicationContext());
            languageToLoad = LocaleHelper.getLanguage(this);
            if (languageToLoad != null && !languageToLoad.isEmpty()) {
                setSaveShowMessage(false);
            } else if (!isShow) {
                showMessageLanguage();
            } else {
                setSaveShowMessage(false);

            }
        }

      /*  if (!isShow) {
            showMessageLanguage();
        } else {
            setSaveShowMessage(false);

        }*/
        if (isChineseApp) {
            layouts = new int[]{
                    R.layout.welcome_slide1_cn,
                    R.layout.welcome_slide2_cn,
                    R.layout.welcome_slide3_cn,
                    R.layout.welcome_slide4_cn,
                    R.layout.welcome_slide5_cn
            };
        } else {
            layouts = new int[]{
                    R.layout.welcome_slide1_en,
                    R.layout.welcome_slide2_en,
                    R.layout.welcome_slide3_en,
                    R.layout.welcome_slide4_en,
                    R.layout.welcome_slide5_en
            };
        }
        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter(this, layouts);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });


    }

    private static boolean getShowMessage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(SHOW_MESSAGE, false);
    }

    public void showMessageLanguage() {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) getApplicationContext()).isDestroyed()) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

                if (dlgAlert != null)
                    dlgAlert.create().dismiss();
                dlgAlert.setMessage(getResources().getString(R.string.please_select_your_language));
                dlgAlert.setTitle(getResources().getString(R.string.app_name));
                dlgAlert.setPositiveButton(getResources().getString(R.string.chinese), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setLocale(getApplicationContext(), "zh");
                        LocaleHelper.onCreate(getApplicationContext(), "zh");
                        refreshActivity();
                    }


                });
                dlgAlert.setNegativeButton(getResources().getString(R.string.english), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setLocale(getApplicationContext(), "en");
                        if (getApplicationContext() != null) {
                            LocaleHelper.onCreate(getApplicationContext(), "en");
                        }
                        refreshActivity();
                    }
                });

                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
//            }
        } catch (Exception ex) {

        }
    }

    private void refreshActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        WelcomeActivity.this.overridePendingTransition(R.anim.nothing, R.anim.nothing);
    }

    public void setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
        setSaveShowMessage(true);
    }

    public void setSaveShowMessage(boolean isShow) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(SHOW_MESSAGE, isShow);
        editor.commit();
        editor.apply();
    }

    private void addBottomDots(int currentPage) {
        if (layouts != null && layouts.length > 0) {

            dots = new TextView[layouts.length];

            int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
            int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

            dotsLayout.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(colorsInactive[currentPage]);
                dotsLayout.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
        finish();
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


}
