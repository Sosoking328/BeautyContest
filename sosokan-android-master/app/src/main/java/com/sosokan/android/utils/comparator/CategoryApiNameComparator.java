package com.sosokan.android.utils.comparator;

import com.sosokan.android.models.CategoryNew;

import java.util.Comparator;

/**
 * Created by macintosh on 2/21/17.
 */

public class CategoryApiNameComparator implements Comparator<CategoryNew> {
    public int compare(CategoryNew s1, CategoryNew s2) {

        String StudentName1 = s1.getName().toUpperCase();
        String StudentName2 = s2.getName().toUpperCase();

        //ascending order
        return StudentName1.compareTo(StudentName2);
    }
}
