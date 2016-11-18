package com.kf5.sdk.ticket.mvp.view;

import com.kf5.sdk.system.mvp.view.MvpView;
import com.kf5.sdk.ticket.entity.UserField;

import java.util.List;

/**
 * author:chosen
 * date:2016/10/21 16:01
 * email:812219713@qq.com
 */

public interface ITicketAttributeView extends MvpView {

    int getTicketId();

    void onLoadTicketAttribute(List<UserField> list);

}
