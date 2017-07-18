package com.sosokan.android.models;

/**
 * Created by AnhZin on 9/22/2016.
 */
public class Favorite{
    public String id;
    public String userId;
    private String advertiseId;


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

    public long createdAt;
    public long updatedAt;

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

    public String getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(String advertiseId) {
        this.advertiseId = advertiseId;
    }


    public Favorite() {
    }

    public Favorite(String id, String userId, String advertiseId, long createdDate, long updatedDate, boolean isFavorite) {
        this.id = id;
        this.userId = userId;
        this.advertiseId = advertiseId;
        this.createdAt = createdDate;
        this.updatedAt = updatedDate;
        this.isFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isFavorite;
}
