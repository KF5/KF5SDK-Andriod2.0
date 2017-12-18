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
 * date:2016/11/9 17:01
 * email:812219713@qq.com
 */

public class AIView extends FrameLayout {

    private EmoticonsEditText mEmojiconEditText;

    private TextView mTextViewSend, mTextViewAIToAgent;

    public AIView(Context context) {
        this(context, null);
    }

    public AIView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.kf5_ai_layout, this);
        mEmojiconEditText = (EmoticonsEditText) findViewById(R.id.kf5_ai_text_view);
        mTextViewSend = (TextView) findViewById(R.id.kf5_ai_textview_send_message);
        mTextViewAIToAgent = (TextView) findViewById(R.id.kf5_ai_to_agent_btn);
        mEmojiconEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!mEmojiconEditText.isFocused()) {
                    mEmojiconEditText.setFocusable(true);
                    mEmojiconEditText.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

    }

    public EmoticonsEditText getEmojiconEditText() {
        return mEmojiconEditText;
    }

    public TextView getTextViewSend() {
        return mTextViewSend;
    }

    public TextView getTextViewAIToAgent() {
        return mTextViewAIToAgent;
    }


}
