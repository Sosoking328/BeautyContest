package com.sosokan.android.mvp.advertise;

import android.app.Activity;

import com.google.gson.Gson;
import com.sosokan.android.models.AdvertiseApi;
import com.sosokan.android.models.VideoSplash;
import com.sosokan.android.mvp.splash.SplashView;
import com.sosokan.android.networking.NetworkError;
import com.sosokan.android.networking.Service;
import com.sosokan.android.ui.activity.EditAdvertiseActivity;
import com.sosokan.android.ui.activity.PrefManager;
import com.sosokan.android.utils.Constant;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by macintosh on 3/16/17.
 */

public class AdvertisePresenter {
    private final Service service;
    private final AdvertiseView view;
    private CompositeSubscription subscriptions;
    private final Gson gson;
    private PrefManager prefManager = null;

    public AdvertisePresenter(Service service, AdvertiseView view, Activity activity) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
        this.gson = new Gson();
        this.prefManager = new PrefManager(activity);
    }



    public void getListAdvertise() {


        if (service != null) {
            Subscription subscription = service.getListAdvertise(new Service.GetAdvertiseListCallBack() {
                @Override
                public void onSuccess(AdvertiseResponse advertiseResponse) {
                    Gson gson = new Gson();
                    prefManager.setListAdvertiseApiWithCategoryId(Constant.sosokanCategoryAll,gson.toJson(advertiseResponse.getResults()));
                    view.getListAdvertiseSuccess(advertiseResponse);

                }

                @Override
                public void onError(NetworkError networkError) {

                    view.onAdvertiseFailure(networkError.getAppErrorMessage());
                }

            });

            subscriptions.add(subscription);
        }
    }

    public void getListAdvertise(final String categoryId) {
        if (service != null) {
            Subscription subscription = service.getListAdvertise(categoryId, new Service.GetAdvertiseListCallBack() {
                @Override
                public void onSuccess(AdvertiseResponse advertiseResponse) {
                    Gson gson = new Gson();
                    prefManager.setListAdvertiseApiWithCategoryId(categoryId,gson.toJson(advertiseResponse.getResults()));
                    view.getListAdvertiseSuccess(advertiseResponse);

                }

                @Override
                public void onError(NetworkError networkError) {

                    view.onAdvertiseFailure(networkError.getAppErrorMessage());
                }

            });

            subscriptions.add(subscription);
        }
    }

    public void getAdvertise(String legacyId) {
        if (service != null) {
            Subscription subscription = service.getAdvertise(legacyId, new Service.GetAdvertiseCallback() {
                @Override
                public void onSuccess(AdvertiseApi advertiseApi) {

                    view.getAdvertiseSuccess(advertiseApi);

                }

                @Override
                public void onError(NetworkError networkError) {

                    view.onAdvertiseFailure(networkError.getAppErrorMessage());
                }

            });

            subscriptions.add(subscription);
        }
    }

    public void getAdvertise(int id) {
        if (service != null) {
            Subscription subscription = service.getAdvertise(id, new Service.GetAdvertiseCallback() {
                @Override
                public void onSuccess(AdvertiseApi advertiseApi) {

                    view.getAdvertiseSuccess(advertiseApi);

                }

                @Override
                public void onError(NetworkError networkError) {

                    view.onAdvertiseFailure(networkError.getAppErrorMessage());
                }

            });

            subscriptions.add(subscription);
        }
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}
