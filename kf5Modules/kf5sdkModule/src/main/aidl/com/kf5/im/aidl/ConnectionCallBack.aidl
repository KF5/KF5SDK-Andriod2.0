// ConnectionCallBack.aidl
package com.kf5.im.aidl;

// Declare any non-default types here with import statements

interface ConnectionCallBack {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
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
