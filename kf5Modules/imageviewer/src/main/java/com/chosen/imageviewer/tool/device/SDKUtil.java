package com.chosen.imageviewer.tool.device;

import android.os.Build;

/**
 * @author Chosen
 * @create 2018/12/17 11:40
 * @email 812219713@qq.com
 */
public class SDKUtil {

    public static boolean haveIceCream14() {
        return Build.VERSION.SDK_INT >= 14;
    }

    public static boolean haveJellyBeanMr117() {
        return Build.VERSION.SDK_INT >= 17;
    }

    public static boolean haveKitkat19() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static boolean haveLollipop21() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public static boolean haveMarshmallow23() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static boolean haveOreo27() {
        return Build.VERSION.SDK_INT >= 27;
    }

}
