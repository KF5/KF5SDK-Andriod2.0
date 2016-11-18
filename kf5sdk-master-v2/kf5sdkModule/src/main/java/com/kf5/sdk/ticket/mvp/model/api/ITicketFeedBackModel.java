package com.kf5.sdk.ticket.mvp.model.api;

import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/21 16:29
 * email:812219713@qq.com
 */

public interface ITicketFeedBackModel {

    void createTicket(Map<String, String> map, HttpRequestCallBack callBack);

    void uploadAttachment(Map<String, String> map, List<File> fileList, HttpRequestCallBack callBack);

}
