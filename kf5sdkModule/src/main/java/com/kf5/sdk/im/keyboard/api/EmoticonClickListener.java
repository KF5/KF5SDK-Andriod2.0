package com.kf5.sdk.im.keyboard.api;

/**
 * author:chosen
 * date:2016/11/1 16:00
 * email:812219713@qq.com
 */

public interface EmoticonClickListener<T> {

    void onEmoticonClick(T t, int actionType, boolean isDelBtn);
}
