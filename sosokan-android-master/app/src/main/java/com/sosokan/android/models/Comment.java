package com.sosokan.android.models;

import android.support.v7.widget.RecyclerView;

import java.io.Serializable;

/**
 * Created by AnhZin on 8/25/2016.
 */
public class Comment  {
    private String name;
    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public long createdAt;
    public String updatedAt;
    private String content;
    private String userId;

    public Comment(String name, long createdDate, String content, String userId, String id, String advertiseId) {
        this.name = name;
        this.createdAt = createdDate;
        this.content = content;
        this.userId = userId;
        this.id = id;
        this.advertiseId = advertiseId;
    }

    public Comment() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(String advertiseId) {
        this.advertiseId = advertiseId;
    }

    private String id; // this is id of comment
    private String advertiseId;// this is id post detail


}
