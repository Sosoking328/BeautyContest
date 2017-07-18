package com.sosokan.android.events.Listener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by AnhZin on 11/14/2016.
 */

public class EndCallListener extends PhoneStateListener {
    public static String LOG_TAG ="EndCallListener";

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        if(TelephonyManager.CALL_STATE_RINGING == state) {
            Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
        }
        if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
            //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
            Log.i(LOG_TAG, "OFFHOOK");
        }
        if(TelephonyManager.CALL_STATE_IDLE == state) {
            //when this state occurs, and your flag is set, restart your app
            Log.i(LOG_TAG, "IDLE");
        }
    }
}
