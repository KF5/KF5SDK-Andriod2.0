package com.kf5.sdk.im.expression.utils;

import android.content.Context;

/**
 * author:chosen
 * date:2016/11/2 15:11
 * email:812219713@qq.com
 */

public class ImageBase {

    private static String getImageSourceFromDrawable(Context context, String sourceId) {
        return "android.resource://" + context.getPackageName() + "/drawable/" + sourceId;
    }

    private static String getImageSourceFromAssets(String name) {
        return "file:///android_asset/" + name;
    }

    private static String getImageSourceFromFile(String url) {
        return "file://" + url;
    }

    private static String getImageSourceFromRaw(Context context, String id) {
        return "android.resource://" + context.getPackageName() + "/raw/" + id;
    }

    private static String getImageSourceFromContentProvider(String name) {
        return "content://media/external/images/media/" + name;
    }

    public static String getImagePath(Scheme scheme, Context context, String name_or_path) {
        switch (scheme) {
            case ASSETS:
                return getImageSourceFromAssets(name_or_path);
            case CONTENT_PROVIDER:
                return getImageSourceFromContentProvider(name_or_path);
            case DRAWABLE:
                return getImageSourceFromDrawable(context, name_or_path);
            case FILE:
                return getImageSourceFromFile(name_or_path);
            case RAW:
                return getImageSourceFromRaw(context, name_or_path);
        }
        return null;
    }

    public enum Scheme {
        FILE, CONTENT_PROVIDER, ASSETS, DRAWABLE, RAW
    }
}
