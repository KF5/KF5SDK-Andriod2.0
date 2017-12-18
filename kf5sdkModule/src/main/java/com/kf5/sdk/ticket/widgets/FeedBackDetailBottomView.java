package com.kf5.sdk.ticket.widgets;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.expression.utils.ImageBase;
import com.kf5.sdk.system.base.BaseActivity;
import com.kf5.sdk.system.image.ImageSelectorActivity;
import com.kf5.sdk.system.utils.ImageLoaderManager;
import com.kf5.sdk.system.utils.ToastUtil;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.ActionSheetDialog;
import com.kf5.sdk.system.widget.DialogBox;
import com.kf5.sdk.ticket.amin.OutToBottomAnimation;
import com.kf5.sdk.ticket.ui.FeedBackDetailsActivity;
import com.kf5.sdk.ticket.widgets.api.FeedBackDetailBottomViewListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * author:chosen
 * date:2016/10/20 17:27
 * email:812219713@qq.com
 */

public class FeedBackDetailBottomView extends FrameLayout implements FeedBackDetailsActivity.BottomLayoutListener, View.OnClickListener {

    private RelativeLayout sendLayoutContainer;

    private RelativeLayout contentLayout;

    private ImageView imgShowImageLayout;

    private TextView tvSubmit;

    private EditText editText;

    private LinearLayout imageLayout;

    private ImageView imgSelectImg;

    private ImageView imgBackToContentLayout;

    private LinearLayout layoutImgContainer;

    private TextView tvReplace;

    private List<File> fileList = new ArrayList<>();

    private FeedBackDetailsActivity feedBackDetailsActivity;

    private static final String[] CAMERA_PERMISSIONS = new String[]{Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE};

    private static final String[] WRITE_EXTERNAL_STORAGE_PERMISSION = new String[]{WRITE_EXTERNAL_STORAGE};

    /**
     * 绑定监听事件
     *
     * @param listener
     */
    public void setListener(FeedBackDetailBottomViewListener listener) {
        this.listener = listener;
    }

    private FeedBackDetailBottomViewListener listener;

    public FeedBackDetailBottomView(Context context) {
        this(context, null);
    }

