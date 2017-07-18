package com.sosokan.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.thrivecom.ringcaptcha.lib.models.RingcaptchaResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by AnhZin on 9/18/2016.
 */
public class RingcaptchaUtils {
    public RingcaptchaResponse processResponse(Context context, String content) throws JSONException {

        JSONObject json = new JSONObject(content);
        RingcaptchaResponse model = new RingcaptchaResponse();
        SharedPreferences.Editor prefsWriter = context.getSharedPreferences("RC", 0).edit();
        String jsonArray;
        if(json.has("type")) {
            jsonArray = json.getString("type");
            if(jsonArray == null) {
                jsonArray = json.getString("service");
            }

            model.type = jsonArray;
        }

        if(json.has("status")) {
            model.status = json.getString("status");
        }

        if(json.has("token")) {
            model.token = json.getString("token");
            prefsWriter.putString("RCTK4" + Config.APP_KEY, model.token);
            prefsWriter.commit();
        }

        if(json.has("expires_in")) {
            int var10 = json.getInt("expires_in");
            Calendar numbers = Calendar.getInstance();
            // numbers.add(13, var10);
            prefsWriter.putLong("RCTKTM4" + Config.APP_KEY, numbers.getTimeInMillis());
            prefsWriter.commit();
        }

        if(json.has("retry_in")) {
            Calendar var11 = Calendar.getInstance();
            //  var11.add(13, json.optInt("retry_in", 0));
            model.timeout = var11.getTime();
        }

        if(json.has("phone")) {
            model.phone = json.getString("phone");
        }

        if(json.has("id")) {
            model.id = json.getString("id");
        }

        if(json.has("message")) {
            model.message = json.getString("message");
        }

        if(json.has("pcp")) {
            jsonArray = json.getString("pcp");
            if(jsonArray.length() > 0) {
                prefsWriter.putString("RCPCP", jsonArray).commit();
                String[] var12 = jsonArray.split(",");
            }
        }

        if(json.has("numbers")) {
            JSONArray var14 = json.getJSONArray("numbers");
            ArrayList var13 = new ArrayList();
            int len = var14.length();

            for(int i = 0; i < len; ++i) {
                var13.add(var14.getString(i));
            }

            model.numbers = var13;
        }

        if(json.has("referer")) {
            model.referer = json.getString("referer");
        }

        return model;
    }

}
