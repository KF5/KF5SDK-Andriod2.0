package com.kf5.sdk.im.api;

/**
 * author:chosen
 * date:2016/10/26 15:13
 * email:812219713@qq.com
 */

public interface FileDownLoadCallBack {

    enum Status {
        SUCCESS, FAILED
    }

    void onResult(Status status, String result, String fileName);
}
