package com.sosokan.android.mvp.banner;

import com.sosokan.android.models.Banner;

import java.util.List;

/**
 * Created by macintosh on 3/17/17.
 */

public interface BannerView {

    void onBannerFailure(String appErrorMessage);

    void getBannerListSuccess(List<Banner> banners);
}
