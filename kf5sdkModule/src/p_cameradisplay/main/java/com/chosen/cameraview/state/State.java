package com.chosen.cameraview.state;

import android.view.Surface;
import android.view.SurfaceHolder;

import com.chosen.cameraview.CameraInterface;

/**
 * @author Chosen
 * @create 2018/12/3 10:27
 * @email 812219713@qq.com
 */
public interface State {

    void start(SurfaceHolder holder, float screenProp);

    void stop();

    void focus(float x, float y, CameraInterface.FocusCallback callback);

    void onSwitch(SurfaceHolder holder, float screenProp);

    void restart();

    void capture();

    void record(Surface surface, float screenProp);

    void stopRecord(boolean isShort, long time);

    void cancel(SurfaceHolder holder, float screenProp);

    void confirm();

    void zoom(float zoom, int type);

    void flash(String mode);
}
