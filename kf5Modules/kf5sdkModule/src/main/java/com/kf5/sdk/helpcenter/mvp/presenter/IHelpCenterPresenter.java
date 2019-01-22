package com.kf5.sdk.helpcenter.mvp.presenter;

import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;

/**
 * author:chosen
 * date:2016/10/18 15:19
 * email:812219713@qq.com
 */

public interface IHelpCenterPresenter {

    void getCategoriesList(HelpCenterRequestType helpCenterRequestType);

    void searchDocument(HelpCenterRequestType helpCenterRequestType);

}
