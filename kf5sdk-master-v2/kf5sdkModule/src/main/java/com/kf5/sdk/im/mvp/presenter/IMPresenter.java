package com.kf5.sdk.im.mvp.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kf5.im.aidl.ConnectionCallBack;
import com.kf5.im.aidl.IPCCallBack;
import com.kf5.im.aidl.MessageManager;
import com.kf5.sdk.R;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.Agent;
import com.kf5.sdk.im.entity.Chat;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.IMMessageManager;
import com.kf5.sdk.im.entity.MessageType;
import com.kf5.sdk.im.entity.Status;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.im.mvp.usecase.IMCase;
import com.kf5.sdk.im.mvp.view.IIMView;
import com.kf5.sdk.im.service.MessageService;
import com.kf5.sdk.im.service.MessageServiceStub;
import com.kf5.sdk.im.service.params.SocketParams;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.GsonManager;
import com.kf5.sdk.system.utils.MD5Utils;
import com.kf5.sdk.system.utils.SafeJson;
import com.kf5.sdk.system.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * author:chosen
 * date:2016/10/27 18:34
 * email:812219713@qq.com
 */

public class IMPresenter extends BasePresenter<IIMView> implements IChatPresenter {

    /**
     * 初始化标识
     */
    private static final int INIT_IM = 0x01;

    /**
     * 收取Message监听
     */
    private static final int ON_MESSAGE = 0x02;

    private static final int SEND_TEXT_MESSAGE = 0x03;

    public static final int SET_METADATA = 0x04;

    public static final int RESULT_OK = 0;

    private MessageManager mMessageManager;

    private final IMCase mIMCase;

    private boolean isReconnect;

    private Map<String, Timer> mTimerMap = new ArrayMap<>();

    private static final int THIRTY_SECONDS = 30 * 1000, ONE_MINUTE = 60 * 1000;

    public IMPresenter(IMCase imCase) {
        mIMCase = imCase;
    }

