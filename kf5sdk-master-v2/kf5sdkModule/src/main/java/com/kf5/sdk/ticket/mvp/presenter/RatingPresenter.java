package com.kf5.sdk.ticket.mvp.presenter;

import com.alibaba.fastjson.JSONObject;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.kf5.sdk.ticket.mvp.usecase.RatingCase;
import com.kf5.sdk.ticket.mvp.view.IRatingView;

import java.util.Map;

/**
 * author:chosen
 * date:2017/1/4 18:29
 * email:812219713@qq.com
 */

public class RatingPresenter extends BasePresenter<IRatingView> implements IRatingPresenter {

    private final RatingCase mRatingCase;

    public RatingPresenter(RatingCase ratingCase) {
        mRatingCase = ratingCase;
    }

    @Override
    public void rating(Map<String, String> dataMap) {
        checkViewAttached();
        getMvpView().showLoading("");
        dataMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        final RatingCase.RequestCase requestCase = new RatingCase.RequestCase(dataMap);
        mRatingCase.setRequestValues(requestCase);
        mRatingCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<RatingCase.ResponseValue>() {
            @Override
            public void onSuccess(RatingCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(response.result);
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        String message = SafeJson.safeGet(jsonObject, Field.MESSAGE);
                        getMvpView().onLoadRatingData(resultCode, message);
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
        mRatingCase.run();
    }
}
