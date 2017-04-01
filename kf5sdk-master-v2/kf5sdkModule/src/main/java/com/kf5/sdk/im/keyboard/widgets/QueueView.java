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
import com.kf5.sdk.im.keyboard.api.AnimationEndListener;

/**
 * author:chosen
 * date:2016/11/9 16:10
 * email:812219713@qq.com
 */

public class QueueView extends FrameLayout {

    private EmoticonsEditText mEditText;

    private TextView mTextViewSend;

//    private Animation animation;

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
    public void startQueueToImAnim(final View secondView, final AnimationEndListener listener) {

        if (isShown() && !secondView.isShown()) {
            postDelayed(new Runnable() {
                @Override
                public void run() {

                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.kf5_anim_out_to_bottom);
                    animation.setFillAfter(false);
                    animation.setStartOffset(240);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            setVisibility(GONE);
                            secondView.setVisibility(VISIBLE);
                            showNextAnim(secondView, listener);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    startAnimation(animation);
                }
            }, 0);
        }

    }

    private void showNextAnim(View view, final AnimationEndListener listener) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.kf5_anim_in_from_bottom);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null) {
                    listener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public void showQueueViewWithAnim(final View targetView) {
        post(new Runnable() {
            @Override
            public void run() {
                if (targetView.isShown()) {
                    final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.kf5_anim_out_to_bottom);
                    animation.setFillAfter(false);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            targetView.setVisibility(GONE);
//                            showQueue();
                            setVisibility(VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    targetView.startAnimation(animation);
                } else {
                    showQueue();
//                    setVisibility(VISIBLE);
                }
            }
        });

    }

    private void showQueue() {
        if (!isShown())
            setVisibility(VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.kf5_anim_in_from_bottom);
        animation.setFillAfter(true);
        startAnimation(animation);
    }

}
