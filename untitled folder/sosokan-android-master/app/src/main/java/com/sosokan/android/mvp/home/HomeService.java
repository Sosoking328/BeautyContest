package com.sosokan.android.mvp.home;

import rx.Observable;

/**
 * Created by macintosh on 3/25/17.
 */

public interface HomeService {
    Observable<String> onCategoryChange();
}
