package com.kf5.sdk.system.utils;

import android.os.Environment;

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
}
