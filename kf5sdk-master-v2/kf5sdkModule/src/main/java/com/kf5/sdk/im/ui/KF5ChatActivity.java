package com.kf5.sdk.im.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.Agent;
import com.kf5.sdk.im.entity.Chat;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.IMMessageManager;
import com.kf5.sdk.im.entity.MessageType;
import com.kf5.sdk.im.entity.Status;
import com.kf5.sdk.im.mvp.presenter.IMPresenter;
import com.kf5.sdk.im.widget.RatingDialog;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.FilePathUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/27 9:55
 * email:812219713@qq.com
 */
public class KF5ChatActivity extends BaseChatActivity {


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
    }

    /**
     * 连接错误
     *
     * @param connectErrorMsg
     */
    @Override
    public void scConnectError(String connectErrorMsg) {
        isAgentOnline = false;
        hideLoading();
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
    public void onChatStatus(Chat chat) {

        try {
            if (chat != null) {
                String status = chat.getStatus();
                setTitleContent(getString(R.string.kf5_allocating));
                //如果状态为chatting,则直接开始聊天
                if (TextUtils.equals(Field.CHATTING, status)) {
                    isAgentOnline = true;
                    setTitleText(chat.getAgent().getDisplayName());
                    dealMessageData();
                    handleShareIntent();
                    mXhsEmoticonsKeyBoard.showIMView();
                    //如果状态为queue,则开始排队，同时接受push过来处于的排队位置
                } else if (TextUtils.equals(Field.QUEUE, status)) {
                    presenter.getAgents(null, false);
                    mXhsEmoticonsKeyBoard.showQueueView();
                    isAgentOnline = false;
                    //如果状态为none，则进入下一级判断
                } else if (TextUtils.equals(Field.NONE, status)) {
                    //如果机器人可用，则开启机器人对话
                    if (chat.isRobotEnable()) {
                        setTitleContent(chat.getRobotName());
                        //显示机器人编辑区域并显示欢迎语
                        mXhsEmoticonsKeyBoard.showAIView();
                        isAgentOnline = false;
                    } else {
                        isAgentOnline = false;
                        presenter.getAgents(null, false);
                        mXhsEmoticonsKeyBoard.showQueueView();
                        dealMessageData();
                        //显示排队输入框
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onReceiveMessageList(List<IMMessage> messageList) {
        refreshListAndNotifyData(messageList);
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
    public void onGetAgentResult(int code, String message) {
        if (code == OK) {
            JSONObject jsonObject = JSONObject.parseObject(message);
            int index;
            if (jsonObject.containsKey(Field.INDEX)) {
                index = jsonObject.getIntValue(Field.INDEX);
            } else {
                index = -1;
            }
            //处于排队中
            if (index >= 0) {
                setTitleContent(getString(R.string.kf5_queue_waiting));
                refreshListAndNotifyData(IMMessageManager.addIMMessageToList(IMMessageManager.buildSendQueueMessage(getString(R.string.kf5_update_queue_num, (index + 1)))));
            } else {
                setTitleContent(getString(R.string.kf5_no_agent_online));
                showNoAgentOnlineReminderDialog();
                isAgentOnline = false;
            }
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
                    mRatingDialog = new RatingDialog(KF5ChatActivity.this);
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
                if (agent != null) {
                    isAgentOnline = true;
                    //排队成功
                    setTitleContent(agent.getDisplayName());
                    mXhsEmoticonsKeyBoard.showQueueViewToIMView();
                    //其他操作
                } else {
                    isAgentOnline = false;
                    setTitleContent(getString(R.string.kf5_chat_ended));
                }
                removeQueueItemView();
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
                JSONObject jsonObject = JSONObject.parseObject(message);
                String agentStatus = jsonObject.getString(Field.STATUS);
                if (TextUtils.equals(Field.ONLINE, agentStatus)) {
                    int index = jsonObject.getIntValue(Field.INDEX);
                    for (IMMessage imMessage : mIMMessageList) {
                        if (imMessage.getMessageType() == MessageType.QUEUE_WAITING) {
                            imMessage.setMessage(getString(R.string.kf5_update_queue_num, (index + 1)));
                            break;
                        }
                    }
                    refreshData();
                } else {
                    isAgentOnline = false;
                    setTitleContent(getString(R.string.kf5_no_agent_online));
                    showNoAgentOnlineReminderDialog();
                }
            }
        });

    }

    /**
     * 更新排队的View组件状态
     */
    @Override
    public void updateQueueView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshData();
                mEditTextQueue.setText(R.string.kf5_agent_handle_later_hint);
                if (mEditTextQueue.isEnabled())
                    mEditTextQueue.setEnabled(false);
                if (mXhsEmoticonsKeyBoard.getQueueSendView().isEnabled())
                    mXhsEmoticonsKeyBoard.getQueueSendView().setEnabled(false);
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
                    setTitleContent(getString(R.string.kf5_cancel_queue_successfully));
                    removeQueueItemView();
                    mEditTextQueue.setText("");
                    if (mEditTextQueue.isEnabled())
                        mEditTextQueue.setEnabled(false);
                    if (mXhsEmoticonsKeyBoard.getQueueSendView().isEnabled())
                        mXhsEmoticonsKeyBoard.getQueueSendView().setEnabled(false);
                } else {
                    setTitleContent(getString(R.string.kf5_cancel_queue_failed));
                }
            }
        });
    }

    /**
     * 显示聊天组件
     */
    @Override
    public void showIMView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showNoAgentOnlineReminderDialog();
                mXhsEmoticonsKeyBoard.showQueueViewToIMView();
            }
        });
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
        switch (index) {
            case 0:
                presenter.sendRating(0);
                break;
            case 1:
                presenter.sendRating(1);
                break;
            default:
                break;
        }
    }

    /**
     * IM连接成功之后的相关操作
     */
    private void dealMessageData() {

        presenter.synchronizationMessages();
        //遍历消息集合，将之前正在发送的消息，位置不变
        for (IMMessage message : mIMMessageList) {
            if (message.getStatus() == Status.SENDING) {
                if (TextUtils.equals(Field.CHAT_MSG, message.getType())) {
                    presenter.sendTextMessage(message);
                }
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


    private void removeQueueItemView() {
        for (IMMessage imMessage : mIMMessageList) {
            if (imMessage.getMessageType() == MessageType.QUEUE_WAITING) {
                mIMMessageList.remove(imMessage);
                refreshData();
                break;
            }
        }
    }


}
