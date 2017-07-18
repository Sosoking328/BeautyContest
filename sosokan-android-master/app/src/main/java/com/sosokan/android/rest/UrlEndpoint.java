package com.sosokan.android.rest;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.sosokan.android.BuildConfig;
import com.sosokan.android.models.UserInformationTokenApi;
import com.sosokan.android.ui.activity.PrefManager;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by macintosh on 1/17/17.
 */

public class UrlEndpoint {
    public UrlEndpoint() {
    }

    public String getUrlApi(String apiUrl) {

        String url = BuildConfig.DEBUG ? Config.API_URL_STAGING : Config.API_URL_STAGING; //LIVE NOT WORK
        String urlWechat = Config.API_URL_WECHAT;
        switch (apiUrl) {
            case Constant.USERS:
                url += "api/users/";
                break;
            case Constant.USER_PROFILE:
                url += "api/userprofiles/";
                break;
            case Constant.ADS:
                url += "api/ads/";
                break;
            case Constant.AD_IMAGES:
                url += "api/adimages/";
                break;
            case Constant.CATEGORIES:
                url += "api/categories/";
                break;
            case Constant.FAVORITES:
                url += "api/favorites/";
                break;
            case Constant.BANNERS:
                url += "api/banners/";
                break;
            case Constant.COMMENTS:
                url += "api/comments/";
                break;
            case Constant.FLAGS:
                url += "api/flags/";
                break;
            case Constant.FLAG_CHOICES:
                url += "api/flagchoices/";
                break;
            case Constant.CONTENT_TYPES:
                url += "api/contenttypes/";
                break;
            case Constant.STATUSES:
                url += "api/statuses/";
                break;
            case Constant.REST_AUTH_FB:
                url += "rest-auth/facebook/";
                break;
            case Constant.REST_AUTH_USER:
                url += "rest-auth/user/";
                break;

            case Constant.PHONE_LOGIN:
                url += "api/phone_login/";
                break;
            case Constant.WECHAT_ACCESS_TOKEN:
                url = urlWechat + "oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
                break;
            case Constant.WECHAT_REFRESH_ACCESS_TOKEN:
                url = urlWechat + "oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
                break;
            case Constant.WECHAT_USER_INFOR:
                url = urlWechat + "userinfo?access_token=%s&openid=%s";
                break;

            case Constant.WECHAT_FIREBASE_CUSTOM_KEY:
                url += "api/get_firebase_key/";
                break;
            case Constant.SYNC_AD:
                url += "api/sync_ad/";
                break;
        }
        return url;
    }

    public String getUserToken(Context context) {
        PrefManager prefManager = new PrefManager(context);
        if(prefManager.getUserInformationToken()!=null)
        {
            UserInformationTokenApi userInformationTokenApi = prefManager.getUserInformationToken();
            if(userInformationTokenApi!=null && userInformationTokenApi.getToken()!=null && !userInformationTokenApi.getToken().isEmpty())
            {
                Log.e("UrlEndpoint","userInformationTokenApi.getToken() " + userInformationTokenApi.getKey());
                return "Token " + userInformationTokenApi.getKey();
            }else{
                Log.e("UrlEndpoint","empty" );
                return "";
            }
        }else{
            Log.e("UrlEndpoint","empty" );
            return "";
        }

    }


    public Map<String, String> getHeaderWithAccountDefault(Context context) {
        Map<String, String> headers = new HashMap<>();
        if(context==null)
        {
            String credentials = "zin:changeit123";
            String auth = "Basic "
                    + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Content-Type", "application/json; charset=utf-8");
            headers.put("Authorization", auth);
        }else {
            PrefManager prefManager = new PrefManager(context);
            if(prefManager.getUserInformationToken()!=null)
            {
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", getUserToken(context));

            }else{
                String credentials = "zin:changeit123";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", auth);

            }
        }
        return headers;
    }
    public Map<String, String> getHeaderWithTokenUser(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Authorization", getUserToken(context));
        return headers;
    }
    public String getLanguage(Boolean isChinese)
    {
        return isChinese? Constant.LANG_CN:Constant.LANG_EN;
    }
}
