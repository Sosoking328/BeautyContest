package com.sosokan.android.models;

/**
 * Created by macintosh on 1/17/17.
 */

public class AdvertiseImage {
    private String id;
    private String ad;
    private String image;

    public AdvertiseImage(String id, String ad, String image) {
        this.id = id;
        this.ad = ad;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
