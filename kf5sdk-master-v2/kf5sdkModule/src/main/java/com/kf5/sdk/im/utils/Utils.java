package com.kf5.sdk.im.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/26 14:54
 * email:812219713@qq.com
 */

public class Utils {

    public static int getImageMaxEdge(Context context) {
        //		return (int) (165.0 / 320.0 * ScreenUtil.screenWidth);
        return (int) (165.0 / 320.0 * context.getResources().getDisplayMetrics().widthPixels);
    }

    public static int getImageMinEdge(Context context) {
        //		return (int) (76.0 / 320.0 * ScreenUtil.screenWidth);
        return (int) (76.0 / 320.0 * context.getResources().getDisplayMetrics().widthPixels);
    }

    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        try {
            Resources rs = context.getResources();
            int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
            if (id > 0) {
                navigationBarHeight = rs.getDimensionPixelSize(id);
            }
        } catch (Exception e) {
            // default 0
        }
        return navigationBarHeight;
    }

    /**
     * 格式化Map参数
     *
     * @param params
     * @return
     */
    public static String getMapAppend(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params == null)
            return "";
        Iterator<String> iter = params.keySet().iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            if (!TextUtils.isEmpty(params.get(name)) && !TextUtils.isEmpty(params.get(name).trim()))
                sb.append(name).append("=").append(params.get(name)).append("&");
        }
        return sb.toString();
    }


}
