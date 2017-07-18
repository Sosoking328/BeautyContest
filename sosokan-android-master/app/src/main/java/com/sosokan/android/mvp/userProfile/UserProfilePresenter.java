package com.sosokan.android.mvp.userProfile;

import android.app.Activity;

import com.google.gson.Gson;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.UserProfile;
import com.sosokan.android.models.UserProfileApi;
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

public class UserProfilePresenter {
    private final Service service;
    private final UserProfileView view;
    private CompositeSubscription subscriptions;
    private final Gson gson;
    private PrefManager prefManager = null;

    public UserProfilePresenter(Service service, UserProfileView view, Activity activity) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
        this.gson = new Gson();
        this.prefManager = new PrefManager(activity);
    }

    public void getUserProfile() {


        if (service != null) {
            Subscription subscription = service.getUserProfile(new Service.GetUserProfileCallback() {

                @Override
                public void onSuccess(UserProfileResponse userProfileResponse) {
                    if (userProfileResponse.getResults() != null && userProfileResponse.getResults().size() > 0) {
                        UserProfileApi userProfile = userProfileResponse.getResults().get(0);
                        prefManager.setUserProfile(userProfile);
                    }

                    view.getUserProfileSuccess(userProfileResponse);

                }

                @Override
                public void onError(NetworkError networkError) {

                    view.onUserProfileFailure(networkError.getAppErrorMessage());
                }

            });

            subscriptions.add(subscription);
        }
    }

    public void getUserProfileWithLegacyId(String legacyId) {


        if (service != null) {
            Subscription subscription = service.getUserProfileWithLegacyId(legacyId, new Service.GetUserProfileCallback() {

                @Override
                public void onSuccess(UserProfileResponse userProfileResponse) {
                    if (userProfileResponse.getResults() != null && userProfileResponse.getResults().size() > 0) {
                        UserProfileApi userProfile = userProfileResponse.getResults().get(0);
                        prefManager.setUserProfile(userProfile);
                    }

                    view.getUserProfileSuccess(userProfileResponse);

                }

                @Override
                public void onError(NetworkError networkError) {

                    view.onUserProfileFailure(networkError.getAppErrorMessage());
                }

            });

            subscriptions.add(subscription);
        }
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}
