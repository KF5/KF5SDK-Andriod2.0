package com.kf5.sdk.im.mvp.model.api;

import com.kf5.sdk.system.internet.HttpRequestCallBack;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/27 18:32
 * email:812219713@qq.com
 */

public interface IChatModel {

    void uploadAttachment(Map<String, String> fieldMap, List<File> fileList, HttpRequestCallBack callBack);


//    void initSystem(HttpRequestCallBack callBack);

}
