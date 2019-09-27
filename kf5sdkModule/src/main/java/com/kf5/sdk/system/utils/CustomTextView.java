package com.kf5.sdk.system.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.im.expression.bean.EmojiDisplay;
import com.kf5.sdk.im.keyboard.utils.EmoticonsKeyboardUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kf5.sdk.im.utils.MessageUtils.HREF_PREFIX_CATEGORY;
import static com.kf5.sdk.im.utils.MessageUtils.HREF_PREFIX_CUSTOM;
import static com.kf5.sdk.im.utils.MessageUtils.HREF_PREFIX_DOCUMENT;
import static com.kf5.sdk.im.utils.MessageUtils.HREF_PREFIX_QUESTION;

/**
 * author:chosen
 * date:2016/10/20 17:17
 * email:812219713@qq.com
 */

public class CustomTextView {

    //A标签
    private static Pattern patternHref = Pattern.compile("<a\\b[^>]+\\bhref\\s*=\\s*\"([^\"]*)\"[^>]*>([\\s\\S]*?)</a>");
    //"\n"
    private static Pattern patternN = Pattern.compile("\n");//"\n"
    //电话号码
    private static Pattern patternPhone = Pattern.compile("1[0-9]{10}(?!\\\\d)");//电话号码
    //webUrl
    private static Pattern patternUrl = Pattern.compile("([hH]ttp[s]{0,1})://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\-~!@#$%^&*+?:_/=<>.\',;]*)?");
    //email
    private static Pattern patternEmail = Patterns.EMAIL_ADDRESS;
    //emoji
    private static Pattern patternEmoji = EmojiDisplay.EMOJI_RANGE;
    //get Agent
    private static Pattern patternGetAgent =Pattern.compile("\\{\\{(.+?)\\}\\}");
    //html img tag
    private static Pattern patternImgHtmlTag = Pattern.compile("<img.*?src\\s*=\\s*\"(.*?)\".*?>");

    private final static String[] PROTOCOL = new String[]{"http://", "https://", "rtsp://"};


    /**
     * 解析html标签的静态解析方式
     *
     * @param context
     * @param tv_content
     * @param text
     * @param mask
     */
    public static void stripUnderlines(final Context context, final TextView tv_content, String text, final int mask) {

        List<LinkEntity> list = new ArrayList<>();
        tv_content.setText("");
        Matcher matcher = patternHref.matcher(text);
        while (matcher.find()) {
            if (matcher.groupCount() != 2) {
                continue;
            }
            String clickUrl = matcher.group(1);
            LinkEntity linkEntity = new LinkEntity();
            linkEntity.content = Html.fromHtml(matcher.group(2)).toString();
            linkEntity.url = clickUrl;
            linkEntity.linkType = TextUtils.equals("[图片]", linkEntity.content) ? LinkType.IMAGE : LinkType.URL;
            if (TextUtils.isEmpty(linkEntity.url)) {
                continue;
            }
            linkEntity.start = matcher.start();
            linkEntity.end = matcher.end();
            list.add(linkEntity);
        }
        addEmailLink(list, text);
        addImgHtmlTagLink(list, text);
        addPhoneLink(list, text);
        applyRichText(list, tv_content, text, null);
    }


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
            Matcher matcher = patternN.matcher(source);
            if (matcher.find()) {
                source = matcher.replaceAll("<br/>");
                return source;
            }
            return source;
        }
        return "";
    }

    public static void applyRichText(TextView textView, String text, OnLongClickCallback callback) {
        List<LinkEntity> list = new ArrayList<>();
        textView.setText("");
        dealHrefTag(list, text);
        addEmailLink(list, text);
        addWebUrlLink(list, text);
        addPhoneLink(list, text);
        addEmoji(list, text);
        addImgHtmlTagLink(list, text);
        addGetAgentLink(list, text);
        applyRichText(list, textView, text, callback);
    }

    // ****************************2018-1-31新增代码 修复部分机型机器人消息url无法正确匹配bug**************************
    private static void applyRichText(List<LinkEntity> list, final TextView textView, String text, final OnLongClickCallback callback) {

        if (list.isEmpty()) {
            textView.setText(Html.fromHtml(filterHtmlTag(text)));//需要开启html显示style，直接使用该接口
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
                final int end = entity.end;
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
                                    String tempUrl = url;
                                    if (!tempUrl.startsWith("http:") && !tempUrl.startsWith("https:")) {
                                        tempUrl = String.format("https://%1$s", SPUtils.getHelpAddress()) + tempUrl;
                                    }
                                    IntentUtils.browserImage(context, tempUrl);
                                    break;
                                case DOCUMENT:
                                    IntentUtils.viewHelpCenterDetail(context, url);
                                    break;
                                case QUESTION:
                                case CATEGORY:
                                    IntentUtils.sendQuestionContent(context, showText, url, entity.linkType == LinkType.CATEGORY);
                                    break;
                                case GET_AGENT:
                                    IntentUtils.getAgent(context);
                                    break;
                                case CUSTOM:
                                    IntentUtils.sendCustomMessageIntent(context, url);
                                    break;
                            }
                        }
                    }, 0, showText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                int subStart = start - lastStart;
                String front = remainText.substring(0, subStart);//截取出一段文字+一段url
                remainText = remainText.substring(end - lastStart, remainText.length());//剩下的部分
                lastStart = end;
                if (front.length() > 0) textView.append(Html.fromHtml(filterHtmlTag(front)));
                textView.append(spannableString);
            } catch (Exception e) {
                continue;
            }
        }
        if (!TextUtils.isEmpty(remainText)) textView.append(Html.fromHtml(remainText));
        final RichTextMovementMethod method = new RichTextMovementMethod();
        textView.setMovementMethod(method);//响应点击事件
