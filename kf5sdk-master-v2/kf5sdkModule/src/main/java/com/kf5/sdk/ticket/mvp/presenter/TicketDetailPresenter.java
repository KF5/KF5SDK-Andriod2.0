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
import com.kf5.sdk.ticket.entity.Comment;
import com.kf5.sdk.ticket.entity.MessageStatus;
import com.kf5.sdk.ticket.entity.Requester;
import com.kf5.sdk.ticket.mvp.usecase.TicketDetailCase;
import com.kf5.sdk.ticket.mvp.view.ITicketDetailView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/20 15:39
 * email:812219713@qq.com
 */

public class TicketDetailPresenter extends BasePresenter<ITicketDetailView> implements ITicketDetailPresenter {

    private final TicketDetailCase mTicketDetailCase;

    public TicketDetailPresenter(TicketDetailCase ticketDetailCase) {
        mTicketDetailCase = ticketDetailCase;
    }

    @Override
    public void getTicketDetail() {
        checkViewAttached();
        getMvpView().showLoading("");
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.TICKET_ID, String.valueOf(getMvpView().getTicketId()));
        map.put(Field.USERTOKEN, SPUtils.getUserToken());
        map.putAll(getMvpView().getTicketDetailMap());
        TicketDetailCase.RequestCase requestCase = new TicketDetailCase.RequestCase(map, TicketDetailCase.RequestType.GET_TICKET_DETAIL);
        mTicketDetailCase.setRequestValues(requestCase);
        mTicketDetailCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketDetailCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketDetailCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(response.result);
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        if (resultCode == RESULT_OK) {
                            JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                            JSONObject requestObj = SafeJson.safeObject(dataObj, Field.REQUEST);
                            Requester requester = GsonManager.getInstance().buildEntity(Requester.class, requestObj.toString());
                            JSONArray commentArray = SafeJson.safeArray(dataObj, Field.COMMENTS);
                            List<Comment> list = new ArrayList<>();
                            if (commentArray != null) {
                                int size = commentArray.size();
                                for (int i = 0; i < size; i++) {
                                    JSONObject itemObj = commentArray.getJSONObject(i);
                                    Comment comment = GsonManager.getInstance().buildComment(itemObj.toString());
                                    comment.setMessageStatus(MessageStatus.SUCCESS);
                                    list.add(comment);
                                }
                            }
                            int nextPage = SafeJson.safeInt(dataObj, Field.NEXT_PAGE);
                            getMvpView().loadTicketDetail(nextPage, requester, list);
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
        mTicketDetailCase.run();
    }

    @Override
    public void replayTicket(Map<String, String> dataMap) {
        if (getMvpView().getFileList().size() > 0) {
            uploadAttachment(dataMap);
        } else {
            updateTicket(dataMap);
        }
    }


    private void uploadAttachment(final Map<String, String> filedMap) {
        checkViewAttached();
//        getMvpView().showLoading("");
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.USERTOKEN, SPUtils.getUserToken());
        final TicketDetailCase.RequestCase requestCase = new TicketDetailCase.RequestCase(map, TicketDetailCase.RequestType.UPLOAD_ATTACHMENT, getMvpView().getFileList());
        mTicketDetailCase.setRequestValues(requestCase);
        mTicketDetailCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketDetailCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketDetailCase.ResponseValue response) {
                if (isViewAttached()) {
//                    getMvpView().hideLoading();
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
                            filedMap.putAll(dataMap);
                            updateTicket(filedMap);
//                            getMvpView().loadUploadAttachmentData(dataMap);
                        } else {
                            String message = SafeJson.safeGet(jsonObject, Field.MESSAGE);
                            getMvpView().replyTicketError(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getMvpView().replyTicketError(e.getMessage());
                    }
                }
            }

            @Override
            public void onError(String msg) {
                if (isViewAttached()) {
//                    getMvpView().hideLoading();
                    getMvpView().replyTicketError(msg);
                }
            }
        });
        mTicketDetailCase.run();
    }


    private void updateTicket(Map<String, String> dataMap) {
        checkViewAttached();
//        getMvpView().showLoading("");
        dataMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        TicketDetailCase.RequestCase requestCase = new TicketDetailCase.RequestCase(dataMap, TicketDetailCase.RequestType.REPLY_TICKET);
        mTicketDetailCase.setRequestValues(requestCase);
        mTicketDetailCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketDetailCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketDetailCase.ResponseValue response) {
                if (isViewAttached()) {
//                    getMvpView().hideLoading();
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(response.result);
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        if (resultCode == RESULT_OK) {
                            JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                            JSONObject requestObj = SafeJson.safeObject(dataObj, Field.REQUEST);
                            getMvpView().replyTicketSuccess(GsonManager.getInstance().buildRequester(requestObj.toString()));
                        } else {
                            String message = SafeJson.safeGet(jsonObject, Field.MESSAGE);
                            getMvpView().replyTicketError(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getMvpView().replyTicketError(e.getMessage());
                    }
                }
            }

            @Override
            public void onError(String msg) {
                if (isViewAttached()) {
//                    getMvpView().hideLoading();
                    getMvpView().replyTicketError(msg);
                }
            }
        });
        mTicketDetailCase.run();
    }

}
