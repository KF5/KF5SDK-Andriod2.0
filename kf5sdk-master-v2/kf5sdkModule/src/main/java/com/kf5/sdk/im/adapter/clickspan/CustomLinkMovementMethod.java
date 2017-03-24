package com.kf5.sdk.im.adapter.clickspan;

import android.graphics.Color;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.BackgroundColorSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * author:chosen
 * date:2017/3/24 15:19
 * email:812219713@qq.com
 */

public class CustomLinkMovementMethod extends LinkMovementMethod {

    private long start = 0;

    private long end = 0;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {

        try {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN ||
                    action == MotionEvent.ACTION_CANCEL) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();
                x += widget.getScrollX();
                y += widget.getScrollY();
                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);
                CustomClickSpan[] link = buffer.getSpans(off, off, CustomClickSpan.class);
                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        end = System.currentTimeMillis();
                        Long time = end - start;
                        if (time > 500) {
                            BackgroundColorSpan[] backgroundColorSpans = buffer
                                    .getSpans(0, buffer.length(), BackgroundColorSpan.class);
                            for (BackgroundColorSpan bkcolor : backgroundColorSpans) {
                                buffer.removeSpan(bkcolor);
                            }
                            return Touch.onTouchEvent(widget, buffer, event);
                        } else {
                            BackgroundColorSpan[] backgroundColorSpans = buffer
                                    .getSpans(0, buffer.length(), BackgroundColorSpan.class);
                            for (BackgroundColorSpan bkcolor : backgroundColorSpans) {
                                buffer.removeSpan(bkcolor);
                            }
                            link[0].onClick(widget);
                        }
                    } else if (action == MotionEvent.ACTION_DOWN) {

                        start = System.currentTimeMillis();
                        BackgroundColorSpan bkcolor;
                        bkcolor = new BackgroundColorSpan(Color.parseColor("#038cc2"));
                        buffer.setSpan(bkcolor,
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]),
                                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    } else {
                        BackgroundColorSpan[] backgroundColorSpans = buffer
                                .getSpans(0, buffer.length(), BackgroundColorSpan.class);
                        for (BackgroundColorSpan bkcolor : backgroundColorSpans) {
                            buffer.removeSpan(bkcolor);
                        }
                    }
                    return true;
                } else {
                    BackgroundColorSpan[] backgroundColorSpans = buffer
                            .getSpans(0, buffer.length(), BackgroundColorSpan.class);
                    for (BackgroundColorSpan bkcolor : backgroundColorSpans) {
                        buffer.removeSpan(bkcolor);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return true;
    }

}