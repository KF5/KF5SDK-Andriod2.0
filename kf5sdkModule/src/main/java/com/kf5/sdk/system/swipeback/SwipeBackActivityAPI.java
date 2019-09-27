package com.kf5.sdk.system.swipeback;

/**
 * author:chosen
 * date:2016/11/9 11:04
 * email:812219713@qq.com
 */

public interface SwipeBackActivityAPI {

    SwipeBackLayout getSwipeBackLayout();

    void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    void scrollToFinishActivity();
}
