package com.kf5.sdk.system.init;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kf5.sdk.system.utils.SPUtils;

/**
 * author:chosen
 * date:2016/10/14 18:04
 * email:812219713@qq.com
 */

public class KF5SDKInitializer {

//    private static KF5User mKF5User;
//
//    public static KF5User getKF5User() {
//        return mKF5User;
//    }
//
//    public static void setKF5User(KF5User mKF5User) {
//        if (mKF5User == null)
//            throw new IllegalArgumentException("KF5User can not be null");
//        if (TextUtils.isEmpty(mKF5User.getHelpAddress()))
//            throw new IllegalArgumentException("helpAddress can not be null");
//        KF5SDKInitializer.mKF5User = mKF5User;
//    }

    private static Context sContext;

    public static void init(@NonNull Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null!");
        }
        sContext = context.getApplicationContext();
        SPUtils.getInstance(context);
    }

    public static Context getContext() {
        return sContext;
    }
}
