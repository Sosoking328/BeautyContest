package com.sosokan.android.models;

/**
 * Created by AnhZin on 10/20/2016.
 */

public class AdvertiseMeta {
    public double descendingTime;
    public String advertiseId;

    public double getAscendingTime() {
        return ascendingTime;
    }

    public void setAscendingTime(double ascendingTime) {
        this.ascendingTime = ascendingTime;
    }

    public double ascendingTime;
    public AdvertiseMeta() {
    }

    public String getStringData() {
        return stringData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }

    public double getDescendingTime() {
        return descendingTime;
    }


    public void setDescendingTime(double descendingTime) {
        this.descendingTime = descendingTime;
    }

    public String getAdvertiseId() {
        return advertiseId;
    }

    public void setAdvertiseId(String advertiseId) {
        this.advertiseId = advertiseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;
    public String stringData;

}
