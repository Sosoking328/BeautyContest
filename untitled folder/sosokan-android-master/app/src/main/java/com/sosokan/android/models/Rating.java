package com.sosokan.android.models;

/**
 * Created by AnhZin on 10/15/2016.
 */

public class Rating {
    public Rating() {
    }

    public String id;
    public String userId;

    public String getUserRatedId() {
        return userRatedId;
    }

    public void setUserRatedId(String userRatedId) {
        this.userRatedId = userRatedId;
    }

    public String userRatedId;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
    public long createdAt;
    public long updatedAt;
    public double rating;
}
