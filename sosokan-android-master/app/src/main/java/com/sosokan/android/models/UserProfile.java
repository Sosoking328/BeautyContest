package com.sosokan.android.models;

/**
 * Created by macintosh on 1/17/17.
 */

public class UserProfile {
    public String user;
    public String phoneNumber;
    public String UUID;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public UserProfile(String user, String phoneNumber, String UUID) {
        this.user = user;
        this.phoneNumber = phoneNumber;
        this.UUID = UUID;
    }
}
