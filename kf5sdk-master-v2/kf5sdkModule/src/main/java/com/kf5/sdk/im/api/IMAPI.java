package com.kf5.sdk.im.api;

import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.internet.BaseHttpManager;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5Engine.api.KF5API;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/11/7 10:39
 * email:812219713@qq.com
 */

public class IMAPI extends BaseHttpManager {

    private static IMAPI sIMAPI;

    private IMAPI() {

    }

    public static IMAPI getInstance() {
        if (sIMAPI == null) {
            synchronized (IMAPI.class) {
                if (sIMAPI == null)
                    sIMAPI = new IMAPI();
            }
        }
        return sIMAPI;
    }


    /**
     * 上传附件
     *
     * @param filedMap body参数
     * @param fileList 文件集合
     * @param callBack 请求回调
     */
    public void uploadAttachment(Map<String, String> filedMap, List<File> fileList, HttpRequestCallBack callBack) {
        uploadIMAttachment(KF5API.uploadIMAttachment(SPUtils.getHelpAddress()), filedMap, fileList, callBack);
    }


    /**
     * 获取未读消息数
     *
     * @param queryMap 请求参数
     * @param callBack 请求回调
     */
    public void getUnReadMessageCount(Map<String, String> queryMap, HttpRequestCallBack callBack) {
        queryMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        sendGetRequest(KF5API.getIMMessageCount(SPUtils.getHelpAddress(), queryMap), queryMap, callBack);
    }

}
