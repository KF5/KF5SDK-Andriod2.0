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
import com.kf5.sdk.system.utils.LogUtil;

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

    /**
     * 将IM状态从机器人转为排队
     *
     * @param secondView
     */
    public void startAIToQueueAnim(final View secondView) {

        LogUtil.printf("AIView切换到QueueView");
        if (isShown() && !secondView.isShown()) {
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
                            LogUtil.printf("第一个动画执行结束");
                            setVisibility(GONE);
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


    }

    private void showNextAnim(View view) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.kf5_anim_in_from_bottom);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LogUtil.printf("第二个动画执行结束");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public void showAIViewWithAnim(final View targetView) {

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
                            setVisibility(VISIBLE);
//                            showAIView();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    targetView.startAnimation(animation);
                } else {
                    showAIView();
//                    setVisibility(VISIBLE);
                }
            }
        });

    }


    private void showAIView() {
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
