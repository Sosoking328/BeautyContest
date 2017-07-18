package com.sosokan.android.utils.comparator;

import android.annotation.TargetApi;
import android.os.Build;

import com.sosokan.android.models.Category;

import java.util.Comparator;

/**
 * Created by AnhZin on 11/9/2016.
 */

public class CategoryPopularComparator  implements Comparator<Category> {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public int compare(Category s1, Category s2) {
        return Integer.compare(s1.getPopular(), s2.getPopular());
    }
}
