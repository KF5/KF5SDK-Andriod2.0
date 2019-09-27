package com.kf5.sdk.ticket.mvp.presenter;

import android.support.v4.util.ArrayMap;

import com.kf5.sdk.helpcenter.entity.Attachment;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.Result;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.ticket.entity.AttachmentsObj;
import com.kf5.sdk.ticket.entity.RequesterObj;
import com.kf5.sdk.ticket.mvp.usecase.TicketFeedBackCase;
import com.kf5.sdk.ticket.mvp.view.ITicketFeedBackView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/21 16:47
 * email:812219713@qq.com
 */

public class TicketFeedBackPresenter extends BasePresenter<ITicketFeedBackView> implements ITicketFeedBackPresenter {

    private final TicketFeedBackCase mTicketFeedBackCase;

    public TicketFeedBackPresenter(TicketFeedBackCase ticketFeedBackCase) {
        mTicketFeedBackCase = ticketFeedBackCase;
    }

    @Override
    public void createTicket(Map<String, String> uploadMap) {
        checkViewAttached();
        getMvpView().showLoading("");
        Map<String, String> map = new ArrayMap<>();
        map.putAll(getMvpView().getDataMap());
        if (uploadMap != null)
            map.putAll(uploadMap);
        final TicketFeedBackCase.RequestCase requestCase = new TicketFeedBackCase.RequestCase(map, null, TicketFeedBackCase.RequestType.CREATE_TICKET);
        mTicketFeedBackCase.setRequestValues(requestCase);
        mTicketFeedBackCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketFeedBackCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketFeedBackCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        Result<RequesterObj> result = Result.fromJson(response.result, RequesterObj.class);
                        if (result != null) {
                            int resultCode = result.getCode();
                            if (resultCode == RESULT_OK) {
                                getMvpView().createTicketSuccess();
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
        mTicketFeedBackCase.run();
    }

    @Override
    public void uploadAttachment() {
        checkViewAttached();
        getMvpView().showLoading("");
        Map<String, String> map = new ArrayMap<>();
        final TicketFeedBackCase.RequestCase requestCase = new TicketFeedBackCase.RequestCase(map, getMvpView().getUploadFileList(), TicketFeedBackCase.RequestType.UPLOAD_ATTACHMENT);
        mTicketFeedBackCase.setRequestValues(requestCase);
        mTicketFeedBackCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketFeedBackCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketFeedBackCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        Result<AttachmentsObj> result = Result.fromJson(response.result, AttachmentsObj.class);
                        if (result != null) {
                            int resultCode = result.getCode();
                            if (resultCode == RESULT_OK) {
                                AttachmentsObj attachmentsObj = result.getData();
                                List<Attachment> attachments = new ArrayList<>();
                                if (attachmentsObj != null) {
                                    List<Attachment> list = attachmentsObj.getAttachments();
                                    if (list != null) {
                                        attachments.addAll(list);
                                    }
                                }
                                Map<String, String> dataMap = new ArrayMap<>();
                                JSONArray jsonArray = new JSONArray();
                                for (int i = 0; i < attachments.size(); i++) {
                                    jsonArray.put(i, attachments.get(i).getToken());
                                }
                                dataMap.put(Field.UPLOADS, jsonArray.toString());
                                getMvpView().loadUploadData(dataMap);
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
        mTicketFeedBackCase.run();
    }
}
