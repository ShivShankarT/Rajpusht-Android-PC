/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package in.rajpusht.pc.data.local.pref;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.IOException;

import javax.inject.Inject;

import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.di.PreferenceInfo;
import in.rajpusht.pc.ui.login.LoginActivity;

/**
 * Created by amitshekhar on 07/07/17.
 */

public class AppPreferencesHelper {
    public static final String PREF_NAME = "app-prefv13";

    public static final long NULL_INDEX = -1L;
    public static final String PREF_FIRST_NAME = "first_name";
    public static final String PREF_LAST_NAME = "last_name";
    public static final String PREF_LAST_SYNC = "last_sync";
    public static final String PREF_CURRENT_VERSION = "current_vers";
    public static final String PREF_MIN_VERSION = "min_vers";
    public static final String PREF_DRIVE_URL = "g_app_url";
    public static final String PREF_LAST_APPCONFIG_FTIME = "last_app_config";

    private static final String PREF_LOGIN = "login";
    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL";
    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";
    @SuppressLint("StaticFieldLeak")//application context
    private static AppPreferencesHelper appPreferencesHelper;
    private final SharedPreferences mPrefs;
    private final Context context;

    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        this.context = context;
        mPrefs = this.context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        appPreferencesHelper = this;
    }

    public static AppPreferencesHelper getAppPreferencesHelper() {
        return appPreferencesHelper;
    }

    public boolean getLogin() {
        return mPrefs.getBoolean(PREF_LOGIN, false);
    }

    public void setLogin(boolean login) {
        mPrefs.edit().putBoolean(PREF_LOGIN, login).apply();
    }

    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    public String getCurrentUserEmail() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_EMAIL, null);
    }

    public void setCurrentUserEmail(String email) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply();
    }

    public Long getCurrentUserId() {
        long userId = mPrefs.getLong(PREF_KEY_CURRENT_USER_ID, NULL_INDEX);
        return userId == NULL_INDEX ? null : userId;
    }

    public void setCurrentUserId(Long userId) {
        long id = userId == null ? NULL_INDEX : userId;
        mPrefs.edit().putLong(PREF_KEY_CURRENT_USER_ID, id).apply();
    }

    public String getCurrentUserName() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_NAME, null);
    }

    public void setCurrentUserName(String userName) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply();
    }

    public String getCurrentUserMob() {
        return mPrefs.getString("mob", null);
    }

    public void setCurrentUserMob(String userName) {
        mPrefs.edit().putString("mob", userName).apply();
    }

    public String getSelectedAwcCode() {
        return mPrefs.getString("awc_code", null);
    }

    public void setAwcCode(String userName) {
        mPrefs.edit().putString("awc_code", userName).apply();
    }

    public boolean isEnglish() {
        return mPrefs.getBoolean("is_eng_language", true);
    }

    public void setLanguage(boolean isEng) {
        mPrefs.edit().putBoolean("is_eng_language", isEng).apply();
    }

    public void putString(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
    }

    public void putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).apply();
    }

    public String getString(String key) {
        return mPrefs.getString(key, null);
    }

    public int getInt(String key) {
        return mPrefs.getInt(key, -1);
    }


    public void logout() {
        mPrefs.edit().clear().apply();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public String getStringRes(int stringId) {
        return context.getString(stringId);
    }


    public String readStringFromAsset(String file) throws IOException {
        return HUtil.readStringFromAsset(context, file);
    }


}

