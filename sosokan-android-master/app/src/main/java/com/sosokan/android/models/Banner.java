package com.sosokan.android.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AnhZin on 11/26/2016.
 */

public class Banner {

    public Banner() {
    }

    public Banner(String category, String categoryId, String language, String image, String url, String address, double latitude, double longitude, String created) {
        this.category = category;
        this.categoryId = categoryId;
        this.language = language;
        this.image = image;
        this.url = url;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created = created;
    }

    @SerializedName("category")
    public String category;

    @SerializedName("categoryId")
    public String categoryId;

    @SerializedName("language")
    public String language;

    @SerializedName("image")
    public String image;

    @SerializedName("url")
    public String url;

    @SerializedName("address")
    public String address;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude; // latitude

    @SerializedName("created")
    public String created;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}

