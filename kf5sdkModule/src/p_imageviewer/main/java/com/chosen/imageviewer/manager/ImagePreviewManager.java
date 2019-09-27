package com.chosen.imageviewer.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.chosen.imageviewer.ImagePreview;
import com.chosen.imageviewer.bean.ImageInfo;
import com.kf5.sdk.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Chosen
 * @create 2019/1/16 15:26
 * @email 812219713@qq.com
 */
public class ImagePreviewManager {

    private ImagePreviewManager() {
    }


    /**
     * @param downloadFilePath 缓存图片路径
     * @param canDownload      是否显示缓存按钮
     */
    public static void startPreviewImage(@NonNull Context context, @NonNull String downloadFilePath, boolean canDownload, @NonNull String... urls) {
        startPreviewImage(context, downloadFilePath, canDownload, 0, Arrays.asList(urls));
    }

    /**
     * @param downloadFilePath 缓存图片路径
     * @param canDownload      是否显示缓存按钮
     * @param index            索引
     */
    public static void startPreviewImage(@NonNull Context context, @NonNull String downloadFilePath, boolean canDownload, int index, @NonNull String... urls) {
        startPreviewImage(context, downloadFilePath, canDownload, index, Arrays.asList(urls));
    }

    /**
     * @param downloadFilePath 缓存图片路径
     * @param canDownload      是否显示缓存按钮
     */
    public static void startPreviewImage(@NonNull Context context, @NonNull String downloadFilePath, boolean canDownload, @NonNull List<String> pathList) {
        startPreviewImage(context, downloadFilePath, canDownload, 0, pathList);
    }

    /**
     * @param downloadFilePath 缓存图片路径
     * @param canDownload      是否显示缓存按钮
     * @param index            索引
     */
    public static void startPreviewImage(@NonNull Context context, @NonNull String downloadFilePath, boolean canDownload, int index, @NonNull List<String> pathList) {
        if (context == null || pathList == null || pathList.isEmpty()) {
            Log.i("ImagePreview", (context == null) + "=========" + (pathList == null));
            return;
        }

        List<ImageInfo> imageInfoList = new ArrayList<>();
        for (String url : pathList) {
            ImageInfo info = new ImageInfo();
            info.setOriginUrl(url);
            info.setThumbnailUrl(url);
            imageInfoList.add(info);
        }
        ImagePreview
                .getInstance()
                .setContext(context)
                .setIndex(Math.max(index, 0))
                .setImageInfoList(imageInfoList)
                .setLoadStrategy(ImagePreview.LoadStrategy.AlwaysOrigin)
                .setFolderName(downloadFilePath)
                .setScaleLevel(1, 2, 4)
                .setZoomTransitionDuration(300)
                .setEnableClickClose(true)// 是否启用点击图片关闭。默认启用
                .setEnableDragClose(false)// 是否启用上拉/下拉关闭。默认不启用
                .setShowCloseButton(false)// 是否显示关闭页面按钮，在页面左下角。默认显示
                .setCloseIconResId(R.drawable.kf5_imageviewer_ic_action_close)// 设置关闭按钮图片资源，可不填，默认为：R.drawable.kf5_imageviewer_ic_action_close
                .setShowDownButton(canDownload)// 是否显示下载按钮，在页面右下角。默认显示
                .setDownIconResId(R.drawable.kf5_imageviewer_icon_download_new)// 设置下载按钮图片资源，可不填，默认为：R.drawable.kf5_imageviewer_icon_download_new
                .setShowIndicator(true)// 设置是否显示顶部的指示器（1/9）。默认显示
                .start();
    }

}
