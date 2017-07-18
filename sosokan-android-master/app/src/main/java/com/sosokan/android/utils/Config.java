package com.sosokan.android.utils;

/**
 * Created by AnhZin on 8/25/2016.
 */
public class Config {
    // Google Console APIs developer key
    // Replace this key with your's
    public static final String YOUTUBE_API_KEY = "AIzaSyActKBpwyN_4WkQw1u73M5NEMjZf9_vmWY";

    public static final String API_URL_LIVE= "http://sosokan.herokuapp.com/";
    public static final String API_URL_STAGING = "http://sosokan-staging.herokuapp.com/";
    public static final String API_URL_WECHAT = "https://api.weixin.qq.com/sns/";
    public static final String FIREBASE_URL = "https://sosokan-1452b.firebaseio.com/";
    public static final String FIREBASE_URL_CATEGORIES = "https://sosokan-1452b.firebaseio.com/categories";
    public static final String FIREBASE_ADVERTISE_URL = "https://sosokan-1452b.firebaseio.com/advertises";
    public static final String FIREBASE_CHAT_URL = "https://sosokan-1452b.firebaseio.com/messages";
    public static final String FIREBASE_FAVORITE_URL = "https://sosokan-1452b.firebaseio.com/favorites";

    /*RINGCAPTCHA*/
    public static final String APP_KEY = "ymo7o5ara1y3y2ymato2";
    public static final String API_KEY = "925f2dcf5370e76a26be65e7e3c0a7b4a1094a27";
    public static final String Secret_KEY = "e5e3ena8i6u2iqyru4al";

    /*END RINGCAPTCHA*/

    /*DOORBLE.IO*/
    public static final int APP_ID_DOORBLE = 4288;
    public static final String API_KEY_DOORBLE = "4UbgZYwt2nWKCY7KjNyTjDA4ezTMm03e2wK6nAWcXvdtC6ACbghlkcf7KG5XRF0a";

    /*END *DOORBLE.IO*/

    public static final String CHILD_USERS="users";

    public static final String CHILD_CHAT="chat";

    public static final String CHILD_CONNECTION="connection";

    // Key for passing user data between activity
    public static final String KEY_PASS_USERS_INFO="usersData";


    /* Key for email */
    public static final String KEY_EMAIL="email";

    /* Key for time */
    public static final String KEY_TIMESTAMP="createdAt";

    /* Key for provider */
    public static final String KEY_PROVIDER="provider";

    /* Key for first name */
    public static final String KEY_FIRST_NAME="firstName";

    /* Key for user email */
    public static final String KEY_USER_EMAIL="userEmail";


    /* Key for avatar id */
    public static final String KEY_AVATAR_ID="avatarId";

    /* User connection status */
    public static final String KEY_ONLINE="online";

    public static final String KEY_OFFLINE="offline";
}