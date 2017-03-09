package com.kf5.sdk.system.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.widget.TextView;

import com.kf5.sdk.im.expression.bean.EmojiDisplay;
import com.kf5.sdk.im.keyboard.utils.EmoticonsKeyboardUtils;
import com.kf5.sdk.system.listener.AIMessageMovementMethod;
import com.kf5.sdk.system.listener.AIURLSpan;
import com.kf5.sdk.system.listener.RichLinkMovementMethod;
import com.kf5.sdk.system.listener.URLSpanNoUnderline;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author:chosen
 * date:2016/10/20 17:17
 * email:812219713@qq.com
 */

public class CustomTextView {

    /**
     * 解析html标签的静态解析方式
     *
     * @param context
     * @param tv_content
     * @param text
     * @param mask
     */
    public static void stripUnderlines(Context context, TextView tv_content, String text, int mask) {

        //至于dealData为什么要调用两次呢？我现在也muji，可能是第一次针对的是链接解析，第二次是html解析
        tv_content.setText(Html.fromHtml(filterHtmlTag(text)));
        dealData(tv_content, context);
        Linkify.addLinks(tv_content, mask);
        tv_content.setMovementMethod(new RichLinkMovementMethod());
        dealData(tv_content, context);
    }


    private static void dealData(TextView tv_content, Context context) {
        CharSequence charSequence = tv_content.getText();
        if (charSequence instanceof Spannable) {
            tv_content.setText("");
//            Spannable s = (Spannable) charSequence;
            Spannable s = EmojiDisplay.spannableFilter(tv_content.getContext(),
                    new SpannableStringBuilder(charSequence),
                    charSequence,
                    EmoticonsKeyboardUtils.getFontHeight(tv_content));
            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            for (URLSpan span : spans) {
                int start = s.getSpanStart(span);
                int end = s.getSpanEnd(span);
                String clickText = s.subSequence(start, end).toString();
                URLSpanNoUnderline urlSpanNoUnderline = new URLSpanNoUnderline(context, span.getURL(), clickText);
                s.setSpan(urlSpanNoUnderline, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv_content.append(s);
        }
    }

    /**
     * 解析机器人搜索到文档的内容
     *
     * @param context
     * @param textView
     * @param text
     */
    public static void setTextWithAIMessage(Context context, TextView textView, String text) {

        textView.setText(Html.fromHtml(filterHtmlTag(text)));
        CharSequence content = textView.getText();
        if (content instanceof Spannable) {
            textView.setText("");
            Spannable s = (Spannable) content;
            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            for (URLSpan span : spans) {
                int start = s.getSpanStart(span);
                int end = s.getSpanEnd(span);
                AIURLSpan myURLSpan = new AIURLSpan(span.getURL(), context);
                s.setSpan(myURLSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.append(s);
        }
        Linkify.addLinks(textView, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
        textView.setMovementMethod(new AIMessageMovementMethod());
        CharSequence charSequence = textView.getText();
        if (charSequence instanceof Spannable) {
            textView.setText("");
            Spannable s = (Spannable) charSequence;
            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            for (URLSpan span : spans) {
                int start = s.getSpanStart(span);
                int end = s.getSpanEnd(span);
                AIURLSpan myURLSpan = new AIURLSpan(span.getURL(), context);
                s.setSpan(myURLSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.append(s);
        }
    }


    private final static String[] PROTOCOL = new String[]{"http://", "https://", "rtsp://"};

    /**
     * 超链接加协议
     *
     * @param url
     * @return
     */
    public static final String makeWebUrl(@NonNull String url) {
        boolean hasPrefix = false;
        for (int i = 0; i < PROTOCOL.length; i++) {
            if (url.regionMatches(true, 0, PROTOCOL[i], 0, PROTOCOL[i].length())) {
                hasPrefix = true;
                // Fix capitalization if necessary
                if (!url.regionMatches(false, 0, PROTOCOL[i], 0, PROTOCOL[i].length())) {
                    url = PROTOCOL[i] + url.substring(PROTOCOL[i].length());
                }
                break;
            }
        }
        if (!hasPrefix && PROTOCOL.length > 0) {
            url = PROTOCOL[0] + url;
        }
        return url;
    }

    /**
     * 格式替换，将\n替换为br
     *
     * @param source
     * @return
     */
    private static String filterHtmlTag(String source) {
        if (!TextUtils.isEmpty(source)) {
            Pattern pattern = Pattern.compile("\n");
            Matcher matcher = pattern.matcher(source);
            if (matcher.find()) {
                source = matcher.replaceAll("<br/>");
                return source;
            }
            return source;
        }
        return "";
    }

}
