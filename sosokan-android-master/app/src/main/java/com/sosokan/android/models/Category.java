package com.sosokan.android.models;

import java.util.Map;

/**
 * Created by AnhZin on 8/21/2016.
 */
public class Category {
    public Category() {
    }

    public Category(String name, int advertiseCount, IconCategory icons, String categoryId, String parentId) {
        this.name = name;
        this.advertiseCount = advertiseCount;
        this.icons = icons;
        this.id = categoryId;
        this.parentId = parentId;
    }

    public Category(String id, long createdDate,
                    long updatedDate,
                    String parentId, String name, String nameChinese, int advertiseCount, IconCategory icons, int sort) {
        this.id = id;
        this.createdAt = createdDate;
        this.updatedAt = updatedDate;
        this.parentId = parentId;
        this.name = name;
        this.nameChinese = nameChinese;
        this.advertiseCount = advertiseCount;
        this.sort = sort;
        this.icons = icons;
    }

    public String id;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String categoryId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }

    public String createdId;
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
    public String parentId;
    public String name;
    public String nameChinese;
    public int advertiseCount;
    public int sort;
    //public ObjectPicture icon;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAdvertiseCount() {
        return advertiseCount;
    }

    public void setAdvertiseCount(int advertiseCount) {
        this.advertiseCount = advertiseCount;
    }

   /* public ObjectPicture getIcon() {
        return icon;
    }

    public void setIcon(ObjectPicture icon) {
        this.icon = icon;
    }*/

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getNameChinese() {
        return nameChinese;
    }

    public void setNameChinese(String nameChinese) {
        this.nameChinese = nameChinese;
    }

    public int getSubscriptionCount() {
        return subscriptionCount;
    }

    public void setSubscriptionCount(int subscriptionCount) {
        this.subscriptionCount = subscriptionCount;
    }


    public int subscriptionCount;

    public Map<String, Object> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Map<String, Object> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Map<String, Object> subscriptions;

    /* public Map<String, Object> getAdvertises() {
         return advertises;
     }

     public void setAdvertises(Map<String, Object> advertises) {
         this.advertises = advertises;
     }

     public Map<String,Object> advertises;*/
    public Map<String, Object> advertisesEnglish;

    public Map<String, Object> getAdvertisesChinese() {
        return advertisesChinese;
    }

    public void setAdvertisesChinese(Map<String, Object> advertisesChinese) {
        this.advertisesChinese = advertisesChinese;
    }

    public Map<String, Object> getAdvertisesEnglish() {
        return advertisesEnglish;
    }

    public void setAdvertisesEnglish(Map<String, Object> advertisesEnglish) {
        this.advertisesEnglish = advertisesEnglish;
    }

    public Map<String, Object> advertisesChinese;

    public Map<String, Object> getAdvertisesExpired() {
        return advertisesExpired;
    }

    public void setAdvertisesExpired(Map<String, Object> advertisesExpired) {
        this.advertisesExpired = advertisesExpired;
    }

    public Map<String, Object> advertisesExpired;

    public int getPopular() {
        return popular;
    }

    public void setPopular(int popular) {
        this.popular = popular;
    }

    public int popular;
    public Double getDescendingTime() {
        return descendingTime;
    }

    public void setDescendingTime(Double descendingTime) {
        this.descendingTime = descendingTime;
    }

    public Double descendingTime;

    public int getDeepLevel() {
        return deepLevel;
    }

    public void setDeepLevel(int deepLevel) {
        this.deepLevel = deepLevel;
    }

    public int deepLevel;

    public IconCategory getIcons() {
        return icons;
    }

    public void setIcons(IconCategory icons) {
        this.icons = icons;
    }

    public IconCategory icons;

}
