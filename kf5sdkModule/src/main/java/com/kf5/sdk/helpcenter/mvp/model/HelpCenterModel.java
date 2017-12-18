package com.kf5.sdk.helpcenter.mvp.model;


import com.kf5.sdk.helpcenter.api.HelpCenterHttpAPI;
import com.kf5.sdk.helpcenter.mvp.model.api.IHelpCenterModel;
import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/18 15:00
 * email:812219713@qq.com
 */

public class HelpCenterModel implements IHelpCenterModel {

    @Override
    public void getCategoriesList(Map<String, String> map, HttpRequestCallBack callBack) {
        HelpCenterHttpAPI.getInstance().getCategoriesList(map, callBack);
    }

    @Override
    public void searchDocument(Map<String, String> map, HttpRequestCallBack callBack) {
        HelpCenterHttpAPI.getInstance().searchPost(map, callBack);
    }

}
