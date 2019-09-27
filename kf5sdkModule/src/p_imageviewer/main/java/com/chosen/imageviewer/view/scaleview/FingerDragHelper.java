package com.chosen.imageviewer.view.scaleview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.chosen.imageviewer.ImagePreview;
import com.chosen.imageviewer.view.nine.ViewHelper;
import com.chosen.imageviewer.view.photoview.PhotoView;
import com.kf5.sdk.R;

/**
 * @author Chosen
 * @create 2018/12/13 14:11
 * @email 812219713@qq.com
 */
public class FingerDragHelper extends LinearLayout {

    private static final String TAG = FingerDragHelper.class.getSimpleName();

    private SubsamplingScaleImageViewDragClose imageView;
    private PhotoView imageGif;

    private float mDownY;
    private float mTranslationY;
    private float mLastTranslationY;
    private static int MAX_TRANSLATE_Y = 500;
    private final static int MAX_EXIT_Y = 300;
    private final static long DURATION = 300;
    private boolean isAnimate = false;
    private int fadeIn = R.anim.kf5_imageviewer_fade_in_150;
    private int fadeOut = R.anim.kf5_imageviewer_fade_out_150;
    private int mTouchslop;
    private onAlphaChangedListener onAlphaChangedListener;

    public FingerDragHelper(Context context) {
        this(context, null);
    }

    public FingerDragHelper(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FingerDragHelper(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        mTouchslop = ViewConfiguration.getTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView = ((SubsamplingScaleImageViewDragClose) getChildAt(0));
        imageGif = ((PhotoView) getChildAt(1));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        int action = ev.getAction() & ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getRawY();
            case MotionEvent.ACTION_MOVE:
                if (ImagePreview.getInstance().isEnableDragClose()) {
                    if (imageGif != null && imageGif.getVisibility() == VISIBLE) {
                        isIntercept = (imageGif.getScale() <= (imageGif.getMinimumScale() + 0.001f))
                                && (imageGif.getMaxTouchCount() == 0 || imageGif.getMaxTouchCount() == 1)
                                && Math.abs(ev.getRawY() - mDownY) > 2 * mTouchslop;
                    } else if (imageView != null && imageView.getVisibility() == VISIBLE) {
                        isIntercept = (imageView.getScale() <= (imageView.getMinScale() + 0.001f))
                                && (imageView.getMaxTouchCount() == 0 || imageView.getMaxTouchCount() == 1)
                                && Math.abs(ev.getRawY() - mDownY) > 2 * mTouchslop
                                && imageView.atYEdge;
                    }
                }
                break;
            default:
                break;
        }
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (ImagePreview.getInstance().isEnableDragClose()) {
                    if (imageGif != null && imageGif.getVisibility() == VISIBLE) {
                        onOneFingerPanActionMove(event);
                    } else if (imageView != null && imageView.getVisibility() == VISIBLE) {
                        onOneFingerPanActionMove(event);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                onActionUp();
                break;
            default:
                break;
        }
        return true;
    }

    private void onOneFingerPanActionMove(MotionEvent event) {
        float moveY = event.getRawY();
        mTranslationY = moveY - mDownY + mLastTranslationY;
        //触发回调，根据距离处理其他控件的透明度，显示或者隐藏角标，文字信息等
        if (onAlphaChangedListener != null) {
            onAlphaChangedListener.onTranslationYChanged(event, mTranslationY);
        }
        ViewHelper.setScrollY(this, -((int) mTranslationY));
    }

    private void onActionUp() {
        if (Math.abs(mTranslationY) > MAX_EXIT_Y) {
            exitWithTranslation(mTranslationY);
        } else {
            resetCallbackAnimation();
        }
    }

    public void exitWithTranslation(float currentY) {
        if (currentY > 0) {
            ValueAnimator animDown = ValueAnimator.ofFloat(mTranslationY, getHeight());
            animDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = ((float) animation.getAnimatedValue());
                    ViewHelper.setScrollY(FingerDragHelper.this, -((int) fraction));
                }
            });
            animDown.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    reset();
                    Activity activity = ((Activity) getContext());
                    activity.finish();
                    activity.overridePendingTransition(fadeIn, fadeOut);
                }
            });
            animDown.setDuration(DURATION);
            animDown.setInterpolator(new LinearInterpolator());
            animDown.start();
        } else {
            ValueAnimator animUp = ValueAnimator.ofFloat(mTranslationY, -getHeight());
            animUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = ((float) animation.getAnimatedValue());
                    ViewHelper.setScrollY(FingerDragHelper.this, -((int) fraction));
                }
            });
            animUp.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    reset();
                    ((Activity) getContext()).finish();
                    ((Activity) getContext()).overridePendingTransition(fadeIn, fadeOut);
                }
            });
            animUp.setDuration(DURATION);
            animUp.setInterpolator(new LinearInterpolator());
            animUp.start();
        }
    }

    private void resetCallbackAnimation() {
        ValueAnimator animatorY = ValueAnimator.ofFloat(mTranslationY, 0);
        animatorY.setDuration(DURATION);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isAnimate) {
                    mTranslationY = ((float) animation.getAnimatedValue());
                    mLastTranslationY = mTranslationY;
                    ViewHelper.setScrollY(FingerDragHelper.this, -((int) mTranslationY));
                }
            }
        });
        animatorY.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isAnimate) {
                    mTranslationY = 0;
                    invalidate();
                    reset();
                }
                isAnimate = false;
            }
        });
        animatorY.start();
    }

    public interface onAlphaChangedListener {
        void onTranslationYChanged(MotionEvent event, float translationY);
    }

    public void setOnAlphaChangedListener(FingerDragHelper.onAlphaChangedListener onAlphaChangedListener) {
        this.onAlphaChangedListener = onAlphaChangedListener;
    }

    private void reset() {
        if (onAlphaChangedListener != null) {
            onAlphaChangedListener.onTranslationYChanged(null, mTranslationY);
        }
    }
}
