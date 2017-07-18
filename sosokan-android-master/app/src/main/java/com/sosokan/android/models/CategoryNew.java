package com.sosokan.android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macintosh on 1/17/17.
 */

public class CategoryNew {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("nameChinese")
    @Expose
    private String nameChinese;

    @SerializedName("legacy_id")
    @Expose
    private String legacy_id;

    @SerializedName("advertiseCount")
    @Expose
    private int advertiseCount;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("count_en")
    @Expose
    private int count_en;

    @SerializedName("count_zh_hans")
    @Expose
    private int count_zh_hans;

    @SerializedName("createdAt")
    @Expose
    private long createdAt;

    @SerializedName("deepLevel")
    @Expose
    private int deepLevel;

    @SerializedName("parentId")
    @Expose
    private String parentId;

    @SerializedName("popular")
    @Expose
    private int popular;

    @SerializedName("sort")
    @Expose
    private int sort;

    @SerializedName("updatedAt")
    @Expose
    private long updatedAt;

    @SerializedName("top_bar")
    @Expose
    private boolean top_bar;

    @SerializedName("lft")
    @Expose
    private int lft;

    @SerializedName("rght")
    @Expose
    private int rght;

    @SerializedName("tree_id")
    @Expose
    private int tree_id;

    @SerializedName("level")
    @Expose
    private int level;

    @SerializedName("parent")
    @Expose
    private String parent;

    @SerializedName("iconChinese")
    @Expose
    private String iconChinese;

    @SerializedName("iconEnglish")
    @Expose
    private String iconEnglish;

    @SerializedName("children")
    @Expose
    private int[] children;

    public int[] getChildren() {
        return children;
    }

    public void setChildren(int[] children) {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public CategoryNew() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNameChinese() {
        return nameChinese;
    }

    public void setNameChinese(String nameChinese) {
        this.nameChinese = nameChinese;
    }

    public String getLegacy_id() {
        return legacy_id;
    }

    public void setLegacy_id(String legacy_id) {
        this.legacy_id = legacy_id;
    }

    public int getAdvertiseCount() {
        return advertiseCount;
    }

    public void setAdvertiseCount(int advertiseCount) {
        this.advertiseCount = advertiseCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getDeepLevel() {
        return deepLevel;
    }

    public void setDeepLevel(int deepLevel) {
        this.deepLevel = deepLevel;
    }

    public int getPopular() {
        return popular;
    }

    public void setPopular(int popular) {
        this.popular = popular;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isTop_bar() {
        return top_bar;
    }

    public void setTop_bar(boolean top_bar) {
        this.top_bar = top_bar;
    }

    public int getLft() {
        return lft;
    }

    public void setLft(int lft) {
        this.lft = lft;
    }

    public int getRght() {
        return rght;
    }

    public void setRght(int rght) {
        this.rght = rght;
    }

    public int getTree_id() {
        return tree_id;
    }

    public void setTree_id(int tree_id) {
        this.tree_id = tree_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getIconChinese() {
        return iconChinese;
    }

    public void setIconChinese(String iconChinese) {
        this.iconChinese = iconChinese;
    }

    public String getIconEnglish() {
        return iconEnglish;
    }

    public void setIconEnglish(String iconEnglish) {
        this.iconEnglish = iconEnglish;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
