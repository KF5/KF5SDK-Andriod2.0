package com.chosen.album.internal.utils;

import android.os.Build;

/**
 * @author Chosen
 * @create 2019/1/9 16:38
 * @email 812219713@qq.com
 */
public class Platform {

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

}
