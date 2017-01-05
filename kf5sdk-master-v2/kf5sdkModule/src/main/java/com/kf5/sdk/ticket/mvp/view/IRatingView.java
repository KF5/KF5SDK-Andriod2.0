package com.kf5.sdk.ticket.mvp.view;

import com.kf5.sdk.system.mvp.view.MvpView;

/**
 * author:chosen
 * date:2017/1/4 18:26
 * email:812219713@qq.com
 */

public interface IRatingView extends MvpView {

    void onLoadRatingData(int resultCode, String message);

}
