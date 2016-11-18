package com.kf5.sdk.im.keyboard.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    /**
     * @param secondView
     */
    public void startQueueToImAnim(final View secondView) {

        post(new Runnable() {
            @Override
            public void run() {
                final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.kf5_anim_out_to_bottom);
                animation.setFillAfter(false);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (isShown())
                            setVisibility(GONE);
                        if (!secondView.isShown())
                            secondView.setVisibility(VISIBLE);
                        showNextAnim(secondView);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                startAnimation(animation);
            }
        });
    }

    private void showNextAnim(View view) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.kf5_anim_in_from_bottom);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public void showQueueViewWithAnim() {
        post(new Runnable() {
            @Override
            public void run() {
                if (!isShown())
                    setVisibility(VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.kf5_anim_in_from_bottom);
                animation.setFillAfter(true);
                startAnimation(animation);
            }
        });

    }

}
