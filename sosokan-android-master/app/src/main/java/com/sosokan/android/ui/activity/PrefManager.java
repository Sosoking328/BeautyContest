package com.sosokan.android.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.sosokan.android.models.AdvertiseApi;
import com.sosokan.android.models.Category;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.CategoryPopularCustomize;
import com.sosokan.android.models.City;
import com.sosokan.android.models.Flag;
import com.sosokan.android.models.FlagChoice;
import com.sosokan.android.models.User;
import com.sosokan.android.models.UserApi;
import com.sosokan.android.models.UserInformationTokenApi;
import com.sosokan.android.models.UserProfile;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.models.UserSelection;
import com.sosokan.android.models.WechatAuth;
import com.sosokan.android.models.WechatResponse;
import com.sosokan.android.models.WechatUserInfor;
import com.sosokan.android.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by AnhZin on 12/16/2016.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    private static final String TAG = "PrefManager";
    // Shared preferences file name
    private static final String PREF_NAME = "sosokan-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String ACCESS_TOKEN = "AccessToken";

    private static final String CSRF_TOKEN = "CsrfToken";

    private static final String CATEGORIES_API = "CategoriesAPI";

    private static final String CATEGORIES_FIREBASE = "CategoriesFirebase";

    private static final String COOKIE = "Cookie";

    private static final String WECHAT = "Wechat";

    private static final String CITIES = "Cities";

    private static final String MAP_STATES = "MapStates";

    private static final String CATEGORIES_POPULAR = "CategoryPopular";

    private static final String USER_SELECTION = "UserSelection";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setAccessToken(String accessToken) {
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    public void setCsrfToken(String csrfToken) {
        editor.putString(CSRF_TOKEN, csrfToken);
        editor.commit();
    }

    public String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, "");

    }

    public String getCsrfToken() {
        return pref.getString(CSRF_TOKEN, "");
    }


    public void setUserApi(UserApi user) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(user);
        editor.putString(Constant.USERS_API, serializedObject);
        editor.commit();
    }

    public UserApi getUserApi() {

        if (pref.contains(Constant.USERS_API)) {
            final Gson gson = new Gson();
            return gson.fromJson(pref.getString(Constant.USERS_API, ""), UserApi.class);
        }
        return null;
    }

    public void setPrimaryKeyUser(int primaryKeyUser) {
        editor.putInt(Constant.PRIMARY_KEY, primaryKeyUser);
        editor.commit();
    }

    public int getPrimaryKeyUser() {
        return pref.getInt(Constant.PRIMARY_KEY, -1);

    }

    public String getUsername() {
        return pref.getString(Constant.USERNAME, "");

    }

    public void setUserName(String userName) {
        editor.putString(Constant.USERNAME, userName);
        editor.commit();
    }

    public String getEmailUser() {
        return pref.getString(Constant.EMAIL, "");

    }

    public void setEmailUser(String emailUser) {
        editor.putString(Constant.EMAIL, emailUser);
        editor.commit();
    }

    public void setFirstName(String firstName) {
        editor.putString(Constant.FIRST_NAME, firstName);
        editor.commit();
    }

    public String getFirstName() {
        return pref.getString(Constant.FIRST_NAME, "");

    }

    public void setLastName(String lastName) {
        editor.putString(Constant.LAST_NAME, lastName);
        editor.commit();
    }

    public String getLastname() {
        return pref.getString(Constant.LAST_NAME, "");

    }

    public void setCategoriesApi(String categories) {
        // editor.putString(TASKS, ObjectSerializer.serialize(currentTasks));
        if (categories != null && !categories.isEmpty()) {
            editor.putString(CATEGORIES_API, categories);
            editor.commit();
        }
    }


    public void setCategoriesFirebase(String categories) {
        // editor.putString(TASKS, ObjectSerializer.serialize(currentTasks));
        if (categories != null && !categories.isEmpty()) {
            editor.putString(CATEGORIES_FIREBASE, categories);
            editor.commit();
        }

    }

    public String getCategoriesFirebase() {
        return pref.getString(CATEGORIES_FIREBASE, "");
    }


    public List<Category> getListCategoriesFirebase() {
        final Gson gson = new Gson();
        List<Category> categories = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        if (pref.getString(CATEGORIES_API, "") != null && !pref.getString(CATEGORIES_API, "").isEmpty()) {

            JsonArray jsonArray = (JsonArray) jsonParser.parse(pref.getString(CATEGORIES_FIREBASE, ""));


            for (int i = 0; i < jsonArray.size(); i++) {

                try {
                    Category category = gson.fromJson(jsonArray.get(i).toString(), Category.class);
                    if (category != null) {
                        categories.add(category);

                    }
                } catch (Exception ex) {
                    Log.e("getCategoriesFirebase", " " + ex);
                }
            }
        }
        return categories;
    }

    public List<CategoryNew> getListCategoriesApi() {
        final Gson gson = new Gson();
        List<CategoryNew> categories = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        if (pref.getString(CATEGORIES_API, "") != null && !pref.getString(CATEGORIES_API, "").isEmpty()) {
            JsonArray jsonArray = (JsonArray) jsonParser.parse(pref.getString(CATEGORIES_API, ""));


            for (int i = 0; i < jsonArray.size(); i++) {

                try {
                    CategoryNew category = gson.fromJson(jsonArray.get(i).toString(), CategoryNew.class);
                    if (category != null) {

                        categories.add(category);

                    }
                } catch (Exception ex) {
                    Log.e("getListCategoriesApi", " " + ex);
                }
            }

        }
        return categories;
    }

    public void setUser(User user) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(user);
        editor.putString(Constant.USERS_FIREBASE, serializedObject);
        editor.commit();
    }

    public User getUser() {

        if (pref.contains(Constant.USERS_FIREBASE)) {
            final Gson gson = new Gson();
            return gson.fromJson(pref.getString(Constant.USERS_FIREBASE, ""), User.class);
        }
        return null;
    }


    public void deleteUser() {

        editor.remove(Constant.USERS);
        editor.commit();
    }

    public void setCookie(String cookie) {
        editor.putString(COOKIE, cookie);
        editor.commit();
    }

    public String getCookie() {

        if (pref.contains(COOKIE)) {
            return pref.getString(COOKIE, "");
        }
        return null;
    }

    public void setUserProfile(UserProfileApi user) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(user);
        editor.putString(Constant.USER_PROFILE, serializedObject);
        editor.commit();
    }

    public UserProfileApi getUserProfile() {

        if (pref.contains(Constant.USER_PROFILE)) {
            final Gson gson = new Gson();
            return gson.fromJson(pref.getString(Constant.USER_PROFILE, ""), UserProfileApi.class);
        }
        return null;
    }


    public void setUserInformationToken(UserInformationTokenApi user) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(user);
        editor.putString(Constant.USERS_INFORMATION_TOKEN_API, serializedObject);
        editor.commit();
    }

    public UserInformationTokenApi getUserInformationToken() {

        if (pref.contains(Constant.USERS_INFORMATION_TOKEN_API)) {
            final Gson gson = new Gson();
            return gson.fromJson(pref.getString(Constant.USERS_INFORMATION_TOKEN_API, ""), UserInformationTokenApi.class);
        }
        return null;
    }

    public void setFlagchoices(List<FlagChoice> flagChoices) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(flagChoices);
        editor.putString(Constant.FLAG_CHOICES, serializedObject);
        editor.commit();
    }

    public List<FlagChoice> getFlagchoices() {

        if (pref.contains(Constant.FLAG_CHOICES)) {
            final Gson gson = new Gson();
            List<FlagChoice> flagChoices = new ArrayList<>();

            JsonParser jsonParser = new JsonParser();
            JsonArray jsonArray = (JsonArray) jsonParser.parse(pref.getString(Constant.FLAG_CHOICES, ""));


            for (int i = 0; i < jsonArray.size(); i++) {

                try {
                    FlagChoice flagChoice = gson.fromJson(jsonArray.get(i).toString(), FlagChoice.class);
                    if (flagChoice != null) {

                        flagChoices.add(flagChoice);

                    }
                } catch (Exception ex) {
                    Log.e("getListCategories", " " + ex);
                }
            }
            return flagChoices;
        }
        return null;
    }

    public void deleteUserProfile() {

        editor.remove(Constant.USER_PROFILE);
        editor.commit();
    }

    public void deleteUserInformationTokenApi() {

        editor.remove(Constant.USERS_INFORMATION_TOKEN_API);
        editor.commit();
    }

    public void deleteUserFirebase() {

        editor.remove(Constant.USERS_FIREBASE);
        editor.commit();
    }

    public void setWechatAuth(WechatAuth wechat) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(wechat);
        editor.putString(WECHAT, serializedObject);
        editor.commit();
    }

    public WechatAuth getWechatAuth() {

        if (pref.contains(WECHAT)) {
            final Gson gson = new Gson();
            return gson.fromJson(pref.getString(WECHAT, ""), WechatAuth.class);
        }
        return null;
    }

    public void setWechatResponse(WechatResponse wechat) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(wechat);
        editor.putString(Constant.WECHAT_ACCESS_TOKEN, serializedObject);
        editor.commit();
    }

    public WechatResponse getWechatResponse() {

        if (pref.contains(Constant.WECHAT_ACCESS_TOKEN)) {
            final Gson gson = new Gson();
            return gson.fromJson(pref.getString(Constant.WECHAT_ACCESS_TOKEN, ""), WechatResponse.class);
        }
        return null;
    }

    public void setWechatUserInfor(WechatUserInfor wechat) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(wechat);
        editor.putString(Constant.WECHAT_USER_INFOR, serializedObject);
        editor.commit();
    }

    public WechatUserInfor getWechatUserInfor() {

        if (pref.contains(Constant.WECHAT_USER_INFOR)) {
            final Gson gson = new Gson();
            return gson.fromJson(pref.getString(Constant.WECHAT_USER_INFOR, ""), WechatUserInfor.class);
        }
        return null;
    }


    public void setCities(String cities) {
        // editor.putString(TASKS, ObjectSerializer.serialize(currentTasks));
        if (cities != null && !cities.isEmpty()) {
            editor.putString(CITIES, cities);
            editor.commit();
        }

    }

    public List<City> getListCities() {
        final Gson gson = new Gson();
        List<City> cities = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        if (pref.getString(CITIES, "") != null && !pref.getString(CITIES, "").isEmpty()) {

            JsonArray jsonArray = (JsonArray) jsonParser.parse(pref.getString(CITIES, ""));


            for (int i = 0; i < jsonArray.size(); i++) {

                try {
                    City city = gson.fromJson(jsonArray.get(i).toString(), City.class);
                    if (city != null) {
                        cities.add(city);

                    }
                } catch (Exception ex) {
                    Log.e("getCategoriesFirebase", " " + ex);
                }
            }
        }
        return cities;
    }

    public void setListAdvertiseApiWithCategoryId(String categoryId, String advertises) {
        // editor.putString(TASKS, ObjectSerializer.serialize(currentTasks));
        if (advertises != null && !advertises.isEmpty()) {
            editor.putString(categoryId, advertises);
            editor.commit();
        }
    }

    public List<AdvertiseApi> getListAdvertiseApiWithCategoryId(String categoryId) {
        final Gson gson = new Gson();
        List<AdvertiseApi> advertiseApis = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        if (pref.getString(categoryId, "") != null && !pref.getString(categoryId, "").isEmpty()) {

            JsonArray jsonArray = (JsonArray) jsonParser.parse(pref.getString(categoryId, ""));


            for (int i = 0; i < jsonArray.size(); i++) {

                try {
                    AdvertiseApi advertiseApi = gson.fromJson(jsonArray.get(i).toString(), AdvertiseApi.class);
                    if (advertiseApi != null) {
                        advertiseApis.add(advertiseApi);

                    }
                } catch (Exception ex) {
                    Log.e(TAG, "getListAdvertiseApiWithCategoryId " + ex);
                }
            }
        }
        return advertiseApis;
    }

    public void setCategoriesPopular(String categories) {
        // editor.putString(TASKS, ObjectSerializer.serialize(currentTasks));
        if (categories != null && !categories.isEmpty()) {
            editor.putString(CATEGORIES_POPULAR, categories);
            editor.commit();
        }

    }


    public List<CategoryPopularCustomize> getListCategoriesPopular() {
        final Gson gson = new Gson();
        List<CategoryPopularCustomize> categories = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        if (pref.getString(CATEGORIES_POPULAR, "") != null && !pref.getString(CATEGORIES_POPULAR, "").isEmpty()) {

            JsonArray jsonArray = (JsonArray) jsonParser.parse(pref.getString(CATEGORIES_POPULAR, ""));


            for (int i = 0; i < jsonArray.size(); i++) {

                try {
                    CategoryPopularCustomize category = gson.fromJson(jsonArray.get(i).toString(), CategoryPopularCustomize.class);
                    if (category != null) {
                        categories.add(category);

                    }
                } catch (Exception ex) {
                    Log.e("getCategoriesFirebase", " " + ex);
                }
            }
        }
        return categories;
    }

    public void setUserSelection(UserSelection user) {
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(user);
        editor.putString(USER_SELECTION, serializedObject);
        editor.commit();
    }

    public UserSelection getUserSelection() {

        if (pref.contains(USER_SELECTION)) {
            final Gson gson = new Gson();
            return gson.fromJson(pref.getString(USER_SELECTION, ""), UserSelection.class);
        }
        return null;
    }


}

