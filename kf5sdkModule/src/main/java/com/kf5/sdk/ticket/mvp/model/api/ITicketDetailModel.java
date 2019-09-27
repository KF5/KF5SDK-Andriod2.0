package com.kf5.sdk.ticket.mvp.model.api;

import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/20 15:27
 * email:812219713@qq.com
 */

public interface ITicketDetailModel {

    void getTicketDetail(Map<String, String> map, HttpRequestCallBack callBack);

    void replyTicket(Map<String, String> map, HttpRequestCallBack callBack);

    void uploadAttachment(Map<String, String> map, List<File> file, HttpRequestCallBack callBack);

}
