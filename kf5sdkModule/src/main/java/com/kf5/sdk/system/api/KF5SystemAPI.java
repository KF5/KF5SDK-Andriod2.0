package com.kf5.sdk.system.api;

import android.support.v4.util.ArrayMap;

import com.kf5.sdk.system.internet.BaseHttpManager;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5Engine.api.KF5API;

import java.util.Map;

/**
 * author:chosen
 * date:2017/1/5 14:21
 * email:812219713@qq.com
 */

public class KF5SystemAPI extends BaseHttpManager {

    private KF5SystemAPI() {

    }

    private static KF5SystemAPI INSTANCE;

    public static KF5SystemAPI getInstance() {
        if (INSTANCE == null) {
            synchronized (KF5SystemAPI.class) {
                if (INSTANCE == null) {
                    INSTANCE = new KF5SystemAPI();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取系统配置项
     *
     * @param callBack 请求回调
     */
    public void systemInit(HttpRequestCallBack callBack) {
        Map<String, String> map = new ArrayMap<>();
//        sendGetRequest(KF5API.systemInit(SPUtils.getHelpAddress()), map, callBack);
        sendPostRequest(KF5API.systemInit(SPUtils.getHelpAddress()), map, callBack);
    }

}
