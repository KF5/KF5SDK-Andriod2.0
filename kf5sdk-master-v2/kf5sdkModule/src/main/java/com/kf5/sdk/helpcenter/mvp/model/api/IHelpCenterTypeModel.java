package com.kf5.sdk.helpcenter.mvp.model.api;

import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/19 14:36
 * email:812219713@qq.com
 */

public interface IHelpCenterTypeModel {

    void getForumByID(Map<String, String> map, HttpRequestCallBack callBack);

    void searchDocument(Map<String, String> map, HttpRequestCallBack callBack);
}
