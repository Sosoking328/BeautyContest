package com.sosokan.android.utils.comparator;

import android.annotation.SuppressLint;

import com.sosokan.android.models.CategoryNew;

import java.util.Comparator;

/**
 * Created by macintosh on 2/21/17.
 */

public class CategoryApiSortComparator implements Comparator<CategoryNew> {
    @SuppressLint("NewApi")
    public int compare(CategoryNew s1, CategoryNew s2) {
        return Integer.compare(s1.getSort(), s2.getSort());
    }
}
