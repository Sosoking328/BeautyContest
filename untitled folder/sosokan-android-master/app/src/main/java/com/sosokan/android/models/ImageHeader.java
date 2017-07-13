package com.sosokan.android.models;

/**
 * Created by macintosh on 1/22/17.
 */

public class ImageHeader {
    /*"url": "https://sosokan-1452b.appspot.com.storage.googleapis.com/CACHE/images/images/Screen_Shot_2017-01-21_at_11.25.13_AM/aced9f14f9abc5e3fca80504c8a68de6.jpg",
            "width": "300",
            "height": "208"*/
    private String url;
    private String width;
    private String height;
    private String location;
    private String saleoff;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSaleoff() {
        return saleoff;
    }

    public void setSaleoff(String saleoff) {
        this.saleoff = saleoff;
    }

    public ImageHeader() {
    }

    public ImageHeader(String url, String width, String height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
