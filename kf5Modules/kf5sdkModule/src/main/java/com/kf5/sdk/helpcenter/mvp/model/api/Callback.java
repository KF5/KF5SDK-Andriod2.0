package com.kf5.sdk.helpcenter.mvp.model.api;

/**
 * author:chosen
 * date:2016/10/18 16:48
 * email:812219713@qq.com
 */

public interface Callback {

    /**
     * 成功
     */
    void onSuccess(String result);

    /**
     * 失败
     *
     * @param errorMsg 失败信息
     */
    void onFailure(String errorMsg);
}
