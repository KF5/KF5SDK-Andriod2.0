package com.kf5.sdk.im.service;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.kf5.im.aidl.ConnectionCallBack;
import com.kf5Engine.service.api.SocketEventCallBack;

/**
 * author:chosen
 * date:2016/10/27 17:51
 * email:812219713@qq.com
 */

public class ConnectionEvent implements SocketEventCallBack {

    private final RemoteCallbackList<ConnectionCallBack> mCallbackList;

    public ConnectionEvent(RemoteCallbackList<ConnectionCallBack> mCallbackList) {
        this.mCallbackList = mCallbackList;
    }

    @Override
    public void connect() {
        final int len = getCallBackSize();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).connect();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        endBroadcast();
    }

    @Override
    public void connectError(String connectErrorMsg) {
        final int len = getCallBackSize();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).connectError(connectErrorMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        endBroadcast();
    }

    @Override
    public void connectTimeout(String timeOutMsg) {
        final int len = getCallBackSize();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).connectTimeout(timeOutMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        endBroadcast();
    }

    @Override
    public void disConnect(String disConnectMsg) {
        final int len = getCallBackSize();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).disConnect(disConnectMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        endBroadcast();
    }

    @Override
    public void error(String errorMsg) {
        final int len = getCallBackSize();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).error(errorMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        endBroadcast();
    }

    @Override
    public void onMessage(String message) {
        final int len = getCallBackSize();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).onMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        endBroadcast();
    }

    @Override
    public void reconnect(String reconnectMsg) {
        final int len = getCallBackSize();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).reconnect(reconnectMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        endBroadcast();
    }

    @Override
    public void reconnectAttempt(String reconnectAttemptMsg) {
        final int len = getCallBackSize();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).reconnectAttempt(reconnectAttemptMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        endBroadcast();
    }

    @Override
    public void reconnectError(String reconnectErrorMsg) {
        final int len = getCallBackSize();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).reconnectError(reconnectErrorMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        endBroadcast();
    }


    @Override
    public void reconnectFailed(String reconnectFailedMsg) {
        final int len = getCallBackSize();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).reconnectFailed(reconnectFailedMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        endBroadcast();
    }

    @Override
    public void reconnecting(String reconnectingMsg) {
        final int len = getCallBackSize();
        for (int i = 0; i < len; i++) {
            try {
                mCallbackList.getBroadcastItem(i).reconnecting(reconnectingMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        endBroadcast();
    }

    /**
     * 获取回调接口总数
     *
     * @return
     */
    private int getCallBackSize() {
        return mCallbackList.beginBroadcast();
    }

    /**
     * 结束回调通知
     */
    private void endBroadcast() {
        mCallbackList.finishBroadcast();
    }


}
