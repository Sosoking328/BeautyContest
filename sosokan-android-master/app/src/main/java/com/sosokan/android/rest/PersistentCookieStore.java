package com.sosokan.android.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * Created by macintosh on 2/5/17.
 */

public class PersistentCookieStore  implements CookieStore {

    /**
     * The default preferences string.
     */
    private final static String PREF_DEFAULT_STRING = "";

    /**
     * The preferences name.
     */
    private final static String PREFS_NAME = PersistentCookieStore.class.getName();

    /**
     * The preferences session cookie key.
     */
    private final static String PREF_SESSION_COOKIE = "session_cookie";
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "sessionid";

    private CookieStore mStore;
    private Context mContext;

    /**
     * @param context The application context
     */
    public PersistentCookieStore(Context context) {
        // prevent context leaking by getting the application context
        mContext = context.getApplicationContext();

        // get the default in memory store and if there is a cookie stored in shared preferences,
        // we added it to the cookie store
        mStore = new CookieManager().getCookieStore();
        Log.e("PersistentCookieStore ","Cookies string:  " + mStore.getCookies());
        String jsonSessionCookie = getJsonSessionCookieString();
        if (!jsonSessionCookie.equals(PREF_DEFAULT_STRING)) {
            Gson gson = new Gson();
            HttpCookie cookie = gson.fromJson(jsonSessionCookie, HttpCookie.class);
            mStore.add(URI.create(cookie.getDomain()), cookie);
        }
    }

    public PersistentCookieStore(Context context, boolean isClear) {
        // prevent context leaking by getting the application context
        mContext = context.getApplicationContext();

        // get the default in memory store and if there is a cookie stored in shared preferences,
        // we added it to the cookie store
        mStore = new CookieManager().getCookieStore();
        String jsonSessionCookie = getJsonSessionCookieString();
        Log.e ("Persitent Cookie", jsonSessionCookie);
        if (!jsonSessionCookie.equals(PREF_DEFAULT_STRING)) {
            Log.e ("Persitent Cookie", "DELETE");

            Gson gson = new Gson();
            HttpCookie cookie = gson.fromJson(jsonSessionCookie, HttpCookie.class);
            mStore.remove(URI.create(cookie.getDomain()), cookie);
            remove(URI.create(cookie.getDomain()), cookie);
            jsonSessionCookie = getJsonSessionCookieString();

            Log.e ("Cookie DELETE ", jsonSessionCookie);
        }
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        if (cookie.getName().equals("sessionid")) {
            // if the cookie that the cookie store attempt to add is a session cookie,
            // we remove the older cookie and save the new one in shared preferences
            remove(URI.create(cookie.getDomain()), cookie);
            saveSessionCookie(cookie);
        }

        mStore.add(URI.create(cookie.getDomain()), cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return mStore.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return mStore.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return mStore.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return mStore.remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return mStore.removeAll();
    }

    private String getJsonSessionCookieString() {
        return getPrefs().getString(PREF_SESSION_COOKIE, PREF_DEFAULT_STRING);
    }

    /**
     * Saves the HttpCookie to SharedPreferences as a json string.
     *
     * @param cookie The cookie to save in SharedPreferences.
     */
    private void saveSessionCookie(HttpCookie cookie) {
        Gson gson = new Gson();
        String jsonSessionCookieString = gson.toJson(cookie);
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(PREF_SESSION_COOKIE, jsonSessionCookieString);
        editor.apply();
    }

    private SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}

