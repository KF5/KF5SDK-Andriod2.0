package com.kf5.sdk.ticket.mvp.presenter;

import android.support.v4.util.ArrayMap;

import com.kf5.sdk.system.entity.Result;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.ticket.entity.Requester;
import com.kf5.sdk.ticket.entity.TicketListObj;
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
        map.putAll(getMvpView().getCustomMap());
        final TicketListCase.RequestCase requestCase = new TicketListCase.RequestCase(map);
        mTicketListCase.setRequestValues(requestCase);
        mTicketListCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketListCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketListCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        Result<TicketListObj> result = Result.fromJson(response.result, TicketListObj.class);
                        if (result != null) {
                            int resultCode = result.getCode();
                            if (resultCode == RESULT_OK) {
                                List<Requester> list = new ArrayList<>();
                                int nextPage = 1;
                                TicketListObj ticketListObj = result.getData();
                                if (ticketListObj != null) {
                                    if (ticketListObj.getRequests() != null) {
                                        list.addAll(ticketListObj.getRequests());
                                    }
                                    if (ticketListObj.getNext_page() > 0) {
                                        nextPage = ticketListObj.getNext_page();
                                    }
                                }
                                getMvpView().loadResultData(nextPage, list);
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
        mTicketListCase.run();

    }
}
