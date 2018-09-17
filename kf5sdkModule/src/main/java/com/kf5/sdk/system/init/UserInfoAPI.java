package com.kf5.sdk.system.init;

import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.internet.BaseHttpManager;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5Engine.api.KF5API;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/14 18:20
 * email:812219713@qq.com
 * SDK对User相关操作的API
 */

public final class UserInfoAPI extends BaseHttpManager {

    private UserInfoAPI() {

    }

    private static UserInfoAPI mUserInfoAPI;

    public static UserInfoAPI getInstance() {
        if (mUserInfoAPI == null)
            synchronized (UserInfoAPI.class) {
                if (mUserInfoAPI == null)
                    mUserInfoAPI = new UserInfoAPI();
            }
        return mUserInfoAPI;
    }

    /**
     * 保存deviceToken
     *
     * @param fieldMap 提交的内容
     * @param callBack 请求回调
     */
    public void saveDeviceToken(Map<String, String> fieldMap, HttpRequestCallBack callBack) {
        fieldMap.put(Field.TYPE, Field.ANDROID);
        sendPostRequest(KF5API.saveDeviceToken(SPUtils.getHelpAddress()), fieldMap, callBack);
    }

    /**
     * 删除deviceToken
     *
     * @param fieldMap 提交的内容
     * @param callBack 请求回调
     */
    public void deleteDeviceToken(Map<String, String> fieldMap, HttpRequestCallBack callBack) {
        fieldMap.put(Field.TYPE, Field.ANDROID);
        sendPostRequest(KF5API.deleteDeviceToken(SPUtils.getHelpAddress()), fieldMap, callBack);
    }

    /**
     * 创建SDK的用户
     *
     * @param fieldMap 提交的用户相关属性
     * @param callBack 请求回调
     */
    public void createUser(Map<String, String> fieldMap, HttpRequestCallBack callBack) {
        fieldMap.put("source", "Github");
        sendPostRequest(KF5API.createUser(SPUtils.getHelpAddress()), fieldMap, callBack);
    }

    /**
     * 更新SDK的用户信息
     *
     * @param fieldMap 更新的内容
     * @param callBack 请求回调
     */
    public void updateUser(Map<String, String> fieldMap, HttpRequestCallBack callBack) {
        sendPostRequest(KF5API.updateUser(SPUtils.getHelpAddress()), fieldMap, callBack);
    }

    /**
     * SDK用户登录，只有登录成功方可正常使用SDK
     *
     * @param fieldMap 用户的登录信息
     * @param callBack 请求回调
     */
    public void loginUser(Map<String, String> fieldMap, HttpRequestCallBack callBack) {
        sendPostRequest(KF5API.loginUser(SPUtils.getHelpAddress()), fieldMap, callBack);
    }

    /**
     * 获取用户信息
     *
     * @param queryMap 追加参数
     * @param callBack 请求回调
     */
    public void getUserInfo(Map<String, String> queryMap, HttpRequestCallBack callBack) {
        queryMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        sendGetRequest(KF5API.getUserInfo(SPUtils.getHelpAddress(), queryMap), queryMap, callBack);
    }

}
