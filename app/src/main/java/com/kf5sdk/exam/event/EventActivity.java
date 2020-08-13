package com.kf5sdk.exam.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kf5.sdk.system.utils.LogUtil;

/**
 * @author Chosen
 * @create 2020/1/14 17:44
 * @email 812219713@qq.com
 */
public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventButton eventButton = new EventButton(this);
        eventButton.setText("测试");
        EventLinearLayout eventLinearLayout = new EventLinearLayout(this);
        ViewGroup.LayoutParams params=  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        eventLinearLayout.setLayoutParams(params);
        eventLinearLayout.setOrientation(LinearLayout.VERTICAL);
        eventLinearLayout.addView(eventButton);
        setContentView(eventLinearLayout);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.printf("Activity dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
//        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.printf("Activity touchEvent");
//        return super.onTouchEvent(event);
        return false;
    }
}
