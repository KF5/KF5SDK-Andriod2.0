package com.kf5.sdk.im.mvp.presenter;

import android.os.Bundle;

/**
 * author:chosen
 * date:2016/10/27 18:34
 * email:812219713@qq.com
 */

public interface IChatPresenter {

    /**
     * IPC连接
     */
    void connectIPC();

    /**
     * 初始化IM必要参数
     *
     * @param bundle
     */
    void initParams(Bundle bundle);

    /**
     * IM连接
     */
    void connect();

    /**
     * IM断开
     */
    void disconnect();

    /**
     * 是否处于连接状态
     *
     * @return
     */
    boolean isConnected();

    /**
     * 断开IPC
     */
    void disconnectIPC();

}
