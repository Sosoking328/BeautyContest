package com.sosokan.android.mvp.flagchoice;

import com.sosokan.android.mvp.userProfile.UserProfileResponse;

/**
 * Created by macintosh on 3/16/17.
 */

public interface FlagChoiceView {
    void onFlagChoiceFailure(String appErrorMessage);

    void getFlagchoiceSuccess(FlagChoiceResponse flagChoiceResponse);
}
