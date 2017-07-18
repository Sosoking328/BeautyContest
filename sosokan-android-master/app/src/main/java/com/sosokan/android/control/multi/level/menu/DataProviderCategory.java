package com.sosokan.android.control.multi.level.menu;

import android.content.Context;

import com.sosokan.android.models.Category;
import com.sosokan.android.utils.comparator.CategoryNameComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by AnhZin on 10/3/2016.
 */


public class DataProviderCategory {

    public static List<Category> listCategories;
    public static Map<String, Category> mapCategory;
    public static Map<String, List<String>> mapCategoryChildren;
    // public static Map<String, List<String>> mapCategoryParent;
    public static Context context;
    // private static DatabaseReference ref;

    /**
     * Do not confuse with MultiLevelListView levels.
     * Following variables refer only to data generation process.
     * For instance, if ITEMS_PER_LEVEL = 2 and MAX_LEVELS = 3,
     * list should look like this:
     * + 1
     * | + 1.1
     * | - - 1.1.1
     * | - - 1.1.2
     * | + 1.2
     * | - - 1.2.1
     * | - - 1.2.2
     * | - - 1.2.3
     * + 2
     * | + 2.1
     * | - - 2.1.1
     * | - - 2.1.2
     * | + 2.2
     * | - - 2.2.1
     * | - - 2.2.2
     */
    public static boolean isExpandableCategory(Category baseItem) {
        return baseItem instanceof Category;
    }

    public static List<Category> getSubCategoryItems(String parentId) {
        List<String> categoryIds = mapCategoryChildren.get(parentId);
        List<Category> listCategoryChildren = new ArrayList<>();
        Category categoryParent= mapCategory.get(parentId);
        if (categoryIds != null && categoryIds.size() > 0) {
            for (int i = 0; i < categoryIds.size(); i++) {
                Category category1 = mapCategory.get(categoryIds.get(i));

               // Log.e("isExpandableCategory",category1.getName());
                if (category1 != null) {
                    listCategoryChildren.add(category1);
                }
            }
        }
        // return listCategoryChildren;
        Collections.sort(listCategoryChildren, new CategoryNameComparator());
        return listCategoryChildren;

    }

    public static void initData(Map<String, Category> mapCategoryParent, Map<String, List<String>> mapCategoryChild, List<Category> categories) {
        mapCategory = mapCategoryParent;
        mapCategoryChildren = mapCategoryChild;
        listCategories = categories;
    }
}