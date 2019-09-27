package com.kf5.sdk.im.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.media.ExifInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kf5.sdk.system.utils.LogUtil;

import java.io.File;
import java.io.IOException;

/**
 * author:chosen
 * date:2016/10/26 14:51
 * email:812219713@qq.com
 */

public class ImageUtils {

    public static class ImageSize {
        public int width = 0;
        public int height = 0;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }


    public static ImageSize getThumbnailDisplaySize(float srcWidth, float srcHeight, float dstMaxWH, float dstMinWH) {
        if (srcWidth <= 0 || srcHeight <= 0) { // bounds check
            return new ImageSize((int) dstMinWH, (int) dstMinWH);
        }

        float shorter;
        float longer;
        boolean widthIsShorter;

        //store
        if (srcHeight < srcWidth) {
            shorter = srcHeight;
            longer = srcWidth;
            widthIsShorter = false;
        } else {
            shorter = srcWidth;
            longer = srcHeight;
            widthIsShorter = true;
        }

        if (shorter < dstMinWH) {
            float scale = dstMinWH / shorter;
            shorter = dstMinWH;
            if (longer * scale > dstMaxWH) {
                longer = dstMaxWH;
            } else {
                longer *= scale;
            }
        } else if (longer > dstMaxWH) {
            float scale = dstMaxWH / longer;
            longer = dstMaxWH;
            if (shorter * scale < dstMinWH) {
                shorter = dstMinWH;
            } else {
                shorter *= scale;
            }
        }

        //restore
        if (widthIsShorter) {
            srcWidth = shorter;
            srcHeight = longer;
        } else {
            srcWidth = longer;
            srcHeight = shorter;
        }

        return new ImageSize((int) srcWidth, (int) srcHeight);
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    public static ImageSize setImageSize(String path, ImageView imageView, float dstMaxWH, float dstMinWH) {

        int[] bounds = null;
        if (path != null) {
            bounds = BitmapDecoder.decodeBound(new File(path));
        }
        if (bounds != null) {
            ImageSize imageSize = getThumbnailDisplaySize(bounds[0], bounds[1], dstMaxWH, dstMinWH);
            int degree = getBitmapDegree(path);
            if (degree == 90 || degree == 270)
                //noinspection SuspiciousNameCombination
                setLayoutParams(imageSize.height, imageSize.width, imageView);
            else
                setLayoutParams(imageSize.width, imageSize.height, imageView);
            return imageSize;
        }
        return null;
    }

    public static ImageSize setImageSize(int width, int height, ImageView imageView, float dstMaxWH, float dstMinWH) {
        ImageSize imageSize = getThumbnailDisplaySize(width, height, dstMaxWH, dstMinWH);
        setLayoutParams(imageSize.width, imageSize.height, imageView);
        return imageSize;
    }


    // 设置控件的长宽
    private static void setLayoutParams(int width, int height, View view) {
        ViewGroup.LayoutParams maskParams = view.getLayoutParams();
        maskParams.width = width;
        maskParams.height = height;
        view.setLayoutParams(maskParams);
    }


    /**
     * 获取视频资源第一帧
     *
     * @param url
     * @return
     */

    public static ImageSize setSizeOfVideo(String url, ImageView targetImage) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        File file = new File(url);
        if (file.exists()) {
            mediaMetadataRetriever.setDataSource(file.getAbsolutePath());//设置数据资源为该文件对象指定的绝对路径
            Bitmap firstFrame = mediaMetadataRetriever.getFrameAtTime();
            return setImageSize(firstFrame.getWidth(), firstFrame.getHeight(), targetImage, Utils.getImageMaxEdge(targetImage.getContext()), Utils.getImageMinEdge(targetImage.getContext()));
        }
        return new ImageSize(0, 0);
    }

    public static ImageSize getSizeOfVideo(String url) {
        if (TextUtils.isEmpty(url)) {
            return new ImageSize(0, 0);
        }
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        File file = new File(url);
        if (file.exists()) {
            mediaMetadataRetriever.setDataSource(file.getAbsolutePath());//设置数据资源为该文件对象指定的绝对路径
            Bitmap firstFrame = null;
            try {
                firstFrame = mediaMetadataRetriever.getFrameAtTime();
                return new ImageSize(firstFrame.getWidth(), firstFrame.getHeight());
            } finally {
                if (firstFrame != null) {
                    firstFrame.recycle();
                }
            }
        }
        return new ImageSize(0, 0);
    }


    public static ImageSize getSizeOfImage(String url) {
        if (TextUtils.isEmpty(url)) {
            return new ImageSize(0, 0);
        }
        File file = new File(url);
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//解析Bitmap对象，该对象不占用内存
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            LogUtil.printf(options.outWidth + "========" + options.outHeight);
            return new ImageSize(options.outWidth, options.outHeight);
        }
        return new ImageSize(0, 0);
    }
}
