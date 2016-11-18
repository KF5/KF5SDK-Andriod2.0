package com.kf5.sdk.ticket.mvp.model;

import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.ticket.api.TicketAPI;
import com.kf5.sdk.ticket.mvp.model.api.ITicketListModel;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/20 13:41
 * email:812219713@qq.com
 */

public class TicketListModel implements ITicketListModel {

    @Override
    public void getTicketList(Map<String, String> map, HttpRequestCallBack callBack) {
        TicketAPI.getInstance().getTicketList(map, callBack);
    }
}
