package com.sosokan.android.models;


public class MenuItem {
    public static final int PROFILE = 0;
    public static final int HOME = 1;
    public static final int MESSAGE = 2;
    public static final int MY_POST = 3;
    public static final int FAVORITE = 4;
    public static final int SUBSCIPTION = 5;
    public static final int SETTING = 6;
    public static final int LOGOUT = 7;

    private int mDrawableIcon;
    private int mTitle;
    private int mId;


    public MenuItem(int id, int title, int drawable) {
        this.mTitle = title;
        this.mId = id;
        this.mDrawableIcon = drawable;
    }

    public int getId() {
        return mId;
    }

    public int getIcon() {
        return this.mDrawableIcon;
    }

    public int getTitle() {
        return this.mTitle;
    }
}
