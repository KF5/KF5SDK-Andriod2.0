package com.kf5.sdk.im.keyboard.utils;

import android.view.View;

/**
 * author:chosen
 * date:2017/4/5 11:05
 * email:812219713@qq.com
 */

public class ViewUtils {

    /**
     * 设置targetView可见，同时将其他View集合设置为不可见
     *
     * @param targetView
     * @param viewList
     */
    public static void toggleTargetViewVisible(View targetView, View... viewList) {

        if (viewList != null) {
            for (View view : viewList) {
                if (view != null && view.getVisibility() != View.GONE) {
                    view.setVisibility(View.GONE);
                }
            }
        }
        if (targetView != null && targetView.getVisibility() != View.VISIBLE) {
            targetView.setVisibility(View.VISIBLE);
        }
    }

}
