package com.kf5.sdk.system.utils;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.View;

import com.kf5.sdk.R;

/**
 * @author Chosen
 * @create 2019/3/29 14:22
 * @email 812219713@qq.com
 */
public class ClickUtils {

    private final static long DEFAULT_TIME = 1_000;

    public static boolean isInvalidClick(@NonNull View target) {
        return isInvalidClick(target, DEFAULT_TIME);
    }

    /**
     * @param target      targetView
     * @param defaultTime timeout
     * @return
     */
    public static boolean isInvalidClick(@NonNull View target, @IntRange(from = 0) long defaultTime) {
        final long curTimeStamp = System.currentTimeMillis();
        long lastClickTimeStamp;
        Object o = target.getTag(R.id.kf5_id_invalid_click);
        if (o == null) {
            target.setTag(R.id.kf5_id_invalid_click, curTimeStamp);
            return false;
        }
        lastClickTimeStamp = (Long) o;
        boolean isInvalid = curTimeStamp - lastClickTimeStamp < defaultTime;
        if (!isInvalid)
            target.setTag(R.id.kf5_id_invalid_click, curTimeStamp);
        return isInvalid;
    }

}
