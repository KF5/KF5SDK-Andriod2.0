package com.kf5.sdk.helpcenter.mvp.view;

import com.kf5.sdk.helpcenter.entity.HelpCenterItem;
import com.kf5.sdk.system.mvp.view.MvpView;

import java.util.List;
import java.util.Map;

import static com.kf5.sdk.im.mvp.presenter.IMPresenter.RESULT_OK;

/**
 * author:chosen
 * date:2016/10/20 11:11
 * email:812219713@qq.com
 */

public interface IHelpCenterBaseView extends MvpView {

    Map<String, String> getCustomMap();

    void onLoadHelpCenterList(int nextPage, List<HelpCenterItem> helpCenterItems);

    /**
     * 文档列表数据解析接口
     */
    class HelpCenterDataBuilder {

        /**
         * 回调返回值
         *
         * @param resultCode
         * @param message
         * @param nextPage
         * @param list
         * @param baseView
         */
        public static void dealHelpCenterData(int resultCode, String message, int nextPage, List<HelpCenterItem> list, IHelpCenterBaseView baseView) {
            if (resultCode == RESULT_OK) {
                baseView.onLoadHelpCenterList(nextPage, list);
            } else {
                baseView.showError(resultCode, message);
            }
        }
    }

}
