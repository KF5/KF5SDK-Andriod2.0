package com.kf5.sdk.im.expression.bean;

import android.content.Context;
import android.text.Spannable;

/**
 * author:chosen
 * date:2016/11/1 12:26
 * email:812219713@qq.com
 */

public interface EmojiDisplayListener {

    void onEmojiDisplay(Context context, Spannable spannable, String emojiHex, int fontSize, int start, int end);
}
