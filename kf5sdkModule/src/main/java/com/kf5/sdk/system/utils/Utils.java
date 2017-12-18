package com.kf5.sdk.system.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.image.ImageSelectorActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * author:chosen
 * date:2016/10/19 11:48
 * email:812219713@qq.com
 */

public class Utils {

    public static final int CHOICE_FROM_FILE = 100;

    public static final int TAKE_PHOTO = 200;

    public static final String KF5_TAG = "D/OkHttp";

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param editText
     */
    public static void hideSoftInput(Context context, EditText editText) {
        if (context == null || editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param editText
     */
    public static void showSoftInput(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, 0);
        }
    }


    /**
     * 判断是否有第三方应用程序能够处理应用程序请求
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        //noinspection WrongConstant
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    @SuppressWarnings("deprecation")
    public static void copyText(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //		ClipData clipData = ClipData.newPlainText(content, content);
        cmb.setText(content);
        //		cmb.setPrimaryClip(clipData);
    }


    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        // 得到显示屏宽度
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 判断是否是图片
     *
     * @param name
     * @return
     */
    public static boolean isImage(String name) {

        try {
            if (name.equalsIgnoreCase(Field.PNG) ||
                    name.equalsIgnoreCase(Field.JPG) ||
                    name.equalsIgnoreCase(Field.JPEG) || name.equalsIgnoreCase(Field.GIF)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 获取后缀名
     *
     * @param name
     * @return
     */
    public static String getPrefix(String name) {
        if (TextUtils.isEmpty(name)) return "";
        try {
            return name.substring(name.lastIndexOf('.') + 1, name.length());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 是否是录音文件
     *
     * @param type
     * @return
     */
    public static boolean isAMR(String type) {

        if (TextUtils.isEmpty(type))
            return false;
        if (type.equalsIgnoreCase(Field.AMR)) {
            return true;
        }
        return false;
    }

    /**
     * 选择拍照
     *
     * @param context
     */
    public static void capturePicture(Context context, File picFile) {

        try {
            if (!picFile.exists())
                picFile.getParentFile().mkdirs();
            picFile.createNewFile();
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, TAKE_PHOTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 选择图片
     *
     * @param context
     */
    public static void choiceImage(Context context, int fileSize) {

        Intent intent = new Intent(context, ImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(ImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
        // 最大可选择图片数量
        intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_COUNT, fileSize);
        // 选择模式
        intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_MODE, ImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, CHOICE_FROM_FILE);
        }

    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkUable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        boolean available = false;
        if (info != null) {
            available = info.isAvailable();
        }
        return available;
    }


    /**
     * 时间格式化
     *
     * @param time
     * @return
     */
    public static String getAllTime(long time) {
        if (time <= 0) {
            return "0000:00:00";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date(time * 1000);
        return format.format(now);
    }


    /**
     * 获取文件后缀名
     *
     * @param name
     * @return
     */
    public static String getFileType(String name) {

        String fileType = "";
        try {
            fileType = name.substring(name.lastIndexOf(".") + 1, name.length());
        } catch (Exception e) {
        }
        return fileType;
    }

    @SuppressLint("HardwareIds")
    public static String getUUID(Context context) {
        if (context == null)
            return "android-";
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, androidId;
        tmDevice = tm.getDeviceId() + "";
        androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID) + "";
        UUID deviceUuid = new UUID(androidId.hashCode(), tmDevice.hashCode());
        return "android-" + deviceUuid.toString();
    }


}
