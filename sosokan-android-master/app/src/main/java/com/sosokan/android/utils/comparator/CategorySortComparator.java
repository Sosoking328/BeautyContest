package com.sosokan.android.utils.comparator;

import android.annotation.SuppressLint;

import com.sosokan.android.models.Category;

import java.util.Comparator;

/**
 * Created by AnhZin on 11/9/2016.
 */

public class CategorySortComparator  implements Comparator<Category> {
    @SuppressLint("NewApi")
    public int compare(Category s1, Category s2) {
        return Integer.compare(s1.getSort(), s2.getSort());
    }
}
