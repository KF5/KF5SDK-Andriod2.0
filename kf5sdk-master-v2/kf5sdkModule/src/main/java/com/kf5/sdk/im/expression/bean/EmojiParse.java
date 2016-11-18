package com.kf5.sdk.im.expression.bean;

/**
 * author:chosen
 * date:2016/11/1 12:28
 * email:812219713@qq.com
 */

public class EmojiParse {

    public static String fromChar(char ch) { return Character.toString(ch); }

    public static String fromCodePoint(int codePoint) { return newString(codePoint); }

    public static final String newString(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }
}
