package com.kf5.sdk.system.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * author:chosen
 * date:2016/11/17 17:26
 * email:812219713@qq.com
 */

public class SPUtils {

    private static final String USER_ID = "user_id";

    private static final String APP_ID = "app_id";

    private static final String USER_TOKEN = "user_token";

    private static final String PREFERENCE_NAME = "kf5_sdk";

    private static final String HELP_ADDRESS = "help_address";

    private static final String USER_AGENT = "user_agent";

    private static final String USER_NAME = "user_name";

    private static final String SDK_TITLE = "ticket_title";

    private static final String CHAT_URL = "chat_url";

    private static SPUtils sSPUtils;

    private static SharedPreferences mSharedPreferences;

    private SPUtils(Context context) {
        mSharedPreferences = context.getSharedPreferences(MD5Utils.GetMD5Code(PREFERENCE_NAME), Context.MODE_PRIVATE);
    }

    public static SPUtils getInstance(Context context) {
        if (sSPUtils == null) {
            synchronized (SPUtils.class) {
                if (sSPUtils == null) {
                    sSPUtils = new SPUtils(context.getApplicationContext());
                }
            }
        }
        return sSPUtils;
    }

    public static void saveUserId(int user_id) {
        mSharedPreferences.edit().putInt(USER_ID, user_id).apply();
    }

    public static int getUserId() {
        return mSharedPreferences.getInt(USER_ID, 0);
    }

    public static String getAppid() {
        return mSharedPreferences.getString(APP_ID, "");
    }

    public static void saveAppID(String appID) {
        mSharedPreferences.edit().putString(APP_ID, appID).apply();
    }

    public static void saveUserToken(String userToken) {
        mSharedPreferences.edit().putString(USER_TOKEN, userToken).apply();
    }

    public static String getUserToken() {
        return mSharedPreferences.getString(USER_TOKEN, "");
    }

    public static void saveHelpAddress(String helpAddress) {
        mSharedPreferences.edit().putString(HELP_ADDRESS, helpAddress).apply();
    }

    public static String getHelpAddress() {
        return mSharedPreferences.getString(HELP_ADDRESS, "");
    }

    public static void saveChatUrl(String chat_url) {
        mSharedPreferences.edit().putString(CHAT_URL, chat_url).apply();
    }

    public static String getChatUrl() {
        return mSharedPreferences.getString(CHAT_URL, "");
    }

    public static void clearSP() {
        mSharedPreferences.edit().clear().apply();
    }

    public static String getUserAgent() {
        return mSharedPreferences.getString(USER_AGENT, "");
    }

    public static void saveUserAgent(String userAgent) {
        mSharedPreferences.edit().putString(USER_AGENT, userAgent).apply();
    }

    public static void saveUserName(String userName) {
        mSharedPreferences.edit().putString(USER_NAME, userName).apply();
    }

    public static String getUserName() {
        return mSharedPreferences.getString(USER_NAME, "");
    }

    public static void saveTicketTitle(String title) {
        mSharedPreferences.edit().putString(SDK_TITLE, title).apply();
    }

    public static String getTicketTitle() {
        return mSharedPreferences.getString(SDK_TITLE, "来自Android SDk 的工单反馈");
    }

}
