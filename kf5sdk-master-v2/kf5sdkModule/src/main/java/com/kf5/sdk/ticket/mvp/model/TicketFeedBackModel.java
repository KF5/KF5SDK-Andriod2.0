package com.kf5.sdk.ticket.mvp.model;

import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.ticket.api.TicketAPI;
import com.kf5.sdk.ticket.mvp.model.api.ITicketFeedBackModel;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/21 16:31
 * email:812219713@qq.com
 */

public class TicketFeedBackModel implements ITicketFeedBackModel {
    @Override
    public void createTicket(Map<String, String> map, HttpRequestCallBack callBack) {
        TicketAPI.getInstance().createTicket(map, callBack);
    }

    @Override
    public void uploadAttachment(Map<String, String> map, List<File> file, HttpRequestCallBack callBack) {
        TicketAPI.getInstance().uploadAttachment(map, file, callBack);
    }
}
