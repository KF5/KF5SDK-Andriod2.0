package com.kf5.sdk.system.utils;

import android.graphics.Bitmap;

import com.kf5.sdk.system.entity.Field;

import java.io.File;
import java.io.FileOutputStream;

/**
 * author:chosen
 * date:2016/11/4 18:05
 * email:812219713@qq.com
 */

public class ByteArrayUtil {
    /**
     * 将bitmap保存为图片
     *
     * @param mBitmap 要保存的bitmap
     * @param type    压缩方式
     * @param file    目标文件
     */
    public static void cacheBitmapToSDCard(Bitmap mBitmap, String type, File file) {

        try {

            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            } else {
                file.getParentFile().mkdirs();
            }
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            if (Field.PNG.equalsIgnoreCase(type)) {
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            } else {
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            }
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
