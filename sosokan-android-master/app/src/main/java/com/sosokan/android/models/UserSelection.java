package com.sosokan.android.models;

/**
 * Created by macintosh on 3/27/17.
 */

public class UserSelection {
    private City city;
    //private State state;
    private CategoryNew category;
    private String categorySelectedId;
    private String textSearch;
    private int distanceMax;
    private int distanceMin;
    private int priceMax;
    private int priceMin;
    private int dayMax;
    private int dayMin;

    private boolean isAllowDistance;
    private boolean isAllPrice;
    private boolean isAllDay;

    public String getTextSearch() {
        return textSearch;
    }

    public void setTextSearch(String textSearch) {
        this.textSearch = textSearch;
    }

    public UserSelection() {
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public CategoryNew getCategory() {
        return category;
    }

    public void setCategory(CategoryNew category) {
        this.category = category;
    }

    public String getCategorySelectedId() {
        return categorySelectedId;
    }

    public void setCategorySelectedId(String categorySelectedId) {
        this.categorySelectedId = categorySelectedId;
    }

    public int getDistanceMax() {
        return distanceMax;
    }

    public void setDistanceMax(int distanceMax) {
        this.distanceMax = distanceMax;
    }

    public int getDistanceMin() {
        return distanceMin;
    }

    public void setDistanceMin(int distanceMin) {
        this.distanceMin = distanceMin;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public int getDayMax() {
        return dayMax;
    }

    public void setDayMax(int dayMax) {
        this.dayMax = dayMax;
    }

    public int getDayMin() {
        return dayMin;
    }

    public void setDayMin(int dayMin) {
        this.dayMin = dayMin;
    }

    public boolean isAllowDistance() {
        return isAllowDistance;
    }

    public void setAllowDistance(boolean allowDistance) {
        isAllowDistance = allowDistance;
    }

    public boolean isAllPrice() {
        return isAllPrice;
    }

    public void setAllPrice(boolean allPrice) {
        isAllPrice = allPrice;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }
}
