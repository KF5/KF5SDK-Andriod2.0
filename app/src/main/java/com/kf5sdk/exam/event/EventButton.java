package com.kf5sdk.exam.event;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kf5.sdk.system.utils.LogUtil;

/**
 * @author Chosen
 * @create 2020/1/14 17:46
 * @email 812219713@qq.com
 */
public class EventButton extends AppCompatButton {

    public EventButton(Context context) {
        super(context);
    }

    public EventButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        LogUtil.printf("View dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.printf("View onTouchEvent");
        return false;
    }
}
