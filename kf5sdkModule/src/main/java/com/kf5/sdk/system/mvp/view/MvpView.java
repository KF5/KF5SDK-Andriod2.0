package com.kf5.sdk.system.mvp.view;

/**
 * author:chosen
 * date:2016/10/13 14:57
 * email:812219713@qq.com
 */

public interface MvpView {

    /**
     * 显示loading对话框
     *
     * @param msg
     */
    void showLoading(String msg);

    /**
     * 隐藏loading对话框
     */
    void hideLoading();


    /**
     * 显示错误信息
     *
     * @param resultCode 错误码
     * @param msg        错误信息
     */
    void showError(int resultCode, String msg);
}
