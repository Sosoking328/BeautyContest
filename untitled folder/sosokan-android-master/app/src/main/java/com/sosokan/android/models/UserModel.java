package com.sosokan.android.models;

/**
 * Created by AnhZin on 9/4/2016.
 */
public class UserModel {

    private String userId;
    private String name;
    private String photoProfile;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoProfile() {
        return photoProfile;
    }

    public void setPhotoProfile(String photoProfile) {
        this.photoProfile = photoProfile;
    }

    public UserModel() {
    }

    public UserModel(String name, String photoProfile, String idUser) {
        this.name = name;
        this.photoProfile = photoProfile;
        this.userId = idUser;
    }

}