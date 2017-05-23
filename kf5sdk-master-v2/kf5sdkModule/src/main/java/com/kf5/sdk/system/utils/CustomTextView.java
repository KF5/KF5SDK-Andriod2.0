package com.kf5.sdk.system.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.clickspan.CustomClickSpan;
import com.kf5.sdk.im.adapter.clickspan.CustomLinkMovementMethod;
import com.kf5.sdk.im.entity.CustomField;
import com.kf5.sdk.im.expression.bean.EmojiDisplay;
import com.kf5.sdk.im.keyboard.utils.EmoticonsKeyboardUtils;
import com.kf5.sdk.im.utils.MessageUtils;
import com.kf5.sdk.system.listener.AIMessageMovementMethod;
import com.kf5.sdk.system.listener.AIURLSpan;
import com.kf5.sdk.system.listener.RichLinkMovementMethod;
import com.kf5.sdk.system.listener.URLSpanNoUnderline;

import org.json.JSONObject;

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
        dealUnderLinesData(tv_content, context);
        Linkify.addLinks(tv_content, mask);
        tv_content.setMovementMethod(new RichLinkMovementMethod());
        dealUnderLinesData(tv_content, context);
    }


    /**
     * 解析link
     *
     * @param tv_content
     * @param context
     */
    private static void dealUnderLinesData(TextView tv_content, Context context) {
        CharSequence charSequence = tv_content.getText();
        if (charSequence instanceof Spannable) {
            tv_content.setText("");
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
     * @param textView
     * @param text
     */
    public static void setTextWithAIMessage(TextView textView, String text ,String type) {
        textView.setText(Html.fromHtml(filterHtmlTag(MessageUtils.dealAIMessage(text))));
        dealAILink(textView, type);
        Linkify.addLinks(textView, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
        textView.setMovementMethod(new AIMessageMovementMethod());
        dealAILink(textView, type);
    }

    /**
     * 解析机器人对话link
     *
     * @param textView
     */
    private static void dealAILink(TextView textView, String type) {
        CharSequence charSequence = textView.getText();
        if (charSequence instanceof Spannable) {
            textView.setText("");
            Spannable s = (Spannable) charSequence;
            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            for (URLSpan span : spans) {
                int start = s.getSpanStart(span);
                int end = s.getSpanEnd(span);
                String clickContent = s.subSequence(start, end).toString();
                AIURLSpan myURLSpan = new AIURLSpan(span.getURL(), type, clickContent, textView.getContext());
                s.setSpan(myURLSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.append(s);
        }
    }


    /**
     * 显示富文本消息
     *
     * @param textView
     * @param message
     */
    public static void setCustomMessage(TextView textView, String message) {
        JSONObject jsonObject = SafeJson.parseObj(message);
        if (SafeJson.isContainKey(jsonObject, CustomField.TYPE)) {
            String type = SafeJson.safeGet(jsonObject, CustomField.TYPE);
            if (TextUtils.equals(CustomField.VIDEO, type)) {
                if (SafeJson.isContainKey(jsonObject, CustomField.VISITOR_URL)) {
                    String url = SafeJson.safeGet(jsonObject, CustomField.VISITOR_URL);
                    textView.setText(makeUrlWithHtmlHref(url, textView.getContext().getString(R.string.kf5_invite_video_chat)));
                } else {
                    textView.setText(resolveTextWithHtmlTag(message));
                }
            } else {
                textView.setText(resolveTextWithHtmlTag(message));
            }
        } else {
            textView.setText(resolveTextWithHtmlTag(message));
        }
        dealCustomLink(textView);
        Linkify.addLinks(textView, Linkify.ALL);
        textView.setMovementMethod(new CustomLinkMovementMethod());
        dealCustomLink(textView);
    }

    /**
     * 解析富文本link
     *
     * @param textView
     */
    private static void dealCustomLink(TextView textView) {
        CharSequence charSequence = textView.getText();
        if (charSequence instanceof Spannable) {
            textView.setText("");
            Spannable s = (Spannable) charSequence;
            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            for (URLSpan span : spans) {
                int start = s.getSpanStart(span);
                int end = s.getSpanEnd(span);
                CustomClickSpan myURLSpan = new CustomClickSpan(textView.getContext(), span.getURL());
                s.setSpan(myURLSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.append(s);
        }
    }

    /**
     * url拼接A标签
     *
     * @param url
     * @param hrefText
     * @return
     */
    public static Spanned makeUrlWithHtmlHref(String url, String hrefText) {
        String string = "<a href=" +
                url +
                ">" +
                hrefText +
                "</a>";
        return resolveTextWithHtmlTag(string);
    }


    private static Spanned resolveTextWithHtmlTag(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
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
