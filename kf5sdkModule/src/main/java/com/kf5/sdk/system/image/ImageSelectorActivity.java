package com.kf5.sdk.system.image;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kf5.sdk.R;

import java.io.File;
import java.util.ArrayList;

public class ImageSelectorActivity extends FragmentActivity implements ImageSelectorFragment.Callback, View.OnClickListener {

    /**
     * 最大图片选择次数，int类型，默认9
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，默认多选
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，默认显示
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";
    /**
     * 默认选择集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<>();

    private int mDefaultCount;

    private Button mButton;

    private ImageView mImageViewBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kf5_activity_image_selector);
        getIntentAndInitFragment();
        mButton = (Button) findViewById(R.id.kf5_right_text_view);
        mButton.setOnClickListener(this);
        mImageViewBack = (ImageView) findViewById(R.id.kf5_return_img);
        mImageViewBack.setOnClickListener(this);
    }

    private void getIntentAndInitFragment() {

        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 6);
        int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(ImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(ImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putStringArrayList(ImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.kf5_image_framelayout, Fragment.instantiate(this, ImageSelectorFragment.class.getName(), bundle))
                .commit();
        initView();
    }

    public void initView() {

        if (resultList == null || resultList.size() <= 0) {
        } else {
            updateDoneText();
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mButton) {
            if (resultList != null && resultList.size() > 0) {
                Intent data = new Intent();
                data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                setResult(RESULT_OK, data);
                finish();
            }
        }else if (v == mImageViewBack){
            finish();
        }
    }

    private void updateDoneText() {
        mButton.setText(String.format("%s(%d/%d)",
                getString(R.string.kf5_action_done), resultList.size(), mDefaultCount));
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if (resultList.size() > 0) {
            updateDoneText();
            if (!mButton.isShown())
                mButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
        }
        updateDoneText();
        // 当为选择图片时候的状态
        if (resultList.size() == 0) {
            mButton.setVisibility(View.INVISIBLE);
            mButton.setText(getString(R.string.kf5_action_done));
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            // notify system
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));
            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
