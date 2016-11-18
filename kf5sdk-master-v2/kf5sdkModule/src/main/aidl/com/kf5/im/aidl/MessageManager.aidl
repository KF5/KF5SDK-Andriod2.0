// MessageManager.aidl
package com.kf5.im.aidl;

// Declare any non-default types here with import statements
import android.os.Bundle;
import com.kf5.im.aidl.IPCCallBack;
import com.kf5.im.aidl.ConnectionCallBack;

interface MessageManager {

    void initParams(in Bundle bundle);

    void connect();

    void disconnect();

    boolean isConnected();

    void sendEventMessage(in String prams , IPCCallBack callback);

    void registerConnectionCallBack(ConnectionCallBack cb);

    void unregisterConnectionCallBack(ConnectionCallBack cb);

}
