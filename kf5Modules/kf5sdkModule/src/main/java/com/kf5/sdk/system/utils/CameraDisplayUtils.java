package com.kf5.sdk.system.utils;

import android.app.Activity;
import android.content.Intent;

import com.chosen.cameraview.ui.CameraActivity;
import com.kf5.sdk.R;

/**
 * @author Chosen
 * @create 2019/1/16 12:18
 * @email 812219713@qq.com
 */
public class CameraDisplayUtils {

    /**
     * 拍照功能
     *
     * @param activity
     * @param requestCode
     */
    public static final void cameraDisplayOnlyPictureFeature(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(CameraActivity.CAMERA_FEATURE_TYPE, CameraActivity.CAMERA_FEATURE_PICTURE);
        activity.startActivityForResult(intent, requestCode);
//        activity.overridePendingTransition(com.chosen.cameraview.R.anim.kf5_camera_bottom_in, com.chosen.cameraview.R.anim.kf5_camera_bottom_silent);
    }


    /**
     * 摄像功能
     *
     * @param activity
     * @param requestCode
     */
    public static final void cameraDisplayOnlyRecorderFeature(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(CameraActivity.CAMERA_FEATURE_TYPE, CameraActivity.CAMERA_FEATURE_RECORDER);
        activity.startActivityForResult(intent, requestCode);
//        activity.overridePendingTransition(com.chosen.cameraview.R.anim.kf5_camera_bottom_in, com.chosen.cameraview.R.anim.kf5_camera_bottom_silent);
    }

    /**
     * 拍照摄像功能
     *
     * @param activity
     * @param requestCode
     */
    public static final void cameraDisplayBothFeatures(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(CameraActivity.CAMERA_FEATURE_TYPE, CameraActivity.CAMERA_FEATURE_BOTH);
        activity.startActivityForResult(intent, requestCode);
//        activity.overridePendingTransition(com.chosen.cameraview.R.anim.kf5_camera_bottom_in, com.chosen.cameraview.R.anim.kf5_camera_bottom_silent);
    }
}
