package com.sosokan.android.utils.comparator;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.sosokan.android.models.AdvertiseApi;
import com.sosokan.android.utils.DateUtils;

import java.util.Comparator;

/**
 * Created by AnhZin on 12/20/2016.
 */

public class AdvertiseNewCreateAtComparator implements Comparator<AdvertiseApi> {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int compare(AdvertiseApi s1, AdvertiseApi s2) {
        long date1 = DateUtils.getTimeFromString(s1.getCreated_on());
        long date2 = DateUtils.getTimeFromString(s2.getCreated_on());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Long.compare(date1, date2);
        }
        return Long.compare(date1, date2);
    }
}
