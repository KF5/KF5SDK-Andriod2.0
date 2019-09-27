package com.kf5.sdk.im.keyboard.api;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.widget.EditText;

import java.io.IOException;

/**
 * author:chosen
 * date:2016/11/1 11:51
 * email:812219713@qq.com
 */

public abstract class EmoticonFilter {

    public abstract void filter(EditText editText, CharSequence text, int start, int lengthBefore, int lengthAfter);

    public static Drawable getDrawableFromAssets(Context context, String emoticonName) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(emoticonName));
            return new BitmapDrawable(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(Context context, String emojiName) {
        if (TextUtils.isEmpty(emojiName)) {
            return null;
        }

        if (emojiName.contains(".")) {
            emojiName = emojiName.substring(0, emojiName.indexOf("."));
        }
        int resID = context.getResources().getIdentifier(emojiName, "mipmap", context.getPackageName());
        if (resID <= 0) {
            resID = context.getResources().getIdentifier(emojiName, "drawable", context.getPackageName());
        }

        try {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? context.getResources().getDrawable(resID, (Resources.Theme) null) : context.getResources().getDrawable(resID);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(Context context, int emoticon) {
        if (emoticon <= 0) {
            return null;
        }
        try {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? context.getResources().getDrawable(emoticon, null) : context.getResources().getDrawable(emoticon);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
