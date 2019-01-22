package com.kf5.sdk.helpcenter.mvp.presenter;

import android.support.v4.util.ArrayMap;

import com.kf5.sdk.helpcenter.entity.HelpCenterItem;
import com.kf5.sdk.helpcenter.entity.HelpCenterItemForum;
import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;
import com.kf5.sdk.helpcenter.mvp.usecase.HelpCenterTypeCase;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterBaseView;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterTypeView;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.Result;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/19 14:59
 * email:812219713@qq.com
 */

public class HelpCenterTypePresenter extends BasePresenter<IHelpCenterTypeView> implements IHelpCenterTypePresenter {

    private HelpCenterTypeCase mHelpCenterTypeCase;

    public HelpCenterTypePresenter(HelpCenterTypeCase helpCenterTypeCase) {
        mHelpCenterTypeCase = helpCenterTypeCase;
    }

    @Override
    public void getForumListById(HelpCenterRequestType helpCenterRequestType) {
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.CATEGORY_ID, String.valueOf(getMvpView().getForumId()));
        map.putAll(getMvpView().getCustomMap());
        HelpCenterTypeCase.RequestCase requestCase = new HelpCenterTypeCase.RequestCase(helpCenterRequestType, map);
        dealData(helpCenterRequestType, requestCase);
    }

    @Override
    public void searchDocument(HelpCenterRequestType helpCenterRequestType) {
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.QUERY, getMvpView().getSearchKey());
        map.putAll(getMvpView().getCustomMap());
        HelpCenterTypeCase.RequestCase requestCase = new HelpCenterTypeCase.RequestCase(helpCenterRequestType, map);
        dealData(helpCenterRequestType, requestCase);
    }

    private void dealData(final HelpCenterRequestType type, HelpCenterTypeCase.RequestCase requestCase) {
        checkViewAttached();
        getMvpView().showLoading("");
        mHelpCenterTypeCase.setRequestValues(requestCase);
        mHelpCenterTypeCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<HelpCenterTypeCase.ResponseValue>() {
            @Override
            public void onSuccess(HelpCenterTypeCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        switch (type) {
                            case DEFAULT:
                                dealForumsList(response.result);
                                break;
                            case SEARCH:
                                IHelpCenterBaseView.HelpCenterDataBuilder.dealPostList(response.result, getMvpView());
                                break;
                        }
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
        mHelpCenterTypeCase.run();
    }

    /**
     * 处理文档分类返回值
     *
     * @param data
     */
    private void dealForumsList(String data) {
        Result<HelpCenterItemForum> result = Result.fromJson(data, HelpCenterItemForum.class);
        if (result != null) {
            int resultCode = result.getCode();
            List<HelpCenterItem> list = new ArrayList<>();
            int nextPage = 1;
            if (resultCode == RESULT_OK) {
                HelpCenterItemForum helpCenterItemObj = result.getData();
                if (helpCenterItemObj != null) {
                    if (helpCenterItemObj.getForums() != null) {
                        list.addAll(helpCenterItemObj.getForums());
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
