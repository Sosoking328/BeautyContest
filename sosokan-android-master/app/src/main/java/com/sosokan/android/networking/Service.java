package com.sosokan.android.networking;


import com.sosokan.android.models.AdvertiseApi;
import com.sosokan.android.models.Banner;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.UserApi;
import com.sosokan.android.models.VideoSplash;
import com.sosokan.android.mvp.advertise.AdvertiseResponse;
import com.sosokan.android.mvp.flagchoice.FlagChoiceResponse;
import com.sosokan.android.mvp.userProfile.UserProfileResponse;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by macintosh on 3/15/17.
 */

public class Service {
    private final NetworkService networkService;

    public Service(NetworkService networkService) {
        this.networkService = networkService;
    }

    /*CATEGORY*/
    public Subscription getListCategory(final GetCategoryListCallback callback) {

        return networkService.getListCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<CategoryNew>>>() {
                    @Override
                    public Observable<? extends List<CategoryNew>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<List<CategoryNew>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(List<CategoryNew> categoryListResponse) {
                        callback.onSuccess(categoryListResponse);

                    }
                });
    }

    public interface GetCategoryListCallback {
        void onSuccess(List<CategoryNew> cityListResponse);

        void onError(NetworkError networkError);
    }


    /*USER PROFILE*/
    public Subscription getUserProfile(final GetUserProfileCallback callback) {

        return networkService.getUserProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends UserProfileResponse>>() {
                    @Override
                    public Observable<? extends UserProfileResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<UserProfileResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(UserProfileResponse userProfileResponse) {
                        callback.onSuccess(userProfileResponse);

                    }
                });
    }

    public Subscription getUserProfileWithLegacyId(String legacyId, final GetUserProfileCallback callback) {

        return networkService.getUserProfile(legacyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends UserProfileResponse>>() {
                    @Override
                    public Observable<? extends UserProfileResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<UserProfileResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(UserProfileResponse userProfileResponse) {
                        callback.onSuccess(userProfileResponse);

                    }
                });
    }

    public interface GetUserProfileCallback {
        void onSuccess(UserProfileResponse userProfileResponse);

        void onError(NetworkError networkError);
    }


    /*USER*/
    public Subscription getUser(final GetUserCallBack callback) {

        return networkService.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends UserApi>>() {
                    @Override
                    public Observable<? extends UserApi> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<UserApi>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(UserApi userApi) {
                        callback.onSuccess(userApi);

                    }
                });
    }

    public interface GetUserCallBack {
        void onSuccess(UserApi userApi);

        void onError(NetworkError networkError);
    }


    /*Video Splash*/
    public Subscription getVideoListSplash(final GetVideoSplashCallBack callback) {

        return networkService.getListVideoSplash()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<VideoSplash>>>() {
                    @Override
                    public Observable<? extends List<VideoSplash>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<List<VideoSplash>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(List<VideoSplash> videoSplashes) {
                        callback.onSuccess(videoSplashes);

                    }
                });
    }

    public interface GetVideoSplashCallBack {
        void onSuccess(List<VideoSplash> videoSplashes);

        void onError(NetworkError networkError);
    }


    /*ADVERTISE*/
    public Subscription getListAdvertise(final GetAdvertiseListCallBack callback) {

        return networkService.getListAdvertise()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends AdvertiseResponse>>() {
                    @Override
                    public Observable<? extends AdvertiseResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<AdvertiseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(AdvertiseResponse advertiseResponse) {
                        callback.onSuccess(advertiseResponse);

                    }
                });
    }

    public Subscription getListAdvertise(String categoryId, final GetAdvertiseListCallBack callback) {

        return networkService.getListAdvertise(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends AdvertiseResponse>>() {
                    @Override
                    public Observable<? extends AdvertiseResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<AdvertiseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(AdvertiseResponse advertiseResponse) {
                        callback.onSuccess(advertiseResponse);

                    }
                });
    }


    public interface GetAdvertiseListCallBack {
        void onSuccess(AdvertiseResponse advertiseResponse);

        void onError(NetworkError networkError);
    }

    public Subscription getAdvertise(String legacyId, final GetAdvertiseCallback callback) {

        return networkService.getAdvertise(legacyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends AdvertiseApi>>() {
                    @Override
                    public Observable<? extends AdvertiseApi> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<AdvertiseApi>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(AdvertiseApi advertise) {
                        callback.onSuccess(advertise);

                    }
                });
    }

    public Subscription getAdvertise(int id, final GetAdvertiseCallback callback) {

        return networkService.getAdvertise(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends AdvertiseApi>>() {
                    @Override
                    public Observable<? extends AdvertiseApi> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<AdvertiseApi>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(AdvertiseApi advertise) {
                        callback.onSuccess(advertise);

                    }
                });
    }

    public interface GetAdvertiseCallback {
        void onSuccess(AdvertiseApi advertiseApi);

        void onError(NetworkError networkError);
    }


    /*BANNER*/
    public Subscription getListBanner(Map<String, String> params, final GetBannerListCallBack callback) {

        return networkService.getListBanner(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Banner>>>() {
                    @Override
                    public Observable<? extends List<Banner>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<List<Banner>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(List<Banner> banners) {
                        callback.onSuccess(banners);

                    }
                });
    }

    public interface GetBannerListCallBack {
        void onSuccess(List<Banner> banners);

        void onError(NetworkError networkError);
    }

    /*FLAG CHOICE*/
    public Subscription getListFlagChoice(final GetFlagChoiceListCallBack callback) {

        return networkService.getListFlagChoice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends FlagChoiceResponse>>() {
                    @Override
                    public Observable<? extends FlagChoiceResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<FlagChoiceResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(FlagChoiceResponse flagChoiceResponse) {
                        callback.onSuccess(flagChoiceResponse);

                    }
                });
    }

    public interface GetFlagChoiceListCallBack {
        void onSuccess(FlagChoiceResponse flagChoiceResponse);

        void onError(NetworkError networkError);
    }
}
