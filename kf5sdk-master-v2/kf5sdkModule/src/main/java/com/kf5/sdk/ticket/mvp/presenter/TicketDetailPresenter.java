package com.kf5.sdk.ticket.mvp.presenter;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.google.gson.stream.JsonReader;
import com.kf5.sdk.helpcenter.entity.Attachment;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.Result;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.ticket.entity.AttachmentsObj;
import com.kf5.sdk.ticket.entity.Comment;
import com.kf5.sdk.ticket.entity.MessageStatus;
import com.kf5.sdk.ticket.entity.Requester;
import com.kf5.sdk.ticket.entity.RequesterObj;
import com.kf5.sdk.ticket.entity.TicketDetailObj;
import com.kf5.sdk.ticket.mvp.usecase.TicketDetailCase;
import com.kf5.sdk.ticket.mvp.view.ITicketDetailView;

import org.json.JSONArray;

import java.io.IOException;
import java.io.Reader;
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
        map.putAll(getMvpView().getTicketDetailMap());
        final TicketDetailCase.RequestCase requestCase = new TicketDetailCase.RequestCase(map, TicketDetailCase.RequestType.GET_TICKET_DETAIL);
        mTicketDetailCase.setRequestValues(requestCase);
        mTicketDetailCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketDetailCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketDetailCase.ResponseValue response) {
                if (isViewAttached()) {
                    getMvpView().hideLoading();
                    try {
                        Result<TicketDetailObj> result = Result.fromJson(response.result, TicketDetailObj.class);
                        if (result != null) {
                            int resultCode = result.getCode();
                            if (resultCode == RESULT_OK) {
                                TicketDetailObj ticketDetailObj = result.getData();
                                List<Comment> commentList = new ArrayList<>();
                                Requester requester = null;
                                int nextPage = 1;
                                if (ticketDetailObj != null) {
                                    if (ticketDetailObj.getComments() != null) {
                                        for (Comment comment : ticketDetailObj.getComments()) {
                                            comment.setMessageStatus(MessageStatus.SUCCESS);
                                            commentList.add(comment);
                                        }
                                    }
                                    if (ticketDetailObj.getRequest() != null) {
                                        requester = ticketDetailObj.getRequest();
                                    }
                                    if (ticketDetailObj.getNext_page() > 0) {
                                        nextPage = ticketDetailObj.getNext_page();
                                    }
                                }
                                getMvpView().loadTicketDetail(nextPage, requester, commentList);
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
        final TicketDetailCase.RequestCase requestCase = new TicketDetailCase.RequestCase(map, TicketDetailCase.RequestType.UPLOAD_ATTACHMENT, getMvpView().getFileList());
        mTicketDetailCase.setRequestValues(requestCase);
        mTicketDetailCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketDetailCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketDetailCase.ResponseValue response) {
                if (isViewAttached()) {
//                    getMvpView().hideLoading();
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
                                filedMap.putAll(dataMap);
                                updateTicket(filedMap);
                            } else {
                                getMvpView().replyTicketError(result.getMessage());
                            }
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

    /**
     * 太绕，还是直接用Json解析吧
     *
     * @param response
     * @throws IOException
     */
    private void parseResponse(Reader response) throws IOException {
        JsonReader reader = new JsonReader(response);
        reader.beginObject();
        String name;
        while (reader.hasNext()) {
            name = reader.nextName();
            if (TextUtils.equals(Field.ERROR, name)) {
                reader.nextInt();
            } else if (TextUtils.equals(Field.MESSAGE, name)) {
                reader.nextString();
            } else if (TextUtils.equals(Field.DATA, name)) {
                reader.beginObject();
                while (reader.hasNext()) {
                    reader.nextName();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        String itemObjKey;
                        while (reader.hasNext()) {
                            itemObjKey = reader.nextName();
                            if (TextUtils.equals(Field.TOKEN, itemObjKey)) {
                                reader.nextString();
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();
                    }
                    reader.endArray();
                }
                reader.endObject();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        reader.close();
    }


    private void updateTicket(Map<String, String> dataMap) {
        checkViewAttached();
//        getMvpView().showLoading("");
        TicketDetailCase.RequestCase requestCase = new TicketDetailCase.RequestCase(dataMap, TicketDetailCase.RequestType.REPLY_TICKET);
        mTicketDetailCase.setRequestValues(requestCase);
        mTicketDetailCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<TicketDetailCase.ResponseValue>() {
            @Override
            public void onSuccess(TicketDetailCase.ResponseValue response) {
                if (isViewAttached()) {
//                    getMvpView().hideLoading();
                    try {
                        Result<RequesterObj> result = Result.fromJson(response.result, RequesterObj.class);
                        if (result != null) {
                            int resultCode = result.getCode();
                            if (resultCode == RESULT_OK) {
                                RequesterObj requesterObj = result.getData();
                                Requester requester = null;
                                if (requesterObj != null) {
                                    requester = requesterObj.getRequest();
                                }
                                getMvpView().replyTicketSuccess(requester);
                            } else {
                                getMvpView().replyTicketError(result.getMessage());
                            }
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
