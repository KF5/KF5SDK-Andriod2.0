package com.kf5.sdk.ticket.mvp.model;

import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.ticket.api.TicketAPI;
import com.kf5.sdk.ticket.mvp.model.api.ITicketDetailModel;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/20 15:28
 * email:812219713@qq.com
 */

public class TicketDetailModel implements ITicketDetailModel {
    @Override
    public void getTicketDetail(Map<String, String> map, HttpRequestCallBack callBack) {
        TicketAPI.getInstance().getTicketDetail(map, callBack);
    }

    @Override
    public void replyTicket(Map<String, String> map, HttpRequestCallBack callBack) {
        TicketAPI.getInstance().updateTicket(map, callBack);
    }

    @Override
    public void uploadAttachment(Map<String, String> map, List<File> file, HttpRequestCallBack callBack) {
        TicketAPI.getInstance().uploadAttachment(map, file, callBack);
    }
}
