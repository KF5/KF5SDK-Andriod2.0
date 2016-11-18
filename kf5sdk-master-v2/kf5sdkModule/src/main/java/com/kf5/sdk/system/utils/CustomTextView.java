package com.kf5.sdk.system.utils;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.widget.TextView;

import com.kf5.sdk.system.listener.AIMessageMovementMethod;
import com.kf5.sdk.system.listener.AIURLSpan;
import com.kf5.sdk.system.listener.RichLinkMovementMethod;
import com.kf5.sdk.system.listener.URLSpanNoUnderline;

/**
 * author:chosen
 * date:2016/10/20 17:17
 * email:812219713@qq.com
 */

public class CustomTextView {

    /**
     * 不解析html标签的静态解析方式
     *
     * @param context
     * @param textView
     * @param text
     * @param mask
     */
    public static void stripUnderlines(Context context, TextView textView, String text, int mask) {

        textView.setText(Html.fromHtml(text));
        CharSequence content = textView.getText();
        if (content instanceof Spannable) {
            textView.setText("");
            Spannable s = (Spannable) content;
            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            for (URLSpan span : spans) {
                int start = s.getSpanStart(span);
                int end = s.getSpanEnd(span);
                String clickText = s.subSequence(start, end).toString();
                URLSpanNoUnderline urlSpanNoUnderline = new URLSpanNoUnderline(context, span.getURL(), clickText);
                s.setSpan(urlSpanNoUnderline, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.append(s);
        }
        Linkify.addLinks(textView, mask);
        textView.setMovementMethod(new RichLinkMovementMethod());
        CharSequence charSequence = textView.getText();
        if (charSequence instanceof Spannable) {
            textView.setText("");
            Spannable s = (Spannable) charSequence;
            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            for (URLSpan span : spans) {
                int start = s.getSpanStart(span);
                int end = s.getSpanEnd(span);
                String clickText = s.subSequence(start, end).toString();
                URLSpanNoUnderline urlSpanNoUnderline = new URLSpanNoUnderline(context, span.getURL(), clickText);
                s.setSpan(urlSpanNoUnderline, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.append(s);
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

        textView.setText(Html.fromHtml(text));
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

}
