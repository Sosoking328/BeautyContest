package com.sosokan.android.models;

/**
 * Created by AnhZin on 9/24/2016.
 */
public class Image {

    public String imageId;
    public String id;
    public long createdAt;
    public long updatedAt;
    public int width;
    public double size;
    public boolean isVideoThumb;
    public double descendingTime;
    public boolean   isStoredInStorage;
    public boolean status;
    public String userId;
    public String isUp;
    public String imageUrl;
    public String name;
    public String advertiseId;
    public int height;
    public double getDescendingTime() {
        return descendingTime;
    }

    public void setDescendingTime(double descendingTime) {
        this.descendingTime = descendingTime;
    }


    public boolean isStoredInStorage() {
        return isStoredInStorage;
    }

    public void setStoredInStorage(boolean storedInStorage) {
        isStoredInStorage = storedInStorage;
    }


    public Image() {
    }

    public String getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(String advertiseId) {
        this.advertiseId = advertiseId;
    }

    public String getIsUp() {
        return isUp;
    }

    public void setIsUp(String isUp) {
        this.isUp = isUp;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isVideoThumb() {
        return isVideoThumb;
    }

    public void setVideoThumb(boolean videoThumb) {
        isVideoThumb = videoThumb;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

}
