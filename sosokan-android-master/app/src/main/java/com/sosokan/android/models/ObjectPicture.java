package com.sosokan.android.models;

/**
 * Created by AnhZin on 10/2/2016.
 */

public class ObjectPicture {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ObjectPicture() {
    }


    public String getAddFrom() {
        return addFrom;
    }

    public void setAddFrom(String addFrom) {
        this.addFrom = addFrom;
    }

    private String addFrom;
    private String name;
    private String nameChinese;
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;
    private String imageUrlChinese;

    public String getNameChinese() {
        return nameChinese;
    }

    public void setNameChinese(String nameChinese) {
        this.nameChinese = nameChinese;
    }

    public String getImageUrlChinese() {
        return imageUrlChinese;
    }

    public void setImageUrlChinese(String imageUrlChinese) {
        this.imageUrlChinese = imageUrlChinese;
    }
}
