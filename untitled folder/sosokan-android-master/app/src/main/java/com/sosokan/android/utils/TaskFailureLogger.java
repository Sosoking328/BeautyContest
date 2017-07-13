package com.sosokan.android.utils;

/**
 * Created by AnhZin on 9/11/2016.
 */

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;

public class TaskFailureLogger implements OnFailureListener {
    private String mTag;
    private String mMessage;

    public TaskFailureLogger(String tag, String message) {
        mTag = tag;
        mMessage = message;
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.w(mTag, mMessage, e);
    }
}
