package com.sosokan.android.mvp.category;


import android.app.Activity;

import com.google.gson.Gson;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.networking.NetworkError;
import com.sosokan.android.networking.Service;
import com.sosokan.android.ui.activity.PrefManager;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by macintosh on 3/15/17.
 */

public class CategoryPresenter {
    private final Service service;
    private final CategoryView view;
    private CompositeSubscription subscriptions;
    private final Gson gson;
    private PrefManager prefManager = null;

    public CategoryPresenter(Service service, CategoryView view, Activity activity) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
        this.gson = new Gson();
        this.prefManager = new PrefManager(activity);
    }

    public void getCategoryList() {


        if (service != null) {
            Subscription subscription = service.getListCategory(new Service.GetCategoryListCallback() {

                @Override
                public void onSuccess(List<CategoryNew> categoryListResponse) {
                    prefManager.setCategoriesApi(gson.toJson(categoryListResponse));
                    view.getCategoryListSuccess(categoryListResponse);

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
