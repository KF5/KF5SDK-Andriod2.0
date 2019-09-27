package com.kf5.sdk.im.mvp.model;

import com.kf5.sdk.im.api.IMAPI;
import com.kf5.sdk.im.mvp.model.api.IChatModel;
import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/27 18:31
 * email:812219713@qq.com
 */

public class IMModel implements IChatModel {
    @Override
    public void uploadAttachment(Map<String, String> fieldMap, List<File> fileList, HttpRequestCallBack callBack) {
        IMAPI.getInstance().uploadAttachment(fieldMap, fileList, callBack);
    }
}
