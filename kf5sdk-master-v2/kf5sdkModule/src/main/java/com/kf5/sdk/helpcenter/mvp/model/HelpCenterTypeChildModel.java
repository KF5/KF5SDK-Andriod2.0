package com.kf5.sdk.helpcenter.mvp.model;

import com.kf5.sdk.helpcenter.api.HelpCenterHttpAPI;
import com.kf5.sdk.helpcenter.mvp.model.api.IHelpCenterTypeChildModel;
import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/19 15:39
 * email:812219713@qq.com
 */

public class HelpCenterTypeChildModel implements IHelpCenterTypeChildModel {
    @Override
    public void getPostByID(Map<String, String> map, HttpRequestCallBack callBack) {
        HelpCenterHttpAPI.getInstance().getPostList(map, callBack);
    }

    @Override
    public void searchDocument(Map<String, String> map, HttpRequestCallBack callBack) {
        HelpCenterHttpAPI.getInstance().searchPost(map, callBack);
    }
}
