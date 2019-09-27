package com.chosen.imageviewer.tool.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chosen.imageviewer.ImagePreview;
import com.chosen.imageviewer.tool.file.FileUtil;
import com.chosen.imageviewer.tool.file.SingleMediaScanner;
import com.chosen.imageviewer.tool.ui.MyToast;
import com.chosen.imageviewer.view.MD5Utils;
import com.kf5.sdk.R;

import java.io.File;

/**
 * @author Chosen
 * @create 2018/12/17 11:47
 * @email 812219713@qq.com
 */
public class DownloadPictureUtil {

    public static void downloadPicture(final Context context, final String url) {

        SimpleTarget<File> target = new SimpleTarget<File>() {
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                MyToast.getInstance()._short(context, context.getString(R.string.kf5_imageviewer_start_to_download));
                super.onLoadStarted(placeholder);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                MyToast.getInstance()._short(context, context.getString(R.string.kf5_imageviewer_failed_to_download));
            }

            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                final String downloadFolderName = ImagePreview.getInstance().getFolderName();
                final String path = Environment.getExternalStorageDirectory() + "/" + downloadFolderName + "/";
                String name = "";
                try {
                    name = url.substring(url.lastIndexOf("/") + 1, url.length());
                    if (name.contains(".")) {
                        name = name.substring(0, name.lastIndexOf("."));
                    }
                    name = MD5Utils.md5Encode(name);
                } catch (Exception e) {
                    e.printStackTrace();
                    name = System.currentTimeMillis() + "";
                }
                String mimeType = ImageUtil.getImageTypeWithMime(resource.getAbsolutePath());
                name = name + "." + mimeType;
                File file = new File(path + name);
                if (file.exists()) {
                    MyToast.getInstance()._short(context, String.format(context.getString(R.string.kf5_imageviewer_file_location_hint), file.getAbsolutePath()));
                } else {
                    FileUtil.createFileByDeleteOldFile(path + name);
                    boolean result = FileUtil.copyFile(resource, path, name);
                    if (result) {
                        MyToast.getInstance()._short(context, String.format(context.getString(R.string.kf5_imageviewer_success_to_download), path + name));
                        new SingleMediaScanner(context, path.concat(name), new SingleMediaScanner.ScanListener() {
                            @Override
                            public void onScanFinish() {
                                // scanning...
                            }
                        });
                    } else {
                        MyToast.getInstance()._short(context, context.getString(R.string.kf5_imageviewer_failed_to_download));
                    }
                }
            }
        };
        Glide.with(context).downloadOnly().load(url).into(target);
    }
}
