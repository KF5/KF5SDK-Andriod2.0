package com.chosen.album.engine.impl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.chosen.album.engine.ImageEngine;
import com.chosen.album.internal.utils.PathUtils;

/**
 * @author Chosen
 * @create 2019/1/9 16:40
 * @email 812219713@qq.com
 * <p>
 * {@link ImageEngine} implementation using Glide.
 */
public class GlideEngine implements ImageEngine {

    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .asBitmap()  // some .jpeg files are actually gif
                .placeholder(placeholder)
                .override(resize, resize)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView,
                                 Uri uri) {
        Glide.with(context)
                .load(uri)
                .asBitmap()
                .placeholder(placeholder)
                .override(resize, resize)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .override(resizeX, resizeY)
                .priority(Priority.HIGH)
                .fitCenter()
                .into(imageView);
    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .asGif()
                .override(resizeX, resizeY)
                .priority(Priority.HIGH)
                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }

}
