package com.sosokan.android.utils;

import android.graphics.Color;

/**
 * Created by AnhZin on 8/24/2016.
 */
public class ColorHelper {
    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }
}
