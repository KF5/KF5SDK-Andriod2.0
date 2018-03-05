package com.kf5.sdk.system.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.clickspan.CustomClickSpan;
import com.kf5.sdk.im.adapter.clickspan.CustomLinkMovementMethod;
import com.kf5.sdk.im.entity.CustomField;
import com.kf5.sdk.im.expression.bean.EmojiDisplay;
import com.kf5.sdk.im.keyboard.utils.EmoticonsKeyboardUtils;
import com.kf5.sdk.system.listener.RichLinkMovementMethod;
import com.kf5.sdk.system.listener.URLSpanNoUnderline;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    public static void stripUnderlines(final Context context, final TextView tv_content, String text, final int mask) {

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
//    public static void setTextWithAIMessage(TextView textView, String text, String type) {
//        textView.setText(Html.fromHtml(filterHtmlTag(MessageUtils.dealAIMessage(text))));
//        dealAILink(textView, type);
//        Linkify.addLinks(textView, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
//        textView.setMovementMethod(new AIMessageMovementMethod());
//        dealAILink(textView, type);
//    }

    /**
     * 解析机器人对话link
     *
     * @param textView
     */
//    private static void dealAILink(TextView textView, String type) {
//        CharSequence charSequence = textView.getText();
//        if (charSequence instanceof Spannable) {
//            textView.setText("");
//            Spannable s = (Spannable) charSequence;
//            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
//            for (URLSpan span : spans) {
//                int start = s.getSpanStart(span);
//                int end = s.getSpanEnd(span);
//                String clickContent = s.subSequence(start, end).toString();
//                AIURLSpan myURLSpan = new AIURLSpan(span.getURL(), type, clickContent, textView.getContext());
//                s.setSpan(myURLSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            textView.append(s);
//        }
//    }


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

    // ****************************2018-1-31新增代码 修复部分机型机器人消息url无法正确匹配bug**************************
    public static void applyRichText(final TextView textView, String text, final OnLongClickCallback callback) {
        List<LinkEntity> list = dealRichList(textView, text);
        if (list.isEmpty()) {
            textView.setText(text);
            return;
        }
        Collections.sort(list, new Comparator<LinkEntity>() {
            @Override
            public int compare(LinkEntity link1, LinkEntity link2) {
                if (link1.start < link2.start) return -1;
                else if (link1.start > link2.start) return 1;
                else
                    return 0;
            }
        });
        String remainText = text;
        int lastStart = 0;//截取到一部分后截掉部分的长度
        for (int i = 0, size = list.size(); i < size; i++) {
            try {
                final LinkEntity entity = list.get(i);
                final String showText = entity.content;
                final String url = entity.url;
                int start = entity.start;
                int end = entity.end;
                SpannableString spannableString = new SpannableString(showText);
                if (LinkType.EMOJI == entity.linkType) {
                    String emojiHex = Integer.toHexString(Character.codePointAt(showText, 0));
                    EmojiDisplay.emojiDisplay(textView.getContext(), spannableString, emojiHex, EmoticonsKeyboardUtils.getFontHeight(textView), 0, showText.length());
                } else {
                    spannableString.setSpan(new ClickableSpan() {

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                        }

                        @Override
                        public void onClick(View widget) {
                            Context context = widget.getContext();
                            switch (entity.linkType) {
                                case EMAIL:
                                    IntentUtils.emailAddress(context, url);
                                    break;
                                case PHONE:
                                    IntentUtils.phoneCall(context, url);
                                    break;
                                case URL:
                                    IntentUtils.openBrowser(context, url);
                                    break;
                                case IMAGE:
                                    IntentUtils.browserImage(context, url);
                                    break;
                                case DOCUMENT:
                                    IntentUtils.viewHelpCenterDetail(context, url);
                                    break;
                                case QUESTION:
                                    IntentUtils.sendQuestionContent(context, showText, url);
                                    break;
                                case GET_AGENT:
                                    IntentUtils.getAgent(context);
                                    break;
                            }
                        }
                    }, 0, showText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                int subStart = start - lastStart;
                String front = remainText.substring(0, subStart);//截取出一段文字+一段url
                remainText = remainText.substring(end - lastStart, remainText.length());//剩下的部分
                lastStart = end;
                if (front.length() > 0) textView.append(front);
                textView.append(spannableString);
            } catch (Exception e) {
                continue;
            }
        }
//        if (remainText != null && remainText.length() > 0) textView.append(remainText);
        if (!TextUtils.isEmpty(remainText)) textView.append(remainText);
        final RichTextMovementMethod method = new RichTextMovementMethod();
        textView.setMovementMethod(method);//响应点击事件
        if (callback != null) {
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    method.removeSpan((Spannable) textView.getText());
                    return callback.onLongClick(v);
                }
            });
        }
    }

    private static List<LinkEntity> dealRichList(TextView textView, String text) {

        List<LinkEntity> list = new ArrayList<>();
        textView.setText("");
        dealHrefTag(list, text);
        addEmailLink(list, text);
        addWebUrlLink(list, text);
        addPhoneLink(list, text);
        addEmoji(list, text);
        addImgHtmlTagLink(list, text);
        addGetAgentLink(list, text);
        return list;
    }

    private static void dealHrefTag(List<LinkEntity> list, String text) {
        Pattern pattern = Pattern.compile("<a href=\".*?\">(.*?)</a>");//首先将a标签分离出来
//        Pattern pattern = Pattern.compile("<a\\b[^>]+\\bhref\\s*=\\s*\"([^\"]*)\"[^>]*>([\\s\\S]*?)</a>");//首先将a标签分离出来
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String raw = matcher.group(0);
            Pattern url_pattern = Pattern.compile("<a href=\"(.*?)\">");//将href分离出来
            Matcher url_matcher = url_pattern.matcher(raw);
//            String content;
//            String clickUrl = null;
//            int startIndex;
//            int endIndex;
            LinkEntity linkEntity = new LinkEntity();
            try {
                if (url_matcher.find()) {
                    String clickUrl = url_matcher.group(1);
                    if (clickUrl.startsWith("chosenDocumentTo://")) {
                        linkEntity.url = clickUrl.substring("chosenDocumentTo://".length(), clickUrl.length());
                        linkEntity.linkType = LinkType.DOCUMENT;
                    } else if (clickUrl.startsWith("chosenQuestionTo://")) {
                        linkEntity.url = clickUrl.substring("chosenQuestionTo://".length(), clickUrl.length());
                        linkEntity.linkType = LinkType.QUESTION;
                    } else {
                        linkEntity.url = clickUrl;
                        linkEntity.linkType = LinkType.URL;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                linkEntity.content = matcher.group(1);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            if (TextUtils.isEmpty(linkEntity.url)) {
                continue;
            }
            linkEntity.start = matcher.start();
            linkEntity.end = matcher.end();
            list.add(linkEntity);
        }
    }


    private static void addPhoneLink(List<LinkEntity> list, String text) {
        Pattern pattern = Patterns.PHONE;
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String common = matcher.group(0);
            int start = matcher.start();
            int end = matcher.end();
            if (!isInBounds(list, start, end)) {
                list.add(new LinkEntity(common, "tel:" + common, start, end, LinkType.PHONE));
            }
        }
    }


    private static void addWebUrlLink(List<LinkEntity> list, String text) {
//        Pattern pattern = Patterns.WEB_URL;
        Pattern pattern = Pattern.compile("([hH]ttp[s]{0,1})://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\-~!@#$%^&*+?:_/=<>.\',;]*)?");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String common = matcher.group(0);
            int start = matcher.start();
            int end = matcher.end();
            if (!isInBounds(list, start, end)) {
                list.add(new LinkEntity(common, makeWebUrl(common), start, end, LinkType.URL));

            }
        }
    }


    private static void addEmailLink(List<LinkEntity> list, String text) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String common = matcher.group(0);
            int start = matcher.start();
            int end = matcher.end();
            if (!isInBounds(list, start, end)) {
                list.add(new LinkEntity(common, "malto:" + common, start, end, LinkType.EMAIL));
            }
        }
    }

    private static void addEmoji(List<LinkEntity> list, String text) {
        Pattern pattern = EmojiDisplay.EMOJI_RANGE;
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String common = matcher.group(0);
            int start = matcher.start();
            int end = matcher.end();
            if (!isInBounds(list, start, end)) {
                list.add(new LinkEntity(common, common, start, end, LinkType.EMOJI));
            }
        }
    }

    private static void addGetAgentLink(List<LinkEntity> list, String text) {
        Pattern toAgentPattern = Pattern.compile("\\{\\{(.+?)\\}\\}");
        Matcher matcher = toAgentPattern.matcher(text);
        while (matcher.find()) {
            String common = matcher.group(0);
            int start = matcher.start();
            int end = matcher.end();
            String clickContent = common.substring("{{".length(), common.indexOf("}}"));
            if (!isInBounds(list, start, end)) {
                list.add(new LinkEntity(clickContent, "getAgent", start, end, LinkType.GET_AGENT));
            }
        }
    }


    private static void addImgHtmlTagLink(List<LinkEntity> list, String text) {
//        Pattern pattern = Pattern.compile("<img.*?src\\s*=\\s*\"(.*?)\".*?>");
        Pattern pattern = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String content = matcher.group(2);
            Pattern srcPattern = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
            Matcher srcMatcher = srcPattern.matcher(content);
            if (srcMatcher.find()) {
                String url = srcMatcher.group(3);
                int start = matcher.start();
                int end = matcher.end();
                if (!isInBounds(list, start, end)) {
                    list.add(new LinkEntity("[图片]", url, start, end, LinkType.IMAGE));
                }
            }
        }
    }

    private static boolean isInBounds(List<LinkEntity> list, int start, int end) {
        boolean inbounds = false;
        for (LinkEntity entity : list) {
            if (start >= entity.start && end <= entity.end) {
                inbounds = true;
            }
        }
        return inbounds;
    }

    private static class LinkEntity {
        private String content;
        private String url;
        private int start;
        private int end;
        private LinkType linkType;

        LinkEntity() {
        }

        LinkEntity(String content, String url, int start, int end, LinkType linkType) {
            this.content = content;
            this.url = url;
            this.start = start;
            this.end = end;
            this.linkType = linkType;
        }

        @Override
        public String toString() {
            return "点击的内容：" + content
                    + " 目标url: " + url
                    + " 起始索引：" + start
                    + " 结束索引：" + end
                    + " 链接类型：" + linkType;
        }
    }


    private enum LinkType {
        URL, PHONE, EMAIL, EMOJI, IMAGE, QUESTION, DOCUMENT, GET_AGENT
    }

    public static class RichTextMovementMethod extends LinkMovementMethod {

        private boolean callback = true;

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            try {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        ClickableSpan[] link = getClickableSpan(widget, buffer, event);
                        if (link.length > 0) {
                            BackgroundColorSpan bkcolor;
                            bkcolor = new BackgroundColorSpan(Color.parseColor("#038cc2"));
                            buffer.setSpan(bkcolor,
                                    buffer.getSpanStart(link[0]),
                                    buffer.getSpanEnd(link[0]),
                                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        }
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        removeSpanAndCallback(buffer);
                        break;
                    case MotionEvent.ACTION_UP: {
                        removeSpanAndCallback(buffer);
                        if (callback) {
                            ClickableSpan[] link = getClickableSpan(widget, buffer, event);
                            if (link.length > 0) {
                                link[0].onClick(widget);
                            }
                        } else {
                            callback = true;
                        }
                    }
                    break;
                    default:
                        removeSpanAndCallback(buffer);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        public void removeSpan(Spannable buffer) {
            removeSpanAndCallback(buffer);
            callback = false;
        }

        private void removeSpanAndCallback(Spannable buffer) {
            BackgroundColorSpan[] backgroundColorSpans = buffer
                    .getSpans(0, buffer.length(), BackgroundColorSpan.class);
            for (BackgroundColorSpan bkcolor : backgroundColorSpans) {
                buffer.removeSpan(bkcolor);
            }
        }

        private ClickableSpan[] getClickableSpan(TextView widget, Spannable buffer, MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            x += widget.getScrollX();
            y += widget.getScrollY();
            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            return buffer.getSpans(off, off, ClickableSpan.class);
        }
    }

    public interface OnLongClickCallback {
        boolean onLongClick(View view);
    }
}
