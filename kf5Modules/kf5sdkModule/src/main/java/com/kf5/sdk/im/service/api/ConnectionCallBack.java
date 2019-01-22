package com.kf5.sdk.im.service.api;

/**
 * author:chosen
 * date:2016/10/27 17:26
 * email:812219713@qq.com
 */

public interface ConnectionCallBack {

    void connect();

    void connectError(String connectErrorMsg);

    void connectTimeout(String timeOutMsg);

    void disConnect(String disConnectMsg);

    void error(String errorMsg);

    void onMessage(String message);

    void reconnect(String reconnectMsg);

    void reconnectAttempt(String reconnectAttemptMsg);

    void reconnectError(String reconnectErrorMsg);

    void reconnectFailed(String reconnectFailedMsg);

    void reconnecting(String reconnectingMsg);
}
