package com.sosokan.android.models;

/**
 * Created by AnhZin on 9/4/2016.
 */
public class FileModel {

    private String type;
    private String fileUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FileModel(String type, String fileUrl, String name, double size) {
        this.type = type;
        this.fileUrl = fileUrl;
        this.name = name;
        this.size = size;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    private String name;
    private double size;
    private int height;

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

    private int width;
    public FileModel() {
    }


}