    public FeedBackDetailBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        if (context instanceof FeedBackDetailsActivity) {
            feedBackDetailsActivity = (FeedBackDetailsActivity) context;
            feedBackDetailsActivity.setLayoutListener(this);
        } else {
            throw new IllegalArgumentException("this view belongs to FeedBackDetailsActivity");
        }
        initWidgets();
        bindListener();
    }

    private void initWidgets() {
        inflate(getContext(), R.layout.kf5_order_detail_bottom_layout, this);
        sendLayoutContainer = (RelativeLayout) findViewById(R.id.kf5_send_layout);
        contentLayout = (RelativeLayout) findViewById(R.id.kf5_send_content_layout);
        imgShowImageLayout = (ImageView) findViewById(R.id.kf5_activity_feed_back_choice_img);
        tvSubmit = (TextView) findViewById(R.id.kf5_activity_feed_back_submit);
        editText = (EditText) findViewById(R.id.kf5_activity_feed_back_content);
        imageLayout = (LinearLayout) findViewById(R.id.kf5_image_layout);
        imgSelectImg = (ImageView) findViewById(R.id.kf5_activity_feed_back_select_img);
        imgBackToContentLayout = (ImageView) findViewById(R.id.kf5_activity_feed_back_back_img);
        layoutImgContainer = (LinearLayout) findViewById(R.id.kf5_image_container_layout);
        tvReplace = (TextView) findViewById(R.id.kf5_activity_feed_back_replace_tv);
    }

    private void bindListener() {
        imgShowImageLayout.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        imgSelectImg.setOnClickListener(this);
        imgBackToContentLayout.setOnClickListener(this);
        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!editText.hasFocus())
                    editText.setFocusableInTouchMode(true);
                return false;
            }
        });

    }

    @Override
    public void onFeedBackDetailsActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case BaseActivity.CAMERA_STATE:
                if (feedBackDetailsActivity.hasPermission(CAMERA_PERMISSIONS))
                    feedBackDetailsActivity.takePictureFromCamera();
                break;
            case BaseActivity.WRITE_EXTERNAL_STORAGE:
                if (feedBackDetailsActivity.hasPermission(WRITE_EXTERNAL_STORAGE_PERMISSION))
                    feedBackDetailsActivity.getPictureFromGallery(6 - fileList.size());
                break;
            default:
                if (resultCode == Activity.RESULT_OK) {
                    switch (requestCode) {
                        case BaseActivity.CAMERA:
                            try {
                                if (feedBackDetailsActivity.picFile != null && feedBackDetailsActivity.picFile.exists()) {
                                    fileList.add(feedBackDetailsActivity.picFile);
                                    layoutImgContainer.addView(getItemImageView(feedBackDetailsActivity.picFile.getAbsolutePath()));
                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                    Uri uri = Uri.fromFile(feedBackDetailsActivity.picFile);
                                    intent.setData(uri);
                                    getContext().sendBroadcast(intent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case BaseActivity.GALLERY:
                            try {
                                if (data != null) {
                                    List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                                    if (pathList != null) {
                                        for (int i = 0; i < pathList.size(); i++) {
                                            String path = pathList.get(i);
                                            if (!TextUtils.isEmpty(path)) {
                                                File file = new File(path);
                                                if (file.exists()) {
                                                    fileList.add(file);
                                                    layoutImgContainer.addView(getItemImageView(file.getAbsolutePath()));
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                break;
        }
    }

    @Override
    public void onSubmitDataSuccess() {
        fileList.clear();
        layoutImgContainer.removeAllViews();
        editText.setText("");
        imgShowImageLayout.setEnabled(true);
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

    @Override
    public List<File> getFileList() {
        return fileList;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.kf5_activity_feed_back_choice_img) {
            Utils.hideSoftInput(getContext(), editText);
            imgShowImageLayout.setAnimation(new OutToBottomAnimation(getContext(), contentLayout, imageLayout));
        } else if (id == R.id.kf5_activity_feed_back_submit) {
            String content = editText.getText().toString();
            if (!TextUtils.isEmpty(content) && !TextUtils.equals(content.trim(), "")) {
                if (listener != null) {
                    imgShowImageLayout.setEnabled(false);
                    listener.submitData();
                }
            } else {
                ToastUtil.showToast(getContext(), getContext().getString(R.string.kf5_editcontent_hint));
            }
        } else if (id == R.id.kf5_activity_feed_back_select_img) {
            dealSelectImage();
        } else if (id == R.id.kf5_activity_feed_back_back_img) {
            imgBackToContentLayout.setAnimation(new OutToBottomAnimation(feedBackDetailsActivity, imageLayout, contentLayout));
        }
    }

    /**
     * 当工单状态为已关闭时，设置该TextView可见
     * 设置发送按钮
     */
    public void setTvReplaceVisible() {
        if (tvReplace != null && !tvReplace.isShown())
            tvReplace.setVisibility(VISIBLE);
        if (sendLayoutContainer != null && sendLayoutContainer.isShown())
            sendLayoutContainer.setVisibility(GONE);
    }


    private void dealSelectImage() {
        if (fileList.size() < 6) {
            new ActionSheetDialog(feedBackDetailsActivity)
                    .builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem(getContext().getString(R.string.kf5_from_camera), ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    if (feedBackDetailsActivity.hasPermission(CAMERA_PERMISSIONS))
                                        feedBackDetailsActivity.takePictureFromCamera();
                                    else
                                        feedBackDetailsActivity.applyPermissions(BaseActivity.CAMERA_STATE, BaseActivity.METHOD_REQUEST_PERMISSION, CAMERA_PERMISSIONS);
                                }
                            })
                    .addSheetItem(getContext().getString(R.string.kf5_from_gallery), ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    if (feedBackDetailsActivity.hasPermission(WRITE_EXTERNAL_STORAGE_PERMISSION))
                                        feedBackDetailsActivity.getPictureFromGallery(6 - fileList.size());
                                    else
                                        feedBackDetailsActivity.applyPermissions(BaseActivity.WRITE_EXTERNAL_STORAGE, BaseActivity.METHOD_REQUEST_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION);
                                }
                            }).show();
        } else {
            ToastUtil.showToast(getContext(), getContext().getString(R.string.kf5_file_limit_hint));
        }
    }

    private View getItemImageView(String path) {

        LinearLayout layout = (LinearLayout) LayoutInflater.from(feedBackDetailsActivity).inflate(R.layout.kf5_item_imageview, null, false);
        ImageView imageView = (ImageView) layout.findViewById(R.id.kf5_imageview);
        ImageLoaderManager.getInstance(getContext()).displayImage(ImageBase.getImagePath(ImageBase.Scheme.FILE, getContext(), path), imageView);
        imageView.setOnClickListener(new ImageClickListener(getContext(), layout, path));
        return layout;
    }


    private class ImageClickListener implements View.OnClickListener {

        private View view;

        private String path;

        private Context mContext;

        public ImageClickListener(Context mContext, View view, String path) {
            super();
            this.mContext = mContext;
            this.view = view;
            this.path = path;
        }

        @Override
        public void onClick(View v) {

            new DialogBox(mContext)
                    .setMessage(getContext().getString(R.string.kf5_delete_file_hint))
                    .setLeftButton(getContext().getString(R.string.kf5_cancel), null)
                    .setRightButton(getContext().getString(R.string.kf5_delete), new DialogBox.onClickListener() {
                        @Override
                        public void onClick(DialogBox dialog) {
                            layoutImgContainer.removeView(view);
                            fileList.remove(new File(path));
                        }
                    }).show();
        }
    }


}
