package com.kf5.sdk.im.utils;

import android.content.Context;
import android.media.ExifInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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


    public static void setImageSize(String path, ImageView imageView, float dstMaxWH, float dstMinWH) {

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
        }
    }


    public static void setNetImageSize(ImageView imageView, int width, int height) {
        Context context = imageView.getContext();
        ImageSize imageSize = getThumbnailDisplaySize(width, height, Utils.getImageMaxEdge(context), Utils.getImageMinEdge(context));
        setLayoutParams(imageSize.width, imageSize.height, imageView);
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

    // 设置控件的长宽
    private static void setLayoutParams(int width, int height, View view) {
        ViewGroup.LayoutParams maskParams = view.getLayoutParams();
        maskParams.width = width;
        maskParams.height = height;
        view.setLayoutParams(maskParams);
    }


}
