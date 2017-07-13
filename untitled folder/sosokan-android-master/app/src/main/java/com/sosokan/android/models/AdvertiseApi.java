package com.sosokan.android.models;

import java.util.List;

/**
 * Created by AnhZin on 12/18/2016.
 */

public class AdvertiseApi {


    public AdvertiseApi() {
    }

    private int id;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private List<CommentNew> comments;
    private String userId;
    private String description;
    private String categoryId;
    private String name;

    private boolean isChinese;
    private String price;
    private int favoriteCount;
    private boolean isFeatured;
    private boolean isStandout;
    private String legacy_id;
    private String created_on;

    private String language;
    private boolean feature;
    private String location;
    private int[] favorite;
    private String video;
    private String email;
    private boolean isEnableEmail;
    private String phone;
    private boolean isEnablePhone;
    private String fax;
    private String website;
    private String address;
    private int saleOff;
    private int shareCount;
    private String user;
    private double createdAt;
    private double updatedAt;
    private int views;

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    private String platform;
    public double getDescendingTime() {
        return descendingTime;
    }

    public void setDescendingTime(double descendingTime) {
        this.descendingTime = descendingTime;
    }

    public double descendingTime;

    public double getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(double createdAt) {
        this.createdAt = createdAt;
    }

    public double getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(double updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ImageAdvertiseApi> getAdimage() {
        return adimage;
    }

    public void setAdimage(List<ImageAdvertiseApi> adimage) {
        this.adimage = adimage;
    }

    public String getDescriptionPlainText() {
        return descriptionPlainText;
    }

    public void setDescriptionPlainText(String descriptionPlainText) {
        this.descriptionPlainText = descriptionPlainText;
    }

    private String descriptionPlainText;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getSaleOff() {
        return saleOff;
    }

    public void setSaleOff(int saleOff) {
        this.saleOff = saleOff;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getVideo() {

        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnableEmail() {
        return isEnableEmail;
    }

    public void setEnableEmail(boolean enableEmail) {
        isEnableEmail = enableEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnablePhone() {
        return isEnablePhone;
    }

    public void setEnablePhone(boolean enablePhone) {
        isEnablePhone = enablePhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int[] getFavorite() {
        return favorite;
    }

    public void setFavorite(int[] favorite) {
        this.favorite = favorite;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isFeature() {
        return feature;
    }

    public void setFeature(boolean feature) {
        this.feature = feature;
    }

    public ImageHeaderApi getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(ImageHeaderApi imageHeader) {
        this.imageHeader = imageHeader;
    }

    /*private String imageHeader;*/
    public ImageHeaderApi imageHeader;
    public List<ImageAdvertiseApi> adimage;
    public List<CommentNew> getComments() {
        return comments;
    }

    public void setComments(List<CommentNew> comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChinese() {
        return isChinese;
    }

    public void setChinese(boolean chinese) {
        isChinese = chinese;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public boolean isStandout() {
        return isStandout;
    }

    public void setStandout(boolean standout) {
        isStandout = standout;
    }

    public String getLegacy_id() {
        return legacy_id;
    }

    public void setLegacy_id(String legacy_id) {
        this.legacy_id = legacy_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String short_description;

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

}
