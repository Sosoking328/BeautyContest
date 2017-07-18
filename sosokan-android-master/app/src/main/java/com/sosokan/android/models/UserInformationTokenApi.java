package com.sosokan.android.models;

/**
 * Created by macintosh on 2/10/17.
 */

public class UserInformationTokenApi {
    public String csrftoken;
    public String country;
    public String username;
    public String id;
    public String status;
    public int user_id;
    public String dialing_code;
    public String referer;
    public int geolocation;
    public String key;
    public String service;
    public String phone;
    public String token;

    public UserInformationTokenApi() {
    }

    public String getCsrftoken() {
        return csrftoken;
    }

    public void setCsrftoken(String csrftoken) {
        this.csrftoken = csrftoken;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDialing_code() {
        return dialing_code;
    }

    public void setDialing_code(String dialing_code) {
        this.dialing_code = dialing_code;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public int getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(int geolocation) {
        this.geolocation = geolocation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
