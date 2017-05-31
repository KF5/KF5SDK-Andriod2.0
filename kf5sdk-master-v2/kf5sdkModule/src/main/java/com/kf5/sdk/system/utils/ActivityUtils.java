package com.kf5.sdk.system.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * author:chosen
 * date:2017/5/31 14:21
 * email:812219713@qq.com
 */

public final class ActivityUtils {

    private ActivityUtils() {

    }

    /*判断应用是否在前台*/
    public static boolean isForeground(Context context) {
        if (context==null)return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
