package com.sosokan.android.mvp.home;

import android.app.Activity;

import com.google.gson.Gson;
import com.sosokan.android.models.VideoSplash;
import com.sosokan.android.mvp.category.CategoryView;
import com.sosokan.android.mvp.splash.SplashView;
import com.sosokan.android.networking.NetworkError;
import com.sosokan.android.networking.Service;
import com.sosokan.android.ui.activity.MenuActivity;
import com.sosokan.android.ui.activity.PrefManager;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by macintosh on 3/25/17.
 */

public class HomePresenter {

    private final Service service;
    private final HomeView view;
    private CompositeSubscription subscriptions;
    private final Gson gson;
    private PrefManager prefManager = null;

    public HomePresenter(Service service, HomeView view, Activity activity) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
        this.gson = new Gson();
        this.prefManager = new PrefManager(activity);
    }


    public void setCategoryChange(final String categoryChange) {
        /*if (service != null) {
            Subscription subscription = new Subscription() {
                @Override
                public void unsubscribe() {

                }

                @Override
                public boolean isUnsubscribed() {
                    view.onCategoryChange(categoryChange);
                    return false;
                }
            };
            subscriptions.add(subscription);
        }*/
        view.onCategoryChange(categoryChange);
    }
}
