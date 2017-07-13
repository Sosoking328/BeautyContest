package com.sosokan.android.models;

import android.location.Location;

import java.util.Map;

/**
 * Created by AnhZin on 8/21/2016.
 */
public class Advertise {
    public Advertise() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLongtitude() {
        return longtitude;
    }


    /*public Location location;*/
    public double latitude; // latitude
    public String phone;
    public String fax;
    public String email;

    public boolean chinese;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String categoryId;

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

    public String id;

    public String getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(String advertiseId) {
        this.advertiseId = advertiseId;
    }

    public String advertiseId;
    public String userId;

    public int favoriteCount;
    public String name;
//    public int saleOff;


    public String currency;
    public String address;

    public String availability;

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String coupon;
    public double price;
    public String expiredDate;
    public boolean hidden;
    public Image imageHeader;
    public Video video;
    public boolean isChinese;
    public String type;
    public int shareCount;
    public int flagCount;

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String expiredAt;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String website;
    public void setHtmlDes(boolean htmlDes) {
        isHtmlDes = htmlDes;
    }

    public boolean isHtmlDes;
    public boolean isFeature;
    public String htmlDescription;

    public double getLongitude() {
        return longtitude;
    }

    public void setLongitude(double longitude) {
        this.longtitude = longitude;
    }

    public double longtitude;

    /*public double getLongtitude() {
        return longtitude;
    }*/

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

  //  public double longtitude;

    public LocationSosokan getLocation() {
        return location;
    }

    public void setLocation(LocationSosokan location) {
        this.location = location;
    }

    public LocationSosokan location;
    public String getDescriptionPlainText() {
        return descriptionPlainText;
    }

    public void setDescriptionPlainText(String descriptionPlainText) {
        this.descriptionPlainText = descriptionPlainText;
    }


    public String descriptionPlainText;
    public boolean isStandout;
    public boolean enableEmail;
    public boolean enablePhone;

    public boolean isPremiumDesc() {
        return isPremiumDesc;
    }

    public void setPremiumDesc(boolean premiumDesc) {
        isPremiumDesc = premiumDesc;
    }

    public boolean   isPremiumDesc;
    public Map<String, Object> images;
    public Map<String, Object> videos;

    public Map<String, Object> getConversations() {
        return conversations;
    }

    public void setConversations(Map<String, Object> conversations) {
        this.conversations = conversations;
    }

    public Map<String, Object> conversations;


    public  String description;

    public Map<String, Object> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Object> categories) {
        this.categories = categories;
    }

    public Map<String, Object> categories;

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public int getSaleOff() {
        return saleOff;
    }

    public void setSaleOff(int saleOff) {
        this.saleOff = saleOff;
    }
*/
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Image getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(Image imageHeader) {
        this.imageHeader = imageHeader;
    }


    public boolean isChinese() {
        return isChinese;
    }

    public void setChinese(boolean chinese) {
        isChinese = chinese;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(int flagCount) {
        this.flagCount = flagCount;
    }

    public boolean isFeature() {
        return isFeature;
    }

    public void setFeature(boolean feature) {
        isFeature = feature;
    }

    public String getHtmlDescription() {
        return htmlDescription;
    }

    public void setHtmlDescription(String htmlDescription) {
        this.htmlDescription = htmlDescription;
    }

    public boolean isStandout() {
        return isStandout;
    }

    public boolean isHtmlDes() {
        return isHtmlDes;
    }

    public void setStandout(boolean standout) {
        isStandout = standout;
    }

    public boolean isEnableEmail() {
        return enableEmail;
    }

    public void setEnableEmail(boolean enableEmail) {
        this.enableEmail = enableEmail;
    }

    public boolean isEnablePhone() {
        return enablePhone;
    }

    public void setEnablePhone(boolean enablePhone) {
        this.enablePhone = enablePhone;
    }


    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Map<String, Object> getImages() {
        return images;
    }

    public void setImages(Map<String, Object> images) {
        this.images = images;
    }

    public Map<String, Object> getVideos() {
        return videos;
    }

    public void setVideos(Map<String, Object> videos) {
        this.videos = videos;
    }

    public Double getDescendingTime() {
        return descendingTime;
    }

    public void setDescendingTime(Double descendingTime) {
        this.descendingTime = descendingTime;
    }
    public Map<String, Object>  favoritedUsers;

    public Map<String, Object> getFavoritedUsers() {
        return favoritedUsers;
    }

    public void setFavoritedUsers(Map<String, Object> favoritedUsers) {
        this.favoritedUsers = favoritedUsers;
    }

    public Double descendingTime;
}
