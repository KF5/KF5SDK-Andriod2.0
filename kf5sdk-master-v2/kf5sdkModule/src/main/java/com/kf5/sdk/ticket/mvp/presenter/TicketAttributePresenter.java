package com.kf5.sdk.ticket.mvp.presenter;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.system.utils.GsonManager;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.kf5.sdk.ticket.entity.UserField;
import com.kf5.sdk.ticket.mvp.usecase.TicketAttributeCase;
import com.kf5.sdk.ticket.mvp.view.ITicketAttributeView;

import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/21 16:00
 * email:812219713@qq.com
 */

public class TicketAttributePresenter extends BasePresenter<ITicketAttributeView> implements ITicketAttributePresenter {

    private final TicketAttributeCase mTicketAttributeCase;

    public TicketAttributePresenter(TicketAttributeCase ticketAttributeCase) {
        mTicketAttributeCase = ticketAttributeCase;
    }

    @Override
    public void getTicketAttribute() {
        checkViewAttached();
        getMvpView().showLoading("");
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.USERTOKEN, SPUtils.getUserToken());
        map.put(Field.TICKET_ID, String.valueOf(getMvpView().getTicketId()));
        final TicketAttributeCase.RequestCase requestCase = new TicketAttributeCase.RequestCase(map);
        mTicketAttributeCase.setRequestValues(requestCase);
        mTicketAttributeCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketAttributeCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketAttributeCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(response.result);
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        if (resultCode == RESULT_OK) {
                            JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                            JSONArray fieldArray = SafeJson.safeArray(dataObj, Field.TICKET_FIELD);
                            List<UserField> list = GsonManager.getInstance().getUserFieldList(fieldArray.toString());
                            getMvpView().onLoadTicketAttribute(list);
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
        mTicketAttributeCase.run();
    }
}
