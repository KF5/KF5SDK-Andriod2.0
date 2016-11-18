package com.kf5.sdk.helpcenter.mvp.presenter;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSONObject;
import com.kf5.sdk.helpcenter.entity.Post;
import com.kf5.sdk.helpcenter.mvp.usecase.HelpCenterDetailCase;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterDetailView;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.system.utils.GsonManager;
import com.kf5.sdk.system.utils.SafeJson;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/19 16:22
 * email:812219713@qq.com
 */

public class HelpCenterDetailPresenter extends BasePresenter<IHelpCenterDetailView> implements IHelpCenterDetailPresenter {

    private HelpCenterDetailCase mHelpCenterDetailCase;

    public HelpCenterDetailPresenter(HelpCenterDetailCase helpCenterDetailCase) {
        mHelpCenterDetailCase = helpCenterDetailCase;
    }

    @Override
    public void getPostDetail() {
        checkViewAttached();
        getMvpView().showLoading("");
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.POST_ID, String.valueOf(getMvpView().getPostId()));
        HelpCenterDetailCase.RequestCase requestCase = new HelpCenterDetailCase.RequestCase(map);
        mHelpCenterDetailCase.setRequestValues(requestCase);
        mHelpCenterDetailCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<HelpCenterDetailCase.ResponseValue>() {
            @Override
            public void onSuccess(HelpCenterDetailCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        JSONObject jsonObject = SafeJson.parseObj(response.result);
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        if (resultCode == RESULT_OK) {
                            JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                            JSONObject postObj = SafeJson.safeObject(dataObj, Field.POST);
                            getMvpView().onLoadResult(GsonManager.getInstance().buildEntity(Post.class, postObj.toString()));
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
        mHelpCenterDetailCase.run();
    }
}
