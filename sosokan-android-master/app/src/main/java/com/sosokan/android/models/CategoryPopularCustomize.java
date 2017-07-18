package com.sosokan.android.models;

/**
 * Created by macintosh on 3/25/17.
 */

public class CategoryPopularCustomize {
    private CategoryNew category;
    private boolean isSelected;

    public CategoryPopularCustomize() {
    }

    public CategoryNew getCategory() {
        return category;
    }

    public void setCategory(CategoryNew category) {
        this.category = category;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
