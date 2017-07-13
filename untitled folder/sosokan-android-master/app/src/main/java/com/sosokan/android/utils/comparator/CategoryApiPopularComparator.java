package com.sosokan.android.utils.comparator;

import android.annotation.TargetApi;
import android.os.Build;

import com.sosokan.android.models.CategoryNew;

import java.util.Comparator;

/**
 * Created by macintosh on 2/21/17.
 */

public class CategoryApiPopularComparator implements Comparator<CategoryNew> {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public int compare(CategoryNew s1, CategoryNew s2) {
        return Integer.compare(s1.getPopular(), s2.getPopular());
    }
}
