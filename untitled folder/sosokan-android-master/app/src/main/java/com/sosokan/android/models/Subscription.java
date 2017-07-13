package com.sosokan.android.models;

/**
 * Created by AnhZin on 9/24/2016.
 */
public class Subscription {

    public Subscription() {
    }

    private String id;
    private String userId;

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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    private String categoryId;

    public Subscription(String id, String userId, String categoryId, long createdDate, long updatedDate) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.createdAt = createdDate;
        this.updatedAt = updatedDate;
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
    public long createdAt;
    public long updatedAt;
}
