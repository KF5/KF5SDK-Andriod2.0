package com.kf5.sdk.system.mvp.presenter;

/**
 * author:chosen
 * date:2016/10/13 15:24
 * email:812219713@qq.com
 */

public interface PresenterFactory<T extends Presenter> {

    T create();

}
