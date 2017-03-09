package com.kf5.sdk.system.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * author:chosen
 * date:2017/3/8 15:49
 * email:812219713@qq.com
 */

public class LogUtil {

    private static final boolean canPrint = true;

    public static void printf(String msg) {

        if (canPrint) {
            printf(msg, null);
        }
    }


    public static void printf(String msg, Exception e) {

        try {
            if (canPrint && !TextUtils.isEmpty(msg)) {
                Log.i(Utils.KF5_TAG, msg, e);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
