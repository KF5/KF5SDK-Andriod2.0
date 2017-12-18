package com.kf5.sdk.ticket.mvp.model;

import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.ticket.api.TicketAPI;
import com.kf5.sdk.ticket.mvp.model.api.ITicketAttributeModel;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/21 15:50
 * email:812219713@qq.com
 */

public class TicketAttributeModel implements ITicketAttributeModel {

    @Override
    public void getTicketAttribute(Map<String, String> map, HttpRequestCallBack callBack) {
        TicketAPI.getInstance().getTicketAttribute(map, callBack);
    }
}
