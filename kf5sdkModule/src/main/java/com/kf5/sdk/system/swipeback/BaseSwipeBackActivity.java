package com.kf5.sdk.system.swipeback;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * author:chosen
 * date:2016/11/9 11:06
 * email:812219713@qq.com
 */

public abstract class BaseSwipeBackActivity extends FragmentActivity implements SwipeBackActivityAPI {

    private SwipeBackActivityHelper mSwipeBackActivityHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackActivityHelper = SwipeBackActivityHelper.create(this);
        mSwipeBackActivityHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackActivityHelper.onPostCreate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    public View findViewById(@IdRes int id) {
        View view = super.findViewById(id);
        if (view == null && mSwipeBackActivityHelper != null)
            return mSwipeBackActivityHelper.findViewById(id);
        return view;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackActivityHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    /**
     * Scroll out contentView and finish the activity
     */
    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
