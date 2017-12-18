package com.kf5.sdk.helpcenter.mvp.model;

import com.kf5.sdk.helpcenter.api.HelpCenterHttpAPI;
import com.kf5.sdk.helpcenter.mvp.model.api.IHelpCenterDetailModel;
import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/19 16:15
 * email:812219713@qq.com
 */

public class HelpCenterDetailModel implements IHelpCenterDetailModel {
    @Override
    public void getPostDetail(Map<String, String> map, HttpRequestCallBack callBack) {
        HelpCenterHttpAPI.getInstance().viewPost(map, callBack);
    }
}
