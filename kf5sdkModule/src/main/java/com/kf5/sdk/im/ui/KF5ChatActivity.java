package com.kf5.sdk.im.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.kf5.sdk.R;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.Agent;
import com.kf5.sdk.im.entity.AgentFailureType;
import com.kf5.sdk.im.entity.Chat;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.IMMessageBuilder;
import com.kf5.sdk.im.mvp.presenter.IMPresenter;
import com.kf5.sdk.im.utils.IMCacheUtils;
import com.kf5.sdk.im.widget.RatingDialog;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.FilePathUtils;
import com.kf5.sdk.system.utils.LogUtil;
import com.kf5.sdk.system.utils.SafeJson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/27 9:55
 * email:812219713@qq.com
 */
public class KF5ChatActivity extends BaseChatActivity {

    public static final String CARD_MESSAGE_KEY = "card_message_content";

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }


    @Override
    public void onLoadResult(String result) {

    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    /**
     * 连接成功
     */
    @Override
    public void scConnect() {
        hideLoading();
        presenter.getIMInfo();
        //设置用户自定义属性
//        try {
//            //每一个JsonObj里必须成对包含name与value，前者是对应的名称，后者是name对应的值；相当于将map的键值拆分了，注意name与value要写正确。
//            JSONArray jsonArray = new JSONArray();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("name", "这里填写用户自定义信息的名称，例如：平台");
//            jsonObject.put("value", "这里填写上面这个名称对应的值，例如：Android系统");
//            jsonArray.put(jsonObject);
//            jsonObject = new JSONObject();
//            jsonObject.put("name", "这里填写用户自定义信息的名称，例如：手机号");
//            jsonObject.put("value", "这里填写上面这个名称对应的值，例如：123456897");
//            jsonArray.put(jsonObject);
//            presenter.setMetadata(jsonArray);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 连接错误
     *
     * @param connectErrorMsg
     */
    @Override
    public void scConnectError(String connectErrorMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isAgentOnline = false;
                hideLoading();
                setTitleText(getString(R.string.kf5_not_connected));
            }
        });
    }

    /**
     * 连接超时
     *
     * @param timeOutMsg
     */
    @Override
    public void scConnectTimeout(String timeOutMsg) {
        isAgentOnline = false;
        hideLoading();
    }

    /**
     * 断开连接
     *
     * @param disConnectMsg
     */
    @Override
    public void scDisConnect(String disConnectMsg) {
    }

    /**
     * 异常信息
     *
     * @param errorMsg
     */
    @Override
    public void scError(String errorMsg) {
        isAgentOnline = false;
        hideLoading();
    }

    /**
     * 消息监听
     *
     * @param message
     */
    @Override
    public void scOnMessage(String message) {

    }

    /**
     * 重连
     *
     * @param reconnectMsg
     */
    @Override
    public void scReconnect(String reconnectMsg) {

    }

    /**
     * 尝试重连
     *
     * @param reconnectAttemptMsg
     */
    @Override
    public void scReconnectAttempt(String reconnectAttemptMsg) {

    }

    /**
     * 重连异常
     *
     * @param reconnectErrorMsg
     */
    @Override
    public void scReconnectError(String reconnectErrorMsg) {

    }

    /**
     * 重连失败
     *
     * @param reconnectFailedMsg
     */
    @Override
    public void scReconnectFailed(String reconnectFailedMsg) {
        isAgentOnline = false;
    }

    /**
     * 正在重连
     *
     * @param reconnectingMsg
     */
    @Override
    public void scReconnecting(String reconnectingMsg) {
    }

    @Override
    public void showError(int resultCode, final String msg) {
        super.showError(resultCode, msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
            }
        });

    }

    /**
     * 初始化IM指令返回值
     *
     * @param chat
     */
    @Override
    public void onChatStatus(final Chat chat) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (chat != null) {
                        String status = chat.getStatus();
                        initRobotData(chat);
                        setTitleContent(getString(R.string.kf5_allocating));
                        //由于可能重连,同时removeQueueItemView
                        removeQueueItemView();
                        presenter.synchronizationRecalledMessages();
                        presenter.synchronizationMessages();
                        //如果状态为chatting,则直接开始聊天
                        if (TextUtils.equals(Field.CHATTING, status)) {
                            isAgentOnline = true;
                            setTitleText(chat.getAgent().getDisplayName());
                            mXhsEmoticonsKeyBoard.showIMView();
                            //如果状态为queue,则开始排队，同时接受push过来处于的排队位置
                        } else if (TextUtils.equals(Field.QUEUE, status)) {
                            setTitleContent(getString(R.string.kf5_queue_waiting));
                            if (chat.isVisitor_queue_notify()) {
                                updateQueueMessage(getString(R.string.kf5_update_queue_num, (chat.getQueueIndex() + 1)));
                            } else {
                                updateQueueMessage(getString(R.string.kf5_update_queue));
                            }
                            mXhsEmoticonsKeyBoard.showIMView();
                            isAgentOnline = false;
                            //如果状态为none，则进入下一级判断
                        } else if (TextUtils.equals(Field.NONE, status)) {
                            isAgentOnline = false;
                            //如果机器人可用，则开启机器人对话
                            if (robotEnable) {
                                setTitleContent(robotName);
                                //显示机器人编辑区域并显示欢迎语
                                mXhsEmoticonsKeyBoard.showAIView();
                            } else {
                                if (!IMCacheUtils.temporaryMessageFirst(mActivity)) {
                                    aiToGetAgents();
                                    mXhsEmoticonsKeyBoard.showIMView();
                                } else {
                                    setTitleContent(getString(R.string.kf5_chat));
                                    mXhsEmoticonsKeyBoard.showTemporaryMessageView();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onReceiveMessageList(List<IMMessage> messageList) {
        List<IMMessage> targetList = new ArrayList<>();
        for (IMMessage receiveMessage : messageList) {
            int messageRecalled = receiveMessage.getRecalledStatus();
            if (messageRecalled == 1) {
                boolean isRecalled = false;
                for (IMMessage localMessage : mIMMessageList) {
                    if (localMessage.getId() == receiveMessage.getId()) {
                        isRecalled = true;
                        localMessage.setRecalledStatus(1);
                        break;
                    }
                }
                if (!isRecalled) {
                    targetList.add(receiveMessage);
                }
            } else {
                targetList.add(receiveMessage);
            }
        }
        refreshListAndNotifyData(targetList);
    }

    @Override
    public void onSendMessageResult() {
        refreshData();
    }

    /**
     * 分配客服回调接口
     *
     * @param code
     * @param message
     */
    @Override
    public void onGetAgentResult(final int code, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    setTitleContent(getString(R.string.kf5_queue_waiting));
                    JSONObject jsonObject = SafeJson.parseObj(message);
                    int index = SafeJson.safeInt(jsonObject, Field.INDEX);
                    String message;
                    if (index <= 0) {
                        message = getString(R.string.kf5_update_queue);
                    } else {
                        message = getString(R.string.kf5_update_queue_num, (index + 1));
                    }
                    updateQueueMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void onSyncMessageResult(int resultCode) {
        if (resultCode == IMPresenter.RESULT_OK) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    KF5ChatActivity.this.mListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = getIntent();
                            if (intent != null && intent.hasExtra(CARD_MESSAGE_KEY)) {
                                String cardContent = intent.getStringExtra(CARD_MESSAGE_KEY);
                                refreshListAndNotifyData(Collections.singletonList(IMMessageBuilder.buildCardMessage(cardContent)));
                                intent.removeExtra(CARD_MESSAGE_KEY);
                            }
                        }
                    }, 200);
                }
            });
        }
    }

    /**
     * 设置标题的内容
     *
     * @param text
     */
    @Override
    public void setTitleContent(String text) {
        setTitleText(text);
    }

    /**
     * 显示请求评价view
     */
    @Override
    public void onShowRatingView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRatingDialog == null) {
                    mRatingDialog = new RatingDialog(KF5ChatActivity.this, mRatingLevelCount);
                    mRatingDialog.setListener(KF5ChatActivity.this);
                }
                if (!mRatingDialog.isShow())
                    mRatingDialog.show();
            }
        });
    }

    /**
     * 排队成功
     *
     * @param agent
     */
    @Override
    public void onQueueSuccess(final Agent agent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeQueueItemView();
                if (agent != null) {
                    isAgentOnline = true;
                    //排队成功
                    setTitleContent(agent.getDisplayName());
                    mXhsEmoticonsKeyBoard.showIMView();
                    //其他操作
                } else {
                    isAgentOnline = false;
                    if (robotEnable) {
                        setTitleContent(robotName);
                        mXhsEmoticonsKeyBoard.showAIView();
                    } else {
                        setTitleContent(getString(R.string.kf5_chat));
                        mXhsEmoticonsKeyBoard.showIMView();
                    }
                }
            }
        });

    }

    /**
     * 更新排队位置
     *
     * @param message
     */
    @Override
    public void updateQueueNum(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mXhsEmoticonsKeyBoard.showIMView();
                    JSONObject jsonObject = SafeJson.parseObj(message);
                    String agentStatus = SafeJson.safeGet(jsonObject, Field.STATUS);

                    if (TextUtils.equals(Field.ONLINE, agentStatus)) {
                        boolean silent = SafeJson.safeBoolean(jsonObject, Field.SILENT);
                        if (!silent) {
                            int index = SafeJson.safeInt(jsonObject, Field.INDEX);
                            updateQueueMessage(getString(R.string.kf5_update_queue_num, (index + 1)));
                        }
                    } else {
                        isAgentOnline = false;
                        setTitleContent(getString(R.string.kf5_no_agent_online));
                        toggleNoAgentOnline(AgentFailureType.NO_AGENT_ONLINE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 更新排队的View组件状态
     */
    @Override
    public void updateQueueView(final int resultCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        });
    }

    /**
     * 取消排队
     *
     * @param resultCode
     */
    @Override
    public void cancelQueue(final int resultCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == IMPresenter.RESULT_OK) {
                    removeQueueItemView();
                    if (robotEnable) {
                        setTitleContent(robotName);
                        mXhsEmoticonsKeyBoard.showAIView();
                    } else {
                        setTitleContent(getString(R.string.kf5_chat));
                    }
                } else {
                    setTitleContent(getString(R.string.kf5_cancel_queue_failed));
                }
            }
        });
    }

    /**
     * 分配客服失败
     */
    @Override
    public void getAgentFailure(final AgentFailureType failureType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toggleNoAgentOnline(failureType);
            }
        });
    }

    private void toggleNoAgentOnline(AgentFailureType failureType) {
        removeQueueItemView();
        setTitleContent(getString(R.string.kf5_chat));
        mXhsEmoticonsKeyBoard.showIMView();
        showNoAgentOnlineReminderDialog(failureType);
    }

    @Override
    public void setRatingLevelCount(int levelCount) {
        mRatingLevelCount = levelCount;
    }

    /**
     * 回调接口 0，满意；1不满意；-1取消
     *
     * @param dialog 对话框
     * @param index  点击位置
     */
    @Override
    public void onRatingClick(RatingDialog dialog, int index) {
        dialog.dismiss();
        if (index >= 0) {
            presenter.sendRating(index);
        }
    }

    @Override
    public void onLoadRecallMessageList(List<IMMessage> messages) {
        if (messages != null && !messages.isEmpty()) {
            boolean notify = false;
            for (IMMessage remoteMessage : messages) {
                int remoteMessageId = remoteMessage.getId();
                int remoteMessageRecalledStatus = remoteMessage.getRecalledStatus();
                if (remoteMessageRecalledStatus == 1) {
                    for (IMMessage localMessage : mIMMessageList) {
                        if (localMessage.getId() == remoteMessageId && localMessage.getRecalledStatus() != remoteMessageRecalledStatus) {
                            notify = true;
                            IMSQLManager.updateMessageRecalledByMessageId(this, remoteMessageId);
                            localMessage.setRecalledStatus(1);
                            break;
                        }
                    }
                }
            }
            if (notify) {
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetInvalidated();
                    }
                }, 0);
            }
        }
    }

    /**
     * 处理接收到ShareIntent
     */
    private void handleShareIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (TextUtils.equals(Intent.ACTION_SEND, action) && !TextUtils.isEmpty(type)) {
            if (TextUtils.equals("text/plain", type)) {
                String content = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (!TextUtils.isEmpty(content)) {
                    onSendTextMessage(content);
                }
            } else if (type.startsWith("image/")) {
                Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (uri != null) {
                    String path = FilePathUtils.getPath(mActivity, uri);
                    if (!TextUtils.isEmpty(path)) {
                        //发送图片
                        onSendImageMessage(Collections.singletonList(new File(path)));
                    }
                }
            }
        }
    }

    /**
     * 移除排队消息
     */
    private void removeQueueItemView() {
        List<IMMessage> queueList = new ArrayList<>();
        for (IMMessage imMessage : mIMMessageList) {
            if (TextUtils.equals(Field.QUEUE_WAITING, imMessage.getType())) {
                queueList.add(imMessage);
            }
        }
        mIMMessageList.removeAll(queueList);
        refreshData();
        isInQueue = false;
    }

    /**
     * 更新排队位置
     *
     * @param content
     */
    private void updateQueueMessage(String content) {
        IMMessage message = null;
        for (IMMessage imMessage : mIMMessageList) {
            if (TextUtils.equals(Field.QUEUE_WAITING, imMessage.getType())) {
                message = imMessage;
                break;
            }
        }
        if (message != null) {
            message.setMessage(content);
        } else {
            Collections.addAll(mIMMessageList, IMMessageBuilder.buildSendQueueMessage(content));
        }
        refreshData();
        isInQueue = true;
    }

    /**
     * 初始化机器人属性
     *
     * @param chat
     */
    private void initRobotData(Chat chat) {
        robotEnable = inWorkTime ? chat.isRobotEnable() : canUseRobot && chat.isRobotEnable();
        robotName = chat.getRobotName();
    }
}
