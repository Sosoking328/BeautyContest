package com.sosokan.android.mvp.flagchoice;

import android.app.Activity;

import com.google.gson.Gson;
import com.sosokan.android.networking.NetworkError;
import com.sosokan.android.networking.Service;
import com.sosokan.android.ui.activity.PrefManager;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by macintosh on 3/16/17.
 */

public class FlagChoicePresenter {
    private final Service service;
    private final FlagChoiceView view;
    private CompositeSubscription subscriptions;
    private final Gson gson;
    private PrefManager prefManager = null;

    public FlagChoicePresenter(Service service, FlagChoiceView view, Activity activity) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
        this.gson = new Gson();
        this.prefManager = new PrefManager(activity);
    }

    public void getListFlagChoice() {


        if (service != null) {
            Subscription subscription = service.getListFlagChoice(new Service.GetFlagChoiceListCallBack() {

                @Override
                public void onSuccess(FlagChoiceResponse flagChoiceResponse) {

                    prefManager.setFlagchoices(flagChoiceResponse.getResults());
                    view.getFlagchoiceSuccess(flagChoiceResponse);

                }

                @Override
                public void onError(NetworkError networkError) {

                    view.onFlagChoiceFailure(networkError.getAppErrorMessage());
                }

            });

            subscriptions.add(subscription);
        }
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}
