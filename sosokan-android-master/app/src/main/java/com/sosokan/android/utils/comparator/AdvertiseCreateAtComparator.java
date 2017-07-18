package com.sosokan.android.utils.comparator;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.Category;

import java.util.Comparator;

/**
 * Created by AnhZin on 12/3/2016.
 */

public class AdvertiseCreateAtComparator implements Comparator<Advertise> {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int compare(Advertise s1, Advertise s2) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Long.compare(s1.getCreatedAt(), s2.getCreatedAt());
        }
        return Long.compare(s1.getCreatedAt(), s2.getCreatedAt());
    }
}
