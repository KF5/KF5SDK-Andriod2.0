package com.kf5.sdk.ticket.mvp.model.api;

import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/20 13:42
 * email:812219713@qq.com
 */

public interface ITicketListModel {

    void getTicketList(Map<String, String> map, HttpRequestCallBack callBack);

}
