package com.kf5.sdk.im.service;

import android.os.Bundle;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.kf5.im.aidl.ConnectionCallBack;
import com.kf5.im.aidl.IPCCallBack;
import com.kf5.im.aidl.MessageManager;
import com.kf5Engine.service.api.ActionCallBack;

/**
 * author:chosen
 * date:2016/10/27 16:55
 * email:812219713@qq.com
 */

public class MessageServiceStub extends MessageManager.Stub {

    private MessageService messageService;

    public RemoteCallbackList<ConnectionCallBack> mCallbackList = new RemoteCallbackList<>();

    public MessageServiceStub(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void initParams(Bundle bundle) throws RemoteException {
        messageService.initParams(bundle);
    }

    @Override
    public void connect() throws RemoteException {
        messageService.createConnect();
    }

    @Override
    public void disconnect() throws RemoteException {
        messageService.offConnect();
    }

    @Override
    public boolean isConnected() throws RemoteException {
        return messageService.isConnectionConnected();
    }

    @Override
    public void sendEventMessage(String prams, final IPCCallBack callback) throws RemoteException {
        messageService.sendEventRequest(new ActionCallBack() {
            @Override
            public void onLoadActionResult(ActionResult actionResult, String result) {
                int code = 0;
                switch (actionResult) {
                    case SUCCESS:
                        code = 0;
                        break;
                    case FAILURE:
                        code = -1;
                        break;
                }
                try {
                    callback.onResult(code, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, prams);
    }

    @Override
    public void registerConnectionCallBack(ConnectionCallBack cb) throws RemoteException {
        mCallbackList.register(cb);
    }

    @Override
    public void unregisterConnectionCallBack(ConnectionCallBack cb) throws RemoteException {
        mCallbackList.unregister(cb);
    }


}
