package com.kf5.sdk.im.expression.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.widget.EditText;

import com.kf5.sdk.im.expression.bean.EmojiDisplayListener;
import com.kf5.sdk.im.expression.emoticon.DefQqEmoticons;
import com.kf5.sdk.im.keyboard.api.EmoticonFilter;
import com.kf5.sdk.im.keyboard.utils.EmoticonsKeyboardUtils;
import com.kf5.sdk.im.keyboard.widgets.EmoticonSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author:chosen
 * date:2016/11/1 14:51
 * email:812219713@qq.com
 */

public class QQFilter extends EmoticonFilter {

    public static final int WRAP_DRAWABLE = -1;
    private int emoticonSize = -1;
    public static final Pattern QQ_RANGE = Pattern.compile("\\[[a-zA-Z0-9\\u4e00-\\u9fa5]+\\]");

    public static Matcher getMatcher(CharSequence matchStr) {
        return QQ_RANGE.matcher(matchStr);
    }

    @Override
    public void filter(EditText editText, CharSequence text, int start, int lengthBefore, int lengthAfter) {
        emoticonSize = emoticonSize == -1 ? EmoticonsKeyboardUtils.getFontHeight(editText) : emoticonSize;
        clearSpan(editText.getText(), start, text.toString().length());
        Matcher m = getMatcher(text.toString().substring(start, text.toString().length()));
        while (m.find()) {
            String key = m.group();
            int icon = DefQqEmoticons.sQqEmoticonHashMap.get(key);
            if (icon > 0) {
                emoticonDisplay(editText.getContext(), editText.getText(), icon, emoticonSize, start + m.start(), start + m.end());
            }
        }
    }

    public static Spannable spannableFilter(Context context, Spannable spannable, CharSequence text, int fontSize, EmojiDisplayListener emojiDisplayListener) {
        Matcher m = getMatcher(text);
        while (m.find()) {
            String key = m.group();
            int icon = DefQqEmoticons.sQqEmoticonHashMap.get(key);
            if (emojiDisplayListener == null) {
                if (icon > 0) {
                    emoticonDisplay(context, spannable, icon, fontSize, m.start(), m.end());
                }
            } else {
                emojiDisplayListener.onEmojiDisplay(context, spannable, "" + icon, fontSize, m.start(), m.end());
            }
        }
        return spannable;
    }

    private void clearSpan(Spannable spannable, int start, int end) {
        if (start == end) {
            return;
        }
        EmoticonSpan[] oldSpans = spannable.getSpans(start, end, EmoticonSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            spannable.removeSpan(oldSpans[i]);
        }
    }

    public static void emoticonDisplay(Context context, Spannable spannable, int emoticon, int fontSize, int start, int end) {
        Drawable drawable = getDrawable(context, emoticon);
        if (drawable != null) {
            int itemHeight;
            int itemWidth;
            if (fontSize == WRAP_DRAWABLE) {
                itemHeight = drawable.getIntrinsicHeight();
                itemWidth = drawable.getIntrinsicWidth();
            } else {
                itemHeight = fontSize;
                itemWidth = fontSize;
            }
            drawable.setBounds(0, 0, itemHeight, itemWidth);
            EmoticonSpan imageSpan = new EmoticonSpan(drawable);
            spannable.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

}
