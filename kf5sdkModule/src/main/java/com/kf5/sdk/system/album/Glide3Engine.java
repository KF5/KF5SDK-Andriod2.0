package com.kf5.sdk.system.album;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.chosen.album.engine.ImageEngine;

/**
 * @author Chosen
 * @create 2019/1/10 10:35
 * @email 812219713@qq.com
 *
 * {@link ImageEngine} implementation using Glide 3.
 * implementation 'com.github.bumptech.glide:glide:' + moduleRemote.glide
 */
public class Glide3Engine implements ImageEngine {
    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .asBitmap()// some .jpeg files are actually gif
//                .override(resize, resize)
//                .placeholder(placeholder)
//                .centerCrop()
//                .into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .asBitmap()// some .jpeg files are actually gif
//                .override(resize, resize)
//                .placeholder(placeholder)
//                .centerCrop()
//                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .override(resizeX, resizeY)
//                .priority(Priority.HIGH)
//                .fitCenter()
//                .into(imageView);
    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
//        Glide.with(context)
//                .load(uri)
//                .asGif()
//                .override(resizeX, resizeY)
//                .priority(Priority.HIGH)
//                .fitCenter()
//                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }
}
