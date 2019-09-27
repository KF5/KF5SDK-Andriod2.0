package com.chosen.cameraview.listener;

import android.graphics.Bitmap;

/**
 * @author Chosen
 * @create 2018/12/3 10:19
 * @email 812219713@qq.com
 */
public interface JCameraListener {

    void captureSuccess(Bitmap bitmap);

    void recordSuccess(String url, Bitmap firstFrame);
}
