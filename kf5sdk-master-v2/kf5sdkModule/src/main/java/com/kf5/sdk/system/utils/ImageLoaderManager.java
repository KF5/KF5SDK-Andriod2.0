package com.kf5.sdk.system.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.kf5.sdk.R;
import com.kf5.sdk.im.utils.ImageUtils;

/**
 * author:chosen
 * date:2016/11/2 14:40
 * email:812219713@qq.com
 * 图片加载管理类
 */

public class ImageLoaderManager {

    protected final Context context;

    private volatile static ImageLoaderManager instance;

    private ImageLoaderManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static ImageLoaderManager getInstance(Context context) {
        if (instance == null) {
            synchronized (ImageLoaderManager.class) {
                if (instance == null) {
                    instance = new ImageLoaderManager(context);
                }
            }
        }
        return new ImageLoaderManager(context);
    }

    /**
     * 暂停加载
     */
    public void pause() {
        Glide.with(context).pauseRequests();
    }

    /**
     * 恢复加载
     */
    public void onResume() {
        Glide.with(context).resumeRequests();
    }

    /**
     * 清楚缓存
     */
    public void clearMemory() {
        Glide.get(context).clearMemory();
    }

    public void displayImage(String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .thumbnail(0.1f)
                .placeholder(R.drawable.kf5_image_loading)
                .error(R.drawable.kf5_image_loading_failed)
                .into(imageView);
    }

    public void displayImage(int id, ImageView imageView) {
        Glide.with(context)
                .load(id)
                .asBitmap()
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.kf5_image_loading)
                .into(imageView);
    }

    public void displayImage(String url, ImageView imageView, RequestListener<String, Bitmap> requestListener) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.kf5_image_loading)
                .error(R.drawable.kf5_image_loading_failed)
                .listener(requestListener)
                .into(imageView);
    }

    public void displayImage(String url, ImageView imageView, int width, int height, RequestListener<String, Bitmap> requestListener) {

        ImageUtils.ImageSize imageSize = ImageUtils.getImageSize(context, width, height);
        Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.kf5_image_loading)
                .error(R.drawable.kf5_image_loading_failed)
                .listener(requestListener)
                .override(imageSize.width, imageSize.height)
                .into(imageView);
    }


}
