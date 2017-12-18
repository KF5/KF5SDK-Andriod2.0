package com.kf5.sdk.helpcenter.mvp.presenter;

import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;

/**
 * author:chosen
 * date:2016/10/19 15:49
 * email:812219713@qq.com
 */

public interface IHelpCenterTypeChildPresenter {

    void getPostListById(HelpCenterRequestType helpCenterRequestType);

    void searchDocument(HelpCenterRequestType helpCenterRequestType);
}
