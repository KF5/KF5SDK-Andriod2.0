package com.kf5.sdk.helpcenter.mvp.view;

import com.kf5.sdk.helpcenter.entity.HelpCenterItem;

import java.util.List;

/**
 * author:chosen
 * date:2016/10/19 14:46
 * email:812219713@qq.com
 */

public interface IHelpCenterTypeView extends IHelpCenterBaseView {

    void onLoadForumList(int nextPage, List<HelpCenterItem> helpCenterItems);

    String getSearchKey();

    int getForumId();


}
