package com.kf5.sdk.system.mvp.presenter;

import com.kf5.sdk.system.mvp.view.MvpView;

/**
 * author:chosen
 * date:2016/10/13 14:58
 * email:812219713@qq.com
 */

public interface Presenter<V extends MvpView> {

    /**
     * Presenter与View建立连接
     *
     * @param mvpView 与此Presenter相对应的View
     */
    void attachView(V mvpView);

    /**
     * Presenter与View连接断开
     */
    void detachView();
}
