package com.kf5.sdk.im.mvp.view;

import android.content.Context;

import com.kf5.sdk.im.entity.Agent;
import com.kf5.sdk.im.entity.AgentFailureType;
import com.kf5.sdk.im.entity.Chat;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.system.mvp.view.MvpView;

import java.util.List;

/**
 * author:chosen
 * date:2016/10/27 18:23
 * email:812219713@qq.com
 */

public interface IIMView extends MvpView {

    void onLoadResult(String result);

    Context getContext();

    void connectIPCSuccess();

    /**
     * 连接成功
     */
    void scConnect();

    /**
     * 连接错误
     *
     * @param connectErrorMsg
     */
    void scConnectError(String connectErrorMsg);

    /**
     * 连接超时
     *
     * @param timeOutMsg
     */
    void scConnectTimeout(String timeOutMsg);

    /**
     * 断开连接
     *
     * @param disConnectMsg
     */
    void scDisConnect(String disConnectMsg);

    /**
     * 异常信息
     *
     * @param errorMsg
     */
    void scError(String errorMsg);

    /**
     * 消息监听
     *
     * @param message
     */
    void scOnMessage(String message);

    /**
     * 重连
     *
     * @param reconnectMsg
     */
    void scReconnect(String reconnectMsg);

    /**
     * 尝试重连
     *
     * @param reconnectAttemptMsg
     */
    void scReconnectAttempt(String reconnectAttemptMsg);

    /**
     * 重连异常
     *
     * @param reconnectErrorMsg
     */
    void scReconnectError(String reconnectErrorMsg);

    /**
     * 重连失败
     *
     * @param reconnectFailedMsg
     */
    void scReconnectFailed(String reconnectFailedMsg);

    /**
     * 正在重连
     *
     * @param reconnectingMsg
     */
    void scReconnecting(String reconnectingMsg);

    /**
     * 初始化IM指令返回值
     *
     * @param chat
     */
    void onChatStatus(Chat chat);

    /**
     * 拉取消息的回调接口
     *
     * @param messageList
     */
    void onReceiveMessageList(List<IMMessage> messageList);

    /**
     * 发送消息回调接口
     */
    void onSendMessageResult();


    /**
     * 分配客服回调接口
     *
     * @param code
     * @param message
     */
    void onGetAgentResult(int code, String message);


    /**
     * 设置标题的内容
     *
     * @param text
     */
    void setTitleContent(String text);


    /**
     * 显示请求评价view
     */
    void onShowRatingView();


    /**
     * 排队成功
     *
     * @param agent
     */
    void onQueueSuccess(Agent agent);

    /**
     * 更新排队位置
     *
     * @param message
     */
    void updateQueueNum(String message);


    /**
     * 更新排队的View组件状态
     */
    void updateQueueView(int resultCode);


    /**
     * 取消排队
     *
     * @param resultCode
     */
    void cancelQueue(int resultCode);


    /**
     * 分配客服失败
     */
    void getAgentFailure(AgentFailureType failureType);


    void onSyncMessageResult(int resultCode);

    void setRatingLevelCount(int levelCount);

    /**
     * 载入撤回消息列表
     *
     * @param messages
     */
    void onLoadRecallMessageList(List<IMMessage> messages);
}
