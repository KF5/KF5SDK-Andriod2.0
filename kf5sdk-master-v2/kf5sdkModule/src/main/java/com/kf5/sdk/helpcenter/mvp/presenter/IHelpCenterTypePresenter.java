package com.kf5.sdk.helpcenter.mvp.presenter;

import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;

/**
 * author:chosen
 * date:2016/10/19 14:55
 * email:812219713@qq.com
 */

public interface IHelpCenterTypePresenter {

    void getForumListById(HelpCenterRequestType helpCenterRequestType);

    void searchDocument(HelpCenterRequestType helpCenterRequestType);

}
