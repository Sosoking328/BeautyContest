package com.sosokan.android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.sosokan.android.R;

public class ApplicationUtils {

    public static boolean isShow() {
        return isShow;
    }

    public static boolean isShow;
    private static AlertDialog mAlertDialog;

    public static void showProgress(Activity context) {
        if (context != null) {
            View view = context.getLayoutInflater().inflate(R.layout.progress_custom, null, false);
            if (view != null) {
                AlertDialog.Builder builder = getBuilder(context)
                        .setCancelable(false)
                        .setView(view);
                show(context, builder);
            }
        }


    }

    public static void closeMessage() {
        try {
            if (mAlertDialog != null && mAlertDialog.isShowing()) {
                mAlertDialog.cancel();
                mAlertDialog = null;
                isShow = false;
            }
        } catch (IllegalArgumentException e) {
            Log.e("Message", "close dialog not in activity !!!: " + e.getMessage());
        }
    }

    private static void show(Activity context, final AlertDialog.Builder builder) {
        if (context != null) {
            if (!context.isFinishing()) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mAlertDialog = builder.create();
                            mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            mAlertDialog.setCanceledOnTouchOutside(false);
                            mAlertDialog.show();
                            isShow = true;
                        } catch (final IllegalArgumentException e) {
                            Log.e("Message", "show dialog error :" + e.getMessage());
                        }
                    }
                });
            }
        }
    }


    public static AlertDialog.Builder getBuilder(Activity context) {
        return new AlertDialog.Builder(context, R.style.AppCompat_AlertDialog);
    }
}
