package com.sosokan.android.models;

/**
 * Created by AnhZin on 10/2/2016.
 */

public class ObjectAuthFaceBook {
    public ObjectAuthFaceBook() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String accessToken;

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String expirationDate;
    public String id;
}
