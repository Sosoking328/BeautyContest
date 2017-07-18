package com.sosokan.android.models;

/**
 * Created by macintosh on 3/17/17.
 */

public class ContentType {
    private int id;
    private String url;
    private String app_label;
    private String model;

    public ContentType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApp_label() {
        return app_label;
    }

    public void setApp_label(String app_label) {
        this.app_label = app_label;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
