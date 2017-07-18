package com.sosokan.android.mvp.category;


import com.sosokan.android.models.CategoryNew;

import java.util.List;

/**
 * Created by macintosh on 3/15/17.
 */

public interface CategoryView {

    void onFailure(String appErrorMessage);

    void getCategoryListSuccess(List<CategoryNew> categoryListResponse);
}
