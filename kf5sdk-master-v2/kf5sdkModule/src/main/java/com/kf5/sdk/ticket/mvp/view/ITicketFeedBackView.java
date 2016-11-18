package com.kf5.sdk.ticket.mvp.view;

import com.kf5.sdk.system.mvp.view.MvpView;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/21 16:44
 * email:812219713@qq.com
 */

public interface ITicketFeedBackView extends MvpView {

    void createTicketSuccess();

    Map<String, String> getDataMap();

    List<File> getUploadFileList();

    void loadUploadData(Map<String, String> map);
}
