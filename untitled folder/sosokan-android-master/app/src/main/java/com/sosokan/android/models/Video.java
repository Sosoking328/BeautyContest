package com.sosokan.android.models;

/**
 * Created by AnhZin on 9/24/2016.
 */
public class Video {
    public String id;
    public String name;
    public String userId;
    public String videoURL;

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public Image getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(Image videoImage) {
        this.videoImage = videoImage;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Image videoImage;
    public long createdAt;
    public long updatedAt;
    public boolean status;
    private double size;
    public String ownerId;
    public double getDescendingTime() {
        return descendingTime;
    }

    public void setDescendingTime(double descendingTime) {
        this.descendingTime = descendingTime;
    }

    public double descendingTime;
    public Video() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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



    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }


}
