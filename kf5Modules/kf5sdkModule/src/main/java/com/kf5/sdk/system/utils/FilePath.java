package com.kf5.sdk.system.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kf5.sdk.system.entity.Field;

import java.io.File;

/**
 * author:chosen
 * date:2016/10/17 15:20
 * email:812219713@qq.com
 */

public class FilePath {

    private static final String SAVE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kf5_Chat";

    public static final String IMAGES_PATH = SAVE + "/Images/";

    public static final String SAVE_RECORDER = SAVE + "/recorder/";

    public static final String FILE = SAVE + "/Files/";

    public static String getCompressImageCacheDir(@NonNull Context context) {
        if (context == null) {
            throw new NullPointerException("context can't be null!");
        }
        return context.getCacheDir().getAbsolutePath();
    }

}
