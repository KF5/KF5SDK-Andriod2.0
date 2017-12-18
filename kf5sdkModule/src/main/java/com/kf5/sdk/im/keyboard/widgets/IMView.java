package com.kf5.sdk.im.keyboard.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.keyboard.EmoticonsKeyBoard;
import com.kf5.sdk.im.keyboard.utils.EmoticonsKeyboardUtils;
import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.im.widget.AudioRecordButton;

/**
 * author:chosen
 * date:2016/11/10 10:48
 * email:812219713@qq.com
 * IM 组件容器
 */

public class IMView extends FrameLayout implements View.OnClickListener {

    private ImageView mImageViewVoiceOrText;

    private AudioRecordButton mButtonVoice;

    private RelativeLayout mLayoutInput;

    private EmoticonsEditText mETContent;

    private ImageView mImageViewEmoji;

    private TextView mTVSend;

    private ImageView mImageViewApps;

    private boolean isChangeText;

    private IMViewListener mIMViewListener;

    public void setIMViewListener(IMViewListener IMViewListener) {
        mIMViewListener = IMViewListener;
    }

    public RelativeLayout getLayoutInput() {
        return mLayoutInput;
    }

    public EmoticonsEditText getETChat() {
        return mETContent;
    }

    public AudioRecordButton getButtonVoice() {
        return mButtonVoice;
    }

    public TextView getTVSend() {
        return mTVSend;
    }

    public IMView(Context context) {
        this(context, null);
    }

    public IMView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 将排队临时消息设置到聊天ET中
     *
     * @param text
     */
    public void toggleText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mETContent.setText(text);
            mETContent.setSelection(text.length());
            EmoticonsKeyboardUtils.openSoftKeyboard(mETContent);
        }
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.kf5_im_layout, this);
        mImageViewVoiceOrText = (ImageView) findViewById(R.id.kf5_btn_voice_or_text);
        mButtonVoice = (AudioRecordButton) findViewById(R.id.kf5_btn_voice);
        mLayoutInput = (RelativeLayout) findViewById(R.id.kf5_rl_input);
        mETContent = (EmoticonsEditText) findViewById(R.id.kf5_et_chat);
        mImageViewEmoji = (ImageView) findViewById(R.id.kf5_btn_emoji);
        mTVSend = (TextView) findViewById(R.id.kf5_btn_send);
        mImageViewApps = (ImageView) findViewById(R.id.kf5_btn_chat_by_others);
        mImageViewVoiceOrText.setOnClickListener(this);
        mImageViewEmoji.setOnClickListener(this);
        mImageViewApps.setOnClickListener(this);
        setETContentAttr();
    }


    private void setETContentAttr() {
        mETContent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!mETContent.isFocused()) {
                    mETContent.setFocusable(true);
                    mETContent.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

        mETContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i1 < i2) {
                    if (getContext() instanceof KF5ChatActivity) {
                        KF5ChatActivity kf5ChatActivity = (KF5ChatActivity) getContext();
                        if (!kf5ChatActivity.imWidgetEnable()) {
                            kf5ChatActivity.aiToGetAgents();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeSendBtnStatus(editable);
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.kf5_btn_voice_or_text) {
            if (getContext() instanceof KF5ChatActivity) {
                KF5ChatActivity kf5ChatActivity = (KF5ChatActivity) getContext();
                if (!kf5ChatActivity.imWidgetEnable()) {
                    kf5ChatActivity.aiToGetAgents();
                } else {
                    changeRecordImgStatus();
                }
            } else {
                changeRecordImgStatus();
            }
        } else if (id == R.id.kf5_btn_emoji) {
            mLayoutInput.setVisibility(VISIBLE);
            mButtonVoice.setVisibility(GONE);
            if (mIMViewListener != null)
                mIMViewListener.onToggleFuncView(EmoticonsKeyBoard.FUNC_TYPE_EMOTION);
        } else if (id == R.id.kf5_btn_chat_by_others) {
            if (mButtonVoice.isShown()) {
                RotateAnimation tAnimation = new RotateAnimation(90, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                tAnimation.setDuration(200);
                tAnimation.setFillAfter(false);
                tAnimation.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        mImageViewVoiceOrText.setImageResource(R.drawable.kf5_chat_by_voice);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub
                    }
                });
                mImageViewVoiceOrText.startAnimation(tAnimation);
            }
            if (mIMViewListener != null)
                mIMViewListener.onToggleFuncView(EmoticonsKeyBoard.FUNC_TYPE_APPS);
        }
    }


    /**
     * 修改录音IMG的图片资源
     */
    private void changeRecordImgStatus() {
        if (mLayoutInput.isShown()) {
            RotateAnimation animation = new RotateAnimation(0, 90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(200);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mImageViewVoiceOrText.setImageResource(R.drawable.kf5_chat_by_text);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLayoutInput.setVisibility(GONE);
                    mButtonVoice.setVisibility(VISIBLE);
                    if (mIMViewListener != null)
                        mIMViewListener.onReset();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mImageViewVoiceOrText.startAnimation(animation);
        } else {
            RotateAnimation animation = new RotateAnimation(90, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(200);
            animation.setFillAfter(false);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mImageViewVoiceOrText.setImageResource(R.drawable.kf5_chat_by_voice);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLayoutInput.setVisibility(VISIBLE);
                    mButtonVoice.setVisibility(GONE);
                    EmoticonsKeyboardUtils.openSoftKeyboard(mETContent);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mImageViewVoiceOrText.startAnimation(animation);
        }
    }

    private void changeSendBtnStatus(Editable editable) {
        if (!TextUtils.isEmpty(editable)) {
            if (!isChangeText) {
                isChangeText = true;
                ScaleAnimation animation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                mTVSend.setAnimation(animation);
                animation.setDuration(200);
                animation.setFillAfter(false);
                animation.start();
                mTVSend.setVisibility(VISIBLE);
                mImageViewApps.setVisibility(GONE);
            }
        } else {
            isChangeText = false;
            ScaleAnimation animation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(200);
            animation.setFillAfter(false);
            mImageViewApps.startAnimation(animation);
            mImageViewApps.setVisibility(VISIBLE);
            mTVSend.setVisibility(GONE);
        }
    }

    public interface IMViewListener {

        void onReset();

        void onToggleFuncView(int key);

    }

}
