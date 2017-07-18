package com.sosokan.android.utils.comparator;

import com.sosokan.android.models.Category;

import java.util.Comparator;

/**
 * Created by AnhZin on 11/9/2016.
 */

public class CategoryNameComparator  implements Comparator<Category> {
    public int compare(Category s1, Category s2) {

        String StudentName1 = s1.getName().toUpperCase();
        String StudentName2 = s2.getName().toUpperCase();

        //ascending order
        return StudentName1.compareTo(StudentName2);
    }
}
