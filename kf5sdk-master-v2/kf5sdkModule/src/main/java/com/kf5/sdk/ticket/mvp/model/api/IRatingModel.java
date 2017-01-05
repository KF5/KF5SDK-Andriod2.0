package com.kf5.sdk.ticket.mvp.model.api;

import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.util.Map;

/**
 * author:chosen
 * date:2017/1/4 18:24
 * email:812219713@qq.com
 */

public interface IRatingModel {

    void onRating(Map<String, String> map, HttpRequestCallBack httpRequestCallBack);
}
