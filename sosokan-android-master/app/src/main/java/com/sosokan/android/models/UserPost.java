package com.sosokan.android.models;

/**
 * Created by AnhZin on 9/24/2016.
 */
public class UserPost {
   public String name;
    public String id;
    public String userId;

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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserPost() {
    }

    public UserPost(String id, String userId, String advertiseId, long createdDate, long updatedDate, String name, int favoriteCount, String description) {
        this.id = id;
        this.userId = userId;
        this.advertiseId = advertiseId;
        this.createdAt = createdDate;
        this.updatedAt = updatedDate;
        this.name = name;
        this.favoriteCount = favoriteCount;
        this.description = description;
    }

    public int favoriteCount;
    public String description;
}
