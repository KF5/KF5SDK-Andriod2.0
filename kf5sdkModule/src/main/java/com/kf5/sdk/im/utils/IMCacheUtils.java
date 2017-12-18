package com.kf5.sdk.im.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.kf5.sdk.system.utils.MD5Utils;

/**
 * author:chosen
 * date:2017/5/8 11:33
 * email:812219713@qq.com
 */

public final class IMCacheUtils {

    private static final String PREFERENCE_NAME = "kf5_sdk_im";

    private static final String TEMPORARY_MESSAGE_FIRST = "temporary_message_first";

    private static final String TEMPORARY_MESSAGE_WAS_SENT = "temporary_message_was_sent";

    /**
     * @param first true 先临时消息再排队
     *              false 先排队再发消息
     */
    public static void setTemporaryMessageFirst(@NonNull Context context, boolean first) {
        getSharedPreferences(context).edit().putBoolean(TEMPORARY_MESSAGE_FIRST, first).apply();
    }

    /**
     * @return 返回是否先发临时消息再排队
     * true 先发临时消息再排队
     * false 先排队再发临时消息
     */
    public static boolean temporaryMessageFirst(@NonNull Context context) {
        return getSharedPreferences(context).getBoolean(TEMPORARY_MESSAGE_FIRST, false);
    }


    /**
     * 缓存排队状态是否已经发送了临时消息
     *
     * @param sent
     */
    public static void setTemporaryMessageWasSent(@NonNull Context context, boolean sent) {
        getSharedPreferences(context).edit().putBoolean(TEMPORARY_MESSAGE_WAS_SENT, sent).apply();
    }

    /**
     * @param context
     * @return 排队状态是否已经发送了临时消息
     */
    public static boolean temporaryMessageWasSent(@NonNull Context context) {
        return getSharedPreferences(context).getBoolean(TEMPORARY_MESSAGE_WAS_SENT, false);
    }


    private static SharedPreferences getSharedPreferences(@NonNull Context context) {
        return context.getApplicationContext().getSharedPreferences(MD5Utils.GetMD5Code(PREFERENCE_NAME), Context.MODE_PRIVATE);
    }

}
