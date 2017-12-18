package com.kf5.sdk.system.swipeback;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;


/**
 * author:chosen
 * date:2016/11/9 10:52
 * email:812219713@qq.com
 */

public class SwipeBackActivityHelper {

    private Activity mActivity;

    private SwipeBackLayout mSwipeBackLayout;

    public static SwipeBackActivityHelper create(Activity activity) {
        return new SwipeBackActivityHelper(activity);
    }

    private SwipeBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

    public void onActivityCreate() {
        Window window = mActivity.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSwipeBackLayout = new SwipeBackLayout(mActivity);
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {

            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                Utils.convertActivityToTranslucent(mActivity);
            }

            @Override
            public void onScrollOverThreshold() {

            }
        });
    }


    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public View findViewById(int id) {
        if (mSwipeBackLayout != null)
            return mSwipeBackLayout.findViewById(id);
        return null;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

}
