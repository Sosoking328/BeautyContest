package com.sosokan.android.mvp.splash;

import android.app.Activity;

import com.google.gson.Gson;
import com.sosokan.android.models.UserApi;
import com.sosokan.android.models.VideoSplash;
import com.sosokan.android.mvp.user.UserView;
import com.sosokan.android.networking.NetworkError;
import com.sosokan.android.networking.Service;
import com.sosokan.android.ui.activity.PrefManager;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by macintosh on 3/16/17.
 */

public class SplashPresenter {
    private final Service service;
    private final SplashView view;
    private CompositeSubscription subscriptions;
    private final Gson gson;
    private PrefManager prefManager = null;

    public SplashPresenter(Service service, SplashView view, Activity activity) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
        this.gson = new Gson();
        this.prefManager = new PrefManager(activity);
    }

    public void getVideoSplash() {


        if (service != null) {
            Subscription subscription = service.getVideoListSplash(new Service.GetVideoSplashCallBack() {
               /* @Override
                public void onSuccess(CategoryListResponse categoryListResponse) {

                    view.getCategoryListSuccess(categoryListResponse);
                }*/

                @Override
                public void onSuccess(List<VideoSplash> videoSplashes) {

                    view.getListVideoSplashSuccess(videoSplashes);

                }

                @Override
                public void onError(NetworkError networkError) {

                    view.onFailure(networkError.getAppErrorMessage());
                }

            });

            subscriptions.add(subscription);
        }
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}
