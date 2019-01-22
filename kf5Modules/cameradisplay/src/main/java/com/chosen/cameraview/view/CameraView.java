package com.chosen.cameraview.view;

import android.graphics.Bitmap;

/**
 * @author Chosen
 * @create 2018/12/3 10:31
 * @email 812219713@qq.com
 */
public interface CameraView {

    void resetState(int type);

    void confirmState(int type);

    void showPicture(Bitmap bitmap, boolean isVertical);

    void playVideo(Bitmap firstFrame, String url);

    void stopVideo();

    void setTip(String tip);

    void startPreviewCallback();

    boolean handlerFocus(float x, float y);
}
