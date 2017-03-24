package com.kf5sdk.exam.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

import java.lang.ref.SoftReference;

/**
 * author:chosen
 * date:2016/10/18 11:32
 * email:812219713@qq.com
 */

public class Utils {

    public static String getAgent(SoftReference<Context> softReference) {

        String agent = "";
        try {
            String ua = System.getProperty("http.agent");
            String packageName = softReference.get().getPackageName();
            PackageInfo info = softReference.get().getPackageManager().getPackageInfo(packageName, 0);
            agent = ua + ", " + packageName + "/" + info.versionName + "呵呵，这里是我在测试";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return agent;
    }
}
