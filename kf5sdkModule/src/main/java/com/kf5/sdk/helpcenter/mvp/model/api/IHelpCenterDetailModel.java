package com.kf5.sdk.helpcenter.mvp.model.api;

import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/19 16:13
 * email:812219713@qq.com
 */

public interface IHelpCenterDetailModel {

    void getPostDetail(Map<String, String> map, HttpRequestCallBack callBack);

}