    /**
     * IPC连接
     */
    @Override
    public void connectIPC() {
        Intent intent = new Intent();
        intent.setClass(getMvpView().getContext(), MessageService.class);
        getMvpView().getContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 初始化IM必要参数
     *
     * @param bundle
     */
    @Override
    public void initParams(Bundle bundle) {
        try {
            mMessageManager.initParams(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * IM连接
     */
    @Override
    public void connect() {
        try {
            mMessageManager.connect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * IM断开
     */
    @Override
    public void disconnect() {
        try {
            mMessageManager.disconnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否处于连接状态
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        try {
            return mMessageManager.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 断开IPC
     */
    @Override
    public void disconnectIPC() {
        try {
            removeAllTask();
            mMessageManager.unregisterConnectionCallBack(mCallBack);
            getMvpView().getContext().unbindService(mServiceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送IM指令
     *
     * @param params
     */
    @Override
    public void sendSCAction(final int requestCode, final String params) {
        try {
            mMessageManager.sendEventMessage(params, new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    Log.i(Utils.KF5_TAG, params + "=====" + result);
                    if (code == 0) {
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        switch (requestCode) {
                            case INIT_IM:
                                Chat chat = GsonManager.getInstance().buildChat(jsonObject.toString());
                                getMvpView().onChatStatus(chat);
                                break;
                            case ON_MESSAGE:
                                try {
                                    JSONArray jsonArray = SafeJson.safeArray(jsonObject, Field.HISTORY);
                                    if (jsonArray != null) {
                                        List<IMMessage> list = GsonManager.getInstance().getIMMessageList(jsonArray.toString());
                                        for (IMMessage imMessage : list) {
                                            String type = imMessage.getType();
                                            switch (type) {
                                                case Field.CHAT_MSG:
                                                    imMessage.setMessageType(MessageType.TEXT);
                                                    break;
                                                case Field.CHAT_UPLOAD:
                                                    Upload upload = imMessage.getUpload();
                                                    String uploadType = upload.getType();
                                                    if (Utils.isImage(uploadType))
                                                        imMessage.setMessageType(MessageType.IMAGE);
                                                    else if (Utils.isAMR(uploadType))
                                                        imMessage.setMessageType(MessageType.VOICE);
                                                    else imMessage.setMessageType(MessageType.FILE);
                                                    break;
                                                case Field.CHAT_SYSTEM:
                                                    imMessage.setMessageType(MessageType.SYSTEM);
                                                    break;
                                            }
                                            if (TextUtils.equals(Field.VISITOR, imMessage.getRole()))
                                                imMessage.setCom(false);
                                            else imMessage.setCom(true);
                                            imMessage.setStatus(Status.SUCCESS);
                                        }
                                        insertMessageToDB(list);
                                        getMvpView().onReceiveMessageList(list);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case SET_METADATA:

                                break;
                        }

                    } else {


                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取IM信息
     */
    public void getIMInfo() {
        sendSCAction(IMPresenter.INIT_IM, SocketParams.getSettingParams());
    }

    /**
     * 同步消息
     */
    public void synchronizationMessages() {
        sendSCAction(ON_MESSAGE, SocketParams.getHistoryMessagesParams(IMSQLManager.getLastMessageId(getMvpView().getContext()), 0).toString());
    }


    /**
     * 设置用户自定义属性
     *
     * @param jsonArray
     */
    public void setMetadata(JSONArray jsonArray) {
        sendSCAction(SET_METADATA, SocketParams.getMetadataParams(jsonArray));
    }

    /**
     * 拉取本地消息
     *
     * @param messageCount
     * @return
     */
    public List<IMMessage> getLastMessages(long messageCount) {
        return IMSQLManager.getPageMessages(getMvpView().getContext(), messageCount);
    }

    /**
     * 获取本地消息总数
     *
     * @return
     */
    public long getDBMessageCount() {
        return IMSQLManager.getMessageCount(getMvpView().getContext());
    }

    /**
     * 发送文本消息
     *
     * @param message
     */
    public void sendTextMessage(IMMessage message) {
        sendMessage(SocketParams.getSendMessagesParams(message.getMessage()), message, true);
    }

    /**
     * AI消息
     *
     * @param message
     */
    public void sendAIMessage(final IMMessage message) {
        try {
            insertMessageToDB(Collections.singletonList(message));
            addTimerTask(message, THIRTY_SECONDS);
            mMessageManager.sendEventMessage(SocketParams.getAIMessageParams(message.getMessage(), message.getTimeStamp()), new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    Log.i(Utils.KF5_TAG, result);
                    String timeStamp = message.getTimeStamp();
                    removeTimerTask(timeStamp);
                    if (code == 0) {
                        message.setStatus(Status.SUCCESS);
                        updateMessageByTimeStamp(message, timeStamp);
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        IMMessage aiMessage = new IMMessage();
                        aiMessage.setMessageType(MessageType.AI_MESSAGE);
                        aiMessage.setStatus(Status.SUCCESS);
                        aiMessage.setCreated(System.currentTimeMillis() / 1000);
                        aiMessage.setCom(true);
                        aiMessage.setMessage(com.kf5.sdk.im.utils.Utils.dealAIMessage(result));
                        aiMessage.setTimeStamp(SafeJson.safeGet(jsonObject, Field.TIMESTAMP));
                        aiMessage.setType(Field.CHAT_MSG);
                        getMvpView().onReceiveMessageList(Collections.singletonList(aiMessage));

                        {
                            IMMessage dbMessage = new IMMessage();
                            dbMessage.setMessageType(MessageType.TEXT);
                            dbMessage.setStatus(Status.SUCCESS);
                            dbMessage.setCreated(aiMessage.getCreated());
                            dbMessage.setCom(true);
                            dbMessage.setType(Field.CHAT_MSG);
                            dbMessage.setTimeStamp(aiMessage.getTimeStamp());
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(SafeJson.safeGet(jsonObject, Field.CONTENT));
                            JSONArray jsonArray = SafeJson.safeArray(jsonObject, Field.DOCUMENTS);
                            if (jsonArray != null) {
                                int size = jsonArray.size();
                                if (size > 0)
                                    stringBuilder.append("<br/>");
                                for (int i = 0; i < size; i++) {
                                    JSONObject itemObj = jsonArray.getJSONObject(i);
                                    stringBuilder.append(itemObj.getString(Field.TITLE_TAG));
                                    if (i != size - 1) {
                                        stringBuilder.append("<br/>");
                                    }
                                }
                                dbMessage.setMessage(stringBuilder.toString());
                                insertMessageToDB(Collections.singletonList(dbMessage));
                            }
                        }
                    } else {
                        message.setStatus(Status.FAILED);
                        updateMessageByTimeStamp(message, timeStamp);
                    }
                    getMvpView().onSendMessageResult();
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * Queue状态发送的临时消息
     *
     * @param message
     */
    public void sendQueueMessage(final IMMessage message) {
//        sendMessage(SocketParams.getQueueMessageParams(message.getMessage()), message, false);
        addTimerTask(message, THIRTY_SECONDS);
        try {
            mMessageManager.sendEventMessage(SocketParams.getQueueMessageParams(message.getMessage()), new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    removeTimerTask(message.getTimeStamp());
                    if (code == RESULT_OK) {
                        message.setStatus(Status.SUCCESS);
                    } else {
                        message.setStatus(Status.FAILED);
                    }
                    getMvpView().updateQueueView();
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消排队
     */
    public void sendCancelQueue() {
        try {
            mMessageManager.sendEventMessage(SocketParams.getCancelQueueParams(), new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    getMvpView().cancelQueue(code);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图片消息
     *
     * @param imMessage
     * @param file
     */
    public void sendImageMessage(final IMMessage imMessage, final File file) {

        insertMessageToDB(Collections.singletonList(imMessage));
        final IMCase.RequestCase requestValues = new IMCase.RequestCase(Collections.singletonList(file));
        mIMCase.executeUseCase(requestValues);
        mIMCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<IMCase.ResponseValue>() {
            @Override
            public void onSuccess(IMCase.ResponseValue response) {
                try {
                    deleteFile(Collections.singletonList(file));
                    Log.i(Utils.KF5_TAG, "上传图片的返回值" + response.result);
                    JSONObject jsonObject = JSONObject.parseObject(response.result);
                    if (jsonObject.containsKey(Field.DATA)) {
                        JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                        String token = SafeJson.safeGet(dataObj, Field.TOKEN);
                        String url = SafeJson.safeGet(dataObj, Field.CONTENT_URL);
                        Upload upload = imMessage.getUpload();
                        upload.setUrl(url);
                        dealUploadMessageResult(imMessage, token);
                    } else {
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        String message = SafeJson.safeGet(jsonObject, Field.MESSAGE);
                        resetUploadMessageStatusFailed(Collections.singletonList(imMessage), true, resultCode, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resetUploadMessageStatusFailed(Collections.singletonList(imMessage), true, RESULT_ERROR, e.getMessage());
                }
            }

            @Override
            public void onError(String msg) {
                Log.i(Utils.KF5_TAG, "上传失败" + msg);
                deleteFile(Collections.singletonList(file));
                resetUploadMessageStatusFailed(Collections.singletonList(imMessage), true, RESULT_ERROR, msg);
            }
        });
        mIMCase.run();
    }


    /**
     * 语音
     *
     * @param imMessage
     * @param voiceFile
     */
    public void sendVoiceMessage(final IMMessage imMessage, final File voiceFile) {

        insertMessageToDB(Collections.singletonList(imMessage));
        final IMCase.RequestCase requestValues = new IMCase.RequestCase(Collections.singletonList(voiceFile));
        mIMCase.executeUseCase(requestValues);
        mIMCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<IMCase.ResponseValue>() {
            @Override
            public void onSuccess(IMCase.ResponseValue response) {
                try {
                    Log.i(Utils.KF5_TAG, "上传语音的返回值" + response.result);
                    JSONObject jsonObject = JSONObject.parseObject(response.result);
                    if (jsonObject.containsKey(Field.DATA)) {
                        JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                        String token = SafeJson.safeGet(dataObj, Field.TOKEN);
                        String url = SafeJson.safeGet(dataObj, Field.URL);
                        Upload upload = imMessage.getUpload();
                        upload.setUrl(url);
                        File newFile = new File(FilePath.SAVE_RECORDER, MD5Utils.GetMD5Code(url) + ".amr");
                        //noinspection ResultOfMethodCallIgnored
                        voiceFile.renameTo(newFile);
                        //noinspection ResultOfMethodCallIgnored
                        voiceFile.delete();
                        dealUploadMessageResult(imMessage, token);
                    } else {
                        int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                        String message = SafeJson.safeGet(jsonObject, Field.MESSAGE);
                        resetUploadMessageStatusFailed(Collections.singletonList(imMessage), true, resultCode, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resetUploadMessageStatusFailed(Collections.singletonList(imMessage), true, RESULT_ERROR, e.getMessage());
                }
            }

            @Override
            public void onError(String msg) {
                Log.i(Utils.KF5_TAG, "上传失败" + msg);
                deleteFile(Collections.singletonList(voiceFile));
                resetUploadMessageStatusFailed(Collections.singletonList(imMessage), true, RESULT_ERROR, msg);
            }
        });
        mIMCase.run();
    }


    /**
     * 将附件属性发送到IM
     *
     * @param message
     * @param token
     */
    private void dealUploadMessageResult(final IMMessage message, String token) {

        if (!TextUtils.isEmpty(token)) {
            try {
                addTimerTask(message, THIRTY_SECONDS);
                mMessageManager.sendEventMessage(SocketParams.getUploadParams(token), new IPCCallBack.Stub() {
                    @Override
                    public void onResult(int code, String result) throws RemoteException {
                        Log.i(Utils.KF5_TAG, code + "==============" + result);
                        String timeStamp = message.getTimeStamp();
                        removeTimerTask(timeStamp);
                        if (code == 0) {
                            message.setStatus(Status.SUCCESS);
                            JSONObject jsonObject = SafeJson.parseObj(result);
                            JSONObject messageObj = SafeJson.safeObject(jsonObject, Field.MESSAGE);
                            message.setChatId(SafeJson.safeInt(messageObj, Field.CHAT_ID));
                            message.setId(SafeJson.safeInt(messageObj, Field.ID));
                            message.setTimeStamp(SafeJson.safeGet(messageObj, Field.TIMESTAMP));
                            message.setCreated(SafeJson.safeInt(messageObj, Field.CREATED));
                        } else {
                            message.setStatus(Status.FAILED);
                        }
                        Log.i(Utils.KF5_TAG, "时间戳" + message.getTimeStamp() + "====" + timeStamp);
                        updateMessageByTimeStamp(message, timeStamp);
                        getMvpView().onSendMessageResult();
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            message.setStatus(Status.FAILED);
            String timeStamp = message.getTimeStamp();
            updateMessageByTimeStamp(message, timeStamp);
            getMvpView().onSendMessageResult();
        }
    }


    /**
     * 消息发送失败
     *
     * @param messageList
     */
    private void resetUploadMessageStatusFailed(List<IMMessage> messageList, boolean showToast, int code, String msg) {
        for (IMMessage imMessage : messageList) {
            imMessage.setStatus(Status.FAILED);
            updateMessageByTimeStamp(imMessage, imMessage.getTimeStamp());
        }
        getMvpView().onSendMessageResult();
        if (showToast)
            getMvpView().showError(code, msg);
    }


    /**
     * 添加计时器
     *
     * @param message
     * @param seconds
     */
    private void addTimerTask(final IMMessage message, int seconds) {

        Log.i(Utils.KF5_TAG, "添加计时器");
        Timer timer = new Timer();
        mTimerMap.put(message.getTimeStamp(), timer);
        //发送时间30s；
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                message.setStatus(Status.FAILED);
                getMvpView().onSendMessageResult();
                IMSQLManager.updateMessageSendStatus(getMvpView().getContext(), Status.FAILED, message.getTimeStamp());
            }
        }, seconds);
    }


    /**
     * 移除计时器
     *
     * @param tag
     */
    private void removeTimerTask(String tag) {
        if (mTimerMap != null && mTimerMap.containsKey(tag)) {
            Log.i(Utils.KF5_TAG, "移除计时器");
            mTimerMap.get(tag).cancel();
            mTimerMap.remove(tag);
        }
    }


    /**
     * 清除所有计时器
     */
    private void removeAllTask() {
        Log.i(Utils.KF5_TAG, "移除所有计时器");
        Iterator<String> iterator = mTimerMap.keySet().iterator();
        while (iterator.hasNext()) {
            String tag = iterator.next();
            mTimerMap.get(tag).cancel();
        }
        mTimerMap.clear();
    }

    /**
     * 保存消息
     *
     * @param messageList
     */
    private void insertMessageToDB(List<IMMessage> messageList) {
        for (IMMessage imMessage : messageList) {
            IMSQLManager.insertMessage(getMvpView().getContext(), imMessage);
        }
    }

    /**
     * 更新状态
     *
     * @param message
     * @param timeStamp
     */
    private void updateMessageByTimeStamp(IMMessage message, String timeStamp) {
        IMSQLManager.updateMessageByTag(getMvpView().getContext(), message, timeStamp);
    }

    /**
     * 删除压缩的压缩的文件集合
     *
     * @param files
     */
    private void deleteFile(List<File> files) {
        for (File file : files) {
            if (file != null && file.exists())
                file.delete();
        }
    }

    /**
     * 分配客服
     *
     * @param ids   客服id数组
     * @param force 是否强制分配
     */
    public void getAgents(int[] ids, boolean force) {
        try {
            mMessageManager.sendEventMessage(SocketParams.getAgentsAssignParams(ids, force), new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    Log.i("KF5测试", "收到了客服的信息" + jsonObject.toString() + "====" + code);
                    if (code != RESULT_OK) {
                        String message = jsonObject.getString(Field.MESSAGE);
                        if (jsonObject.containsKey(Field.ERROR) && jsonObject.getInteger(Field.ERROR) == 1001) {
                            getMvpView().setTitleContent(getMvpView().getContext().getString(R.string.kf5_no_agent_online));
                        } else {
                            getMvpView().setTitleContent(message);
                        }
                        getMvpView().showIMView();
                    } else {
                        getMvpView().onGetAgentResult(code, result);
                    }
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 满意度评价
     *
     * @param rating
     */
    public void sendRating(int rating) {
        try {
            mMessageManager.sendEventMessage(SocketParams.getRatingParams(rating), new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    Log.i(Utils.KF5_TAG, result);
                    String content;
                    if (code == RESULT_OK)
                        content = getMvpView().getContext().getString(R.string.kf5_rating_successfully);
                    else
                        content = getMvpView().getContext().getString(R.string.kf5_rating_failed);
                    getMvpView().onReceiveMessageList(IMMessageManager.addIMMessageToList(IMMessageManager.buildSystemMessage(content)));
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步发送指令common接口
     *
     * @param params
     * @param message
     */
    private void sendMessage(String params, final IMMessage message, final boolean saveToDB) {
        try {
            if (saveToDB)
                insertMessageToDB(Collections.singletonList(message));
            addTimerTask(message, THIRTY_SECONDS);
            mMessageManager.sendEventMessage(params, new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    Log.i(Utils.KF5_TAG, code + "==============" + result);
                    String timeStamp = message.getTimeStamp();
                    removeTimerTask(timeStamp);
                    if (code == 0) {
                        message.setStatus(Status.SUCCESS);
                        if (saveToDB) {
                            JSONObject jsonObject = SafeJson.parseObj(result);
                            JSONObject messageObj = SafeJson.safeObject(jsonObject, Field.MESSAGE);
                            message.setChatId(SafeJson.safeInt(messageObj, Field.CHAT_ID));
                            message.setId(SafeJson.safeInt(messageObj, Field.ID));
                            message.setTimeStamp(SafeJson.safeGet(messageObj, Field.TIMESTAMP));
                            message.setCreated(SafeJson.safeInt(messageObj, Field.CREATED));
                            updateMessageByTimeStamp(message, timeStamp);
                            Log.i(Utils.KF5_TAG, "时间戳" + message.getTimeStamp() + "====" + timeStamp);
                        }
                    } else {
                        message.setStatus(Status.FAILED);
                        if (saveToDB)
                            updateMessageByTimeStamp(message, timeStamp);
                    }
                    getMvpView().onSendMessageResult();
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mMessageManager = MessageServiceStub.Stub.asInterface(iBinder);
            try {
                mMessageManager.registerConnectionCallBack(mCallBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            getMvpView().connectIPCSuccess();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMessageManager = null;
        }
    };

    private ConnectionCallBack mCallBack = new ConnectionCallBack.Stub() {
        @Override
        public void connect() throws RemoteException {
            Log.i(Utils.KF5_TAG, "连接成功");
            getMvpView().scConnect();
        }

        @Override
        public void connectError(String connectErrorMsg) throws RemoteException {
            Log.i(Utils.KF5_TAG, "连接失败" + connectErrorMsg);
            getMvpView().scConnectError(connectErrorMsg);
        }

        @Override
        public void connectTimeout(String timeOutMsg) throws RemoteException {
            Log.i(Utils.KF5_TAG, "连接超时" + timeOutMsg);
            getMvpView().scConnectTimeout(timeOutMsg);
        }

        @Override
        public void disConnect(String disConnectMsg) throws RemoteException {
            Log.i(Utils.KF5_TAG, "断开连接" + disConnectMsg);
            getMvpView().scDisConnect(disConnectMsg);
        }

        @Override
        public void error(String errorMsg) throws RemoteException {
            Log.i(Utils.KF5_TAG, "连接错误" + errorMsg);
            getMvpView().scError(errorMsg);
            getMvpView().setTitleContent(getMvpView().getContext().getString(R.string.kf5_not_connected));
        }

        @Override
        public void onMessage(String message) throws RemoteException {
            Log.i(Utils.KF5_TAG, "收到消息" + message);
            if (isReconnect)
                isReconnect = false;
            JSONObject jsonObject = JSONObject.parseObject(message);
            JSONObject valueObj = jsonObject.getJSONObject(Field.VALUE);
            String path = jsonObject.getString(Field.PATH);
            //排队位置push
            if (valueObj.containsKey(Field.QUEUE_UPDATE)) {
                JSONObject updateObj = valueObj.getJSONObject(Field.QUEUE_UPDATE);
                getMvpView().updateQueueNum(updateObj.toString());
            }
            //接收到Message消息
            if (valueObj.containsKey(Field.MESSAGES)) {
                if (valueObj.containsKey(Field.AGENT)) {
                    JSONObject agentObj = SafeJson.safeObject(valueObj, Field.AGENT);
                    isChangeAgent(agentObj);
                } else {
                    //新消息接收
                    JSONArray msgArray = SafeJson.safeArray(valueObj, Field.MESSAGES);
                    List<IMMessage> messageList = GsonManager.getInstance().getIMMessageList(msgArray.toString());
                    if (messageList != null) {
                        for (IMMessage imMessage : messageList) {
                            String type = imMessage.getType();
                            switch (type) {
                                case Field.CHAT_MSG:
                                    imMessage.setMessageType(MessageType.TEXT);
                                    imMessage.setCom(true);
                                    break;
                                case Field.CHAT_UPLOAD:
                                    Upload upload = imMessage.getUpload();
                                    String uploadType = upload.getType();
                                    imMessage.setCom(true);
                                    if (Utils.isImage(uploadType))
                                        imMessage.setMessageType(MessageType.IMAGE);
                                    else if (Utils.isAMR(uploadType))
                                        imMessage.setMessageType(MessageType.VOICE);
                                    else imMessage.setMessageType(MessageType.FILE);
                                    break;
                                case Field.CHAT_SYSTEM:
                                    imMessage.setMessageType(MessageType.SYSTEM);
                                    imMessage.setCom(true);
                                    if (valueObj.containsKey(Field.AGENT)) {
                                        isChangeAgent(SafeJson.safeObject(valueObj, Field.AGENT));
                                    }
                                    break;
                            }
                            imMessage.setStatus(Status.SUCCESS);
                            IMSQLManager.insertMessage(getMvpView().getContext(), imMessage);
                        }
                        getMvpView().onReceiveMessageList(messageList);
                    }
                }
            } else if (valueObj.containsKey(Field.AGENT)) {
                JSONObject agentObj = valueObj.getJSONObject(Field.AGENT);
                if (!isChangeAgent(agentObj))
                    getMvpView().onQueueSuccess(null);
            } else if (valueObj.containsKey(Field.RATING)) {
                //请求评价
                if (TextUtils.equals(Field.SDK_PUSH, path)) {
                    getMvpView().onShowRatingView();
                }
            }
        }

        @Override
        public void reconnect(String reconnectMsg) throws RemoteException {
            Log.i(Utils.KF5_TAG, "重连" + reconnectMsg);
            getMvpView().scReconnect(reconnectMsg);
            isReconnect = true;
        }

        @Override
        public void reconnectAttempt(String reconnectAttemptMsg) throws RemoteException {
            Log.i(Utils.KF5_TAG, "尝试重连" + reconnectAttemptMsg);
            getMvpView().scReconnectAttempt(reconnectAttemptMsg);
        }

        @Override
        public void reconnectError(String reconnectErrorMsg) throws RemoteException {
            Log.i(Utils.KF5_TAG, "连接异常" + reconnectErrorMsg);
            getMvpView().scReconnectError(reconnectErrorMsg);
        }

        @Override
        public void reconnectFailed(String reconnectFailedMsg) throws RemoteException {
            Log.i(Utils.KF5_TAG, "连接失败" + reconnectFailedMsg);
            getMvpView().scReconnectFailed(reconnectFailedMsg);
        }

        @Override
        public void reconnecting(String reconnectingMsg) throws RemoteException {
            Log.i(Utils.KF5_TAG, "正在重连" + reconnectingMsg);
            getMvpView().scReconnecting(reconnectingMsg);
        }
    };

    /**
     * 处理收到客服信息
     *
     * @param agentObj
     * @return
     */
    private boolean isChangeAgent(JSONObject agentObj) {
        Agent agent = GsonManager.getInstance().buildAgent(agentObj.toString());
        boolean hasAgent = agent != null && agent.getId() > 0;
        //会话进行时
        if (hasAgent) {
            getMvpView().onQueueSuccess(agent);
            if (agentObj.containsKey(Field.WELCOME_MSG)) {
                String welComeMsg = SafeJson.safeGet(agentObj, Field.WELCOME_MSG);
                List<IMMessage> list = new ArrayList<>();
                list.add(IMMessageManager.buildReceiveTextMessage(welComeMsg));
                getMvpView().onReceiveMessageList(list);
            }
        }
        return hasAgent;
    }


}
