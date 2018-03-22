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
    private static int LOG_MAXLENGTH = 2000;

    public static void printf(String msg, Exception e) {

        try {
            if (canPrint && !TextUtils.isEmpty(msg)) {
//                Log.i(Utils.KF5_TAG, msg, e);
                int strLength = msg.length();
                int start = 0;
                int end = LOG_MAXLENGTH;
                for (int i = 0; i < 100; i++) {
                    //剩下的文本还是大于规定长度则继续重复截取并输出
                    if (strLength > end) {
                        Log.i(Utils.KF5_TAG + i, msg.substring(start, end),e);
                        start = end;
                        end = end + LOG_MAXLENGTH;
                    } else {
                        Log.i(Utils.KF5_TAG, msg.substring(start, strLength),e);
                        break;
                    }
                }
            }
        } catch (Exception e1) {
           e1.printStackTrace();
//            printf("这里好像有异常",e1);
        }
    }

}
