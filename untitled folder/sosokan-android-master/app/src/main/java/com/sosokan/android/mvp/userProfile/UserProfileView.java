package com.sosokan.android.mvp.userProfile;

import com.sosokan.android.models.CategoryNew;

import java.util.List;

/**
 * Created by macintosh on 3/16/17.
 */

public interface UserProfileView {

    void onUserProfileFailure(String appErrorMessage);

    void getUserProfileSuccess(UserProfileResponse userProfileResponse);
}
