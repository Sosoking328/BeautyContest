package com.sosokan.android.deps;

import com.sosokan.android.networking.NetworkModule;
import com.sosokan.android.ui.activity.AdvertiseDetailApiActivity;
import com.sosokan.android.ui.activity.EditAdvertiseActivity;
import com.sosokan.android.ui.activity.EditProfileActivity;
import com.sosokan.android.ui.activity.HomeActivity;
import com.sosokan.android.ui.activity.MenuActivity;
import com.sosokan.android.ui.activity.NewAdvertiseActivity;
import com.sosokan.android.ui.activity.SplashActivity;
import com.sosokan.android.ui.activity.VideoImageSplashActivity;
import com.sosokan.android.ui.fragment.HomeFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by macintosh on 3/16/17.
 */
@Singleton
@Component(modules = {NetworkModule.class,})
public interface Deps {
    void injectMenu(MenuActivity menuActivity);
    void injectHome(HomeFragment homeFragment);
    void injectSplash(SplashActivity splashActivity);
    void injectAdvertiseApiDetail(AdvertiseDetailApiActivity advertiseDetailApiActivity);
    void injectAdvertiseEdit(EditAdvertiseActivity editAdvertiseActivity);
    void injectAdvertiseNew(NewAdvertiseActivity newAdvertiseActivity);
    void injectVideoSplash(VideoImageSplashActivity videoImageSplashActivity);
    void injectEditProfile(EditProfileActivity editProfileActivity);
    void injectHomeActivity(HomeActivity homeActivity);
}
