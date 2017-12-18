package com.kf5.sdk.system.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * author:chosen
 * date:2017/12/11 11:08
 * email:812219713@qq.com
 */

public final class FileProviderUtil {

    /**
     * 兼容7.0拍照
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriFromFile(Context context, File file) {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".chosen.FileProvider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }
}
