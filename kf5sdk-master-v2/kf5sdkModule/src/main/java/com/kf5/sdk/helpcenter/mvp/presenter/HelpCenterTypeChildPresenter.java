package com.kf5.sdk.helpcenter.mvp.presenter;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kf5.sdk.helpcenter.entity.HelpCenterItem;
import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;
import com.kf5.sdk.helpcenter.mvp.usecase.HelpCenterTypeChildCase;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterTypeChildView;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.system.utils.GsonManager;
import com.kf5.sdk.system.utils.SafeJson;

import java.util.ArrayList;
import java.util.List;
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
        dealData(requestCase, Field.POSTS);
    }

    @Override
    public void searchDocument(HelpCenterRequestType helpCenterRequestType) {
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.QUERY, getMvpView().getSearchKey());
        map.putAll(getMvpView().getCustomMap());
        HelpCenterTypeChildCase.RequestCase requestCase = new HelpCenterTypeChildCase.RequestCase(helpCenterRequestType, map);
        dealData(requestCase, Field.POSTS);
    }

    private void dealData(HelpCenterTypeChildCase.RequestCase requestCase, final String filedKey) {

        checkViewAttached();
        getMvpView().showLoading("");
        mHelpCenterTypeChildCase.setRequestValues(requestCase);
        mHelpCenterTypeChildCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<HelpCenterTypeChildCase.ResponseValue>() {

            @Override
            public void onSuccess(HelpCenterTypeChildCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        JSONObject jsonObject = SafeJson.parseObj(response.result);
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        if (resultCode == RESULT_OK) {
                            JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                            JSONArray jsonArray = SafeJson.safeArray(dataObj, filedKey);
                            List<HelpCenterItem> list = new ArrayList<>();
                            int nextPage = SafeJson.safeInt(jsonObject, Field.NEXT_PAGE);
                            if (jsonArray != null) {
                                list.addAll(GsonManager.getInstance().getHelpCenterItemList(jsonArray.toString()));
                            }
                            getMvpView().onLoadPostList(nextPage, list);
                        } else {
                            String message = SafeJson.safeGet(jsonObject, Field.MESSAGE);
                            getMvpView().showError(resultCode, message);
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
        mHelpCenterTypeChildCase.run();
    }
}
