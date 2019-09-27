package com.chosen.imageviewer.view.listener;

import android.view.View;

/**
 * @author Chosen
 * @create 2019/3/12 14:31
 * @email 812219713@qq.com
 */
public interface OnOriginProgressListener {

    /**
     * 加载中
     */
    void progress(View parentView, int progress);

    /**
     * 加载完成
     */
    void finish(View parentView);

}
