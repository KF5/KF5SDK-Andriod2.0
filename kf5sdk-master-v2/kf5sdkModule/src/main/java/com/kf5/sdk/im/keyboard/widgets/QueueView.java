package com.kf5.sdk.im.keyboard.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kf5.sdk.R;

/**
 * author:chosen
 * date:2016/11/9 16:10
 * email:812219713@qq.com
 */

public class QueueView extends FrameLayout {

    private EmoticonsEditText mEditText;

    private TextView mTextViewSend;

    public QueueView(Context context) {
        this(context, null);
    }

    public QueueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.kf5_queue_layout, this);
        mEditText = (EmoticonsEditText) findViewById(R.id.kf5_queue_edit_text);
        mTextViewSend = (TextView) findViewById(R.id.kf5_queue_send_message);
        mEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!mEditText.isFocused()) {
                    mEditText.setFocusable(true);
                    mEditText.setFocusableInTouchMode(true);
                }
                return false;
            }
        });
    }

    public EmoticonsEditText getEditText() {
        return mEditText;
    }

    public TextView getTextViewSend() {
        return mTextViewSend;
    }


}