//        textView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                method.removeSpan((Spannable) textView.getText());
//                return callback != null && callback.onLongClick(v);
//            }
//        });
    }

//    private static String filterAllHtmlTag(String text) {
//        Pattern pattern = Pattern.compile("<p>((.|\n)*?)</p>");
//        Matcher matcher = pattern.matcher(text);
//        LogUtil.printf("========="+matcher.replaceAll(""));
//        String target = text;
//        target = target.replaceAll("<br\\s{0,1}/?>", "");
//        target = target.replaceAll("<p>((.|\n)*?)</p>", "");
//        target = target.replaceAll("<[^>]+>|\n+$|(&nbsp;|\\s)+$", "");
//        target = target.replaceAll("&nbsp;", "");
//        return target;
//    }

    private static void dealHrefTag(List<LinkEntity> list, String text) {
        Matcher matcher = patternHref.matcher(text);
        while (matcher.find()) {
            if (matcher.groupCount() != 2) {
                continue;
            }
            String clickUrl = matcher.group(1);
            LinkEntity linkEntity = new LinkEntity();
            if (clickUrl.startsWith(HREF_PREFIX_DOCUMENT)) {
                linkEntity.url = clickUrl.substring(HREF_PREFIX_DOCUMENT.length(), clickUrl.length());
                linkEntity.linkType = LinkType.DOCUMENT;
            } else if (clickUrl.startsWith(HREF_PREFIX_QUESTION)) {
                linkEntity.url = clickUrl.substring(HREF_PREFIX_QUESTION.length(), clickUrl.length());
                linkEntity.linkType = LinkType.QUESTION;
            } else if (clickUrl.startsWith(HREF_PREFIX_CUSTOM)) {
                linkEntity.url = clickUrl.substring(HREF_PREFIX_CUSTOM.length(), clickUrl.length());
                linkEntity.linkType = LinkType.CUSTOM;
            } else if (clickUrl.startsWith(HREF_PREFIX_CATEGORY)) {
                linkEntity.url = clickUrl.substring(HREF_PREFIX_CATEGORY.length(), clickUrl.length());
                linkEntity.linkType = LinkType.CATEGORY;
            } else {
                linkEntity.url = clickUrl;
                linkEntity.linkType = LinkType.URL;
            }
            if (TextUtils.isEmpty(linkEntity.url)) {
                continue;
            }
            linkEntity.content = Html.fromHtml(matcher.group(2)).toString();
            linkEntity.start = matcher.start();
            linkEntity.end = matcher.end();
            list.add(linkEntity);
        }
    }


    private static void addPhoneLink(List<LinkEntity> list, String text) {
//        Pattern pattern = Patterns.PHONE;
        Matcher matcher = patternPhone.matcher(text);
        while (matcher.find()) {
            String common = matcher.group(0);
            int start = matcher.start();
            int end = matcher.end();
            if (!isInBounds(list, start, end)) {
                list.add(new LinkEntity(common, common, start, end, LinkType.PHONE));
            }
        }
    }


    private static void addWebUrlLink(List<LinkEntity> list, String text) {
        Matcher matcher = patternUrl.matcher(text);
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
        Matcher matcher = patternEmail.matcher(text);
        while (matcher.find()) {
            String common = matcher.group(0);
            int start = matcher.start();
            int end = matcher.end();
            if (!isInBounds(list, start, end)) {
                list.add(new LinkEntity(common, common, start, end, LinkType.EMAIL));
            }
        }
    }

    private static void addEmoji(List<LinkEntity> list, String text) {
        Matcher matcher = patternEmoji.matcher(text);
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
        Matcher matcher = patternGetAgent.matcher(text);
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
        Matcher matcher = patternImgHtmlTag.matcher(text);
        while (matcher.find()) {
            String url = matcher.group(1);
            int start = matcher.start();
            int end = matcher.end();
            if (!isInBounds(list, start, end)) {
                list.add(new LinkEntity("[图片]", url, start, end, LinkType.IMAGE));
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
        URL, PHONE, EMAIL, EMOJI, IMAGE, QUESTION, DOCUMENT, GET_AGENT, CUSTOM, CATEGORY
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
