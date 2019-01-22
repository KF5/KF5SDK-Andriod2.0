package com.chosen.imageviewer.tool.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chosen.imageviewer.R;
import com.chosen.imageviewer.tool.file.FileUtil;
import com.chosen.imageviewer.tool.file.SingleMediaScanner;
import com.chosen.imageviewer.tool.ui.MyToast;

import java.io.File;

/**
 * @author Chosen
 * @create 2018/12/17 11:47
 * @email 812219713@qq.com
 */
public class DownloadPictureUtil {

    public static void downloadPicture(final Context context, String url, final String path, final String name) {
        MyToast.getInstance()._short(context, context.getString(R.string.kf5_imageviewer_start_to_download));

        SimpleTarget<File> target = new SimpleTarget<File>() {

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                MyToast.getInstance()._short(context, context.getString(R.string.kf5_imageviewer_failed_to_download));
            }

            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                boolean result = FileUtil.copyFile(resource, path, name);
                if (result) {
                    MyToast.getInstance()._short(context, String.format(context.getString(R.string.kf5_imageviewer_success_to_download), path + name));
                    new SingleMediaScanner(context, path.concat(name), new SingleMediaScanner.ScanListener() {
                        @Override
                        public void onScanFinish() {

                        }
                    });
                } else {
                    MyToast.getInstance()._short(context, context.getString(R.string.kf5_imageviewer_failed_to_download));
                }
            }
        };
        Glide.with(context)
                .downloadOnly()
                .load(url)
                .into(target);
    }
}
