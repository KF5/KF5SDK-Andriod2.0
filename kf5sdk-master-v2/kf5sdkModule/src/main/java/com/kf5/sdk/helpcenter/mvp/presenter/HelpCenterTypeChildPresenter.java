package com.kf5.sdk.helpcenter.mvp.presenter;

import android.support.v4.util.ArrayMap;

import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;
import com.kf5.sdk.helpcenter.mvp.usecase.HelpCenterTypeChildCase;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterBaseView;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterTypeChildView;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/19 15:47
 * email:812219713@qq.com
 */

public class HelpCenterTypeChildPresenter extends BasePresenter<IHelpCenterTypeChildView> implements IHelpCenterTypeChildPresenter {


    private HelpCenterTypeChildCase mHelpCenterTypeChildCase;

    public HelpCenterTypeChildPresenter(HelpCenterTypeChildCase helpCenterTypeChildCase) {
        mHelpCenterTypeChildCase = helpCenterTypeChildCase;
    }

    @Override
    public void getPostListById(HelpCenterRequestType helpCenterRequestType) {
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.FORUM_ID, String.valueOf(getMvpView().getPostId()));
        map.putAll(getMvpView().getCustomMap());
        HelpCenterTypeChildCase.RequestCase requestCase = new HelpCenterTypeChildCase.RequestCase(helpCenterRequestType, map);
        dealData(requestCase);
    }

    @Override
    public void searchDocument(HelpCenterRequestType helpCenterRequestType) {
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.QUERY, getMvpView().getSearchKey());
        map.putAll(getMvpView().getCustomMap());
        HelpCenterTypeChildCase.RequestCase requestCase = new HelpCenterTypeChildCase.RequestCase(helpCenterRequestType, map);
        dealData(requestCase);
    }

    private void dealData(HelpCenterTypeChildCase.RequestCase requestCase) {

        checkViewAttached();
        getMvpView().showLoading("");
        mHelpCenterTypeChildCase.setRequestValues(requestCase);
        mHelpCenterTypeChildCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<HelpCenterTypeChildCase.ResponseValue>() {

            @Override
            public void onSuccess(HelpCenterTypeChildCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        IHelpCenterBaseView.HelpCenterDataBuilder.dealPostList(response.result, getMvpView());
                    } catch (Exception e) {
                        e.printStackTrace();
                        getMvpView().showError(RESULT_ERROR, e.getMessage());
                    }
                }
            }

            @Override
            public void onError(String msg) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    getMvpView().showError(RESULT_ERROR, msg);
                }
            }
        });
        mHelpCenterTypeChildCase.run();
    }
}
