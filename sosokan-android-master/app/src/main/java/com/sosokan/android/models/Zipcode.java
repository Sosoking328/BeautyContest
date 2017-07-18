package com.sosokan.android.models;

/**
 * Created by macintosh on 1/17/17.
 */

public class Zipcode {
    private String zipcode;
    private Double latitude;
    private Double longitude;
    private String city;
    private String state;

    public Zipcode(String zipcode, Double latitude, Double longitude, String city, String state) {
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
