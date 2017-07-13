package com.sosokan.android.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by AnhZin on 8/27/2016.
 */
public class ImageUtils {
   public String GenerationImageFromDrawable(Activity activity, int idImage)
   {
       Bitmap bmp =  BitmapFactory.decodeResource(activity.getResources(), idImage);//your image
       ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
       bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
       bmp.recycle();
       byte[] byteArray = bYtE.toByteArray();
       String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);

       return imageFile;
   }
}
