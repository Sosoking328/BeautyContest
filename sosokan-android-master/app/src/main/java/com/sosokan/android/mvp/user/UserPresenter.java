package com.sosokan.android.mvp.user;

import android.app.Activity;

import com.google.gson.Gson;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.UserApi;
import com.sosokan.android.mvp.category.CategoryView;
import com.sosokan.android.networking.NetworkError;
import com.sosokan.android.networking.Service;
import com.sosokan.android.ui.activity.PrefManager;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by macintosh on 3/16/17.
 */

public class UserPresenter {
    private final Service service;
    private final UserView view;
    private CompositeSubscription subscriptions;
    private final Gson gson;
    private PrefManager prefManager = null;

    public UserPresenter(Service service, UserView view, Activity activity) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
        this.gson = new Gson();
        this.prefManager = new PrefManager(activity);
    }

    public void getUser() {


        if (service != null) {
            Subscription subscription = service.getUser(new Service.GetUserCallBack() {
               /* @Override
                public void onSuccess(CategoryListResponse categoryListResponse) {

                    view.getCategoryListSuccess(categoryListResponse);
                }*/

                @Override
                public void onSuccess(UserApi userApi) {
                    prefManager.setUserApi(userApi);
                    view.getUserSuccess(userApi);

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
