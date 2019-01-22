package com.kf5.sdk.helpcenter.mvp.model;

import com.kf5.sdk.helpcenter.api.HelpCenterHttpAPI;
import com.kf5.sdk.helpcenter.mvp.model.api.IHelpCenterTypeModel;
import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/19 14:41
 * email:812219713@qq.com
 */

public class HelpCenterTypeModel implements IHelpCenterTypeModel {

    @Override
    public void getForumByID(Map<String, String> map, HttpRequestCallBack callBack) {
        HelpCenterHttpAPI.getInstance().getForumsList(map, callBack);
    }

    @Override
    public void searchDocument(Map<String, String> map, HttpRequestCallBack callBack) {
        HelpCenterHttpAPI.getInstance().searchPost(map, callBack);
    }
}
