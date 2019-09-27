package com.chosen.album.listener;

/**
 * @author Chosen
 * @create 2019/1/9 16:30
 * @email 812219713@qq.com
 * <p>
 * PreViewItemFragment 和  BasePreViewActivity 通信的接口 ，为了方便拿到 ImageViewTouch 的点击事件
 */
public interface OnFragmentInteractionListener {
    /**
     * ImageViewTouch 被点击了
     */
    void onClick();
}