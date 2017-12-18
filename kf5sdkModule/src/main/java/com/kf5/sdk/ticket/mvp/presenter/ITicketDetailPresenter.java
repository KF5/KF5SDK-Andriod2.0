package com.kf5.sdk.ticket.mvp.presenter;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/20 15:34
 * email:812219713@qq.com
 */

public interface ITicketDetailPresenter {

    void getTicketDetail();

    void replayTicket(Map<String, String> uploadMap);

}
