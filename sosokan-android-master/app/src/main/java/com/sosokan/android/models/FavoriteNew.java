package com.sosokan.android.models;

/**
 * Created by macintosh on 1/17/17.
 */

public class FavoriteNew {
    private String ad;
    private String user;

    public FavoriteNew(String ad, String user) {
        this.ad = ad;
        this.user = user;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
