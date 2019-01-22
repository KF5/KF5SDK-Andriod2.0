package com.kf5.sdk.system.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.kf5.sdk.R;

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
        return instance;
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
        //For glide3
//        Glide.with(context)
//                .load(url)
//                .thumbnail(0.1f)
//                .placeholder(R.drawable.kf5_image_loading)
//                .error(R.drawable.kf5_image_loading_failed)
//                .into(imageView);

        //For glide4
        Glide.with(context)
                .load(url)
                .thumbnail(0.1f)
                .apply(buildCommonRequestOptions())
                .into(imageView);
    }

    public void displayImage(int id, ImageView imageView) {
        //For glide 3
//        Glide.with(context)
//                .load(id)
//                .asBitmap()
//                .thumbnail(0.1f)
//                .placeholder(R.drawable.kf5_image_loading)
//                .into(imageView);

        //For glide4
        Glide.with(context)
                .asBitmap()
                .load(id)
                .thumbnail(0.1f)
                .apply(buildCommonRequestOptions())
                .into(imageView);
    }


    //For glide3
//    public void displayImage(String url, ImageView imageView, RequestListener<String, Bitmap> requestListener) {
//
//        Glide.with(context)
//                .load(url)
//                .asBitmap()
//                .thumbnail(0.1f)
//                .placeholder(R.drawable.kf5_image_loading)
//                .error(R.drawable.kf5_image_loading_failed)
//                .listener(requestListener)
//                .into(imageView);
//    }


    //For glide4
    public void displayImage(String url, ImageView imageView, RequestListener<Bitmap> requestListener) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .thumbnail(0.1f)
                .apply(buildCommonRequestOptions())
                .listener(requestListener)
                .into(imageView);
    }

    private static RequestOptions buildCommonRequestOptions() {
        return new RequestOptions().placeholder(R.drawable.kf5_image_loading).error(R.drawable.kf5_image_loading_failed);
    }
}
