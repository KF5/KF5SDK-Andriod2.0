package com.kf5.sdk.ticket.mvp.presenter;

import android.support.v4.util.ArrayMap;

import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.Result;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.ticket.entity.TicketAttributeObj;
import com.kf5.sdk.ticket.entity.UserField;
import com.kf5.sdk.ticket.mvp.usecase.TicketAttributeCase;
import com.kf5.sdk.ticket.mvp.view.ITicketAttributeView;

import java.util.ArrayList;
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
        map.put(Field.TICKET_ID, String.valueOf(getMvpView().getTicketId()));
        final TicketAttributeCase.RequestCase requestCase = new TicketAttributeCase.RequestCase(map);
        mTicketAttributeCase.setRequestValues(requestCase);
        mTicketAttributeCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketAttributeCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketAttributeCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        Result<TicketAttributeObj> result = Result.fromJson(response.result, TicketAttributeObj.class);
                        if (result != null) {
                            int resultCode = result.getCode();
                            if (resultCode == RESULT_OK) {
                                TicketAttributeObj attributeObj = result.getData();
                                List<UserField> list = new ArrayList<>();
                                if (attributeObj.getTicket_field() != null) {
                                    list.addAll(attributeObj.getTicket_field());
                                }
                                getMvpView().onLoadTicketAttribute(list);
                            } else {
                                getMvpView().showError(resultCode, result.getMessage());
                            }
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
