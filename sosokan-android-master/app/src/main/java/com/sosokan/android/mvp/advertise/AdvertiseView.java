package com.sosokan.android.mvp.advertise;

import com.sosokan.android.models.AdvertiseApi;
import com.sosokan.android.models.VideoSplash;

import java.util.List;

/**
 * Created by macintosh on 3/16/17.
 */

public interface AdvertiseView {
    void onAdvertiseFailure(String appErrorMessage);

    void getListAdvertiseSuccess(AdvertiseResponse advertiseResponse);

    void getAdvertiseSuccess(AdvertiseApi advertiseApi);
}
