package com.kf5.sdk.ticket.mvp.model.api;

import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/21 15:47
 * email:812219713@qq.com
 */

public interface ITicketAttributeModel {

    void getTicketAttribute(Map<String, String> map, HttpRequestCallBack callBack);

}
