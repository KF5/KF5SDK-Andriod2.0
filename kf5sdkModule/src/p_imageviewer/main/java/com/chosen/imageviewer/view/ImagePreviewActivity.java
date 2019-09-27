package com.chosen.imageviewer.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chosen.imageviewer.ImagePreview;
import com.chosen.imageviewer.bean.ImageInfo;
import com.chosen.imageviewer.glide.FileTarget;
import com.chosen.imageviewer.glide.ImageLoader;
import com.chosen.imageviewer.glide.progress.OnProgressListener;
import com.chosen.imageviewer.glide.progress.ProgressManager;
import com.chosen.imageviewer.tool.common.HandlerUtils;
import com.chosen.imageviewer.tool.image.DownloadPictureUtil;
import com.chosen.imageviewer.tool.ui.MyToast;
import com.kf5.sdk.R;

import java.io.File;
import java.util.List;

public class ImagePreviewActivity extends AppCompatActivity implements Handler.Callback, View.OnClickListener {

    private static final String TAG = "ImagePreview";

    private Context context;

    private List<ImageInfo> imageInfoList;
    private int currentItem;//当前显示的图片索引
    private boolean isShowDownButton;
    private boolean isShowCloseButton;
    private boolean isShowOriginButton;
    private boolean isShowIndicator;

    private ImagePreviewAdapter imagePreviewAdapter;
    private HackyViewPager viewPager;
    private TextView tv_indicator;
    private FrameLayout fm_image;
    private TextView tv_show_origin;
    private ImageView img_download;
    private ImageView imgCloseButton;
    private View rootView;

    //指示器显示状态
    private boolean indicatorStatus = false;
    //原图按钮显示状态
    private boolean originalStatus = false;
    //下载按钮显示状态
    private boolean downloadButtonStatus = false;
    //关闭按钮显示状态
    private boolean closeButtonStatus = false;

    private String currentItemOriginalPathUrl = "";//当前显示的原图链接
    private HandlerUtils.HandlerHolder handlerHolder;

