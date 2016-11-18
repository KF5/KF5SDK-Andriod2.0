package com.kf5.sdk.helpcenter.mvp.view;

import com.kf5.sdk.helpcenter.entity.HelpCenterItem;

import java.util.List;

/**
 * author:chosen
 * date:2016/10/19 15:48
 * email:812219713@qq.com
 */

public interface IHelpCenterTypeChildView extends IHelpCenterBaseView {

    void onLoadPostList(int nextPage, List<HelpCenterItem> helpCenterItems);

    String getSearchKey();

    int getPostId();
}
