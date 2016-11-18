package com.kf5.sdk.helpcenter.mvp.presenter;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kf5.sdk.helpcenter.entity.HelpCenterItem;
import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;
import com.kf5.sdk.helpcenter.mvp.usecase.HelpCenterCase;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterView;
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
 * date:2016/10/18 15:20
 * email:812219713@qq.com
 */

public class HelpCenterPresenter extends BasePresenter<IHelpCenterView> implements IHelpCenterPresenter {

    private final HelpCenterCase mHelpCenterCase;

    public HelpCenterPresenter(HelpCenterCase helpCenterCase) {
        mHelpCenterCase = helpCenterCase;
    }

    @Override
    public void getCategoriesList(HelpCenterRequestType helpCenterRequestType) {
        Map<String, String> map = new ArrayMap<>();
        map.putAll(getMvpView().getCustomMap());
        dealData(helpCenterRequestType, map, Field.CATEGORIES);
    }

    @Override
    public void searchDocument(HelpCenterRequestType helpCenterRequestType) {
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.QUERY, getMvpView().getSearchKey());
        map.putAll(getMvpView().getCustomMap());
        dealData(helpCenterRequestType, map, Field.POSTS);
    }

    private void dealData(HelpCenterRequestType helpCenterRequestType, Map<String, String> map, final String fieldKey) {
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
                        JSONObject jsonObject = SafeJson.parseObj(response.result);
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        if (resultCode == RESULT_OK) {
                            JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                            JSONArray jsonArray = SafeJson.safeArray(dataObj, fieldKey);
                            List<HelpCenterItem> list = new ArrayList<>();
                            int nextPage = SafeJson.safeInt(jsonObject, Field.NEXT_PAGE);
                            if (jsonArray != null) {
                                list.addAll(GsonManager.getInstance().getHelpCenterItemList(jsonArray.toString()));
                            }
                            getMvpView().onLoadCategoriesList(nextPage, list);
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
        mHelpCenterCase.run();
    }

}
