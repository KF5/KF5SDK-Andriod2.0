package com.kf5.sdk.helpcenter.mvp.view;

import com.kf5.sdk.helpcenter.entity.HelpCenterItem;

import java.util.List;

/**
 * author:chosen
 * date:2016/10/18 15:17
 * email:812219713@qq.com
 */

public interface IHelpCenterView extends IHelpCenterBaseView {

    void onLoadCategoriesList(int nextPage, List<HelpCenterItem> hepCenterItems);

    String getSearchKey();

}
