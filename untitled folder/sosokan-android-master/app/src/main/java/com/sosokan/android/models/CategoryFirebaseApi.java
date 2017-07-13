package com.sosokan.android.models;

/**
 * Created by macintosh on 3/13/17.
 */

public class CategoryFirebaseApi {
    private Category category;
    private int id;
    private String name;
    private String nameChinese;
    private String url;
    private int count_en;
    private int count_zh_hans;
    private int deepLevel;
    private int sort;
    private int popular;
    private String iconEnglish;
    private String iconChinese;
    private boolean hasChild;

    public CategoryFirebaseApi() {
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameChinese() {
        return nameChinese;
    }

    public void setNameChinese(String nameChinese) {
        this.nameChinese = nameChinese;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCount_en() {
        return count_en;
    }

    public void setCount_en(int count_en) {
        this.count_en = count_en;
    }

    public int getCount_zh_hans() {
        return count_zh_hans;
    }

    public void setCount_zh_hans(int count_zh_hans) {
        this.count_zh_hans = count_zh_hans;
    }

    public int getDeepLevel() {
        return deepLevel;
    }

    public void setDeepLevel(int deepLevel) {
        this.deepLevel = deepLevel;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getPopular() {
        return popular;
    }

    public void setPopular(int popular) {
        this.popular = popular;
    }

    public String getIconEnglish() {
        return iconEnglish;
    }

    public void setIconEnglish(String iconEnglish) {
        this.iconEnglish = iconEnglish;
    }

    public String getIconChinese() {
        return iconChinese;
    }

    public void setIconChinese(String iconChinese) {
        this.iconChinese = iconChinese;
    }
}
