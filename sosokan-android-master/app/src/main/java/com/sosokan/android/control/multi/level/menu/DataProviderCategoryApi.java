package com.sosokan.android.control.multi.level.menu;

import android.content.Context;

import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.utils.comparator.CategoryApiNameComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by macintosh on 2/21/17.
 */

public class DataProviderCategoryApi {
    public static List<CategoryNew> listCategories;
    public static Map<String, CategoryNew> mapCategory;
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
    public static boolean isExpandableCategory(CategoryNew baseItem) {
        return baseItem instanceof CategoryNew;
    }

    public static List<CategoryNew> getSubCategoryItems(String parentId) {
        List<String> categoryIds = mapCategoryChildren.get(parentId);
        List<CategoryNew> listCategoryChildren = new ArrayList<>();
      //  CategoryNew categoryParent= mapCategory.get(parentId);
        if (categoryIds != null && categoryIds.size() > 0) {
            for (int i = 0; i < categoryIds.size(); i++) {
                CategoryNew category1 = mapCategory.get(categoryIds.get(i));

                // Log.e("isExpandableCategory",category1.getName());
                if (category1 != null) {
                    listCategoryChildren.add(category1);
                }
            }
        }
        // return listCategoryChildren;
        Collections.sort(listCategoryChildren, new CategoryApiNameComparator());
        return listCategoryChildren;

    }

    public static void initData(Map<String, CategoryNew> mapCategoryParent, Map<String, List<String>> mapCategoryChild, List<CategoryNew> categories) {
        mapCategory = mapCategoryParent;
        mapCategoryChildren = mapCategoryChild;
        listCategories = categories;
    }
}
