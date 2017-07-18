package com.sosokan.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by AnhZin on 9/18/2016.
 */
public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static void onCreate(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        setLocale(context, lang);

    }

    public static void onCreate(Context context, String defaultLanguage) {
        // String lang = getPersistedData(context, defaultLanguage);
       if(context!= null)
       {
           setLocale(context, defaultLanguage);
       }
    }

    public static String getLanguage(Context context) {
        if(context == null) return  "en";
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    public static void setLocale(Context context, String language) {
      if(context!= null)
      {
          persist(context, language);
          updateResources(context, language);
      }
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        if(context!= null ) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
        }
        return "en";
    }

    private static void persist(Context context, String language) {
        if(context!= null)
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SELECTED_LANGUAGE, language);
            editor.commit();
            editor.apply();
        }
    }

    private static void updateResources(Context context, String language) {
       if(context!= null)
       {
           Locale locale = new Locale(language);
           Locale.setDefault(locale);

           Resources resources = context.getResources();

           Configuration configuration = resources.getConfiguration();
           configuration.locale = locale;

           resources.updateConfiguration(configuration, resources.getDisplayMetrics());
       }
    }

    public static LatLng getLocationFromAddress(String strAddress, Context context) {
        LatLng latLng = null;
        if(context!= null)
        {
            Geocoder geocoder = new Geocoder(context, Locale.US);
            try {
                if(geocoder.getFromLocationName(strAddress, 1)!=null && geocoder.getFromLocationName(strAddress, 1).size()>0)
                {
                    Address addresses = geocoder.getFromLocationName(strAddress, 1).get(0);
                    latLng = new LatLng(addresses.getLatitude(), addresses.getLongitude());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return latLng;
        }
        return latLng;
    }
}