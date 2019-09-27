package com.kf5.sdk.helpcenter.mvp.presenter;

import android.support.v4.util.ArrayMap;

import com.kf5.sdk.helpcenter.entity.HelpCenterCollection;
import com.kf5.sdk.helpcenter.entity.HelpCenterItem;
import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;
import com.kf5.sdk.helpcenter.mvp.usecase.HelpCenterCase;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterBaseView;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterView;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.Result;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/18 15:20
 * email:812219713@qq.com
 */

public class HelpCenterPresenter extends BasePresenter<IHelpCenterView> implements IHelpCenterPresenter {

    private final HelpCenterCase mHelpCenterCase;

    public HelpCenterPresenter(HelpCenterCase helpCenterCase) {
        mHelpCenterCase = helpCenterCase;
    }

    @Override
    public void searchDocument(HelpCenterRequestType helpCenterRequestType) {
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.QUERY, getMvpView().getSearchKey());
        map.putAll(getMvpView().getCustomMap());
        dealData(helpCenterRequestType, map);
    }

    @Override
    public void getCommonDataList(HelpCenterRequestType helpCenterRequestType) {
        Map<String, String> map = new ArrayMap<>();
        map.putAll(getMvpView().getCustomMap());
        switch (helpCenterRequestType) {
            case FORUM:
                map.put(Field.CATEGORY_ID, String.valueOf(getMvpView().getItemId()));
                break;
            case POST:
                map.put(Field.FORUM_ID, String.valueOf(getMvpView().getItemId()));
                break;
        }
        dealData(helpCenterRequestType, map);
    }


    private void dealData(final HelpCenterRequestType helpCenterRequestType, Map<String, String> map) {
        checkViewAttached();
        getMvpView().showLoading("");
        final HelpCenterCase.RequestCase requestCase = new HelpCenterCase.RequestCase(helpCenterRequestType, map);
        mHelpCenterCase.setRequestValues(requestCase);
        mHelpCenterCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<HelpCenterCase.ResponseValue>() {
            @Override
            public void onSuccess(HelpCenterCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        dealResult(response.result);
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
        mHelpCenterCase.run();
    }

    private void dealResult(String data) {
        Result<HelpCenterCollection> result = Result.fromJson(data, HelpCenterCollection.class);
        if (result != null) {
            int resultCode = result.getCode();
            List<HelpCenterItem> list = new ArrayList<>();
            int nextPage = 1;
            if (resultCode == RESULT_OK) {
                HelpCenterCollection helpCenterItemObj = result.getData();
                if (helpCenterItemObj != null) {
                    if (helpCenterItemObj.getList() != null) {
                        list.addAll(helpCenterItemObj.getList());
                    }
                    if (helpCenterItemObj.getNext_page() > 0) {
                        nextPage = helpCenterItemObj.getNext_page();
                    }
                }
            }
            IHelpCenterBaseView.HelpCenterDataBuilder.dealHelpCenterData(resultCode, result.getMessage(), nextPage, list, getMvpView());
        }
    }
}
