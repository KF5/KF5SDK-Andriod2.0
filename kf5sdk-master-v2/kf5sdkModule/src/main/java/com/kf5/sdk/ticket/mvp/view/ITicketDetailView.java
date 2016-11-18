package com.kf5.sdk.ticket.mvp.view;

import com.kf5.sdk.system.mvp.view.MvpView;
import com.kf5.sdk.ticket.entity.Comment;
import com.kf5.sdk.ticket.entity.Requester;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/20 15:39
 * email:812219713@qq.com
 */

public interface ITicketDetailView extends MvpView {

    Map<String, String> getTicketDetailMap();

    int getTicketId();

    void loadTicketDetail(int _nextPage, Requester requester, List<Comment> commentList);

    List<File> getFileList();

    void replyTicketSuccess(Requester requester);

    void replyTicketError(String msg);

}
