package com.chosen.cameraview.listener;

/**
 * @author Chosen
 * @create 2018/12/3 10:18
 * @email 812219713@qq.com
 */
public interface CaptureListener {

    void takePictures();

    void recordShort(long time);

    void recordStart();

    void recordEnd(long time);

    void recordZoom(float zoom);

    void recordError();
}
