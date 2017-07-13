package com.sosokan.android.mvp.user;

import com.sosokan.android.models.UserApi;
import com.sosokan.android.mvp.userProfile.UserProfileResponse;

/**
 * Created by macintosh on 3/16/17.
 */

public interface UserView {
    void onFailure(String appErrorMessage);

    void getUserSuccess(UserApi userApi);
}
