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
import com.kf5.sdk.ticket.entity.Requester;
import com.kf5.sdk.ticket.mvp.usecase.TicketListCase;
import com.kf5.sdk.ticket.mvp.view.ITicketListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/20 13:48
 * email:812219713@qq.com
 */

public class TicketListPresenter extends BasePresenter<ITicketListView> implements ITicketListPresenter {

    private final TicketListCase mTicketListCase;

    public TicketListPresenter(TicketListCase ticketListCase) {
        mTicketListCase = ticketListCase;
    }

    @Override
    public void getTicketList() {
        checkViewAttached();
        getMvpView().showLoading("");
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.USERTOKEN, SPUtils.getUserToken());
        map.putAll(getMvpView().getCustomMap());
        final TicketListCase.RequestCase requestCase = new TicketListCase.RequestCase(map);
        mTicketListCase.setRequestValues(requestCase);
        mTicketListCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketListCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketListCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(response.result);
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        if (resultCode == RESULT_OK) {
                            JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                            JSONArray jsonArray = SafeJson.safeArray(dataObj, Field.REQUESTS);
                            List<Requester> requestList = new ArrayList<>();
                            requestList.addAll(GsonManager.getInstance().getRequesterList(jsonArray.toString()));
                            int nextPage = SafeJson.safeInt(dataObj, Field.NEXT_PAGE);
                            getMvpView().loadResultData(nextPage, requestList);
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
        mTicketListCase.run();

    }
}
