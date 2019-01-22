package com.chosen.album.internal.utils;

import android.content.Context;

/**
 * @author Chosen
 * @create 2019/1/9 16:38
 * @email 812219713@qq.com
 */
public class UIUtils {

    public static int spanCount(Context context, int gridExpectedSize) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        float expected = (float) screenWidth / (float) gridExpectedSize;
        int spanCount = Math.round(expected);
        if (spanCount == 0) {
            spanCount = 1;
        }
        return spanCount;
    }

}
