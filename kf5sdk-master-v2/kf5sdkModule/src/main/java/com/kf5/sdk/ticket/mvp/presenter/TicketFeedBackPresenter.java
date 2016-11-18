package com.kf5.sdk.ticket.mvp.presenter;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kf5.sdk.helpcenter.entity.Attachment;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.system.utils.GsonManager;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.kf5.sdk.ticket.mvp.usecase.TicketFeedBackCase;
import com.kf5.sdk.ticket.mvp.view.ITicketFeedBackView;

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
        map.put(Field.USERTOKEN, SPUtils.getUserToken());
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
                        JSONObject jsonObject = JSONObject.parseObject(response.result);
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        if (resultCode == RESULT_OK) {
                            getMvpView().createTicketSuccess();
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
        mTicketFeedBackCase.run();
    }

    @Override
    public void uploadAttachment() {
        checkViewAttached();
        getMvpView().showLoading("");
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.USERTOKEN, SPUtils.getUserToken());
        final TicketFeedBackCase.RequestCase requestCase = new TicketFeedBackCase.RequestCase(map, getMvpView().getUploadFileList(), TicketFeedBackCase.RequestType.UPLOAD_ATTACHMENT);
        mTicketFeedBackCase.setRequestValues(requestCase);
        mTicketFeedBackCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketFeedBackCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketFeedBackCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(response.result);
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        if (resultCode == RESULT_OK) {
                            JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                            JSONArray attachmentArray = SafeJson.safeArray(dataObj, Field.ATTACHMENTS);
                            List<Attachment> list = new ArrayList<>();
                            if (attachmentArray != null)
                                list.addAll(GsonManager.getInstance().getAttachmentList(attachmentArray.toString()));
                            Map<String, String> dataMap = new ArrayMap<>();
                            JSONArray jsonArray = new JSONArray();
                            for (int i = 0; i < list.size(); i++)
                                jsonArray.add(i, list.get(i).getToken());
                            dataMap.put(Field.UPLOADS, jsonArray.toString());
                            getMvpView().loadUploadData(dataMap);
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
        mTicketFeedBackCase.run();
    }
}
