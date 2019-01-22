package com.kf5.sdk.im.expression.filter;

import android.text.Spannable;
import android.widget.EditText;

import com.kf5.sdk.im.expression.bean.EmojiDisplay;
import com.kf5.sdk.im.keyboard.api.EmoticonFilter;
import com.kf5.sdk.im.keyboard.utils.EmoticonsKeyboardUtils;
import com.kf5.sdk.im.keyboard.widgets.EmoticonSpan;

import java.util.regex.Matcher;

/**
 * author:chosen
 * date:2016/11/1 12:22
 * email:812219713@qq.com
 */

public class EmojiFilter extends EmoticonFilter {

    private int emojiSize = -1;

    @Override
    public void filter(EditText editText, CharSequence text, int start, int lengthBefore, int lengthAfter) {
        emojiSize = emojiSize == -1 ? EmoticonsKeyboardUtils.getFontHeight(editText) : emojiSize;
        clearSpan(editText.getText(), start, text.toString().length());
        Matcher m = EmojiDisplay.getMatcher(text.toString().substring(start, text.toString().length()));
        while (m.find()) {
            String emojiHex = Integer.toHexString(Character.codePointAt(m.group(), 0));
            EmojiDisplay.emojiDisplay(editText.getContext(), editText.getText(), emojiHex, emojiSize, start + m.start(), start + m.end());
        }
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
}
