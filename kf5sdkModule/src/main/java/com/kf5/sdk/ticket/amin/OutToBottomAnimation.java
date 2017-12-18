package com.kf5.sdk.ticket.amin;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.kf5.sdk.R;

/**
 * author:chosen
 * date:2016/10/20 18:13
 * email:812219713@qq.com
 */

public class OutToBottomAnimation extends Animation implements Animation.AnimationListener {

    private Context context;

    private View nextView;

    private View animView;

    public OutToBottomAnimation(Context context, View animView, View nextView) {
        this.context = context;
        this.nextView = nextView;
        this.animView = animView;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.kf5_anim_out_to_bottom);
        animation.setAnimationListener(this);
        animation.setFillAfter(false);
        animView.startAnimation(animation);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (animView.isShown()) {
            animView.setVisibility(View.GONE);
        }
        if (!nextView.isShown()) {
            nextView.setVisibility(View.VISIBLE);
        }
        Animation nextAnimation = AnimationUtils.loadAnimation(context, R.anim.kf5_anim_in_from_bottom);
        nextAnimation.setFillAfter(true);
        nextView.startAnimation(nextAnimation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


}
