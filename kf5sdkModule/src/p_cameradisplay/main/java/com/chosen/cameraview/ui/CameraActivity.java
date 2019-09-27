package com.chosen.cameraview.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.chosen.cameraview.JCameraView;
import com.chosen.cameraview.listener.ClickListener;
import com.chosen.cameraview.listener.ErrorListener;
import com.chosen.cameraview.listener.JCameraListener;
import com.chosen.cameraview.utils.FileUtil;
import com.kf5.sdk.R;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    private JCameraView jCameraView;

    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";
    public static final String PATH = "path";
    public static final String TYPE = "type";

    public static final String CAMERA_FEATURE_TYPE = "feature_type";
    public static final String CAMERA_FEATURE_BOTH = "feature_both";
    public static final String CAMERA_FEATURE_RECORDER = "feature_recorder";
    public static final String CAMERA_FEATURE_PICTURE = "feature_picture";

    private static final String CAPTURE_FILE_PATH = "Kf5_Chat/PHOTO";
    private static final String VIDEO_RECORD_PATH = "Kf5_Chat/VIDEO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.kf5_camera_activity_camera);
        jCameraView = findViewById(R.id.jcameraview);
        //设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + VIDEO_RECORD_PATH);
        String featureType = getIntent().getStringExtra(CAMERA_FEATURE_TYPE);
        if (TextUtils.equals(CAMERA_FEATURE_PICTURE, featureType)) {
            jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_CAPTURE);
        } else if (TextUtils.equals(CAMERA_FEATURE_RECORDER, featureType)) {
            jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);
        } else {
            jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        }
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_HIGH);
        jCameraView.setErrorListener(new ErrorListener() {
            @Override
            public void onError() {
                finish();
            }

            @Override
            public void AudioPermissionError() {

            }
        });
        jCameraView.setJCameraListener(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                String path = FileUtil.saveBitmap(CAPTURE_FILE_PATH, bitmap);
                Intent intent = new Intent();
                intent.putExtra(PATH, path);
                intent.putExtra(TYPE, TYPE_IMAGE);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                Intent intent = new Intent();
                intent.putExtra(PATH, url);
                intent.putExtra(TYPE, TYPE_VIDEO);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
//        else {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(option);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.kf5_camera_bottom_silent, R.anim.kf5_camera_bottom_out);
    }
}