    public static void activityStart(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(context, ImagePreviewActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.kf5_imageviewer_fade_in, R.anim.kf5_imageviewer_fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kf5_imageviewer_activity_image_preview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        context = this;
        handlerHolder = new HandlerUtils.HandlerHolder(this);
        imageInfoList = ImagePreview.getInstance().getImageInfoList();
        if (imageInfoList == null || imageInfoList.size() == 0) {
            onBackPressed();
        }
        currentItem = ImagePreview.getInstance().getIndex();
        isShowDownButton = ImagePreview.getInstance().isShowDownButton();
        isShowCloseButton = ImagePreview.getInstance().isShowCloseButton();
        isShowIndicator = ImagePreview.getInstance().isShowIndicator();

        currentItemOriginalPathUrl = imageInfoList.get(currentItem).getOriginUrl();
        isShowOriginButton = ImagePreview.getInstance().isShowOriginButton(currentItem);
        if (isShowOriginButton) {
            //检查缓存是否存在
            checkCache(currentItemOriginalPathUrl);
        }

        rootView = findViewById(R.id.rootView);
        viewPager = findViewById(R.id.viewPager);
        tv_indicator = findViewById(R.id.tv_indicator);
        fm_image = findViewById(R.id.fm_image);
        tv_show_origin = findViewById(R.id.tv_show_origin);
        img_download = findViewById(R.id.img_download);
        imgCloseButton = findViewById(R.id.imgCloseButton);
        img_download.setImageResource(ImagePreview.getInstance().getDownIconResId());
        imgCloseButton.setImageResource(ImagePreview.getInstance().getCloseIconResId());
        //关闭页面按钮
        imgCloseButton.setOnClickListener(this);
        //查看原图按钮
        tv_show_origin.setOnClickListener(this);
        //下载图片按钮
        img_download.setOnClickListener(this);
        if (!isShowIndicator) {
            tv_indicator.setVisibility(View.GONE);
            indicatorStatus = false;
        } else {
            if (imageInfoList.size() > 1) {
                tv_indicator.setVisibility(View.VISIBLE);
                indicatorStatus = true;
            } else {
                tv_indicator.setVisibility(View.GONE);
                indicatorStatus = false;
            }
        }

        if (isShowDownButton) {
            img_download.setVisibility(View.VISIBLE);
            downloadButtonStatus = true;
        } else {
            img_download.setVisibility(View.GONE);
            downloadButtonStatus = false;
        }

        if (isShowCloseButton) {
            imgCloseButton.setVisibility(View.VISIBLE);
            closeButtonStatus = true;
        } else {
            imgCloseButton.setVisibility(View.GONE);
            closeButtonStatus = false;
        }

        //更新进度指示器
        tv_indicator.setText(String.format("%1$s/%2$s", currentItem + 1 + "", "" + imageInfoList.size()));

        imagePreviewAdapter = new ImagePreviewAdapter(this, imageInfoList);
        viewPager.setAdapter(imagePreviewAdapter);
        viewPager.setCurrentItem(currentItem);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                currentItemOriginalPathUrl = imageInfoList.get(position).getOriginUrl();
                isShowOriginButton = ImagePreview.getInstance().isShowOriginButton(currentItem);
                if (isShowOriginButton) {
                    //检查缓存是否存在
                    checkCache(currentItemOriginalPathUrl);
                }
                //更新进度指示器
                tv_indicator.setText(String.format("%1$s/%2$s", currentItem + 1 + "", "" + imageInfoList.size()));
            }
        });
    }

    private void downloadCurrentImg() {
//        String path = Environment.getExternalStorageDirectory() + "/" + downloadFolderName + "/";
//        String suffix = currentItemOriginalPathUrl.substring(currentItemOriginalPathUrl.lastIndexOf('.') + 1, currentItemOriginalPathUrl.length());
        //suffix后缀名，有的时候url的不是以.jpg/.png这种格式结尾，先暂时默认保存为.png吧。。。。。。
//        String fileName;
//        if (currentItemOriginalPathUrl.startsWith("http:") || currentItemOriginalPathUrl.startsWith("https")) {
//            fileName = downloadFolderName + MD5Utils.md5Encode(currentItemOriginalPathUrl) + ".png";
//        } else {
//            fileName = currentItemOriginalPathUrl;
//        }
//        File file = new File(fileName);
//        if (file.exists()) {
//            Toast.makeText(context, String.format(getResources().getString(R.string.kf5_imageviewer_file_location_hint), file.getAbsolutePath()), Toast.LENGTH_SHORT).show();
//            return;
//        }
//        FileUtil.createOrExistsFile(file);
//        DownloadPictureUtil.downloadPicture(context, currentItemOriginalPathUrl, downloadFolderName, file.getName());
        DownloadPictureUtil.downloadPicture(context.getApplicationContext(), currentItemOriginalPathUrl);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.kf5_imageviewer_fade_in, R.anim.kf5_imageviewer_fade_out);
    }

    public int convertPercentToBlackAlphaColor(float percent) {
        percent = Math.min(1, Math.max(0, percent));
        int intAlpha = ((int) (percent * 255));
        String stringAlpha = Integer.toHexString(intAlpha).toLowerCase();
        String color = "#" + (stringAlpha.length() < 2 ? "0" : "") + stringAlpha + "000000";
        return Color.parseColor(color);
    }

    public void setAlpha(float alpha) {
        int colorId = convertPercentToBlackAlphaColor(alpha);
        rootView.setBackgroundColor(colorId);
        if (alpha >= 1) {
            if (indicatorStatus) {
                tv_indicator.setVisibility(View.VISIBLE);
            }
            if (originalStatus) {
                fm_image.setVisibility(View.VISIBLE);
            }
            if (downloadButtonStatus) {
                img_download.setVisibility(View.VISIBLE);
            }
            if (closeButtonStatus) {
                imgCloseButton.setVisibility(View.VISIBLE);
            }
        } else {
            tv_indicator.setVisibility(View.GONE);
            fm_image.setVisibility(View.GONE);
            img_download.setVisibility(View.GONE);
            imgCloseButton.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 0) {
            //查看原图
            final String path = imageInfoList.get(currentItem).getOriginUrl();
            visible();
            tv_show_origin.setText("0 %");
            if (checkCache(path)) {
                Message message = handlerHolder.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("url", path);
                message.what = 1;
                message.obj = bundle;
                handlerHolder.sendMessage(message);
                return true;
            }

            Glide.with(context)
                    .downloadOnly().load(path)
                    .into(new FileTarget());

            ProgressManager.addListener(path, new OnProgressListener() {
                @Override
                public void onProgress(String url, boolean isComplete, int percentage, long bytesRead, long totalBytes) {
                    if (isComplete) {
                        //加载完成
                        Message message = handlerHolder.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        message.what = 1;
                        message.obj = bundle;
                        handlerHolder.sendMessage(message);
                    } else {
                        //加载中
                        Message message = handlerHolder.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        bundle.putInt("progress", percentage);
                        message.what = 2;
                        message.obj = bundle;
                        handlerHolder.sendMessage(message);
                    }
                }
            });
        } else if (msg.what == 1) {
            //加载完成
            Bundle bundle = ((Bundle) msg.obj);
            String url = bundle.getString("url");
            gone();
            if (currentItem == getRealIndexWithPath(url)) {
                imagePreviewAdapter.loadOrigin(currentItem);
            }
        } else if (msg.what == 2) {
            //加载中
            Bundle bundle = ((Bundle) msg.obj);
            String url = bundle.getString("url");
            int progress = bundle.getInt("progress");
            if (currentItem == getRealIndexWithPath(url)) {
                visible();
                tv_show_origin.setText(progress + " %");
            }
        } else if (msg.what == 3) {
            tv_show_origin.setText(R.string.kf5_imageviewer_view_original_img);
            fm_image.setVisibility(View.GONE);
            originalStatus = false;
        } else if (msg.what == 4) {
            fm_image.setVisibility(View.VISIBLE);
            originalStatus = true;
        }
        return true;
    }

    private int getRealIndexWithPath(String path) {
        for (int i = 0; i < imageInfoList.size(); i++) {
            if (path.equalsIgnoreCase(imageInfoList.get(i).getOriginUrl())) {
                return i;
            }
        }
        return 0;
    }

    private boolean checkCache(String url) {
        gone();
        File cacheFile = ImageLoader.getGlideCacheFile(context, url);
        if (cacheFile != null && cacheFile.exists()) {
            gone();
            return true;
        } else {
            visible();
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_download) {
            //检查权限
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //拒绝权限
                    MyToast.getInstance()._short(context, context.getResources().getString(R.string.kf5_imageviewer_permission_denied_hint));
                } else {
                    //申请权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            } else {
                downloadCurrentImg();
            }
        } else if (i == R.id.tv_show_origin) {
            handlerHolder.sendEmptyMessage(0);
        } else if (i == R.id.imgCloseButton) {
            onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    downloadCurrentImg();
                } else {
                    MyToast.getInstance()._short(context, context.getResources().getString(R.string.kf5_imageviewer_permission_denied_hint));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImagePreview.getInstance().reset();
        if (imagePreviewAdapter != null) {
            imagePreviewAdapter.closePage();
        }
    }

    private void gone() {
        handlerHolder.sendEmptyMessage(3);
    }

    private void visible() {
        handlerHolder.sendEmptyMessage(4);
    }
}
