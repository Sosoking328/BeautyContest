package com.sosokan.android.mvp.splash;

import com.sosokan.android.models.UserApi;
import com.sosokan.android.models.VideoSplash;

import java.util.List;

/**
 * Created by macintosh on 3/16/17.
 */

public interface SplashView {
    void onFailure(String appErrorMessage);

    void getListVideoSplashSuccess(List<VideoSplash> videoSplash);
}
