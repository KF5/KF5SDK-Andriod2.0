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

import com.chosen.imageviewer.tool.file.FileUtil;
import com.kf5.im.aidl.ConnectionCallBack;
import com.kf5.im.aidl.IPCCallBack;
import com.kf5.im.aidl.MessageManager;
import com.kf5.sdk.R;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.Agent;
import com.kf5.sdk.im.entity.AgentFailureType;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.IMMessageBuilder;
import com.kf5.sdk.im.entity.Status;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.im.mvp.usecase.IMCase;
import com.kf5.sdk.im.mvp.view.IIMView;
import com.kf5.sdk.im.service.MessageService;
import com.kf5.sdk.im.service.MessageServiceStub;
import com.kf5.sdk.im.service.params.SocketParams;
import com.kf5.sdk.im.ui.BaseChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.BasePresenter;
import com.kf5.sdk.system.mvp.usecase.BaseUseCase;
import com.kf5.sdk.system.utils.GsonManager;
import com.kf5.sdk.system.utils.LogUtil;
import com.kf5.sdk.system.utils.SafeJson;
import com.kf5.sdk.system.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
//            cancelAgentTimerTask();
            mMessageManager.unregisterConnectionCallBack(mCallBack);
            getMvpView().getContext().unbindService(mServiceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取IM信息
     */
    public void getIMInfo(boolean robotEnable) {
        try {
            final String params = SocketParams.getSettingParams(robotEnable);
            LogUtil.printf("初始化个人信息参数" + params);
            mMessageManager.sendEventMessage(params, new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    LogUtil.printf("初始化个人信息状态值" + code + "=====返回值====" + result);
                    getMvpView().onChatStatus(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步消息
     */
    public void synchronizationMessages() {
        try {
            final String params = SocketParams.getHistoryMessagesParams(IMSQLManager.getLastMessageId(getMvpView().getContext()), 0);
            LogUtil.printf("同步消息参数" + params);
            mMessageManager.sendEventMessage(params, new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    LogUtil.printf("同步消息状态值" + code + "=====返回值====" + result);
                    if (code == RESULT_OK) {
                        JSONObject jsonObject = SafeJson.parseObj(result);
                        JSONArray jsonArray = SafeJson.safeArray(jsonObject, Field.MESSAGES);
                        if (jsonArray != null)
                            onMessageReceive(jsonArray);
                    }
                    getMvpView().onSyncMessageResult(code);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取撤回消息列表
     */
    public void synchronizationRecalledMessages() {
        try {
            final String params = SocketParams.getRecallMessageListString();
            LogUtil.printf("获取撤回消息列表参数" + params);
            mMessageManager.sendEventMessage(params, new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    LogUtil.printf("获取撤回消息列表返回值" + code + "=====返回值====" + result);
                    if (code == RESULT_OK) {
                        JSONObject jsonObject = SafeJson.parseObj(result);
                        JSONArray jsonArray = SafeJson.safeArray(jsonObject, Field.MESSAGES);
                        if (jsonArray != null) {
                            List<IMMessage> list = GsonManager.getInstance().getIMMessageList(jsonArray.toString());
                            getMvpView().onLoadRecallMessageList(list);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置用户自定义属性
     *
     * @param jsonArray
     */
    public void setMetadata(JSONArray jsonArray) {
        try {
            final String params = SocketParams.getMetadataParams(jsonArray);
            mMessageManager.sendEventMessage(params, new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    LogUtil.printf("设置用户自定义内容的参数" + params + "状态值" + code + "返回值" + result);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
    public void sendTextMessage(final IMMessage message) {
        try {
            insertMessageToDB(Collections.singletonList(message));
            addTimerTask(message, THIRTY_SECONDS);
            final String params = SocketParams.getSendMessagesParams(message.getMessage(), message.getTimeStamp());
            LogUtil.printf("原版发送文本消息参数" + params);
            mMessageManager.sendEventMessage(params, new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    LogUtil.printf("原版发送文本消息状态码" + code + "返回值" + result);
                    dealMessageResult(message, code, result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * AI消息
     *
     * @param message
     */
    public void sendAIMessage(final IMMessage message, final JSONArray category_ids, final JSONArray forum_ids) {
        try {
            insertMessageToDB(Collections.singletonList(message));
            addTimerTask(message, THIRTY_SECONDS);
            final String params = SocketParams.getAIMessageParams(message.getMessage(), message.getTimeStamp(), category_ids, forum_ids);
            LogUtil.printf("发送机器人消息" + params);
            mMessageManager.sendEventMessage(params, new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    LogUtil.printf("机器人消息返回值" + params + "状态码" + code + "返回值" + result);
                    dealMessageResult(message, code, result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送分词消息
     *
     * @param message
     * @param id
     * @param isCategory 是否是推荐类型的消息
     */
    public void sendAIAnswerMessage(final IMMessage message, int id, boolean isCategory) {
        try {
            insertMessageToDB(Collections.singletonList(message));
            addTimerTask(message, THIRTY_SECONDS);
            final String params = SocketParams.getAIAnswerParams(id, message.getTimeStamp(), isCategory);
            LogUtil.printf("老版机器人分词消息参数" + params);
            mMessageManager.sendEventMessage(params, new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    LogUtil.printf("老版机器人分词消息状态码" + code + "返回值" + result);
                    dealMessageResult(message, code, result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Queue状态发送的临时消息
     *
     * @param message
     */
    public void sendQueueMessage(final IMMessage message, final boolean temporaryMessageFirst) {
        try {
            addTimerTask(message, THIRTY_SECONDS);
            insertMessageToDB(Collections.singletonList(message));
            mMessageManager.sendEventMessage(SocketParams.getQueueMessageParams(message.getMessage(), message.getTimeStamp()), new IPCCallBack.Stub() {
                @Override
                public void onResult(int code, String result) throws RemoteException {
                    LogUtil.printf("发送临时消息状态码" + code + "返回结果" + result);
                    dealMessageResult(message, code, result);
                    if (temporaryMessageFirst && code == RESULT_OK) {
                        if (getMvpView().getContext() instanceof BaseChatActivity) {
                            BaseChatActivity chatActivity = (BaseChatActivity) getMvpView().getContext();
                            chatActivity.aiToGetAgents();
                        }
                    }
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
        sendUploadMessage(imMessage, file, true);
    }


    /**
     * 语音
     *
     * @param imMessage
     * @param voiceFile
     */
    public void sendVoiceMessage(final IMMessage imMessage, final File voiceFile) {
        sendUploadMessage(imMessage, voiceFile, false);
    }


    /**
     * 发送视频
     *
     * @param imMessage
     * @param videoFile
     */
    public void sendVideoMessage(final IMMessage imMessage, final File videoFile) {
        sendUploadMessage(imMessage, videoFile, false);
    }

    /**
     * 附件消息
     *
     * @param imMessage
     * @param file
     */
    private void sendUploadMessage(final IMMessage imMessage, final File file, final boolean delete) {
        insertMessageToDB(Collections.singletonList(imMessage));
        String token = imMessage.getMessage();
        //如果令牌已经缓存，直接发送令牌与远程文件绑定
        if (!TextUtils.isEmpty(token)) {
            dealUploadMessageResult(imMessage, token);
        } else {
            //否则先上传附件，在绑定文件
            final IMCase.RequestCase requestValues = new IMCase.RequestCase(Collections.singletonList(file));
            mIMCase.executeUseCase(requestValues);
            mIMCase.setUseCaseCallBack(new BaseUseCase.UseCaseCallBack<IMCase.ResponseValue>() {
                @Override
                public void onSuccess(IMCase.ResponseValue response) {
                    try {
                        if (delete) {
                            FileUtil.deleteFile(file);
                        }
                        LogUtil.printf("上传附件的返回值" + response.result);
                        JSONObject jsonObject = SafeJson.parseObj(response.result);
                        if (jsonObject != null) {
                            if (jsonObject.has(Field.DATA)) {
                                JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                                String token = SafeJson.safeGet(dataObj, Field.TOKEN);
                                String url = SafeJson.safeGet(dataObj, Field.URL);
                                Upload upload = imMessage.getUpload();
                                upload.setUrl(url);
//                            File newFile = new File(FilePath.SAVE_RECORDER, MD5Utils.GetMD5Code(url) + ".amr");
//                            //noinspection ResultOfMethodCallIgnored
//                            voiceFile.renameTo(newFile);
//                            //noinspection ResultOfMethodCallIgnored
//                            voiceFile.delete();
                                IMSQLManager.updateRemoteUrlByTimestamp(getMvpView().getContext(), url, token, imMessage.getTimeStamp());
                                dealUploadMessageResult(imMessage, token);
                            } else {
                                int resultCode = SafeJson.safeInt(jsonObject, Field.ERROR);
                                String message = SafeJson.safeGet(jsonObject, Field.MESSAGE);
                                resetUploadMessageStatusFailed(Collections.singletonList(imMessage), true, resultCode, message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (delete) {
                            FileUtil.deleteFile(file);
                        }
                        resetUploadMessageStatusFailed(Collections.singletonList(imMessage), true, RESULT_ERROR, e.getMessage());
                    }
                }

                @Override
                public void onError(String msg) {
                    LogUtil.printf("上传失败" + msg);
//                deleteFile(Collections.singletonList(voiceFile));
                    if (delete) {
                        FileUtil.deleteFile(file);
                    }
                    resetUploadMessageStatusFailed(Collections.singletonList(imMessage), true, RESULT_ERROR, msg);
                }
            });
            mIMCase.run();
        }
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
                final String params = SocketParams.getUploadParams(token, message.getTimeStamp());
                LogUtil.printf("老版发送附件参数" + params);
                mMessageManager.sendEventMessage(params, new IPCCallBack.Stub() {
                    @Override
                    public void onResult(int code, String result) throws RemoteException {
                        LogUtil.printf("旧版发送附件消息状态码" + code + "返回值" + result);
                        dealMessageResult(message, code, result);
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            resetUploadMessageStatusFailed(Collections.singletonList(message), false, RESULT_ERROR, "");
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

        LogUtil.printf("添加计时器");
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
            LogUtil.printf("移除计时器");
            mTimerMap.get(tag).cancel();
            mTimerMap.remove(tag);
        }
    }


    /**
     * 清除所有计时器
     */
    private void removeAllTask() {
        LogUtil.printf("移除所有计时器");
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
    private List<IMMessage> insertMessageToDB(List<IMMessage> messageList) {
        List<IMMessage> list = new ArrayList<>();
        for (IMMessage imMessage : messageList) {
            IMMessage newMessage = IMSQLManager.insertMessage(getMvpView().getContext(), imMessage);
            if (newMessage != null) {
                list.add(newMessage);
            }
        }
        return list;
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
     * @param agentIdArray 客服id数组
     * @param force        是否强制分配
     */
    public void getAgents(String agentIdArray, int force) {
        try {

            mMessageManager.sendEventMessage(SocketParams.getAgentsAssignParams(agentIdArray, force), new IPCCallBack.Stub() {
                @Override
                public void onResult(final int code, final String result) throws RemoteException {
                    try {
                        JSONObject jsonObject = SafeJson.parseObj(result);
                        LogUtil.printf("收到了客服的信息" + jsonObject.toString() + "====" + code);
                        if (code != RESULT_OK) {
                            String message = jsonObject.getString(Field.MESSAGE);
                            if (jsonObject.has(Field.ERROR) && jsonObject.getInt(Field.ERROR) == 1001) {
                                getMvpView().setTitleContentBySocket(getMvpView().getContext().getString(R.string.kf5_no_agent_online));
                            } else {
                                getMvpView().setTitleContentBySocket(message);
                            }
                            getMvpView().getAgentFailure(AgentFailureType.NO_AGENT_ONLINE);
                        } else {
                            getMvpView().onGetAgentResult(code, result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 触发器分配客服
     *
     * @param keyId 触发器id
     */
    public void getAgents(int keyId) {
        try {
            mMessageManager.sendEventMessage(SocketParams.getAgentsAssignParams(keyId), new IPCCallBack.Stub() {
                @Override
                public void onResult(final int code, final String result) throws RemoteException {
                    try {
                        JSONObject jsonObject = SafeJson.parseObj(result);
                        LogUtil.printf("触发器分配客服收到了客服的信息" + jsonObject.toString() + "====" + code);
                        if (code != RESULT_OK) {
                            String message = jsonObject.getString(Field.MESSAGE);
                            if (jsonObject.has(Field.ERROR) && jsonObject.getInt(Field.ERROR) == 1001) {
                                getMvpView().setTitleContentBySocket(getMvpView().getContext().getString(R.string.kf5_no_agent_online));
                            } else {
                                getMvpView().setTitleContentBySocket(message);
                            }
                            getMvpView().getAgentFailure(AgentFailureType.NO_AGENT_ONLINE);
                        } else {
                            getMvpView().onGetAgentResult(code, result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                    getMvpView().onReceiveMessageList(IMMessageBuilder.addIMMessageToList(IMMessageBuilder.buildSystemMessage(content)));
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
            LogUtil.printf("连接成功");
            getMvpView().scConnect();
        }

        @Override
        public void connectError(String connectErrorMsg) throws RemoteException {
            LogUtil.printf("连接失败" + connectErrorMsg);
            getMvpView().scConnectError(connectErrorMsg);
        }

        @Override
        public void connectTimeout(String timeOutMsg) throws RemoteException {
            LogUtil.printf("连接超时" + timeOutMsg);
            getMvpView().scConnectTimeout(timeOutMsg);
        }

        @Override
        public void disConnect(String disConnectMsg) throws RemoteException {
            LogUtil.printf("断开连接" + disConnectMsg);
            getMvpView().scDisConnect(disConnectMsg);
        }

        @Override
        public void error(String errorMsg) throws RemoteException {
            LogUtil.printf("连接错误" + errorMsg);
            getMvpView().scError(errorMsg);
            getMvpView().setTitleContentBySocket(getMvpView().getContext().getString(R.string.kf5_not_connected));
        }

        @Override
        public void onMessage(String message) throws RemoteException {
            LogUtil.printf("收到消息" + message);
            if (isReconnect)
                isReconnect = false;
            try {
                JSONObject jsonObject = SafeJson.parseObj(message);
                dealForceAgentAssignFailure(jsonObject);
                JSONObject valueObj = SafeJson.safeObject(jsonObject, Field.VALUE);
                String path = jsonObject.getString(Field.PATH);
                //排队位置push
                if (valueObj.has(Field.QUEUE_UPDATE)) {
                    JSONObject updateObj = valueObj.getJSONObject(Field.QUEUE_UPDATE);
                    getMvpView().updateQueueNum(updateObj.toString());
                }
                //接收到Message消息
                if (valueObj.has(Field.MESSAGES)) {
                    //新消息接收
                    JSONArray msgArray = SafeJson.safeArray(valueObj, Field.MESSAGES);
                    onMessageReceive(msgArray);
                    if (valueObj.has(Field.AGENT)) {
                        changeAgent(SafeJson.safeObject(valueObj, Field.AGENT));
                    }
                } else if (valueObj.has(Field.AGENT)) {
                    changeAgent(valueObj.getJSONObject(Field.AGENT));
                } else if (valueObj.has(Field.RATING)) {
                    //请求评价
                    if (TextUtils.equals(Field.SDK_PUSH, path)) {
                        getMvpView().onShowRatingView();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void reconnect(String reconnectMsg) throws RemoteException {
            LogUtil.printf("重连" + reconnectMsg);
            getMvpView().scReconnect(reconnectMsg);
            isReconnect = true;
        }

        @Override
        public void reconnectAttempt(String reconnectAttemptMsg) throws RemoteException {
            LogUtil.printf("尝试重连" + reconnectAttemptMsg);
            getMvpView().scReconnectAttempt(reconnectAttemptMsg);
        }

        @Override
        public void reconnectError(String reconnectErrorMsg) throws RemoteException {
            LogUtil.printf("连接异常" + reconnectErrorMsg);
            getMvpView().scReconnectError(reconnectErrorMsg);
        }

        @Override
        public void reconnectFailed(String reconnectFailedMsg) throws RemoteException {
            LogUtil.printf("连接失败" + reconnectFailedMsg);
            getMvpView().scReconnectFailed(reconnectFailedMsg);
        }

        @Override
        public void reconnecting(String reconnectingMsg) throws RemoteException {
            LogUtil.printf("正在重连" + reconnectingMsg);
            getMvpView().scReconnecting(reconnectingMsg);
        }
    };


    /**
     * 处理收到客服信息
     *
     * @param agentObj
     * @return
     */
    private void changeAgent(JSONObject agentObj) {
        Agent agent = GsonManager.getInstance().buildAgent(agentObj.toString());
        if (agent.getId() > 0)
            IMSQLManager.insertAgentInfo(getMvpView().getContext(), agent);
        //会话进行时
        getMvpView().onQueueSuccess(agent);
        if (agentObj.has(Field.WELCOME_MSG)) {
            String welComeMsg = SafeJson.safeGet(agentObj, Field.WELCOME_MSG);
            if (!TextUtils.isEmpty(welComeMsg)) {
                getMvpView().onReceiveMessageList(Collections.singletonList(IMMessageBuilder.buildReceiveTextMessage(welComeMsg)));
            }
        }
    }

    /**
     * 强制分配失败更新UI
     */
    private void dealForceAgentAssignFailure(JSONObject jsonObject) throws JSONException {
        if (jsonObject != null && jsonObject.has(Field.EVENT)) {
            String event = jsonObject.getString(Field.EVENT);
            String error = jsonObject.has("error") ? jsonObject.getString("error") : "";
            if (TextUtils.equals(Field.AGENT_ASSIGN_FAIL, event)) {
                if (TextUtils.equals("no setting", error) || TextUtils.equals("create chat failed", error)) {
                    getMvpView().setTitleContentBySocket(getMvpView().getContext().getString(R.string.kf5_queue_error));
                    getMvpView().getAgentFailure(AgentFailureType.WAITING_IN_QUEUE_FAILURE);
                } else if (jsonObject.has("no assignable agent")) {
                    getMvpView().setTitleContentBySocket(getMvpView().getContext().getString(R.string.kf5_no_agent_online));
                    getMvpView().getAgentFailure(AgentFailureType.NO_AGENT_ONLINE);
                }
            } else if (TextUtils.equals(Field.VISITOR_QUEUE_FAIL, event)) {
                if (TextUtils.equals("toolong", error)) {
                    getMvpView().setTitleContentBySocket(getMvpView().getContext().getString(R.string.kf5_chat));
                    getMvpView().getAgentFailure(AgentFailureType.QUEUE_TOO_LONG);
                } else if (TextUtils.equals("offline", error)) {
                    getMvpView().setTitleContentBySocket(getMvpView().getContext().getString(R.string.kf5_no_agent_online));
                    getMvpView().getAgentFailure(AgentFailureType.NO_AGENT_ONLINE);
                }
            } else if (TextUtils.equals(Field.VISITOR_NOTIFY, event)) {
                if (jsonObject.has("value")) {
                    JSONObject valueObj = jsonObject.getJSONObject("value");
                    if (valueObj.has("data")) {
                        JSONObject dataObj = valueObj.getJSONObject("data");
                        if (dataObj.has("notify")) {
                            String notifyData = dataObj.getString("notify");
                            if (!TextUtils.isEmpty(notifyData)) {
                                getMvpView().onReceiveMessageList(Collections.singletonList(IMMessageBuilder.buildSystemMessage(notifyData)));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理消息返回值
     *
     * @param imMessage
     * @param code
     * @param result
     */
    private void dealMessageResult(IMMessage imMessage, int code, String result) {

        try {
            String timeStamp = imMessage.getTimeStamp();
            removeTimerTask(timeStamp);
            if (code == 0) {
                JSONObject jsonObject = SafeJson.parseObj(result);
                JSONArray messageArray = SafeJson.safeArray(jsonObject, Field.MESSAGES);
                //ack现在发送机器人消息的返回值直接返回一个数组，第一个为发送的消息对象，第二个为接收的消息对象。
                if (messageArray != null) {
                    JSONObject messageSendObj = messageArray.getJSONObject(0);
                    imMessage.setStatus(Status.SUCCESS);
                    imMessage.setMessageId(SafeJson.safeInt(messageSendObj, Field.ID));
                    imMessage.setCreated(SafeJson.safeLong(messageSendObj, Field.CREATED));
                    imMessage.setChatId(SafeJson.safeInt(messageSendObj, Field.CHAT_ID));
                    imMessage.setUserId(SafeJson.safeInt(messageSendObj, Field.USER_ID));
                    imMessage.setName(SafeJson.safeGet(messageSendObj, Field.NAME));
                    imMessage.setUserId(SafeJson.safeInt(messageSendObj, Field.USER_ID));
                    Upload upload = imMessage.getUpload();
                    final String uploadKey = "upload";
                    if (upload != null && SafeJson.isContainKey(messageSendObj, uploadKey)) {
                        JSONObject uploadObj = SafeJson.safeObject(messageSendObj, uploadKey);
                        upload.setUrl(SafeJson.safeGet(uploadObj, Field.URL));
                        imMessage.setMessage(SafeJson.safeGet(uploadObj, Field.TOKEN));
                        LogUtil.printf("远程附件地址" + upload.getUrl());
                    }
                    updateMessageByTimeStamp(imMessage, timeStamp);
                    if (messageArray.length() == 2) {
                        JSONObject receiveObj = messageArray.getJSONObject(1);
                        JSONArray receiveArray = new JSONArray();
                        receiveArray.put(receiveObj);
                        onMessageReceive(receiveArray);
                    }
                } else {
                    imMessage.setStatus(Status.FAILED);
                    updateMessageByTimeStamp(imMessage, timeStamp);
                }
            } else {
                imMessage.setStatus(Status.FAILED);
                updateMessageByTimeStamp(imMessage, timeStamp);
            }
            getMvpView().onSendMessageResult();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onMessageReceive(JSONArray messageArray) {
        List<IMMessage> messageList = GsonManager.getInstance().getIMMessageList(messageArray.toString());
        if (messageList != null && messageList.size() > 0) {
            for (IMMessage imMessage : messageList) {
                Upload upload = imMessage.getUpload();
                if (upload != null && TextUtils.isEmpty(upload.getType())) {
                    String url = upload.getUrl();
                    String suffix = url.substring(url.lastIndexOf('.') + 1, url.length());
                    if (Utils.isImage(suffix) || Utils.isMP4(suffix) || Utils.isAMR(suffix)) {
                        upload.setType(suffix.toLowerCase());
                    }
                }
                imMessage.setStatus(Status.SUCCESS);
            }
            getMvpView().onReceiveMessageList(insertMessageToDB(messageList));
        }
    }
}
