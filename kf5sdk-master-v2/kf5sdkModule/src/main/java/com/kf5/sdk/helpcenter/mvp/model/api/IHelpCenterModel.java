package com.kf5.sdk.helpcenter.mvp.model.api;

import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/18 15:01
 * email:812219713@qq.com
 */

public interface IHelpCenterModel {

    void getCategoriesList(Map<String, String> queryMap, HttpRequestCallBack callBack);

    void searchDocument(Map<String, String> queryMap, HttpRequestCallBack callBack);


}
