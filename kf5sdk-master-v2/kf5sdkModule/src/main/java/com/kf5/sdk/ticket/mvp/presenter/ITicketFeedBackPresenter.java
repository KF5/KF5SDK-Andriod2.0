package com.kf5.sdk.ticket.mvp.presenter;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/21 16:46
 * email:812219713@qq.com
 */

public interface ITicketFeedBackPresenter {

    void createTicket(Map<String, String> uploadMap);

    void uploadAttachment();

}
