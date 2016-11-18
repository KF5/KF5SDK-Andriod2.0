package com.kf5.sdk.ticket.mvp.view;

import com.kf5.sdk.system.mvp.view.MvpView;
import com.kf5.sdk.ticket.entity.Requester;

import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/20 13:46
 * email:812219713@qq.com
 */

public interface ITicketListView extends MvpView {

    Map<String, String> getCustomMap();

    void loadResultData(int _nextPage, List<Requester> mRequesterList);

}

