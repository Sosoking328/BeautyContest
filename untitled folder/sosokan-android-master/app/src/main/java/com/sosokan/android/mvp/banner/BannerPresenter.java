package com.sosokan.android.mvp.banner;

import android.app.Activity;

import com.google.gson.Gson;
import com.sosokan.android.models.Banner;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.mvp.category.CategoryView;
import com.sosokan.android.networking.NetworkError;
import com.sosokan.android.networking.Service;
import com.sosokan.android.ui.activity.PrefManager;

import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by macintosh on 3/17/17.
 */

public class BannerPresenter {
    private final Service service;
    private final BannerView view;
    private CompositeSubscription subscriptions;
    private final Gson gson;
    private PrefManager prefManager = null;

    public BannerPresenter(Service service, BannerView view, Activity activity) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
        this.gson = new Gson();
        this.prefManager = new PrefManager(activity);
    }

    public void getBannerList(Map<String, String> params) {


        if (service != null) {
            Subscription subscription = service.getListBanner(params, new Service.GetBannerListCallBack() {
                @Override
                public void onSuccess(List<Banner> banners) {
                    view.getBannerListSuccess(banners);

                }

                @Override
                public void onError(NetworkError networkError) {

                    view.onBannerFailure(networkError.getAppErrorMessage());
                }

            });

            subscriptions.add(subscription);
        }
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}
