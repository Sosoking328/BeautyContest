package com.sosokan.android.models;

/**
 * Created by macintosh on 1/17/17.
 */

public class Flag {
    private String id;
    private String ad;
    private String user;
    private String reason;

    public Flag() {
    }

    public Flag(String id, String ad, String user, String reason) {
        this.id = id;
        this.ad = ad;
        this.user = user;
        this.reason = reason;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
