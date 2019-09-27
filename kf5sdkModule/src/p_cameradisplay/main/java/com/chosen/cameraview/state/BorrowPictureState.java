package com.chosen.cameraview.state;

import android.view.Surface;
import android.view.SurfaceHolder;

import com.chosen.cameraview.CameraInterface;
import com.chosen.cameraview.JCameraView;
import com.chosen.cameraview.utils.LogUtil;

/**
 * @author Chosen
 * @create 2018/12/3 10:36
 * @email 812219713@qq.com
 */
class BorrowPictureState implements State {

    private final String TAG = BorrowPictureState.class.getSimpleName();
    private CameraMachine machine;

    BorrowPictureState(CameraMachine machine) {
        this.machine = machine;
    }


    @Override
    public void start(SurfaceHolder holder, float screenProp) {
        CameraInterface.getInstance().doStartPreview(holder, screenProp);
        machine.setState(machine.getPreviewState());
    }

    @Override
    public void stop() {

    }


    @Override
    public void focus(float x, float y, CameraInterface.FocusCallback callback) {
    }

    @Override
    public void onSwitch(SurfaceHolder holder, float screenProp) {

    }

    @Override
    public void restart() {

    }

    @Override
    public void capture() {

    }

    @Override
    public void record(Surface surface, float screenProp) {

    }

    @Override
    public void stopRecord(boolean isShort, long time) {
    }

    @Override
    public void cancel(SurfaceHolder holder, float screenProp) {
        CameraInterface.getInstance().doStartPreview(holder, screenProp);
        machine.getView().resetState(JCameraView.TYPE_PICTURE);
        machine.setState(machine.getPreviewState());
    }

    @Override
    public void confirm() {
        machine.getView().confirmState(JCameraView.TYPE_PICTURE);
        machine.setState(machine.getPreviewState());
    }

    @Override
    public void zoom(float zoom, int type) {
        LogUtil.i(TAG, "zoom");
    }

    @Override
    public void flash(String mode) {

    }
}
